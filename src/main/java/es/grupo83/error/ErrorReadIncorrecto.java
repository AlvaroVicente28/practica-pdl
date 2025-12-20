package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorReadIncorrecto extends ErrorSemantico {
  private String nombreVariable;
  private String tipoVariable;

  private int posVariable;

  public ErrorReadIncorrecto(int posVariable, TablaSimbolos ts) {
    this.posVariable = posVariable;
    this.rellenarConTS(ts);
  }

  @Override
  public void rellenarConTS(TablaSimbolos ts) {
    this.nombreVariable = ts.recuperarLexema(this.posVariable);
    this.tipoVariable = ts.buscarTipoTS(this.posVariable).nombre();
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.READ_INCORRECTO;
  }

  @Override
  public String getMsg() {
    return "Sentencia read incorrecta: el tipo de la variable '" + this.nombreVariable + "' es '" + this.tipoVariable + "'";
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorReadIncorrecto otherErr)) {
      return false;
    }
    return this.nombreVariable.equals(otherErr.nombreVariable);
  }
}
