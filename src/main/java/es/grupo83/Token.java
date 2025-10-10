package es.grupo83;

public class Token {
  public enum TipoToken {
    EOF,
    IDENTIFICADOR,
    CTE_ENTERA,
    CTE_REAL,
    CTE_CADENA,
    CTE_LOGICA,
    OP_RESTA,
    OP_MODULO,
    IGUALDAD,
    MENOR_ESTRICTO,
    AND,
    NOT,
    AUTOINCREMENTO,
    ASIGNACION,
    PUNTO_COMA,
    LLAVES,
    PARENTESIS,
    COMA,
    DOS_PUNTOS,
    INT,
    FLOAT,
    BOOLEAN,
    VOID,
    LET,
    WRITE,
    READ,
    RETURN,
    FUNCTION,
    IF,
    SWITCH,
    CASE,
    DEFAULT,
    BREAK
  }

  public enum ORIENTACION_SIMBOLO {
    APERTURA,
    CIERRE
  }

  private TipoToken tipoToken;

  private Object attr;

  public Token(TipoToken tipo, Object attr) {
    this.tipoToken = tipo;
    this.attr = attr;
  }

  public TipoToken getTipoToken() {
    return this.tipoToken;
  }

  public Object getAttr() {
    return this.attr;
  }
}
