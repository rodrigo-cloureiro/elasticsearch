package br.com.rodrigo.elasticsearch.dto;

import java.util.List;

public record FaixaPreco(
        String faixa,
        long quantidade,
        List<ProdutoResponse> produtos
) {
}
