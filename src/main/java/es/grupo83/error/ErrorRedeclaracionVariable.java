package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorRedeclaracionVariable extends ErrorSemantico {
  private int posVariableOriginalTS;

  private String idVariableOriginal;
  private String tipoVariableOriginal;

  private boolean esGlobal;

  public ErrorRedeclaracionVariable(int pos, boolean esGlobal) {
    this.posVariableOriginalTS = pos;
    this.idVariableOriginal = null;
    this.tipoVariableOriginal = null;
    this.esGlobal = esGlobal;
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.REDECLARACION_VARIABLE;
  }

  @Override
  public String getMsg() {
    String res = "Variable " + (this.esGlobal ? "global " : "") + "redeclarada con nombre '" + this.idVariableOriginal + "'";
    if("funcion".equals(this.tipoVariableOriginal)) {
      res += "\nOBS: La variable fue declarada como funcion";
    } else {
      res += "\nOBS: La variable fue declarada con tipo '" + this.tipoVariableOriginal + "'";
    }
    return res;
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorRedeclaracionVariable)) {
      return false;
    }
    ErrorRedeclaracionVariable err = (ErrorRedeclaracionVariable) other;
    if(this.idVariableOriginal == null) {
      return err.idVariableOriginal == null;
    }
    if(err.idVariableOriginal == null) {
      return false;
    }
    return this.esGlobal == err.esGlobal && this.idVariableOriginal.equals(err.idVariableOriginal);
  }

  @Override
  public void rellenarConTS(TablaSimbolos ts) {
    this.idVariableOriginal = ts.recuperarLexema(this.posVariableOriginalTS);
    this.tipoVariableOriginal = ts.buscarTipoTS(this.posVariableOriginalTS).nombre();
  }
}
