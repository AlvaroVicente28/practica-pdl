package es.grupo83.error;

/**
 * Representa el contexto del error: el lexema afectado, numero de linea, ...
 */
public class CtxError {
  private int nLinea;
  // Deben ser estrictamente mayores que 0
  private int colInicio, colFin;

  private String ctxArchivo;

  public CtxError(int nLinea, int colInicio, int colFin, String ctxArchivo) {
    this.nLinea = nLinea;
    this.colInicio = colInicio;
    this.colFin = colFin;
    this.ctxArchivo = ctxArchivo;
  }

  public int getLinea() {
    return this.nLinea;
  }

  public int getColInicio() {
    return this.colInicio;
  }

  public int getColFin() {
    return this.colFin;
  }

  public String getCtxArchivo() {
    return this.ctxArchivo;
  }
}
