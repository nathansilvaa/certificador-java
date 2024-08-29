package com.nathan.certificador.controllers;

import com.nathan.certificador.entities.Usuario;
import com.nathan.certificador.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<Usuario> registrar(@RequestParam String nomeUsuario, @RequestParam String senha) {
        Usuario usuario = usuarioService.registrarUsuario(nomeUsuario, senha);
        return ResponseEntity.ok(usuario);
    }
}
