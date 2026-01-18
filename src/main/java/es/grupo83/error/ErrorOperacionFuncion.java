package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorOperacionFuncion extends ErrorSemantico {
  private int posTSFunc;

  private String nombreFuncion;

  public ErrorOperacionFuncion(int posTSFunc, TablaSimbolos ts) {
    this.posTSFunc = posTSFunc;
    this.rellenarConTS(ts);
  }

  @Override
  public void rellenarConTS(TablaSimbolos ts) {
    this.nombreFuncion = ts.recuperarLexema(this.posTSFunc);
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.OPERANDO_CON_FUNCION;
  }

  @Override
  public String getMsg() {
    return "Se ha intentado operar con una funcion: '" + this.nombreFuncion + "'";
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorOperacionFuncion otherErr)) {
      return false;
    }
    return this.posTSFunc == otherErr.posTSFunc;
  }
}
