package com.nathan.certificador.entities;
import jakarta.persistence.*;


@Entity
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String remetente;
    private String destinatario;

    @Lob
    private byte[] conteudo;

    @Lob
    private byte[] assinatura;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Getters e Setters
}
