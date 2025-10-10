package es.grupo83.afd;

public class Transicion {
  private int estadoFinal;

  private AccionSemantica accionSemantica;

  public Transicion(int estadoFinal, AccionSemantica accionSemantica) {
    this.estadoFinal = estadoFinal;
    this.accionSemantica = accionSemantica;
  }

  public int getEstadoFinal() {
    return this.estadoFinal;
  }

  public AccionSemantica getAccionSemantica() {
    return this.accionSemantica;
  }
}
