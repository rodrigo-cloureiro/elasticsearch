package br.com.rodrigo.elasticsearch.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.aggregations.AverageAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.RangeAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdutoQueryService {

    /*
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

    public Query numberRange(String field, double min, double max) {
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
    */

    // ============================================================================================================== \\

    public MatchQuery matchQuery(String field, String value) {
        return MatchQuery.of(q -> q
                .field(field)
                .query(value)
        );
    }

    public MatchPhraseQuery matchPhraseQuery(String field, String value) {
        return MatchPhraseQuery.of(q -> q
                .field(field)
                .query(value)
        );
    }

    public FuzzyQuery fuzzyQuery(String field, String value) {
        return FuzzyQuery.of(q -> q
                .field(field)
                .value(value)
                .fuzziness("AUTO")
        );
    }

    public MultiMatchQuery multiMatchQuery(List<String> fields, String value) {
        return MultiMatchQuery.of(q -> q
                .fields(fields)
                .query(value)
        );
    }

    public NumberRangeQuery numberRangeQuery(String field, double min, double max) {
        return NumberRangeQuery.of(q -> q
                .field(field)
                .gte(min)
                .lte(max)
        );
    }

    public TermQuery termQuery(String field, String value) {
        return TermQuery.of(q -> q
                .field(field)
                .value(value)
        );
    }

    public Aggregation termsAggregation(String field) {
        return Aggregation.of(a -> a
                .terms(t -> t
                        .field(field)
                )
        );
    }

    public AverageAggregation averageAggregation(String field) {
        return AverageAggregation.of(a -> a
                .field(field)
        );
    }

    public RangeAggregation rangeAggregation(String field, List<AggregationRange> ranges) {
        return Aggregation.of(a -> a
                        .range(RangeAggregation.of(r -> r
                                .field(field)
                                .ranges(ranges))
                        )
                )
                .range();
    }
}
