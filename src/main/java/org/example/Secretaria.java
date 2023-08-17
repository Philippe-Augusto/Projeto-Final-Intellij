package org.example;
public class Secretaria extends Funcionario{
    double salario;

    Secretaria(int codigo, String nome, String cpf, double salario) {
        super(codigo, nome, cpf);
        this.salario = salario;
    }

    Secretaria(String nome, String cpf, double salario) {
        super(nome, cpf);
        this.salario = salario;
    }


    /**
     * @return String
     */
    public String toString() {
        return String.format("Codigo Secretaria: %d\nNome: %s\nCPF: %s\nSalario: %f", super.codigo, super.nome, super.cpf, salario);
    }
}

