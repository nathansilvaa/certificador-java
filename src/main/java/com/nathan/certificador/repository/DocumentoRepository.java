package com.nathan.certificador.repository;

import com.nathan.certificador.entities.Documento;
import com.nathan.certificador.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    List<Documento> findByUsuario(Usuario usuario);
}
