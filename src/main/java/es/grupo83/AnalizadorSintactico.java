package es.grupo83;

import es.grupo83.error.GestorErrores;

import java.io.IOException;
import java.util.ArrayList;

public class AnalizadorSintactico {
  private AnalizadorLexico alex;

  private GestorErrores gestorErrores;

  private ArrayList<Token> listaTokens = new ArrayList<>();

  public AnalizadorSintactico(GestorErrores gestorErrores, AnalizadorLexico alex) {
    this.alex = alex;
    this.gestorErrores = gestorErrores;
  }

  public void run() throws IOException {
    Token token;
    do {
      token = this.alex.nextToken();
      System.out.println("Token encontrado: " + token.getTipoToken().ordinal()
              + "(" + token.getTipoToken().name() + ") con atributo: " + token.getAttr());
      this.listaTokens.add(token);        
    }
    while(token.getTipoToken() != Token.TipoToken.EOF);
  }

  public ArrayList<Token> getListaTokens() {
    return this.listaTokens;
  }
}
