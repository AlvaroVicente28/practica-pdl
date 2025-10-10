package es.grupo83.error;

public class ErrorNoReconocido extends Error {
  private char chr;

  public ErrorNoReconocido(char chr) {
    this.chr = chr;
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.CARACTER_NO_RECONOCIDO;
  }

  @Override
  public TipoError getTipoError() {
    return  TipoError.LEXICO;
  }

  @Override
  public String getMsg() {
    return "Caracter no reconocido: " + this.chr;
  }
}
