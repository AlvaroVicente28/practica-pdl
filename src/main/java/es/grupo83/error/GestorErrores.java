package es.grupo83.error;

import java.util.ArrayList;

public class GestorErrores {
  private int lineaActual;

  private ArrayList<Error> errores;

  public void pasarLinea() {
    ++this.lineaActual;
  }

  public void agregarError(Error error) {
    // TODO
  }
}
