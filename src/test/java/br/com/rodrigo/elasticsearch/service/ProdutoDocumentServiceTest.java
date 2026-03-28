package br.com.rodrigo.elasticsearch.service;

import br.com.rodrigo.elasticsearch.model.ProdutoDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ProdutoDocumentServiceTest {

    @Autowired
    private ProdutoDocumentService service;

    @Test
    void buscarPorNome() {
        List<ProdutoDocument> result = service.buscarPorNome("Espada");
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getNome()
                .toLowerCase().
                contains("espada")
        ));
    }

    @Test
    void buscarPorDescricao() {
        List<ProdutoDocument> result = service.buscarPorDescricao("norte");
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getDescricao()
                .toLowerCase()
                .contains("norte")
        ));
    }

    @Test
    void buscarPorFraseExata() {
        List<ProdutoDocument> result = service.buscarPorFraseExata("artefato raro");
        assertFalse(result.isEmpty());
        result.forEach(r -> assertTrue(r.getDescricao()
                .toLowerCase()
                .contains("artefato raro")
        ));
    }
}
