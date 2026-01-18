package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorAsignacionFuncion extends ErrorSemantico {
  private String nombreFuncion;

  private int posFuncionTS;

  public ErrorAsignacionFuncion(int posFuncionTS, TablaSimbolos ts) {
    this.posFuncionTS = posFuncionTS;
    this.rellenarConTS(ts);
  }

  @Override
  public void rellenarConTS(TablaSimbolos ts) {
    this.nombreFuncion = ts.recuperarLexema(this.posFuncionTS);
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.ASIGNACION_DE_FUNCION;
  }

  @Override
  public String getMsg() {
    return "Se intenta asignar un valor a la funcion '" + this.nombreFuncion + "'";
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorAsignacionFuncion)) {
      return false;
    }
    ErrorAsignacionFuncion otherErr = (ErrorAsignacionFuncion) other;
    return this.nombreFuncion.equals(otherErr.nombreFuncion);
  }
}
