package es.grupo83.error;

public class ErrorComentarioSinFin extends Error {
  @Override
  public CodigoError getCodigo() {
    return CodigoError.COMENTARIO_SIN_FIN;
  }

  @Override
  public TipoError getTipoError() {
    return TipoError.LEXICO;
  }

  @Override
  public String getMsg() {
    return "Se ha encontrado EOF sin finalizar el comentario";
  }

  @Override
  public boolean igual(Object other) {
    return other instanceof ErrorComentarioSinFin;
  }
}
