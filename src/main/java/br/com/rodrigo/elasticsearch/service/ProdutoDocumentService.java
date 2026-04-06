package br.com.rodrigo.elasticsearch.service;

import br.com.rodrigo.elasticsearch.dto.*;
import br.com.rodrigo.elasticsearch.mapper.ProdutoDocumentMapper;
import br.com.rodrigo.elasticsearch.model.ProdutoDocument;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
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

    /* public List<ProdutoDocument> searchByFieldMatch(String field, String term) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .match(m -> m
                                    .field(field)
                                    .query(term)
                            )
                    ), ProdutoDocument.class
            );

            return extractSources(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } */

    /* public List<ProdutoDocument> searchByExactPhrase(String field, String exactTerm) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .matchPhrase(mp -> mp
                                    .field(field)
                                    .query(exactTerm)
                            )
                    ), ProdutoDocument.class
            );

            return extractSources(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } */

    /* public List<ProdutoDocument> searchByFieldWithFuzziness(String field, String term) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .fuzzy(f -> f
                                    .field(field)
                                    .value(term)
                                    .fuzziness("AUTO")
                            )
                    ), ProdutoDocument.class
            );

            return extractSources(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } */

    /* public List<ProdutoDocument> searchMultiMatch(List<String> fields, String term) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .multiMatch(mm -> mm
                                    .fields(fields)
                                    .query(term)
                            )
                    ), ProdutoDocument.class
            );

            return extractSources(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } */

    /* public List<ProdutoDocument> searchByNumberRange(String field, double min, double max) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .range(r -> r
                                    .number(n -> n
                                            .field(field)
                                            .gte(min)
                                            .lte(max)
                                    )
                            )
                    ), ProdutoDocument.class
            );

            return extractSources(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } */

    /* ************************************************************************************************************** */

    private List<ProdutoDocument> search(Query query) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                            .query(query),
                    ProdutoDocument.class
            );

            return extractSources(response);
        } catch (IOException e) {
            throw new RuntimeException();
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
        return search(queryService.range(field, min, max));
    }

    private List<ProdutoDocument> extractSources(SearchResponse<ProdutoDocument> response) {
        return response.hits()
                .hits()
                .stream()
                .map(Hit::source)
                .toList();
    }

    /* ************************************************************************************************************** */

    // NEW => OK ==> TESTAR
    public List<ProdutoDocument> buscarPorNome(String nome) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .match(m -> m
                                    .field("nome")
                                    .query(nome)
                            )
                    ), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // NEW => OK ==> TESTAR
    public List<ProdutoDocument> buscarPorDescricao(String descricao) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .match(m -> m
                                    .field("descricao")
                                    .query(descricao)
                            )
                    ), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // NEW => OK ==> TESTAR
    public List<ProdutoDocument> buscarPorFraseExata(String descricaoExata) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .matchPhrase(mp -> mp
                                    .field("descricao")
                                    .query(descricaoExata)
                            )
                    ), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // NEW => OK ==> TESTAR
    public List<ProdutoDocument> buscarPorNomeComTolerancia(String nome) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .fuzzy(f -> f
                                    .field("nome")
                                    .value(nome)
                                    .fuzziness("AUTO")
                            )
                    ), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // NEW => OK ==> TESTAR
    public List<ProdutoDocument> buscarPorNomeEDescricao(String termo) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .multiMatch(mm -> mm
                                    .fields(List.of("nome", "descricao"))
                                    .query(termo)
                            )
                    )
                    .size(20), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // NEW => TODO
    public List<ProdutoDocument> buscarPorDescricaoECategoria(String descricao, String categoria) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .bool(b -> b
                                    .must(m -> m
                                            .match(v -> v
                                                    .field("descricao")
                                                    .query(descricao)
                                            )
                                    )
                                    .filter(f -> f
                                            .term(t -> t
                                                    .field("categoria")
                                                    .value(categoria)
                                            )
                                    )
                            )
                    ), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // NEW => OK ==> TESTAR
    public List<ProdutoDocument> buscarPorFaixaPreco(double min, double max) {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                            .query(q -> q
                                    .range(r -> r
                                            .number(n -> n
                                                    .field("preco")
                                                    .gte(min)
                                                    .lte(max)
                                            )
                                    )
                            )/*.sort(so -> so
                                    .field(f -> f
                                            .field("preco")
                                            .order(SortOrder.Asc)
                                    )
                            )*/,
                    ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // NEW => TODO
    public List<ProdutoDocument> buscaCombinada(String categoria, String raridade, double min, double max) {
        TermQuery categoriaTermQuery = TermQuery.of(t -> t.field("categoria").value(categoria));
        TermQuery raridadeTermQuery = TermQuery.of(t -> t.field("raridade").value(raridade));
        RangeQuery faixaPreco = RangeQuery.of(r -> r.number(n -> n.field("preco").gte(min).lte(max)));

        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .query(q -> q
                            .bool(b -> b
                                    .filter(
                                            categoriaTermQuery,
                                            raridadeTermQuery,
                                            faixaPreco
                                    )
                            )

                    ), ProdutoDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // NEW => TODO
    public List<CategoriaAggregation> quantidadeProdutosPorCategoria() {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .aggregations("por_categoria", a -> a
                            .terms(t -> t
                                    .field("categoria")
                            )
                    ), ProdutoDocument.class
            );

            return response.aggregations()
                    .get("por_categoria")
                    .sterms()
                    .buckets()
                    .array()
                    .stream()
                    .map(bucket -> new CategoriaAggregation(
                            bucket.key().stringValue(),
                            bucket.docCount()
                    ))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // NEW => TODO
    public List<RaridadeAggregation> quantidadeProdutosPorRaridade() {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .aggregations("por_raridade", a -> a
                            .terms(t -> t
                                    .field("raridade")
                            )
                    ), ProdutoDocument.class
            );

            return response.aggregations()
                    .get("por_raridade")
                    .sterms()
                    .buckets()
                    .array()
                    .stream()
                    .map(bucket -> new RaridadeAggregation(
                            bucket.key().stringValue(),
                            bucket.docCount()
                    ))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // NEW => TODO
    public PrecoMedioAggregation precoMedioProdutos() {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .aggregations("preco_medio", a -> a
                            .avg(avg -> avg
                                    .field("preco"))
                    ), ProdutoDocument.class
            );

            return new PrecoMedioAggregation(response.aggregations()
                    .get("preco_medio")
                    .avg()
                    .value()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // NEW => TODO
    public List<FaixaPreco> agruparEmFaixaPreco() {
        try {
            SearchResponse<ProdutoDocument> response = client.search(s -> s
                    .aggregations("faixa_preco", a -> a
                            .range(r -> r
                                    .field("preco")
                                    .ranges(getRangesPreco())
                            )
                            .aggregations("produtos", sa -> sa
                                    .topHits(th -> th)
                            )
                    ), ProdutoDocument.class
            );

            return response.aggregations()
                    .get("faixa_preco")
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
}
