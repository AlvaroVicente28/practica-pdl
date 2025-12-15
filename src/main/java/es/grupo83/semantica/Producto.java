package es.grupo83.semantica;

import java.util.ArrayDeque;

public class Producto extends Tipo {
  private ArrayDeque<TipoBasico> operandos;

  /**
   * Contruye el tipo Producto
   * @param primerTipo El primer tipo de la secuencia
   */
  public Producto(TipoBasico primerTipo) {
    if(primerTipo == null) {
      throw new RuntimeException("El primer tipo del Producto debe ser distinto de null");
    }
    this.operandos = new ArrayDeque<>();
    this.operandos.add(primerTipo);
  }

  /**
   * Construye un nuevo tipo Producto agregando el nuevo tipo a la secuencia
   * @param nuevoTipo El nuevo tipo a agregar en la secuencia por la izquierda
   */
  public void agregarTipo(TipoBasico nuevoTipo) {
    this.operandos.addFirst(nuevoTipo);
  }

  /**
   * Convierte el tipo Producto actual a una lista ordenada con los nombres de los tipos participantes
   * @return Una lista con los nombres de los tipos basicos participantes
   */
  public String[] toList() {
    return this.operandos.stream()
            .map(tipo -> tipo.nombre())
            .toList().toArray(new String[0]);
  }

  @Override
  public String nombre() {
    return this.operandos.stream()
            .skip(1)
            .map(tipo -> tipo.nombre())
            .reduce(this.operandos.getFirst().nombre(), (acc, next) -> next + " X " + acc);
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
    return this.operandos.stream()
            .map(tipo -> tipo.hashCode())
            .reduce(0, (acc, next) -> 5 * acc + next.hashCode());
  }
}
