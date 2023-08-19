package org.example;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ConexaoTest {
    Conexao conexaoTeste = new Conexao();

    @Test
    public void testeBuscaCliente() {
        String nomeCliente = "Philippe";

        Cliente cliente = conexaoTeste.buscaCliente(nomeCliente);
        assertNotNull(cliente);
    }
    @Test
    public void testeExibeHorarios() {
        assertEquals(10,conexaoTeste.exibeHorariosDia("17/08/2023"));
    }

    @Test
    public void testeLeituraDentistas() {
        assertFalse(conexaoTeste.leituraDentistas().isEmpty());
    }
}