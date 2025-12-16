package es.grupo83;

import tslib.TS_Gestor;
import tslib.TS_Gestor.Tabla;

import es.grupo83.semantica.*;

import es.grupo83.error.*;

import java.util.Arrays;
import java.util.List;

public class TablaSimbolos {
  // Variables
  private static final String DESPLAZAMIENTO = "desplazamiento";
  // Funciones
  private static final String N_PARAMETROS = "n_param";
  private static final String LISTA_PARAMETROS = "lista_param";
  private static final String MODO_PARAMETROS = "modo_param";
  private static final String TIPO_RETORNO = "tipo_ret";
  private static final String ETIQUETA = "etiqueta";

  private GestorErrores gestorErrores;

  private TS_Gestor gestor;

  private boolean zonaDecl;

  private int desp;

  private int despGlobal;

  private Tabla tablaActual;

  public TablaSimbolos(GestorErrores gestorErrores) {
    this.gestorErrores = gestorErrores;
    this.zonaDecl = false;
    this.desp = 0;
    this.despGlobal = 0;
    this.tablaActual = Tabla.GLOBAL;
    this.gestor = new TS_Gestor("simbolos.txt");
    this.gestor.createTPalabrasReservadas();
    this.gestor.activarDebug();

    this.gestor.createAtributo(DESPLAZAMIENTO, TS_Gestor.DescripcionAtributo.DIR, TS_Gestor.TipoDatoAtributo.ENTERO);

    this.gestor.createAtributo(N_PARAMETROS, TS_Gestor.DescripcionAtributo.NUM_PARAM, TS_Gestor.TipoDatoAtributo.ENTERO);
    this.gestor.createAtributo(LISTA_PARAMETROS, TS_Gestor.DescripcionAtributo.TIPO_PARAM, TS_Gestor.TipoDatoAtributo.LISTA);
    this.gestor.createAtributo(MODO_PARAMETROS, TS_Gestor.DescripcionAtributo.MODO_PARAM, TS_Gestor.TipoDatoAtributo.LISTA);
    this.gestor.createAtributo(TIPO_RETORNO, TS_Gestor.DescripcionAtributo.TIPO_RET, TS_Gestor.TipoDatoAtributo.CADENA);
    this.gestor.createAtributo(ETIQUETA, TS_Gestor.DescripcionAtributo.ETIQUETA, TS_Gestor.TipoDatoAtributo.CADENA);
  }

  public void inicioZonaDecl() {
    this.zonaDecl = true;
  }

  public void finZonaDecl() {
    this.zonaDecl = false;
  }

  public void crearTS() {
    this.gestor.createTSGlobal();
  }

  public void crearAmbitoLocal() {
    this.gestor.createTSLocal();
    this.tablaActual = Tabla.LOCAL;
    this.despGlobal = this.desp;
    this.desp = 0;
  }

  public void destruirAmbitoLocal() {
    this.tablaActual = Tabla.GLOBAL;
    this.desp = this.despGlobal;
    this.gestor.write(TS_Gestor.Tabla.LOCAL);
    this.gestor.destroy(TS_Gestor.Tabla.LOCAL);
  }

  public void finTablaSimbolos() {
    this.gestor.write(TS_Gestor.Tabla.GLOBAL);
    this.gestor.destroy(TS_Gestor.Tabla.GLOBAL);
  }

  public void incrementarDesp(int ancho) {
    this.desp += ancho;
  }

  public void insertarTipoBasicoTS(int pos, TipoBasico tipo) {
    int ok = this.gestor.setTipo(pos, tipo.nombre());
    if(ok != 0) {
      System.err.println("Error de TS: " + ok);
    }
  }

  public void insertarDesplazamientoTS(int pos) {
    this.gestor.setValorAtributoEnt(pos, DESPLAZAMIENTO, this.desp);
  }

  public void insertarTipoFuncionTS(int pos, Funcion funcion) {
    String retorno = funcion.getTipoRetorno().nombre();
    String[] parametros = funcion.getParametros().toList();
    this.gestor.setValorAtributoEnt(pos, N_PARAMETROS, parametros.length);
    this.gestor.setValorAtributoLista(pos, LISTA_PARAMETROS, parametros);
    String[] modoParam = Arrays.stream(parametros)
            .map(str -> "valor")
            .toList().toArray(new String[0]);
    this.gestor.setValorAtributoLista(pos, MODO_PARAMETROS, modoParam);
    this.gestor.setValorAtributoCad(pos, TIPO_RETORNO, retorno);
  }

  public void insertarEtiquetaTS(int pos, String etiqueta) {
    this.gestor.setValorAtributoCad(pos, ETIQUETA, etiqueta);
  }

  /**
   * Recupera el lexema de la variable en la posicion dada
   * @param pos La posicion a recuperar el lexema del id de la variable
   * @return Si sale bien, el lexema. En caso de error, null
   */
  public String recuperarLexema(int pos) {
    // TODO: Modificar tslib para que nos deje hacerlo
    return null;
  }

  private static TipoBasico fromStringGestor(String tipo) {
    switch (tipo) {
      case "entero" -> {
        return new TipoBasico(TipoBasico.Basicos.ENTERO);
      }
      case "real" -> {
        return new TipoBasico(TipoBasico.Basicos.REAL);
      }
      case "cadena" -> {
        return new TipoBasico(TipoBasico.Basicos.CADENA);
      }
      case "lógico" -> {
        return new TipoBasico(TipoBasico.Basicos.LOGICO);
      }
      default -> {
        return null;
      }
    }
  }

  public Tipo buscarTipoTS(int pos) {
    String atributo = this.gestor.getTipo(pos);
    if(atributo == null) {
      return new TipoInterno(TipoInterno.Internos.ERROR);
    }
    if(atributo.equals("función")) {
       String[] parametros = this.gestor.getValorAtributoLista(pos, LISTA_PARAMETROS);
       String retorno = this.gestor.getValorAtributoCad(pos, TIPO_RETORNO);
       if(parametros == null || retorno == null) {
         return new TipoInterno(TipoInterno.Internos.ERROR);
       }

       List<Tipo> paramTipoBasico = Arrays.stream(parametros)
               .map(str -> (Tipo) fromStringGestor(str))
               .toList();
       return new Funcion(new Producto(paramTipoBasico), fromStringGestor(retorno));
    } else {
      return fromStringGestor(atributo);
    }
  }

  /**
   * Agrega o recupera la entrada de la TS. Usado por el ALex para construir el token ID
   * @param lexema El lexema del identificador
   * @return La posicion en la TS del nuevo identificador. Si esta en zona de declaracion y ha ocurrido
   * una redeclaracion, devuelve la posicion de la variable original
   */
  public int agregarORecuperarEntrada(String lexema) {
    int pos = 0;
    if (this.zonaDecl) {
      switch (this.tablaActual) {
        case LOCAL -> {
          pos = this.gestor.addEntradaTSLocal(lexema);
          if (pos == 0) {
            pos = this.gestor.getEntradaTS(lexema);
            ErrorRedeclaracionVariable err = new ErrorRedeclaracionVariable(pos, false);
            err.rellenarConTS(this);
            this.gestorErrores.agregarError(err);
          }
        }
        case GLOBAL -> {
          pos = this.gestor.addEntradaTSGlobal(lexema);
          if (pos == 0) {
            pos = this.gestor.getEntradaTS(lexema);
            ErrorRedeclaracionVariable err = new ErrorRedeclaracionVariable(pos, true);
            err.rellenarConTS(this);
            this.gestorErrores.agregarError(err);
          }
        }
        case PALRES -> throw new RuntimeException("Tabla == PALRES !!");
      }
    } else {
      pos = this.gestor.getEntradaTS(lexema);
      if (pos == 0) {
        // Variable no declarada: se agrega como variable entera y global
        pos = this.gestor.addEntradaTSGlobal(lexema);
        this.insertarTipoBasicoTS(pos, new TipoBasico(TipoBasico.Basicos.ENTERO));
      }
    }
    assert pos != 0;
    return pos;
  }
}
