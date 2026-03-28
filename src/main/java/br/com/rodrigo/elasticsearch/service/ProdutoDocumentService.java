package br.com.rodrigo.elasticsearch.service;

import br.com.rodrigo.elasticsearch.model.ProdutoDocument;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
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
}
