package es.grupo83.error;

public class ErrorRealFueraRango extends Error {
  private double realIlegal;

  public ErrorRealFueraRango(double realIlegal) {
    this.realIlegal = realIlegal;
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.REAL_FUERA_RANGO;
  }

  @Override
  public TipoError getTipoError() {
    return TipoError.LEXICO;
  }

  @Override
  public String getMsg() {
    return "Real fuera de rango: " + this.realIlegal;
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorRealFueraRango)) {
      return false;
    }
    return this.realIlegal == ((ErrorRealFueraRango) other).realIlegal;
  }
}
