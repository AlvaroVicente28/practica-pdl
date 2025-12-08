package es.grupo83.error;

public class ErrorCadenaDemasiadoLarga extends Error {
  private String cadenaIlegal;

  public ErrorCadenaDemasiadoLarga(String cadenaIlegal) {
    this.cadenaIlegal = cadenaIlegal;
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.CADENA_DEMASIADO_LARGA;
  }

  @Override
  public TipoError getTipoError() {
    return TipoError.LEXICO;
  }

  @Override
  public String getMsg() {
    return "Cadena demasiado larga: " + this.cadenaIlegal + " - Longitud = " + this.cadenaIlegal.length();
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorCadenaDemasiadoLarga)) {
      return false;
    }
    ErrorCadenaDemasiadoLarga otherErr = (ErrorCadenaDemasiadoLarga) other;
    return this.cadenaIlegal.equals(otherErr.cadenaIlegal);
  }
}
