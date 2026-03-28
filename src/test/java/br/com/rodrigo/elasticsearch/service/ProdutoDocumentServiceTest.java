package br.com.rodrigo.elasticsearch.service;

import br.com.rodrigo.elasticsearch.model.ProdutoDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Comparator;
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
    void buscarPorFaixaPreco() throws IOException {
        double min = 100;
        double max = 150;

        List<ProdutoDocument> result = service.buscarPorFaixaPreco(min, max);
        assertFalse(result.isEmpty());
        assertTrue(result.stream()
                .allMatch(p -> p.getPreco() >= min && p.getPreco() <= max)
        );
    }
}
