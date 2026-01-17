package es.grupo83.error;

/**
 * Representa el contexto del error: el lexema afectado, numero de linea, ...
 */
public class CtxError {
  private int nLinea;
  // Deben ser estrictamente mayores que 0
  private int colInicio, colFin;

  /**
   * Linea donde se lanza el error
   */
  private String ctxArchivo;

  /**
   * Nombre de la funcion donde se lanza el error
   */
  private String nombreFuncion;

  public CtxError(int nLinea, int colInicio, int colFin, String ctxArchivo, String nombreFuncion) {
    this.nLinea = nLinea;
    this.colInicio = colInicio;
    this.colFin = colFin;
    this.ctxArchivo = ctxArchivo;
    this.nombreFuncion = nombreFuncion;
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

  public String getNombreFuncion() {
    return this.nombreFuncion;
  }

  @Override
  public boolean equals(Object other) {
    if(this == other) {
      return true;
    }
    if(other == null) {
      return false;
    }
    if(!(other instanceof CtxError)) {
      return false;
    }
    // Solo comparamos la linea
    return this.nLinea == ((CtxError) other).nLinea;
  }
}
