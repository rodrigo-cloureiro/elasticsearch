package br.com.rodrigo.elasticsearch.advice;

import br.com.rodrigo.elasticsearch.exception.ElasticsearchComunicacaoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ElasticsearchComunicacaoException.class)
    public ResponseEntity<?> handleElasticsearchCommunicationException(ElasticsearchComunicacaoException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
