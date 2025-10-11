package es.grupo83.error;

import java.util.ArrayList;

import es.grupo83.util.Pair;

public class GestorErrores {
  private int lineaActual;

  private ArrayList<Pair<Error, Integer>> errores;

  public GestorErrores() {
    this.errores = new ArrayList<>();
  }

  public void pasarLinea() {
    ++this.lineaActual;
  }

  public void agregarError(Error error) {
    this.errores.add(new Pair<>(error, this.lineaActual));
  }

  public String mensajes() {
    String res = "";

    for(Pair<Error, Integer> error : this.errores) {
      res += "ERROR (linea " + error.getSecond() + ") " + error.getFirst() + "\n";
    }

    return res;
  }

  /**
   * Si existen errores reportados, aborta el programa.
   * EOC, es NO-OP
   */
  public void pararEnError() {
    if(!this.errores.isEmpty()) {
      System.out.println(this.mensajes());
      System.exit(-1);
    }
  }
}
