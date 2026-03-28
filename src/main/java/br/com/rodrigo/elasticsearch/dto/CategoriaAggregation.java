package br.com.rodrigo.elasticsearch.dto;

public record CategoriaAggregation(
        String categoria,
        long quantidade
) {
}
