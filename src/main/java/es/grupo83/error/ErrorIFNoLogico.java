package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorIFNoLogico extends ErrorSemantico {
  private String tipoRecibido;

  public ErrorIFNoLogico(String tipoRecibido) {
    this.tipoRecibido = tipoRecibido;
  }

  @Override
  public void rellenarConTS(TablaSimbolos ts) { }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.EXPR_IF_NO_LOGICA;
  }

  @Override
  public String getMsg() {
    return "La expresion del 'if' es de tipo '" + this.tipoRecibido + "', no logica";
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorIFNoLogico otherErr)) {
      return false;
    }
    return this.tipoRecibido.equals(otherErr.tipoRecibido);
  }
}
