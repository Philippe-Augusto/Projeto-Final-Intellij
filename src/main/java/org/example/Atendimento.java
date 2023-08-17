package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Atendimento {
    Cliente cliente;
    Dentista dentista;
    Date dataHorarioAtendimento;
    String tipo; // Tipo - Orçamento / Restauração / Canal / Cirurgia / Manutenção ...
    String statusAtendimento; // Aguardando - Realizando - Realizada
    int valor;
    int cod_atendimento;

    SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    //Impressao
    Atendimento(int cod_atendimento, Cliente cliente, Dentista dentista, Date dataHorarioAtendimento, String tipo, int valor) {
        this.cod_atendimento = cod_atendimento;
        this.cliente = cliente;
        this.dentista = dentista;
        this.dataHorarioAtendimento = dataHorarioAtendimento;
        this.tipo = tipo;
        this.valor = valor;
        this.statusAtendimento = "Aguardando";
    }

    //Inserção
    Atendimento(Cliente cliente, Dentista dentista, Date dataHorarioAtendimento, String tipo) {
        this.cliente = cliente;
        this.dentista = dentista;
        this.dataHorarioAtendimento = dataHorarioAtendimento;
        this.tipo = tipo;
        this.statusAtendimento = "Aguardando";
    }

    public void defineValor() {
        switch (tipo) {
            case "Orçamento":
                valor = 50;
                break;
            case "Canal":
                valor = 300;
                break;

            case "Restauração":
                valor = 200;
                break;
            case "Manutenção":
                valor = 120;
                break;

            case "Limpeza":
                valor = 300;
                break;

            default:
                break;
        }
    }


    /**
     * @return String
     */
    public String toString() {
        return String.format("Codigo Atendimento: %s\nCliente: %s\nDentista: %s\nStatus Atendimento: %s\nHorario: %s\nValor: %f",
                cod_atendimento, cliente.nome, dentista.nome, statusAtendimento, formataData.format(dataHorarioAtendimento), valor);
    }
}
