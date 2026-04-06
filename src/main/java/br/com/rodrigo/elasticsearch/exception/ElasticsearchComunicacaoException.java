package br.com.rodrigo.elasticsearch.exception;

public class ElasticsearchComunicacaoException extends RuntimeException {
    public ElasticsearchComunicacaoException(String message) {
        super(message);
    }
}
