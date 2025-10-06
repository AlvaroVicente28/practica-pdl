package es.grupo83.error;

public abstract class Error {
  public enum TipoError {
    LEXICO,
    SINTACTICO,
    SEMANTICO
  }

  public abstract CodigoError getCodigo();

  public abstract Error.TipoError getTipoError();
}
