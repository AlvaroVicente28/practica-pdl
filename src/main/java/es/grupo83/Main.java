package es.grupo83;

import es.grupo83.afd.AutomataFinito;
import es.grupo83.afd.ImplementacionAFD;
import es.grupo83.error.GestorErrores;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    if(args.length != 1) {
      System.err.println("Se necesita exactamente un argumento");
      System.exit(-1);
    }
    String filePath = args[0];

    AutomataFinito afd = ImplementacionAFD.getAutomataFinito();

    GestorErrores gestorErrores = new GestorErrores();
    TablaSimbolos tablaSimbolos = new TablaSimbolos();

    try(AnalizadorLexico alex = new AnalizadorLexico(gestorErrores, tablaSimbolos, afd, filePath)) {
      AnalizadorSintactico asint = new AnalizadorSintactico(gestorErrores, alex);
      asint.run();
      escribirArchivoTokens(asint.getListaTokens());
      escribirParse(asint.getParse());
      System.out.println("ANALISIS CORRECTO");
    }
    catch (FileNotFoundException e) {
      System.err.println("Archivo no encontrado: " + filePath);
    }
    catch (IOException e) {
      System.err.println("Error al leer archivo: " + e.getMessage());
      e.printStackTrace();
    }
    tablaSimbolos.finTablaSimbolos();
  }

  private static void escribirArchivoTokens(Iterable<Token> tokens) throws IOException {
    FileWriter writer = new FileWriter("tokens.txt");
    for (Token token : tokens) {
      Object attr = token.getAttr();
      String atributeStr;
      if (attr == null) {
        atributeStr = "";
      }
      else if (attr instanceof Token.ORIENTACION_SIMBOLO) {
        atributeStr = ((Token.ORIENTACION_SIMBOLO) attr).ordinal() + "";
      }
      else if (token.getTipoToken() == Token.TipoToken.CTE_CADENA) {
        atributeStr = "\"" + attr.toString() + "\"";
      }
      else {
        atributeStr = attr.toString();
      }
      writer.write("<" + token.getTipoToken().ordinal() + ", " + atributeStr + ">\r\n");
    }
    writer.flush();
    writer.close();
  }

  private static void escribirParse(Iterable<Integer> parse) throws IOException {
    try(FileWriter writer = new FileWriter("parse.txt")) {
      writer.write("D");
      for(Integer regla : parse) {
        writer.write(" " + regla);
      }
      writer.flush();
    }
  }
}
