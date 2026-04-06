package br.com.rodrigo.elasticsearch.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdutoQueryService {

    public Query match(String field, String value) {
        return Query.of(q -> q
                .match(m -> m
                        .field(field)
                        .query(value)
                )
        );
    }

    public Query matchPhrase(String field, String value) {
        return Query.of(q -> q
                .matchPhrase(mp -> mp
                        .field(field)
                        .query(value)
                )
        );
    }

    public Query fuzzy(String field, String value) {
        return Query.of(q -> q
                .fuzzy(f -> f
                        .field(field)
                        .value(value)
                        .fuzziness("AUTO")
                )
        );
    }

    public Query multiMatch(List<String> fields, String value) {
        return Query.of(q -> q
                .multiMatch(mm -> mm
                        .fields(fields)
                        .query(value)
                )
        );
    }

    public Query range(String field, double min, double max) {
        return Query.of(q -> q
                .range(r -> r
                        .number(n -> n
                                .field(field)
                                .gte(min)
                                .lte(max)
                        )
                )
        );
    }
}
