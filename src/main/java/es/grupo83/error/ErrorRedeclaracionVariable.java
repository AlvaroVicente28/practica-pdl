package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorRedeclaracionVariable extends ErrorSemantico {
  private int posVariableOriginalTS;

  private String idVariableOriginal;

  private boolean esGlobal;

  public ErrorRedeclaracionVariable(int pos, boolean esGlobal) {
    this.posVariableOriginalTS = pos;
    this.idVariableOriginal = null;
    this.esGlobal = esGlobal;
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.REDECLARACION_VARIABLE;
  }

  @Override
  public String getMsg() {
    return "Variable " + (this.esGlobal ? "global " : "") + "redeclarada con nombre " + this.idVariableOriginal;
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
    if(this.idVariableOriginal == null) {
      this.idVariableOriginal = "" + this.posVariableOriginalTS;
      // TODO: Implementar este error
      //      throw new RuntimeException("ErrorRedeclaracionVariable: ts.recuperarLexema == null !!");
    }
  }
}
