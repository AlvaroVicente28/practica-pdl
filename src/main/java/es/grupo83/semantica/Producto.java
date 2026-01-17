package es.grupo83.semantica;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;

public class Producto extends Tipo {
  private ArrayDeque<Tipo> operandos;

  /**
   * Contruye el tipo Producto inicializandolo con un unico elemento
   * @param primerTipo El primer tipo de la secuencia
   */
  public Producto(Tipo primerTipo) {
    if(primerTipo == null) {
      throw new RuntimeException("El primer tipo del Producto debe ser distinto de null");
    }
    this.operandos = new ArrayDeque<>();
    this.operandos.add(primerTipo);
  }

  /**
   * Construye el tipo Producto vacio. Usado para funciones sin parametros
   */
  public Producto() {
    this.operandos = new ArrayDeque<>();
  }

  /**
   * Construye el tipo Producto a partir de una lista ordenada de tipos
   * @param miembros La lista de tipos miembros
   */
  public Producto(List<Tipo> miembros) {
    this.operandos = new ArrayDeque<>(miembros);
  }

  /**
   * Construye un nuevo tipo Producto agregando el nuevo tipo a la secuencia
   * @param nuevoTipo El nuevo tipo a agregar en la secuencia por la izquierda
   */
  public void agregarTipo(Tipo nuevoTipo) {
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
            .reduce(this.operandos.getFirst().nombre(), (acc, next) -> next + "," + acc);
  }

  private boolean productosIguales(Producto other) {
    Iterator<Tipo> it1 = this.operandos.iterator();
    Iterator<Tipo> it2 = other.operandos.iterator();
    while(it1.hasNext() && it2.hasNext()) {
      if(!it1.next().equals(it2.next())) {
        return false;
      }
    }

    return !it1.hasNext() && !it2.hasNext();
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof Producto)) {
      return false;
    }

    return this.productosIguales((Producto) other);
  }
}
