package com.nathan.certificador.services;

import com.nathan.certificador.entities.Usuario;
import com.nathan.certificador.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(String nomeUsuario, String senha) {
        KeyPair parDeChaves = gerarParDeChaves();
        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(nomeUsuario);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setChavePublica(Base64.getEncoder().encodeToString(parDeChaves.getPublic().getEncoded()));
        usuario.setChavePrivada(Base64.getEncoder().encodeToString(parDeChaves.getPrivate().getEncoded()));
        return usuarioRepository.save(usuario);
    }

    private KeyPair gerarParDeChaves() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Usuario findByNomeUsuario(String nomeUsuario) {
        return usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
