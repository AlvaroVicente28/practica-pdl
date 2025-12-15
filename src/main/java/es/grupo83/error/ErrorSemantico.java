package es.grupo83.error;

import es.grupo83.TablaSimbolos;

/**
 * Esta clase abstracta define un servicio para los errores semanticos:
 * un metodo para completar informacion del error usando la tabla de simbolos.
 * Esta operacion debe ser ejecutada lo antes posible
 */
public abstract class ErrorSemantico extends Error {
  @Override
  public TipoError getTipoError() {
    return TipoError.SEMANTICO;
  }

  public abstract void rellenarConTS(TablaSimbolos ts);
}
