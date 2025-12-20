package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorSwitchNoEntero extends ErrorSemantico {
  private String tipoRecibido;

  public ErrorSwitchNoEntero(String tipoRecibido) {
    this.tipoRecibido = tipoRecibido;
  }

  @Override
  public void rellenarConTS(TablaSimbolos ts) { }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.EXPR_SWITCH_NO_ENTERA;
  }

  @Override
  public String getMsg() {
    return "La expresion del 'switch' es de tipo '" + this.tipoRecibido + "', no entera";
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorSwitchNoEntero otherErr)) {
      return false;
    }
    return this.tipoRecibido.equals(otherErr.tipoRecibido);
  }
}
