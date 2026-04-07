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
public class ProdutoDocumentSearchController {

    private final ProdutoDocumentService produtoDocumentService;

    @GetMapping(value = "/nome")
    public ResponseEntity<List<ProdutoDocument>> buscarPorNomeProduto(
            @RequestParam(name = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.searchByFieldMatch("nome", termo));
    }

    @GetMapping(value = "/descricao")
    public ResponseEntity<List<ProdutoDocument>> buscarPorDescricaoProduto(
            @RequestParam(name = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.searchByFieldMatch("descricao", termo));
    }

    @GetMapping(value = "/frase")
    public ResponseEntity<List<ProdutoDocument>> buscarPorFraseExata(
            @RequestParam(name = "campo") String campo,
            @RequestParam(name = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.searchByExactPhrase(campo, termo));
    }

    @GetMapping(value = "/fuzzy")
    public ResponseEntity<List<ProdutoDocument>> buscarPorNomeComTolerancia(
            @RequestParam(value = "campo") String campo,
            @RequestParam(value = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.searchByFieldWithFuzziness(campo, termo));
    }

    @GetMapping(value = "/multicampos")
    public ResponseEntity<List<ProdutoDocument>> buscarPorNomeEDescricao(
            @RequestParam(name = "campos") List<String> campos,
            @RequestParam(name = "termo") String termo
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.searchMultiMatch(campos, termo));
    }

    @GetMapping(value = "/com-filtro")
    public ResponseEntity<List<ProdutoDocument>> buscarPorDescricaoECategoria(
            @RequestParam(name = "termo") String termo,
            @RequestParam(name = "categoria") String categoria
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscarPorDescricaoECategoria(termo, categoria));
    }

    @GetMapping(value = "/faixa-preco")
    public ResponseEntity<List<ProdutoDocument>> buscarPorFaixaPreco(
            @RequestParam(name = "campo") String campo,
            @RequestParam(name = "min", defaultValue = "0") double min,
            @RequestParam(name = "max", defaultValue = "100") double max
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.searchByNumberRange(campo, min, max));
    }

    @GetMapping(value = "/avancada")
    public ResponseEntity<List<ProdutoDocument>> buscaCombinada(
            @RequestParam(name = "categoria") String categoria,
            @RequestParam(name = "raridade") String raridade,
            @RequestParam(name = "min") double min,
            @RequestParam(name = "max") double max
    ) {
        return ResponseEntity.ok()
                .body(produtoDocumentService.buscaCombinada(categoria, raridade, min, max));
    }
}
