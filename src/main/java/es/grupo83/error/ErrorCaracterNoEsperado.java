package es.grupo83.error;

public class ErrorCaracterNoEsperado extends Error {
  private char chr;

  public ErrorCaracterNoEsperado(char chr) {
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
    return "Caracter no esperado: " + this.chr;
  }
}
