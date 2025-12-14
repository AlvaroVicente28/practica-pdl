package es.grupo83.semantica;

public class TipoInterno extends Tipo {
  public enum Internos {
    OK,
    ERROR
  }

  private Internos claseTipo;

  public TipoInterno(Internos claseTipo) {
    this.claseTipo = claseTipo;
  }

  @Override
  public String nombre() {
    return switch (this.claseTipo) {
      case OK -> "tipo_ok";
      case ERROR -> "tipo_err";
    };
  }

  @Override
  public boolean igual(Object other) {
    if(!(other instanceof TipoInterno)) {
      return false;
    }

    return this.claseTipo == ((TipoInterno) other).claseTipo;
  }
}
