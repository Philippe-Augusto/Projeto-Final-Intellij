package org.example;
import java.util.ArrayList;

public interface InterfaceConexao {
    ArrayList<Funcionario> leituraDentistas();
    ArrayList<Funcionario> leituraSecretarias();
    Cliente buscaCliente(String nomeCliente);
    int exibeHorariosDia(String data);
    void inserirObjeto (Object object);
    void desmarcarAtendimento(int cod_atendimento);
    int executaSQL(String sql);
}

