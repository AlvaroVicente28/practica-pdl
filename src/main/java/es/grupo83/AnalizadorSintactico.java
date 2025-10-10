package es.grupo83;

import es.grupo83.error.GestorErrores;

import java.io.IOException;

public class AnalizadorSintactico {
  private AnalizadorLexico alex;

  private GestorErrores gestorErrores;

  public AnalizadorSintactico(GestorErrores gestorErrores, AnalizadorLexico alex) {
    this.alex = alex;
    this.gestorErrores = gestorErrores;
  }

  public void run() throws IOException {
    // TODO: Llamar al lexico
  }
}
