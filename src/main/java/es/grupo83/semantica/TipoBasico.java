package es.grupo83.semantica;

public class TipoBasico extends Tipo {
  public enum Basicos {
    ENTERO,
    REAL,
    CADENA,
    LOGICO,
    VACIO
  }

  private Basicos claseTipo;

  public TipoBasico(Basicos claseTipo) {
    this.claseTipo = claseTipo;
  }

  @Override
  public String nombre() {
    return switch (this.claseTipo) {
      case ENTERO -> "entero";
      case REAL -> "real";
      case CADENA -> "cadena";
      case LOGICO -> "logico";
      case VACIO -> "vacio";
    };
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof TipoBasico)) {
      return false;
    }
    return this.claseTipo == ((TipoBasico) other).claseTipo;
  }

  @Override
  public int hashCode() {
    return this.claseTipo.ordinal();
  }
}
