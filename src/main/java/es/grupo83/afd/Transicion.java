package es.grupo83.afd;

public class Transicion {
  private int estadoFinal;

  private int accionSemantica;

  public Transicion(int estadoFinal, int accionSemantica) {
    this.estadoFinal = estadoFinal;
    this.accionSemantica = accionSemantica;
  }

  public int getEstadoFinal() {
    return this.estadoFinal;
  }

  public int getAccionSemantica() {
    return this.accionSemantica;
  }
}
