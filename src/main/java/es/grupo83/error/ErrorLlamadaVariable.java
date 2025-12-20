package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorLlamadaVariable extends ErrorSemantico {
  private String nombreVariable;

  private int posVariable;

  public ErrorLlamadaVariable(int posVariable, TablaSimbolos ts) {
    this.posVariable = posVariable;
    this.rellenarConTS(ts);
  }

  @Override
  public void rellenarConTS(TablaSimbolos ts) {
    this.nombreVariable = ts.recuperarLexema(this.posVariable);
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.LLAMADA_A_VARIABLE;
  }

  @Override
  public String getMsg() {
    return "Se ha intentado llamar a la variable '" + this.nombreVariable + "', que no es una funcion";
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorLlamadaVariable)) {
      return false;
    }
    return this.nombreVariable.equals(((ErrorLlamadaVariable) other).nombreVariable);
  }
}
