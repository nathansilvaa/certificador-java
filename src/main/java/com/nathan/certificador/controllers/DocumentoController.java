package com.nathan.certificador.controllers;

import com.nathan.certificador.entities.Documento;
import com.nathan.certificador.services.DocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @PostMapping("/salvar")
    public ResponseEntity<Documento> salvarDocumento(@RequestParam String nomeUsuario, @RequestParam String nome,
                                                     @RequestParam byte[] conteudo, @RequestParam String destinatario) {
        Documento documento = documentoService.salvarDocumento(nomeUsuario, nome, conteudo, destinatario);
        return ResponseEntity.ok(documento);
    }

    @PostMapping("/assinar/{id}")
    public ResponseEntity<Documento> assinarDocumento(@RequestParam String nomeUsuario, @PathVariable Long id) {
        Documento documento = documentoService.assinarDocumento(nomeUsuario, id);
        return ResponseEntity.ok(documento);
    }

    @GetMapping("/assinados")
    public ResponseEntity<List<Documento>> getDocumentosAssinados(@RequestParam String nomeUsuario) {
        List<Documento> documentos = documentoService.getDocumentosAssinados(nomeUsuario);
        return ResponseEntity.ok(documentos);
    }

    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> verificarAssinaturaDocumento(@PathVariable Long id) {
        boolean isValid = documentoService.verificarAssinaturaDocumento(id);
        return ResponseEntity.ok(isValid);
    }
}
