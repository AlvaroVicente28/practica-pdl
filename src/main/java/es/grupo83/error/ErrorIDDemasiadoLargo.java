package es.grupo83.error;

public class ErrorIDDemasiadoLargo extends Error {
  private String lexemaIlegal;

  public ErrorIDDemasiadoLargo(String lexemaIlegal) {
    this.lexemaIlegal = lexemaIlegal;
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.ID_DEMASIADO_LARGO;
  }

  @Override
  public TipoError getTipoError() {
    return TipoError.LEXICO;
  }

  @Override
  public String getMsg() {
    return "ID Demasiado largo: " + this.lexemaIlegal;
  }
}
