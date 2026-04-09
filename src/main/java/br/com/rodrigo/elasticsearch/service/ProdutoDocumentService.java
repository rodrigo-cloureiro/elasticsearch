package br.com.rodrigo.elasticsearch.service;

import br.com.rodrigo.elasticsearch.dto.*;
import br.com.rodrigo.elasticsearch.exception.ElasticsearchComunicacaoException;
import br.com.rodrigo.elasticsearch.mapper.ProdutoDocumentMapper;
import br.com.rodrigo.elasticsearch.model.ProdutoDocument;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoDocumentService {

    private final ElasticsearchClient client;
    private final ProdutoQueryService queryService;

    /*
    private List<ProdutoDocument> search(Query query) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                            .query(query),
                    ProdutoDocument.class
            );

            return extractSources(response);
        } catch (IOException e) {
            throw new ElasticsearchComunicacaoException("Erro ao executar busca no Elasticsearch");
        }
    }

    public List<ProdutoDocument> searchByFieldMatch(String field, String term) {
        return search(queryService.match(field, term));
    }

    public List<ProdutoDocument> searchByExactPhrase(String field, String exactTerm) {
        return search(queryService.matchPhrase(field, exactTerm));
    }

    public List<ProdutoDocument> searchByFieldWithFuzziness(String field, String term) {
        return search(queryService.fuzzy(field, term));
    }

    public List<ProdutoDocument> searchMultiMatch(List<String> fields, String term) {
        return search(queryService.multiMatch(fields, term));
    }

    public List<ProdutoDocument> searchByNumberRange(String field, double min, double max) {
        return search(queryService.numberRange(field, min, max));
    }

    private List<ProdutoDocument> extractSources(SearchResponse<ProdutoDocument> response) {
        return response.hits()
                .hits()
                .stream()
                .map(Hit::source)
                .toList();
    }
    */

    // ============================================================================================================== \\

    public List<ProdutoDocument> buscarPorNome(String nome) {
        return search(Query.of(q -> q
                .match(queryService.matchQuery("nome", nome))
        ));
    }

    public List<ProdutoDocument> buscarPorDescricao(String descricao) {
        return search(Query.of(q -> q
                .match(queryService.matchQuery("descricao", descricao))
        ));
    }

    public List<ProdutoDocument> buscarPorFraseExata(String descricaoExata) {
        return search(Query.of(q -> q
                .matchPhrase(queryService.matchPhraseQuery("descricao", descricaoExata))
        ));
    }

    public List<ProdutoDocument> buscarPorNomeComTolerancia(String nome) {
        return search(Query.of(q -> q
                .fuzzy(queryService.fuzzyQuery("nome", nome))
        ));
    }

    public List<ProdutoDocument> buscarPorNomeEDescricao(String termo) {
        return search(Query.of(q -> q
                .multiMatch(queryService.multiMatchQuery(List.of("nome", "descricao"), termo))
        ));
    }

    public List<ProdutoDocument> buscarPorDescricaoECategoria(String descricao, String categoria) {
        return search(Query.of(q -> q
                .bool(b -> b
                        .must(queryService.matchQuery("descricao", descricao))
                        .filter(queryService.termQuery("categoria", categoria))
                )
        ));
    }

    public List<ProdutoDocument> buscarPorFaixaPreco(double min, double max) {
        return search(Query.of(q -> q
                .range(r -> r
                        .number(queryService.numberRangeQuery("preco", min, max))
                )
        ));
    }

    public List<ProdutoDocument> buscaCombinada(String categoria, String raridade, double min, double max) {
        return search(Query.of(q -> q
                .bool(b -> b
                        .filter(
                                queryService.termQuery("categoria", categoria),
                                queryService.termQuery("raridade", raridade),
                                queryService.numberRangeQuery("preco", min, max)._toRangeQuery()
                        )
                )

        ));
    }

    public List<ContagemCampoAggregation> quantidadeProdutosPorCampo(String field) {
        return search(
                String.format("por_%s", field),
                queryService.termsAggregation(field),
                field
        );
    }

    public PrecoMedioAggregation precoMedioProdutos() {
        String key = "preco_medio";

        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .aggregations(
                            key,
                            queryService.averageAggregation("preco")
                    ), ProdutoDocument.class
            );

            return new PrecoMedioAggregation(response.aggregations()
                    .get(key)
                    .avg()
                    .value()
            );
        } catch (IOException e) {
            throw new ElasticsearchComunicacaoException("Erro ao executar busca no Elasticsearch");
        }
    }

    public List<FaixaPreco> agruparEmFaixaPreco() {
        String aggregationKey = "faixa_preco";
        String subAggregationKey = "produtos";

        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .aggregations(aggregationKey, a -> a
                            .range(queryService.rangeAggregation("preco", getRangesPreco()))
                            .aggregations(subAggregationKey, sa -> sa
                                    .topHits(th -> th)
                            )
                    ), ProdutoDocument.class
            );

            return response.aggregations()
                    .get(aggregationKey)
                    .range()
                    .buckets()
                    .array()
                    .stream()
                    .map(ProdutoDocumentMapper::bucketToFaixaPreco)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<AggregationRange> getRangesPreco() {
        return List.of(
                AggregationRange.of(a -> a.to(100.0).key("Abaixo de 100")),
                AggregationRange.of(a -> a.from(100.0).to(300.0).key("De 100 a 300")),
                AggregationRange.of(a -> a.from(300.0).to(700.0).key("De 300 a 700")),
                AggregationRange.of(a -> a.from(700.0).key("Acima de 700"))
        );
    }

    private List<ProdutoDocument> search(Query query) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                            .query(query),
                    ProdutoDocument.class
            );

            return extractSources(response);
        } catch (IOException e) {
            throw new ElasticsearchComunicacaoException("Erro ao executar busca no Elasticsearch");
        }
    }

    private List<ContagemCampoAggregation> search(String key, Aggregation value, String field) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                            .aggregations(key, value),
                    ProdutoDocument.class
            );

            return mapStringTermsAggregate(response, key, field);
        } catch (IOException e) {
            throw new ElasticsearchComunicacaoException("Erro ao executar busca no Elasticsearch");
        }
    }

    private List<ProdutoDocument> extractSources(SearchResponse<ProdutoDocument> response) {
        return response.hits()
                .hits()
                .stream()
                .map(Hit::source)
                .toList();
    }

    public List<ContagemCampoAggregation> mapStringTermsAggregate(
            SearchResponse<ProdutoDocument> response,
            String key,
            String field
    ) {
        return response.aggregations()
                .get(key)
                .sterms()
                .buckets()
                .array()
                .stream()
                .map(bucket -> new ContagemCampoAggregation(
                        field,
                        bucket.key().stringValue(),
                        bucket.docCount()
                ))
                .toList();
    }
}
