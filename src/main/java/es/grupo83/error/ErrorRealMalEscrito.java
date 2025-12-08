package es.grupo83.error;

public class ErrorRealMalEscrito extends Error {
  private long numeroActual;
  private char chrInvalido;

  public ErrorRealMalEscrito(long numeroActual, char chrInvalido) {
    this.numeroActual = numeroActual;
    this.chrInvalido = chrInvalido;
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.REAL_MAL_ESCRITO;
  }

  @Override
  public TipoError getTipoError() {
    return TipoError.LEXICO;
  }

  @Override
  public String getMsg() {
    String repr = "" + this.chrInvalido;
    if(this.chrInvalido == ' ') {
      repr = "<ESPACIO>";
    }
    if(this.chrInvalido == '\t') {
      repr = "<TAB>";
    }
    if(this.chrInvalido == '\n' || this.chrInvalido == '\r') {
      repr = "<SALTO-LINEA>";
    }
    if(this.chrInvalido == '\0') {
      repr = "<NIL>";
    }

    return "Real mal escrito: " + numeroActual + "." + repr;
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorRealMalEscrito)) {
      return false;
    }
    return this.numeroActual == ((ErrorRealMalEscrito) other).numeroActual
            && this.chrInvalido == ((ErrorRealMalEscrito) other).chrInvalido;
  }
}
