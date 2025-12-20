package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorReturnSinFuncion extends ErrorSemantico {
  @Override
  public void rellenarConTS(TablaSimbolos ts) { }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.RETURN_SIN_FUNCION;
  }

  @Override
  public String getMsg() {
    return "Se ha introducido una sentencia 'return' fuera de una funcion";
  }

  @Override
  public boolean igual(Object other) {
    return true;
  }
}
