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
    String repr = "" + this.chr;
    if(this.chr == ' ') {
      repr = "< >";
    }
    if(this.chr == '\n' || this.chr == '\r') {
      repr = "<SALTO-LINEA>";
    }
    if(this.chr == '\0') {
      repr = "<NIL>";
    }
    return "Caracter no esperado: " + repr;
  }
}
