package org.example;
public class Dentista extends Funcionario{
    private int CRO;

    Dentista(int codigo, String nome, String cpf, int CRO) {
        super(codigo, nome, cpf);
        this.CRO = CRO;
    }

    Dentista(String nome, String cpf, int CRO) {
        super(nome, cpf);
        this.CRO = CRO;
    }


    /**
     * @return String
     */
    public String toString() {
        return String.format("Codigo Dentista: %02d\n Nome: %s \nCPF: %s \nCRO: %d", super.codigo, super.nome, super.cpf, CRO);
    }

    //Getters and Setters
    public int getCRO() {
        return CRO;
    }

    public void setCRO(int cRO) {
        CRO = cRO;
    }
}

