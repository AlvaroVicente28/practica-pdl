package es.grupo83;

import es.grupo83.afd.AutomataFinito;
import es.grupo83.afd.Transicion;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AnalizadorLexico {
  private AutomataFinito afd;

  private FileReader lectorFichero;

  private char chrActual;

  public AnalizadorLexico(AutomataFinito afd, String filePath) throws IOException {
    this.afd = afd;

    FileReader lector = new FileReader(filePath);
    this.lectorFichero = lector;

    int read = this.lectorFichero.read();
    // TODO: EOF ES '\0'
    this.chrActual = read == -1 ? AutomataFinito.EOF : (char) read;
  }

  public Token nextToken() throws IOException {
    Transicion tr = this.afd.transicionar(this.chrActual);
    if(tr == null) {
      // TODO: Lanzar error
      return null;
    }
    int accionSem = tr.getAccionSemantica();
    switch (accionSem) {
      // TODO: Manejar accion
    }

    return null;
  }

}
