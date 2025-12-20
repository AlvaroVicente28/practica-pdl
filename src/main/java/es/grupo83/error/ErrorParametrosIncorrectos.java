package es.grupo83.error;

import es.grupo83.TablaSimbolos;

import es.grupo83.semantica.Funcion;

public class ErrorParametrosIncorrectos extends ErrorSemantico {
  private String nombreFuncion;
  private String tipoEsperadoFuncion;
  private String tipoRecibido;

  private int idFuncion;

  public ErrorParametrosIncorrectos(int idFuncion, String tipoRecibido, TablaSimbolos ts) {
    this.idFuncion = idFuncion;
    this.tipoRecibido = tipoRecibido;
    this.rellenarConTS(ts);
  }

  @Override
  public void rellenarConTS(TablaSimbolos ts) {
    this.nombreFuncion = ts.recuperarLexema(this.idFuncion);
    this.tipoEsperadoFuncion = ((Funcion) ts.buscarTipoTS(this.idFuncion)).getParametros().nombre();
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.PARAMETROS_INCORRECTOS;
  }

  @Override
  public String getMsg() {
    String res = "Los parametros de la funcion '" + this.nombreFuncion + "' son incorrectos";
    res += "\nSe esperaba '" + this.tipoEsperadoFuncion + "'";
    res += "\nLa funcion recibe '" + this.tipoRecibido + "'";
    return res;
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorParametrosIncorrectos)) {
      return false;
    }

    ErrorParametrosIncorrectos otherErr = (ErrorParametrosIncorrectos) other;
    return this.nombreFuncion.equals(otherErr.nombreFuncion) && this.tipoRecibido.equals(otherErr.tipoRecibido);
  }
}
