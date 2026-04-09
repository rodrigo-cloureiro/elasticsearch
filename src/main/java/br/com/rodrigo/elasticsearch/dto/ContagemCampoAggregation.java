package br.com.rodrigo.elasticsearch.dto;

public record ContagemCampoAggregation(
        String campo,
        String valor,
        long quantidade
) {
}
