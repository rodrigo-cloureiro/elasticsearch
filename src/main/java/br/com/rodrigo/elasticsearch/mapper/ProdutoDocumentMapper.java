package br.com.rodrigo.elasticsearch.mapper;

import br.com.rodrigo.elasticsearch.dto.FaixaPreco;
import br.com.rodrigo.elasticsearch.dto.ProdutoResponse;
import br.com.rodrigo.elasticsearch.model.ProdutoDocument;
import co.elastic.clients.elasticsearch._types.aggregations.RangeBucket;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ProdutoDocumentMapper {
    public static ProdutoResponse toProdutoResponse(ProdutoDocument document) {
        return new ProdutoResponse(
                document.getNome(),
                document.getDescricao(),
                document.getCategoria(),
                document.getRaridade(),
                document.getPreco()
        );
    }

    public static FaixaPreco bucketToFaixaPreco(RangeBucket bucket) {
        List<ProdutoResponse> produtos = bucket.aggregations()
                .get("produtos")
                .topHits()
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .map(source -> source.to(ProdutoDocument.class))
                .map(ProdutoDocumentMapper::toProdutoResponse)
                .toList();

        return new FaixaPreco(
                bucket.key(),
                bucket.docCount(),
                produtos
        );
    }
}
