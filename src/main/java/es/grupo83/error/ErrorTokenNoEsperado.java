package es.grupo83.error;

import es.grupo83.Token;

public class ErrorTokenNoEsperado extends Error {
  public enum ContextoSintactico {
    EXPRESION,
    SENTENCIA_SIMPLE,
    SENTENCIA_COMPUESTA,
    SENTENCIA_RETORNO,
    SENTENCIA_SWITCH,
    DECLARACION_FUNCION,
    DECLARACION_TIPO,
    NADA
  }

  private Token token;

  private ContextoSintactico ctx;

  public ErrorTokenNoEsperado(Token token, ContextoSintactico ctx) {
    this.token = token;
    this.ctx = ctx;
  }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.TOKEN_NO_ESPERADO;
  }

  @Override
  public TipoError getTipoError() {
    return TipoError.SINTACTICO;
  }

  @Override
  public String getMsg() {
    String res = "";

    // 1. Que se ha encontrado
    switch(this.token.getTipoToken()) {
      case IDENTIFICADOR -> res += "IDENTIFICADOR";
      case CTE_ENTERA -> res += "ENTERO ('" + this.token.getAttr() + "')";
      case CTE_REAL -> res += "REAL ('" + this.token.getAttr() + "')";
      case CTE_CADENA -> res += "CADENA ('" + this.token.getAttr() + "')";
      case CTE_LOGICA -> res += "LOGICO ('" + this.token.getAttr() + "')";
      case OP_RESTA -> res += "'-'";
      case OP_MODULO -> res += "'%'";
      case IGUALDAD -> res += "'=='";
      case MENOR_ESTRICTO -> res += "'<'";
      case AND -> res += "'&&'";
      case NOT -> res += "'!'";
      case AUTOINCREMENTO -> res += "'++'";
      case ASIGNACION -> res += "'='";
      case PUNTO_COMA -> res += "';'";
      case LLAVES -> {
        if(this.token.getAttr().equals(Token.ORIENTACION_SIMBOLO.APERTURA)) {
          res += "'{'";
        } else if(this.token.getAttr().equals(Token.ORIENTACION_SIMBOLO.CIERRE)) {
          res += "'}'";
        }
      }
      case PARENTESIS -> {
        if(this.token.getAttr().equals(Token.ORIENTACION_SIMBOLO.APERTURA)) {
          res += "'('";
        } else if(this.token.getAttr().equals(Token.ORIENTACION_SIMBOLO.CIERRE)) {
          res += "')'";
        }
      }
      case COMA -> res += "','";
      case DOS_PUNTOS -> res += "':'";
      case INT -> res += "'INT'";
      case FLOAT -> res += "'FLOAT'";
      case BOOLEAN -> res += "'BOOLEAN'";
      case STRING -> res += "'STRING'";
      case VOID -> res += "'VOID'";
      case LET -> res += "'LET'";
      case WRITE -> res += "'WRITE'";
      case READ -> res += "'READ'";
      case RETURN -> res += "'RETURN'";
      case FUNCTION -> res += "'FUNCTION'";
      case IF -> res += "'IF'";
      case SWITCH -> res += "'SWITCH'";
      case CASE -> res += "'CASE'";
      case DEFAULT -> res += "'DEFAULT'";
      case BREAK -> res += "'BREAK'";
      case EOF -> res += "FINAL ARCHIVO";
    }

    // 2. Donde se ha encontrado
    switch(this.ctx) {
      case EXPRESION -> res += " encontrado en EXPRESION";
      case SENTENCIA_COMPUESTA -> res += " encontrado en SENTENCIA COMPUESTA";
      case SENTENCIA_SIMPLE -> res += " encontrado en SENTENCIA SIMPLE";
      case SENTENCIA_RETORNO -> res += " encontrado en SENTENCIA DE RETORNO";
      case SENTENCIA_SWITCH -> res += " encontrado en SENTENCIA SWITCH";
      case DECLARACION_FUNCION -> res += " encontrado en DECLARACION DE FUNCION";
      case DECLARACION_TIPO -> res += " encontrado en DECLARACION DE TIPO";
      case NADA -> res += " encontrado";
    }

    return res;
  }
}
