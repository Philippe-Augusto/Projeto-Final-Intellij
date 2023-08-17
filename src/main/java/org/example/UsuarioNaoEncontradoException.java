package org.example;

import javax.swing.*;
public class UsuarioNaoEncontradoException extends RuntimeException {
    UsuarioNaoEncontradoException(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
