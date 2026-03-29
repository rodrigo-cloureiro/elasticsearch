package br.com.rodrigo.elasticsearch.controller;

import br.com.rodrigo.elasticsearch.dto.CategoriaAggregation;
import br.com.rodrigo.elasticsearch.dto.FaixaPreco;
import br.com.rodrigo.elasticsearch.dto.PrecoMedioAggregation;
import br.com.rodrigo.elasticsearch.dto.RaridadeAggregation;
import br.com.rodrigo.elasticsearch.service.ProdutoDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/produtos/agregacoes")
@RequiredArgsConstructor
public class ProdutoDocumentAggregationController {

    private final ProdutoDocumentService produtoDocumentService;

    @GetMapping(value = "/por-categoria")
    public ResponseEntity<List<CategoriaAggregation>> quantidadeProdutosPorCategoria() {
        return ResponseEntity.ok()
                .body(produtoDocumentService.quantidadeProdutosPorCategoria());
    }

    @GetMapping(value = "/por-raridade")
    public ResponseEntity<List<RaridadeAggregation>> quantidadeProdutosPorRaridade() {
        return ResponseEntity.ok()
                .body(produtoDocumentService.quantidadeProdutosPorRaridade());
    }

    @GetMapping(value = "/preco-medio")
    public ResponseEntity<PrecoMedioAggregation> precoMedioProdutos() {
        return ResponseEntity.ok()
                .body(produtoDocumentService.precoMedioProdutos());
    }

    @GetMapping(value = "/faixas-preco")
    public ResponseEntity<List<FaixaPreco>> agruparEmFaixaPreco() {
        return ResponseEntity.ok()
                .body(produtoDocumentService.agruparEmFaixaPreco());
    }
}
