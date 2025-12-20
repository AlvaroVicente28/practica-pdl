package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorAsignacionTipoDistinto extends ErrorSemantico {
  private String nombreVariable;
  private String tipoVariable;
  private String tipoRecibido;

  private int posVariable;

  public ErrorAsignacionTipoDistinto(int posVariable, String tipoRecibido, TablaSimbolos ts) {
    this.posVariable = posVariable;
    this.tipoRecibido = tipoRecibido;
    this.rellenarConTS(ts);
  }

  @Override
  public void rellenarConTS(TablaSimbolos ts) {
    this.nombreVariable = ts.recuperarLexema(this.posVariable);
    this.tipoVariable = ts.buscarTipoTS(this.posVariable).nombre();
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.ASIGNACION_TIPOS_DISTINTOS;
  }

  @Override
  public String getMsg() {
    String res = "Se ha intentado asignar a la variable '" + this.nombreVariable + "' el tipo '" + this.tipoRecibido + "'";
    res += "\nOBS: La variable es de tipo '" + this.tipoVariable + "'";
    return res;
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorAsignacionTipoDistinto otherErr)) {
      return false;
    }
    return this.nombreVariable.equals(otherErr.nombreVariable) && this.tipoRecibido.equals(otherErr.tipoRecibido);
  }
}
