package es.grupo83;

import tslib.TS_Gestor;

// TODO: Cuando tengamos el A. Semantico, podremos continuar la impl
public class TablaSimbolos {
  private TS_Gestor gestor;

  private boolean zonaDecl;

  public TablaSimbolos() {
    this.zonaDecl = false;
    this.gestor = new TS_Gestor("simbolos.txt");
    this.gestor.createTPalabrasReservadas();
    this.gestor.activarDebug();
    // TODO: Crear los atributos de la tabla de simbolos: Despl, N_Param, ...
    this.gestor.createAtributo("desplazamiento", TS_Gestor.DescripcionAtributo.DIR, TS_Gestor.TipoDatoAtributo.ENTERO);
    this.gestor.createTSGlobal();
  }

  public void inicioZonaDecl() {
    this.zonaDecl = true;
  }

  public void finZonaDecl() {
    this.zonaDecl = false;
  }

  public void crearAmbitoLocal() {
    this.gestor.createTSLocal();
  }

  public void destruirAmbitoLocal() {
    this.gestor.write(TS_Gestor.Tabla.LOCAL);
    this.gestor.destroy(TS_Gestor.Tabla.LOCAL);
  }

  public void finTablaSimbolos() {
    this.gestor.write(TS_Gestor.Tabla.GLOBAL);
    this.gestor.destroy(TS_Gestor.Tabla.GLOBAL);
  }

  public int agregarORecuperarEntrada(String lexema) {
    // Buscar en las tablas
    int pos = this.gestor.getEntradaTS(lexema);
    if(pos == 0) {
      // TODO: De momento solo se añade en la tabla global
      return this.gestor.addEntradaTSGlobal(lexema);
    }
    return pos;
  }
}
