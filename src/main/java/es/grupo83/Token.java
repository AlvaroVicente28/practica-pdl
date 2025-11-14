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
    STRING,
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

  @Override
  public boolean equals(Object obj) {
    if(obj == this) {
      return true;
    }
    if(obj == null) {
      return false;
    }

    if(!(obj instanceof Token)) {
      return false;
    }
    Token other = (Token) obj;

    if(!this.tipoToken.equals(other.tipoToken)) {
      return false;
    }

    // En este equals solo se comprueba que los atributos son iguales si son del tipo ORIENTACION_SIMBOLO

    if(this.attr == null || other.attr == null) {
      return true;
    }

    if(this.attr instanceof ORIENTACION_SIMBOLO) {
      return (ORIENTACION_SIMBOLO) (this.attr) == (ORIENTACION_SIMBOLO) (other.attr);
    }

    return true;
  }

  @Override
  public int hashCode() {
    return this.tipoToken.ordinal();
  }
}
