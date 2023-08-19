package org.example;

//import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.util.Date;

public class Principal {
    /**
     */
    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        Cliente cliente;
        Dentista dentista;
        Funcionario funcionario;

        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formataDataHorario = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        while (true) {
            try {
                Object[] entradasPossiveis = { "Cadastrar Pessoa", "Atualizar dados Cadastrais (Cliente)", "Agendar um atendimento",
                        "Desmarcar um atendimento", "Visualizar Agenda", "Realizar pagamento", "Consultar", "Excluir Cliente", "Sair" };
                Object selectValue = JOptionPane.showInputDialog(null, "Escolha uma opção",
                        "Bem vindo", JOptionPane.INFORMATION_MESSAGE, null, entradasPossiveis, entradasPossiveis[0]);

                if (selectValue == null || selectValue.equals("Sair")) {
                    break;
                }

                else if (selectValue.equals("Cadastrar Pessoa")) {
                    String[] objs = {"Cliente", "Dentista", "Secretaria"};

                    selectValue = JOptionPane.showInputDialog(null, "Escolha a pessoa a ser cadastrado",
                            "Ateliê Odontológico", JOptionPane.INFORMATION_MESSAGE, null, objs, objs[0]);

                    if (selectValue.equals("Cliente")) {
                        String nomeCliente = JOptionPane.showInputDialog("Nome do cliente: ");
                        String cpf = JOptionPane.showInputDialog("CPF: ");
                        char sexo = JOptionPane.showInputDialog("Genero: (M) | (F)").charAt(0);
                        String telefone = JOptionPane.showInputDialog("Telefone: ");
                        String email = JOptionPane.showInputDialog("Email: ");
                        String dataDeNascimentoProv = JOptionPane.showInputDialog("Data de Nascimento: ");
                        String endereco = JOptionPane.showInputDialog("Endereço: ");
                        Date dataDeNascimento = formataData.parse(dataDeNascimentoProv);

                        cliente = new Cliente(nomeCliente, cpf, sexo, telefone, email, dataDeNascimento, endereco);
                        conexao.inserirObjeto(cliente);
                    }

                    else if (selectValue.equals("Dentista")) {
                        String nomeDentista = JOptionPane.showInputDialog("Nome do Dentista: ");
                        int CRO = Integer.parseInt(JOptionPane.showInputDialog("CRO: "));
                        String cpf = JOptionPane.showInputDialog("CPF: ");

                        funcionario = new Dentista(nomeDentista, cpf, CRO);
                        conexao.inserirObjeto(funcionario);
                    }

                    else if (selectValue.equals("Secretaria")) {
                        String nomeSecretaria = JOptionPane.showInputDialog("Nome da Secretaria: ");
                        double salario = Double.parseDouble(JOptionPane.showInputDialog("Salario da Secretaria: "));
                        String cpf = JOptionPane.showInputDialog("CPF: ");

                        funcionario = new Secretaria(nomeSecretaria, cpf, salario);
                        conexao.inserirObjeto(funcionario);
                    }
                }

                else if (selectValue.equals("Atualizar dados Cadastrais (Cliente)")) {
                    String nomeCliente = JOptionPane.showInputDialog("Insira o nome do cliente: ");
                    cliente = conexao.buscaCliente(nomeCliente);

                    if (cliente == null) {
                        throw new UsuarioNaoEncontradoException("Usuario não encontrado para o nome: " + nomeCliente);
                    }

                    String[] saidas = {"Telefone", "Email", "Endereço"};

                    Object dado = JOptionPane.showInputDialog(null, "Escolha o dado a ser alterado",
                            "Ateliê Odontológico", JOptionPane.QUESTION_MESSAGE, null, saidas, saidas[0]);

                    if (dado.equals("Telefone")) {
                        String telefone = JOptionPane.showInputDialog(null, "Insira o novo telefone");
                        String sql = String.format("UPDATE cliente SET telefone = '%s' where nome = '%s'", telefone, nomeCliente);

                        conexao.executaSQL(sql);
                    }

                    else if (dado.equals("Email")) {
                        String email = JOptionPane.showInputDialog(null, "Insira o novo email");
                        String sql = String.format("UPDATE cliente SET email = '%s' where nome = '%s'", email, nomeCliente);

                        conexao.executaSQL(sql);
                    }

                    else if (dado.equals("Endereço")) {
                        String endereco = JOptionPane.showInputDialog(null, "Insira o novo endereco");
                        String sql = String.format("UPDATE cliente SET endereco = '%s' where nome = '%s'", endereco, nomeCliente);

                        conexao.executaSQL(sql);
                    }

                    else {
                        JOptionPane.showMessageDialog(null, "Selecione uma opção válida");
                    }

                }

                else if (selectValue.equals("Excluir Cliente")) {
                    String nomeCliente = JOptionPane.showInputDialog("Nome do cliente: ");

                    cliente = conexao.buscaCliente(nomeCliente);

                    if (cliente == null) {
                        throw new UsuarioNaoEncontradoException("Usuario não encontrado para o nome: " + nomeCliente);
                    }

                    int opcao = JOptionPane.showConfirmDialog(null, "Você confirma excluir o Cliente:\n" + cliente);

                    if (opcao == JOptionPane.YES_OPTION) {
                        String sql = String.format("""
                        DELETE FROM pagamento
                        WHERE cod_atendimento IN (SELECT cod
                                                    FROM atendimento
                                                    WHERE cpf_cliente = '%s');
                        
                        DELETE FROM atendimento
                            WHERE cpf_cliente = '%s';
                        
                        DELETE FROM cliente
                            WHERE cpf = '%s';
                            """, cliente.cpf, cliente.cpf, cliente.cpf);
                        conexao.executaSQL(sql);
                    }

                }

                else if (selectValue.equals("Agendar um atendimento")) {
                    String nomeCliente = JOptionPane.showInputDialog("Insira o nome do cliente: ");
                    cliente = conexao.buscaCliente(nomeCliente);

                    if (cliente == null) {
                        throw new UsuarioNaoEncontradoException("Usuario não encontrado para o nome: " + nomeCliente);
                    }

                    String dataProv = JOptionPane.showInputDialog(null,
                            "Insira a data desejada (dd/MM/yyyy)");
                    Date data = formataData.parse(dataProv);

                    JOptionPane.showMessageDialog(null,"Escolha um horário disponível");
                    conexao.exibeHorariosDia(formataData.format(data));

                    String horarioProv = JOptionPane.showInputDialog(null,
                            "Insira horario do atendimento (hh:mm:ss)");

                    String dataHorarioProv = dataProv + " " + horarioProv;
                    Date dataHorario = formataDataHorario.parse(dataHorarioProv);

                    dentista = (Dentista)JOptionPane.showInputDialog(null, "Escolha a dentista", "Dentistas",
                            JOptionPane.QUESTION_MESSAGE, null, conexao.leituraDentistas().toArray(), conexao.leituraDentistas().get(0));

                    String[] tipos = {"Orçamento", "Canal", "Restauração", "Manutenção", "Limpeza"};

                    String tipo = (String)JOptionPane.showInputDialog(null, "Escolha o procedimento", "Ateliê Odontológico",
                            JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);

                    Atendimento atendimento = new Atendimento(cliente, dentista, dataHorario, tipo);

                    atendimento.defineValor();
                    conexao.inserirObjeto(atendimento);
                }

                else if (selectValue.equals("Desmarcar um atendimento")) {
                    String nomeCliente = JOptionPane.showInputDialog("Insira o nome do cliente: ");
                    cliente = conexao.buscaCliente(nomeCliente);

                    if (cliente == null) {
                        throw new UsuarioNaoEncontradoException("Usuario não encontrado para o nome: " + nomeCliente);
                    }

                    cliente.proximosAtendimentos();

                    int codigo = Integer.parseInt(JOptionPane.showInputDialog("Insira o codigo do atendimento a ser desmarcado: "));

                    conexao.desmarcarAtendimento(codigo);

                }

                else if (selectValue.equals("Visualizar Agenda")) {
                    String dataP = JOptionPane.showInputDialog(null, "Data: (dd/MM/yyyy)");
                    conexao.exibeHorariosDia(dataP);
                }

                else if (selectValue.equals("Realizar pagamento")) {
                    String nomeCliente = JOptionPane.showInputDialog("Nome do cliente: ");

                    cliente = conexao.buscaCliente(nomeCliente);

                    if (cliente == null) {
                        throw new UsuarioNaoEncontradoException("Usuario não encontrado para o nome: " + nomeCliente);
                    }

                    if (cliente.atendimentosNaoPagos().isEmpty()) {
                        String atendimento = (String)JOptionPane.showInputDialog(null, "Escolha o atendimento a ser pago", "Atendimentos",
                                JOptionPane.QUESTION_MESSAGE, null, cliente.atendimentosNaoPagos().toArray(), cliente.atendimentosNaoPagos().get(0));

                        int cod_atendimento = Integer.parseInt(atendimento.substring(20, 22));

                        String[] tiposPg = {"Cartao (Débito)", "Cartao (Crédito)", "Pix", "Dinheiro"};
                        String tipoPg = (String)JOptionPane.showInputDialog(null, "Escolha o modo de pagamento", "Pagamento",
                                JOptionPane.QUESTION_MESSAGE, null, tiposPg, tiposPg[0]);

                        Pagamento pg = new Pagamento(cod_atendimento, tipoPg);

                        conexao.inserirObjeto(pg);
                    }

                    else {
                        JOptionPane.showMessageDialog(null, "O cliente não possui débitos");
                    }
                }

                else if (selectValue.equals("Consultar")) {
                    String[] obj = {"Cliente", "Dentista", "Secretária"};

                    selectValue = JOptionPane.showInputDialog(null, "Escolha o objeto a ser consultado",
                            "Ateliê Odontológico", JOptionPane.INFORMATION_MESSAGE, null, obj, obj[0]);

                    if (selectValue.equals("Cliente")) {
                        String nomeCliente = JOptionPane.showInputDialog("Nome do cliente: ");
                        cliente = conexao.buscaCliente(nomeCliente);

                        if (cliente == null) {
                            throw new UsuarioNaoEncontradoException("Usuario não encontrado para o nome: " + nomeCliente);
                        }

                        String[] possiveisConsultas = {"Dados", "Débitos", "Próximos Atendimentos", "Histórico Clínico"};

                        selectValue = JOptionPane.showInputDialog(null, "Escolha o dado a ser consultado",
                                cliente.nome, JOptionPane.INFORMATION_MESSAGE, null, possiveisConsultas, possiveisConsultas[0]);

                        if (selectValue.equals("Dados")) {
                            JOptionPane.showMessageDialog(null, cliente);
                        }

                        else if (selectValue.equals("Débitos")) {
                            JOptionPane.showMessageDialog(null, "Valor total de Débitos: " + cliente.calculaDebitos());
                        }

                        else if (selectValue.equals("Próximos Atendimentos")) {
                            cliente.proximosAtendimentos();
                        }

                        else if (selectValue.equals("Histórico Clínico")) {
                            cliente.historicoClinico();
                        }
                    }

                    else if (selectValue.equals("Dentista")) {
                        dentista = (Dentista)JOptionPane.showInputDialog(null, "Escolha a dentista",
                                "Dentistas", JOptionPane.QUESTION_MESSAGE, null, conexao.leituraDentistas().toArray(), conexao.leituraDentistas().get(0));

                        JOptionPane.showMessageDialog(null, dentista);
                    }

                    else if (selectValue.equals("Secretária")) {
                        Funcionario f = (Funcionario)JOptionPane.showInputDialog(null, "Escolha a funcionaria",
                                "Funcionarios", JOptionPane.QUESTION_MESSAGE, null, conexao.leituraSecretarias().toArray(), conexao.leituraSecretarias().get(0));

                        JOptionPane.showMessageDialog(null, f);
                    }
                }

                else {
                    JOptionPane.showMessageDialog(null, "Selecione uma opção válida");
                }
            } catch (ParseException exception) {
                JOptionPane.showMessageDialog(null, "Data Invalida");
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
}

