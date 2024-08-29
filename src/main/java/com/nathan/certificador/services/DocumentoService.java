package com.nathan.certificador.services;

import com.nathan.certificador.entities.Documento;
import com.nathan.certificador.entities.Usuario;
import com.nathan.certificador.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Documento salvarDocumento(String nomeUsuario, String nomeDocumento, byte[] conteudo, String destinatario) {
        Usuario usuario = usuarioService.findByNomeUsuario(nomeUsuario);
        Documento documento = new Documento();
        documento.setNome(nomeDocumento);
        documento.setRemetente(usuario.getNomeUsuario());
        documento.setDestinatario(destinatario);
        documento.setConteudo(conteudo);
        documento.setUsuario(usuario);
        return documentoRepository.save(documento);
    }

    public Documento assinarDocumento(String nomeUsuario, Long documentoId) {
        Usuario usuario = usuarioService.findByNomeUsuario(nomeUsuario);
        Documento documento = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        byte[] assinatura = assinarDados(documento.getConteudo(), Base64.getDecoder().decode(usuario.getChavePrivada()));
        documento.setAssinatura(assinatura);
        return documentoRepository.save(documento);
    }

    private byte[] assinarDados(byte[] dados, byte[] chavePrivadaBytes) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(chavePrivadaBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey chavePrivada = keyFactory.generatePrivate(keySpec);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(chavePrivada);
            signature.update(dados);
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Documento> getDocumentosAssinados(String nomeUsuario) {
        Usuario usuario = usuarioService.findByNomeUsuario(nomeUsuario);
        return documentoRepository.findByUsuario(usuario);
    }

    public boolean verificarAssinaturaDocumento(Long documentoId) {
        Documento documento = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado"));

        byte[] chavePublicaBytes = Base64.getDecoder().decode(documento.getUsuario().getChavePublica());
        return verificarAssinatura(documento.getConteudo(), documento.getAssinatura(), chavePublicaBytes);
    }

    private boolean verificarAssinatura(byte[] dados, byte[] assinatura, byte[] chavePublicaBytes) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(chavePublicaBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey chavePublica = keyFactory.generatePublic(keySpec);
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(chavePublica);
            sig.update(dados);
            return sig.verify(assinatura);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
