package es.grupo83.error;

public class ErrorOperadorSuma extends Error {
  @Override
  public CodigoError getCodigo() {
    return CodigoError.USO_OPERADOR_SUMA;
  }

  @Override
  public TipoError getTipoError() {
    return TipoError.LEXICO;
  }

  @Override
  public String getMsg() {
    return "No esta disponible el operador suma: '+'. Si esta disponible el preincremento: '++'";
  }

  @Override
  public boolean igual(Object other) {
    return other instanceof ErrorOperadorSuma;
  }
}
