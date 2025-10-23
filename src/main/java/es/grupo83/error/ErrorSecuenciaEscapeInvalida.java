package es.grupo83.error;

public class ErrorSecuenciaEscapeInvalida extends Error {
  private char secuencia;

  public ErrorSecuenciaEscapeInvalida(char secuencia) {
    this.secuencia = secuencia;
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.ESCAPE_INVALIDO;
  }

  @Override
  public TipoError getTipoError() {
    return TipoError.LEXICO;
  }

  @Override
  public String getMsg() {
    String repr = "" + this.secuencia;
    if(this.secuencia == ' ') {
      repr = "<ESPACIO>";
    }
    if(this.secuencia == '\t') {
      repr = "<TAB>";
    }
    if(this.secuencia == '\n' || this.secuencia == '\r') {
      repr = "<SALTO-LINEA>";
    }
    if(this.secuencia == '\0') {
      repr = "<NIL>";
    }

    return "Secuencia de escape invalida: " + repr;
  }
}
