package org.example;
public abstract class Funcionario {
    String nome;
    String cpf;
    int codigo;

    Funcionario (int codigo, String nome, String cpf) {
        this.codigo = codigo;
        this.nome = nome;
        this.cpf = cpf;
    }

    Funcionario (String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }
}
