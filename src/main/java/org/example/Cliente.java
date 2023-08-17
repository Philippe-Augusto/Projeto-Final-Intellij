package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

public class Cliente {
    //Data de nascimento sera passado ao banco de dados, e a idade sera consultada a partir do banco
    Conexao c = new Conexao();
    int codigo;
    String nome;
    String cpf;
    char sexo;
    String telefone;
    String email;
    int idade;
    String endereco;
    Date dataDeNascimento;

    //Construtor para inserção no banco
    Cliente (String nome, String cpf, char sexo, String telefone, String email, Date dataDeNascimento, String endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.sexo = sexo;
        this.telefone = telefone;
        this.email = email;
        this.dataDeNascimento = dataDeNascimento;
        this.endereco = endereco;
    }

    //Construtor para impressão no código
    Cliente (int codigo, String nome, String cpf, char sexo, String telefone, String email, int idade, String endereco) {
        this.codigo = codigo;
        this.nome = nome;
        this.cpf = cpf;
        this.sexo = sexo;
        this.telefone = telefone;
        this.email = email;
        this.idade = idade;
        this.endereco = endereco;
    }


    /**
     * @return double
     */
    public double calculaDebitos() {
        try {
            String sql = String.format(
                    """
                    select c.nome, sum(a.valor) as debitos from atendimento a
                    join cliente c on c.cpf = a.cpf_cliente
                    left join pagamento p on p.cod_atendimento = a.cod
                    where c.cpf = '%s' and p.cod is null
                    group by c.nome
                    """, cpf);
            Statement stm = c.c.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            double valor = 0;

            while (rs.next()) {
                valor = rs.getDouble("debitos");
            }

            return valor;
        } catch (Exception e) {
            e.getMessage();
            return 0;
        }
    }

    public void historicoClinico() {
        try {
            String sql = String.format(
                    """
                    select cliente.nome, atendimento.cod, atendimento.tipo, TO_CHAR(atendimento.data_hora, 'DD/MM/YYYY HH24:MI') as data_hora, atendimento.valor from atendimento
                    join cliente on atendimento.cpf_cliente = cliente.cpf
                    where atendimento.data_hora < current_timestamp and cliente.cpf = '%s'
                    order by data_hora
                    """, cpf);

            Statement stm = c.c.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            String[] saidas = new String[20];
            int i = 0;
            while (rs.next()) {
                saidas[i] = String.format("Codigo Atendimento: %d - Nome: %s - Tipo: %s - Data-Hora: %s - Valor: %s", rs.getInt("cod"),
                        rs.getString("nome"), rs.getString("tipo"), rs.getString("data_hora"), rs.getInt("valor"));
                i++;
            }

            JOptionPane.showMessageDialog(null, saidas, "Historico Clinico", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void proximosAtendimentos() {
        try {
            String sql = String.format(
                    """
                    select cliente.nome, atendimento.cod, atendimento.tipo, TO_CHAR(atendimento.data_hora, 'DD/MM/YYYY HH24:MI') as data_hora, atendimento.valor from atendimento
                    join cliente on atendimento.cpf_cliente = cliente.cpf
                    where atendimento.data_hora > current_timestamp and cliente.cpf = '%s'
                    order by data_hora
                    """, cpf);

            Statement stm = c.c.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            String[] saidas = new String[10];
            int i = 0;

            while (rs.next()) {
                saidas[i] = String.format("Codigo Atendimento: %d - Nome: %s - Tipo: %s - Data-Hora: %s - Valor: %s", rs.getInt("cod"),
                        rs.getString("nome"), rs.getString("tipo"), rs.getString("data_hora"), rs.getInt("valor"));
                i++;
            }

            JOptionPane.showMessageDialog(null, saidas, "Proximos Atendimentos", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> atendimentosNaoPagos() {
        try {
            String sql = String.format(
                    """
                    select c.nome, a.cod, a.tipo, TO_CHAR(a.data_hora, 'DD/MM/YYYY HH24:MI') as data_hora, a.valor from atendimento a
                    join cliente c on c.cpf = a.cpf_cliente
                    left join pagamento p on p.cod_atendimento = a.cod
                    where c.cpf = '%s' and p.cod is null and a.data_hora < current_timestamp
                    order by a.data_hora
                    """, cpf);
            Statement stm = c.c.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            ArrayList<String> saidas = new ArrayList<>();

            while (rs.next()) {
                saidas.add(String.format("Codigo Atendimento: %d - Nome: %s - Tipo: %s - Data-Hora: %s - Valor: %s", rs.getInt("cod"),
                        rs.getString("nome"), rs.getString("tipo"), rs.getString("data_hora"), rs.getInt("valor")));
            }

            return saidas;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String toString() {
        return String.format("Codigo Cliente: %d\nNome: %s\nCPF: %s\nSexo: %c\nIdade: %d\nTelefone: %s\nEmail: %s\nEndereço: %s\n",
                codigo, nome, cpf, sexo, idade, telefone, email, endereco);
    }
}



