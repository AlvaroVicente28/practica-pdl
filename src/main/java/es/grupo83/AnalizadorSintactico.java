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
    Token token;
    do {
      token = this.alex.nextToken();
      System.out.println("Token encontrado: " + token.getTipoToken().name() + " con atributo: " + token.getAttr());
    }
    while(token.getTipoToken() != Token.TipoToken.EOF);
  }
}
