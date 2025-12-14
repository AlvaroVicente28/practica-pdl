package es.grupo83.semantica;

import java.util.ArrayList;

// TODO: Se puede prescindir del acceso aleatorio??
public class Producto extends Tipo {
  private ArrayList<TipoBasico> operandos;

  /**
   * Contruye el tipo Producto
   * @param primerTipo El primer tipo de la secuencia
   */
  public Producto(TipoBasico primerTipo) {
    if(primerTipo == null) {
      throw new RuntimeException("El primer tipo del Producto debe ser distinto de null");
    }
    this.operandos = new ArrayList<>();
    this.operandos.add(primerTipo);
  }

  /**
   * Construye un nuevo tipo Producto agregando el nuevo tipo a la secuencia
   * @param nuevoTipo El nuevo tipo a agregar en la secuencia por la izquierda
   * @return El nuevo tipo Producto
   */
  public void agregarTipo(TipoBasico nuevoTipo) {
    this.operandos.add(nuevoTipo);
  }

  public String[] toList() {
    ArrayList<String> temp = new ArrayList<>();
    for(int i = this.operandos.size() - 1; i >= 0; --i) {
      temp.add(this.operandos.get(i).nombre());
    }
    return temp.toArray(new String[0]);
  }

  @Override
  public String nombre() {
    return this.operandos.stream()
            .map(tipo -> tipo.nombre())
            .reduce("", (acc, next) -> next + " X " + acc);
  }

  private boolean productosIguales(Producto other) {
    return this.operandos.equals(other.operandos);
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof Producto)) {
      return false;
    }

    return this.productosIguales((Producto) other);
  }

  @Override
  public int hashCode() {
    double res = 0;
    for(int i = 0; i < this.operandos.size(); ++i) {
      res += this.operandos.get(i).hashCode() * Math.pow(5, i);

    }
    return (int) res;
  }
}
