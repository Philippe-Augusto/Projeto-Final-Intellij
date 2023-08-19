package org.example;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class Conexao implements InterfaceConexao{
    Connection c;
    SimpleDateFormat formataDataHorario = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    Conexao() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ATELIE_ODONTOLOGICO",
                    "postgres", "philippe09");
            //JOptionPane.showMessageDialog(null, "Conexão realizada com sucesso!!");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Driver do banco não encontrado");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao acessar o banco: " + ex.getMessage());
        }
    }

    /**
     * @return ArrayList<Funcionario>
     */
    public ArrayList<Funcionario> leituraDentistas() {
        ArrayList<Funcionario> funcionarios = new ArrayList<>();
        try {
            Statement stm = c.createStatement();
            String sql = "select * from dentista";
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                int codigo = rs.getInt("cod");
                int cro = rs.getInt("cro");
                funcionarios.add(new Dentista(codigo, nome, cpf, cro));
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return funcionarios;
    }

    public ArrayList<Funcionario> leituraSecretarias() {
        ArrayList<Funcionario> funcionarios = new ArrayList<>();
        try {
            Statement stm = c.createStatement();
            String sql = "select * from secretaria";
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                int codigo = rs.getInt("cod");
                double salario = rs.getInt("salario");
                funcionarios.add(new Secretaria(codigo, nome, cpf, salario));
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return funcionarios;
    }

    public Cliente buscaCliente(String nomeCliente) {
        Cliente cliente = null;
        try {
            Statement stm = c.createStatement();
            String sql = String.format("SELECT * FROM CLIENTE WHERE NOME = '%s'", nomeCliente);

            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                int cod_cliente = rs.getInt("cod");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                char sexo = rs.getString("sexo").charAt(0);
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                int idade = rs.getInt("idade");
                //String endereco = rs.getString("endereco");

                cliente = new Cliente(cod_cliente, nome, cpf, sexo, telefone, email, idade, null);
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return cliente;
    }

    public int exibeHorariosDia(String data) {
        try {
            String[] saidas = new String[10];
            int i = 0;
            String sql = String.format("SELECT * FROM agenda WHERE TO_CHAR(data_hora, 'DD/MM/YYYY') LIKE '%s' order by data_hora", data);
            Statement stm = c.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            JOptionPane.showMessageDialog(null, String.format("Horários do dia: %s", data));

            while (rs.next()) {
                saidas[i] = (rs.getTime("data_hora") + " - " + rs.getString("status"));
                i++;
            }
            JOptionPane.showMessageDialog(null, saidas, "Horarios", JOptionPane.INFORMATION_MESSAGE);
            return i;
        } catch (Exception e) {
            e.getMessage();
            return 0;
        }
    }

    public void inserirObjeto (Object object) {
        try {
            if (object instanceof Cliente) {
                Cliente cliente = (Cliente)object;
                String sql = (String.format("INSERT INTO cliente VALUES (default, '%s', '%s', '%c', '%s', '%s', '%s', default, null)",
                        cliente.nome, cliente.cpf, cliente.sexo, cliente.telefone, cliente.email, cliente.dataDeNascimento));

                if (executaSQL(sql) > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar cliente");
                }
            }

            else if (object instanceof Dentista) {
                Dentista dentista = (Dentista)object;
                String sql = (String.format("INSERT INTO dentista VALUES (default, '%s', '%s', %d)",
                        dentista.nome, dentista.cpf, dentista.getCRO()));

                if (executaSQL(sql) > 0) {
                    JOptionPane.showMessageDialog(null, "Dentista cadastrado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar dentista");
                }
            }

            else if (object instanceof Secretaria) {
                Secretaria secretaria = (Secretaria)object;
                String sql = (String.format("INSERT INTO secretaria VALUES (default, '%s', '%s', %.0f)",
                        secretaria.nome, secretaria.cpf, secretaria.salario));

                if (executaSQL(sql) > 0) {
                    JOptionPane.showMessageDialog(null, "Secretaria cadastrada com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar secretária");
                }
            }

            else if (object instanceof Atendimento) {
                Atendimento atendimento = (Atendimento)object;
                String sql = String.format("INSERT INTO atendimento VALUES (default, '%s', '%s', '%s', %d)",
                        atendimento.tipo, atendimento.dataHorarioAtendimento, atendimento.cliente.cpf, atendimento.valor);

                if (executaSQL(sql) > 0) {
                    JOptionPane.showMessageDialog(null, "Atendimento cadastrado com sucesso!");

                    sql =  String.format("select atualizar_status('%s')", atendimento.dataHorarioAtendimento);
                    executaSQL(sql);
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar atendimento");
                }
            }

            else if (object instanceof Pagamento) {
                Pagamento pagamento = (Pagamento)object;

                String sql = String.format("INSERT INTO pagamento VALUES (default, %d, '%s', '%s')",
                        pagamento.cod_atendimento, pagamento.tipoPagamento, formataDataHorario.format(pagamento.dataHoraPagamento));

                if (executaSQL(sql) > 0) {
                    JOptionPane.showMessageDialog(null, "Pagamento realizado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao realizar pagamento");
                }
            }

            else {
                JOptionPane.showMessageDialog(null, "Objeto não cadastrado");
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void desmarcarAtendimento(int cod_atendimento) {
        String sql = String.format("delete from atendimento where cod = '%d'", cod_atendimento);

        if (executaSQL(sql) > 0) {
            JOptionPane.showMessageDialog(null, "Atendimento excluído com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao excluir atendimento");
        }
    }

    public int executaSQL(String sql) {
        try {
            Statement stm = c.createStatement();
            return stm.executeUpdate(sql);
        } catch (Exception e) {
            e.getMessage(); //trocar para getMessage()
            return 0;
        }
    }
}
