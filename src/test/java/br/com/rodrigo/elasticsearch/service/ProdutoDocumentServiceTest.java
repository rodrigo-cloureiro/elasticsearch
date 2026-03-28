package br.com.rodrigo.elasticsearch.service;

import br.com.rodrigo.elasticsearch.dto.CategoriaAggregation;
import br.com.rodrigo.elasticsearch.model.ProdutoDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ProdutoDocumentServiceTest {

    @Autowired
    private ProdutoDocumentService service;

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

    @Test
    void quantidadeProdutosPorCategoria() {
        List<CategoriaAggregation> result = service.quantidadeProdutosPorCategoria();
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(r -> r.quantidade() >= 0));
    }
}
