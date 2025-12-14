package es.grupo83.semantica;

import es.grupo83.util.Equivalencia;

public abstract class Tipo implements Equivalencia {
  public abstract String nombre();

  @Override
  public boolean equals(Object other) {
    if(this == other) {
      return true;
    }
    if(other == null) {
      return false;
    }
    return this.igual(other);
  }
}
