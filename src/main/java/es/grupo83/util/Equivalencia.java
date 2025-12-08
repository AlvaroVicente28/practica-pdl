package es.grupo83.util;

/**
 * Interfaz para pedir en la clase Error que los errores implementen una relacion de equivalencia.
 * Así, podremos comparar los errores en los tests unitarios.
 */
public interface Equivalencia {
  /**
   * Compara dos valores
   * @param other El otro objeto a comparar. DEBE ser distinto de null
   * @return Cierto, si son iguales. Eoc, Falso
   */
  boolean igual(Object other);
}
