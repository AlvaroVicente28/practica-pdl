package es.grupo83.error;

import es.grupo83.TablaSimbolos;

public class ErrorTipoOperandosInvalido extends ErrorSemantico {
  public enum TipoOperador {
    AND_LOGICO,
    IGUALDAD,
    MENOR_ESTRICTO,
    RESTA,
    MODULO,
    NEGACION_LOGICA,
    PREINCREMENTO,
    MENOS_UNARIO
  }

  private TipoOperador operador;
  private String tipoIzqda;
  private String tipoDrcha;

  public ErrorTipoOperandosInvalido(TipoOperador operador, String tipoIzqda, String tipoDrcha) {
    if(tipoDrcha == null) {
      throw new RuntimeException("ErrorTipoOperandosInvalido: tipoDrcha == null !!");
    }
    this.operador = operador;
    this.tipoIzqda = tipoIzqda;
    this.tipoDrcha = tipoDrcha;
  }

  @Override
  public void rellenarConTS(TablaSimbolos ts) { }

  @Override
  public CodigoError getCodigo() {
    return CodigoError.TIPO_OPERANDOS_INCORRECTO;
  }

  @Override
  public String getMsg() {
    String operadorStr = this.operador.name();
    String res = "El operador " + operadorStr + " ha recibido operandos de tipo incorrecto.";
    if(this.tipoIzqda != null) {
      res += "\n\tA la izquierda: '" + this.tipoIzqda + "'";
    }
    res += "\n\tA la derecha: '" + this.tipoDrcha + "'";
    return res;
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof ErrorTipoOperandosInvalido otherErr)) {
      return false;
    }
    if(this.operador != otherErr.operador || !this.tipoDrcha.equals(otherErr.tipoDrcha)) {
      return false;
    }
    if(this.tipoIzqda == null) {
      return otherErr.tipoIzqda == null;
    }

    return this.tipoIzqda.equals(otherErr.tipoIzqda);
  }
}
