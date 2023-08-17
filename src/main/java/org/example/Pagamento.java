package org.example;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pagamento {
    int cod_atendimento;
    Date dataHoraPagamento;
    String tipoPagamento; // Cartao (Debito/Credito) - Dinheiro - Pix

    SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    Pagamento(int cod_atendimento, String tipoPagamento) {
        this.cod_atendimento = cod_atendimento;
        this.dataHoraPagamento = new Date();
        this.tipoPagamento = tipoPagamento;
    }


    /**
     * @return String
     */
    public String toString() {
        return String.format("\nData-Horario Pagamento: %s\nTipo: %s", formataData.format(dataHoraPagamento), tipoPagamento);
    }
}

