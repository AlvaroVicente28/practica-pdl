package es.grupo83.error;

public class ErrorEnteroFueraRango extends Error {
  private long enteroIlegal;

  public ErrorEnteroFueraRango(long enteroIIlegal) {
    this.enteroIlegal = enteroIIlegal;
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.ENTERO_FUERA_RANGO;
  }

  @Override
  public TipoError getTipoError() {
    return TipoError.LEXICO;
  }

  @Override
  public String getMsg() {
    return "Entero ilegal: " + this.enteroIlegal;
  }
}
