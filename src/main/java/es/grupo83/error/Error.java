package es.grupo83.error;

import es.grupo83.util.Equivalencia;

public abstract class Error implements Equivalencia {
  public enum TipoError {
    LEXICO,
    SINTACTICO,
    SEMANTICO
  }

  public abstract CodigoError getCodigo();

  public abstract Error.TipoError getTipoError();

  public abstract String getMsg();

  @Override
  public String toString() {
    return this.getMsg();
  }

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
