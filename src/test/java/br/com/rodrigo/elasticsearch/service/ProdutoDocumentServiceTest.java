package br.com.rodrigo.elasticsearch.service;

import br.com.rodrigo.elasticsearch.dto.*;
import br.com.rodrigo.elasticsearch.model.ProdutoDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProdutoDocumentServiceTest {

    @Autowired
    private ProdutoDocumentService service;

    /*
    @ParameterizedTest
    @CsvSource(value = {
            "nome, Espada",
            "descricao, norte",
    })
    void searchByFieldMatchTest(String field, String term) {
        List<ProdutoDocument> result = service.searchByFieldMatch(field, term);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(r -> {
            if (field.equalsIgnoreCase("nome")) {
                return r.getNome().toLowerCase().contains(term.toLowerCase());
            }
            if (field.equalsIgnoreCase("descricao")) {
                return r.getDescricao().toLowerCase().contains(term.toLowerCase());
            }
            return false;
        }));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "descricao, artefato raro",
    })
    void searchByExactPhraseTest(String field, String exactTerm) {
        List<ProdutoDocument> result = service.searchByExactPhrase(field, exactTerm);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(r -> {
            if (field.equalsIgnoreCase("descricao")) {
                return r.getDescricao().toLowerCase().contains(exactTerm.toLowerCase());
            }
            return false;
        }));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "nome, espdaa, espada",
            "descricao, utilizdo, utilizado",
    })
    void searchByFieldWithFuzzinessTest(String field, String term, String correctTerm) {
        List<ProdutoDocument> result = service.searchByFieldWithFuzziness(field, term);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(r -> {
            if (field.equalsIgnoreCase("nome")) {
                return r.getNome().toLowerCase().contains(correctTerm.toLowerCase());
            }
            if (field.equalsIgnoreCase("descricao")) {
                return r.getDescricao().toLowerCase().contains(correctTerm.toLowerCase());
            }
            return false;
        }));
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "nome,descricao; Antiga"
            },
            delimiter = ';'
    )
    void searchMultiMatchTest(String strFields, String term) {
        List<String> fields = Arrays.asList(strFields.split(","));
        List<ProdutoDocument> result = service.searchMultiMatch(fields, term);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(r ->
                fields.stream().anyMatch(field -> {
                    if (field.equalsIgnoreCase("nome")) {
                        return r.getNome().toLowerCase().contains(term.toLowerCase());
                    }
                    if (field.equalsIgnoreCase("descricao")) {
                        return r.getDescricao().toLowerCase().contains(term.toLowerCase());
                    }
                    return false;
                })
        ));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "preco, 100, 150",
            "preco, 100, 250"
    })
    void searchByNumberRangeTest(String field, double min, double max) {
        List<ProdutoDocument> result = service.searchByNumberRange(field, min, max);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(r ->
                r.getPreco() >= min && r.getPreco() <= max)
        );
    }
    */

    // ============================================================================================================== \\

    @Test
    void buscarPorNome() {
        String termo = "Espada";

        List<ProdutoDocument> result = service.buscarPorNome(termo);
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getNome()
                .toLowerCase().
                contains(termo.toLowerCase())
        ));
    }

    @Test
    void buscarPorDescricao() {
        String termo = "norte";

        List<ProdutoDocument> result = service.buscarPorDescricao(termo);
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getDescricao()
                .toLowerCase()
                .contains(termo)
        ));
    }

    @Test
    void buscarPorFraseExata() {
        String termo = "artefato raro";

        List<ProdutoDocument> result = service.buscarPorFraseExata(termo);
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getDescricao()
                .toLowerCase()
                .contains(termo)
        ));
    }

    @Test
    void buscarPorNomeComTolerancia() {
        String fuzzy = "espdaa";
        String termo = "espada";

        List<ProdutoDocument> result = service.buscarPorNomeComTolerancia(fuzzy);
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getNome()
                .toLowerCase()
                .contains(termo)
        ));
    }

    @Test
    void buscarPorNomeEDescricao() {
        String termo = "Antiga";

        List<ProdutoDocument> result = service.buscarPorNomeEDescricao(termo);
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getNome()
                .toLowerCase()
                .contains(termo.toLowerCase()) ||
                r.getDescricao()
                        .toLowerCase()
                        .contains(termo.toLowerCase())
        ));
    }

    @Test
    void buscarPorDescricaoECategoria() {
        String descricao = "antiga";
        String categoria = "ingredientes";

        List<ProdutoDocument> result = service.buscarPorDescricaoECategoria(descricao, categoria);
        assertFalse(result.isEmpty());
        assertTrue(result.stream()
                .allMatch(r -> r
                        .getDescricao()
                        .toLowerCase()
                        .contains(descricao.toLowerCase()) &&
                        r.getCategoria()
                                .equalsIgnoreCase(categoria))
        );
    }

    @Test
    void buscarPorFaixaPreco() {
        double min = 100;
        double max = 150;

        List<ProdutoDocument> result = service.buscarPorFaixaPreco(min, max);
        assertFalse(result.isEmpty());
        assertTrue(result.stream()
                .allMatch(p -> p.getPreco() >= min && p.getPreco() <= max)
        );
    }

    @Test
    void buscaCombinada() {
        String categoria = "armas";
        String raridade = "epico";
        double min = 1_990;
        double max = 2_010;

        List<ProdutoDocument> result = service.buscaCombinada(categoria, raridade, min, max);
        assertFalse(result.isEmpty());
        assertTrue(result.stream()
                .allMatch(r -> r.getCategoria()
                        .equalsIgnoreCase(categoria) &&
                        r.getRaridade()
                                .equalsIgnoreCase(raridade) &&
                        r.getPreco() >= min && r.getPreco() <= max
                )
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "categoria",
            "raridade"
    })
    void quantidadeProdutosPorCampo(String campo) {
        List<ContagemCampoAggregation> result = service.quantidadeProdutosPorCampo(campo);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(r ->
                r.quantidade() >= 0 && r.campo().equalsIgnoreCase(campo)
        ));
    }

    @Test
    void precoMedioProdutos() {
        PrecoMedioAggregation result = service.precoMedioProdutos();
        assertNotNull(result.precoMedio());
        assertTrue(result.precoMedio() >= 0);
    }

    @Test
    void agruparEmFaixaPreco() {
        List<FaixaPreco> result = service.agruparEmFaixaPreco();
        assertFalse(result.isEmpty());
    }
}
