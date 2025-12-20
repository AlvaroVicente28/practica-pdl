package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorReturnTipoIncorrecto extends ErrorSemantico {
  private String tipoRetornoFuncion;
  private String tipoRecibido;

  public ErrorReturnTipoIncorrecto(String tipoRecibido, String tipoRetornoFuncion) {
    this.tipoRecibido = tipoRecibido;
    this.tipoRetornoFuncion = tipoRetornoFuncion;
  }

  @Override
  public void rellenarConTS(TablaSimbolos ts) { }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.RETURN_TIPO_INCORRECTO;
  }

  @Override
  public String getMsg() {
    return "La sentencia 'return' usa el tipo '" + this.tipoRecibido + "'" +
            " mientras que dicha funcion devuelve '" + this.tipoRetornoFuncion + "'";
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorReturnTipoIncorrecto otherErr)) {
      return false;
    }
    return this.tipoRetornoFuncion.equals(otherErr.tipoRetornoFuncion) && this.tipoRecibido.equals(otherErr.tipoRecibido);
  }
}
