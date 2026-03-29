package br.com.rodrigo.elasticsearch.dto;

public record ProdutoResponse(
        String nome,
        String descricao,
        String categoria,
        String raridade,
        Float preco
) {
}
