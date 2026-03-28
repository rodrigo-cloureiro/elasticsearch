package br.com.rodrigo.elasticsearch.controller;

import br.com.rodrigo.elasticsearch.model.ProdutoDocument;
import br.com.rodrigo.elasticsearch.service.ProdutoDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/produtos/busca")
@RequiredArgsConstructor
public class ProdutoDocumentController {
    private final ProdutoDocumentService produtoDocumentService;

    @GetMapping(value = "/nome")
    public ResponseEntity<List<ProdutoDocument>> buscarPorNomeProduto(
            @RequestParam(name = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscarPorNome(termo));
    }

    @GetMapping(value = "/descricao")
    public ResponseEntity<List<ProdutoDocument>> buscarPorDescricaoProduto(
            @RequestParam(name = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscarPorDescricao(termo));
    }

    @GetMapping(value = "/frase")
    public ResponseEntity<List<ProdutoDocument>> buscarPorFraseExata(
            @RequestParam(name = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscarPorFraseExata(termo));
    }

    @GetMapping(value = "/faixa-preco")
    public ResponseEntity<List<ProdutoDocument>> buscarPorFaixaPreco(
            @RequestParam(name = "min", defaultValue = "0") double min,
            @RequestParam(name = "max", defaultValue = "100") double max
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscarPorFaixaPreco(min, max));
    }
}
