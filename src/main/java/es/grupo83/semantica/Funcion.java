package es.grupo83.semantica;

public class Funcion extends Tipo {
  private Producto parametros;

  private TipoBasico tipoRetorno;

  public Funcion(Producto parametros, TipoBasico tipoRetorno) {
    this.parametros = parametros;
    this.tipoRetorno = tipoRetorno;
  }

  public Producto getParametros() {
    return this.parametros;
  }

  public TipoBasico getTipoRetorno() {
    return this.tipoRetorno;
  }

  public String nombreCanonico() {
    return this.parametros.nombre() + " -> " + this.tipoRetorno.nombre();
  }

  @Override
  public String nombre() {
    return "funcion";
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof Funcion)) {
      return false;
    }
    Funcion elem = (Funcion) other;
    return this.tipoRetorno.equals(elem.tipoRetorno) && this.parametros.equals(elem.parametros);
  }
}
