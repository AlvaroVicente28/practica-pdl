package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorWriteIncorrecto extends ErrorSemantico {
  private String tipoRecibido;

  public ErrorWriteIncorrecto(String tipoRecibido) {
    this.tipoRecibido = tipoRecibido;
  }

  @Override
  public void rellenarConTS(TablaSimbolos ts) { }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.WRITE_INCORRECTO;
  }

  @Override
  public String getMsg() {
    return "Llamada incorrecta a write: el tipo recibido es '" + this.tipoRecibido + "'";
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorWriteIncorrecto otherErr)) {
      return false;
    }
    return this.tipoRecibido.equals(otherErr.tipoRecibido);
  }
}
