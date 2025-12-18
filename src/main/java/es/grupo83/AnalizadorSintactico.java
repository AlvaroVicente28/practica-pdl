package es.grupo83;

import es.grupo83.error.GestorErrores;
import es.grupo83.error.ErrorTokenNoEsperado;
import es.grupo83.error.ErrorTokenNoEsperado.ContextoSintactico;

import es.grupo83.semantica.*;
import es.grupo83.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class AnalizadorSintactico {
  private AnalizadorLexico alex;

  private GestorErrores gestorErrores;

  private TablaSimbolos tablaSimbolos;

  private ArrayList<Token> listaTokens;

  private ArrayList<Integer> parse;

  private Token next_tok;

  public AnalizadorSintactico(GestorErrores gestorErrores, TablaSimbolos tablaSimbolos, AnalizadorLexico alex) {
    this.alex = alex;
    this.gestorErrores = gestorErrores;
    this.tablaSimbolos = tablaSimbolos;
    this.listaTokens = new ArrayList<>();
    this.parse = new ArrayList<>();
    this.next_tok = null;
  }

  public void run() throws IOException {
    this.next_tok = this.alex.nextToken();
    this.listaTokens.add(this.next_tok);
    this.tablaSimbolos.crearTS();
    this.P();
    if(this.next_tok.getTipoToken() != Token.TipoToken.EOF) {
      // TODO: Error
      throw new RuntimeException("SE HA LLEGADO SALIDO DEL AXIOMA SIN EOF");
    }
    this.tablaSimbolos.finTablaSimbolos();
  }

  private Object equipara(Token token, ContextoSintactico ctx) throws IOException {
    if(this.next_tok.equals(token)) {
      Object attr = this.next_tok.getAttr();
      this.next_tok = this.alex.nextToken();
      this.listaTokens.add(this.next_tok);
      return attr;
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ctx));
      this.gestorErrores.pararEnError();
      return null;
    }
  }

  private void P() throws IOException {
    var first1 = Set.of(new Token(Token.TipoToken.WRITE, null),
            new Token(Token.TipoToken.READ, null),
            new Token(Token.TipoToken.IDENTIFICADOR, 0),
            new Token(Token.TipoToken.SWITCH, null),
            new Token(Token.TipoToken.IF, null),
            new Token(Token.TipoToken.LET, null));
    var first2 = Set.of(new Token(Token.TipoToken.FUNCTION, null));
    var follow = Set.of(new Token(Token.TipoToken.EOF, null));

    if(first1.contains(next_tok)) {
      this.parse.add(65);
      this.B(new TipoInterno(TipoInterno.Internos.ERROR));
      this.P();
    }
    else if(first2.contains(next_tok)) {
      this.parse.add(66);
      this.F();
      this.P();
    }
    else if(follow.contains(next_tok)) {
      this.parse.add(67);
      // lambda
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.NADA));
      this.gestorErrores.pararEnError();
    }
  }

  private TipoInterno B(Tipo tipoRet) throws IOException {
    TipoInterno b_tipo = null;
    if(this.next_tok.getTipoToken() == Token.TipoToken.IF) {
      this.parse.add(40);
      this.equipara(new Token(Token.TipoToken.IF, null), ContextoSintactico.SENTENCIA_COMPUESTA);
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA), ContextoSintactico.SENTENCIA_COMPUESTA);
      Tipo e_tipo = this.E();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE), ContextoSintactico.SENTENCIA_COMPUESTA);
      TipoInterno s_tipo = this.S(tipoRet);

      if(e_tipo.equals(new TipoBasico(TipoBasico.Basicos.LOGICO))) {
        b_tipo = s_tipo;
      } else {
        b_tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.LET) {
      this.parse.add(41);
      this.tablaSimbolos.inicioZonaDecl();
      this.equipara(new Token(Token.TipoToken.LET, null), ContextoSintactico.SENTENCIA_COMPUESTA);
      Pair<TipoBasico, Integer> attrs_t = this.T();
      TipoBasico t_tipo = attrs_t.getFirst();
      int t_ancho = attrs_t.getSecond();
      int id_pos = (int) this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, null), ContextoSintactico.SENTENCIA_COMPUESTA);
      this.tablaSimbolos.finZonaDecl();
      Tipo b2_tipo = this.B2();

      this.tablaSimbolos.insertarTipoBasicoTS(id_pos, t_tipo);
      this.tablaSimbolos.insertarDesplazamientoTS(id_pos);
      this.tablaSimbolos.incrementarDesp(t_ancho);
      if(b2_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
        b_tipo = new TipoInterno(TipoInterno.Internos.OK);
      } else {
        if(b2_tipo.equals(t_tipo)) {
          b_tipo = new TipoInterno(TipoInterno.Internos.OK);
        } else {
          b_tipo = new TipoInterno(TipoInterno.Internos.ERROR);
        }
      }
    }
    else if(Set.of(new Token(Token.TipoToken.RETURN, null),
                    new Token(Token.TipoToken.WRITE, null),
                    new Token(Token.TipoToken.READ, null),
                    new Token(Token.TipoToken.IDENTIFICADOR, 0))
            .contains(this.next_tok)) {
      this.parse.add(42);
      TipoInterno s_tipo = this.S(tipoRet);
      b_tipo = s_tipo;
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.SWITCH) {
      this.parse.add(43);
      this.equipara(new Token(Token.TipoToken.SWITCH, null), ContextoSintactico.SENTENCIA_COMPUESTA);
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA), ContextoSintactico.SENTENCIA_COMPUESTA);
      Tipo e_tipo = this.E();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE), ContextoSintactico.SENTENCIA_COMPUESTA);
      this.equipara(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.APERTURA), ContextoSintactico.SENTENCIA_COMPUESTA);
      TipoInterno sw_tipo = this.SW(tipoRet);
      this.equipara(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE), ContextoSintactico.SENTENCIA_COMPUESTA);

      if(e_tipo.equals(new TipoBasico(TipoBasico.Basicos.ENTERO))) {
        b_tipo = sw_tipo;
      } else {
        b_tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.SENTENCIA_COMPUESTA));
      this.gestorErrores.pararEnError();
    }
    return b_tipo;
  }

  private Tipo B2() throws IOException {
    Tipo b2_tipo = null;
    if(this.next_tok.getTipoToken() == Token.TipoToken.PUNTO_COMA) {
      this.parse.add(44);
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null), ContextoSintactico.SENTENCIA_COMPUESTA);
      b2_tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.ASIGNACION) {
      this.parse.add(45);
      this.equipara(new Token(Token.TipoToken.ASIGNACION, null), ContextoSintactico.SENTENCIA_COMPUESTA);
      Tipo e_tipo = this.E();
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null), ContextoSintactico.SENTENCIA_COMPUESTA);

      b2_tipo = e_tipo;
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.SENTENCIA_COMPUESTA));
      this.gestorErrores.pararEnError();
    }
    return b2_tipo;
  }

  private TipoInterno F() throws IOException {
    this.parse.add(56);
    this.tablaSimbolos.inicioZonaDecl();

    this.equipara(new Token(Token.TipoToken.FUNCTION, null), ContextoSintactico.DECLARACION_FUNCION);
    TipoBasico h_tipo = this.H();
    int id_pos = (int) this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0), ContextoSintactico.DECLARACION_FUNCION);

    this.tablaSimbolos.crearAmbitoLocal();

    this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA), ContextoSintactico.DECLARACION_FUNCION);
    Producto a_tipo = this.A();
    this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE), ContextoSintactico.DECLARACION_FUNCION);

    Funcion tipoFunc = new Funcion(a_tipo, h_tipo);
    this.tablaSimbolos.insertarTipoFuncionTS(id_pos, tipoFunc);
    // TODO: Insertar etiqueta

    this.tablaSimbolos.finZonaDecl();

    this.equipara(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.APERTURA), ContextoSintactico.DECLARACION_FUNCION);
    TipoInterno c_tipo = this.C(h_tipo);
    this.equipara(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE), ContextoSintactico.DECLARACION_FUNCION);

    this.tablaSimbolos.destruirAmbitoLocal();
    return c_tipo;
  }

  private TipoBasico H() throws IOException {
    TipoBasico tipo = null;
    if(Set.of(new Token(Token.TipoToken.INT, null),
            new Token(Token.TipoToken.FLOAT, null),
            new Token(Token.TipoToken.BOOLEAN, null),
            new Token(Token.TipoToken.STRING, null))
            .contains(this.next_tok)) {
      this.parse.add(57);
      TipoBasico t_tipo = this.T().getFirst();
      tipo = t_tipo;
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.VOID) {
      this.parse.add(58);
      this.equipara(new Token(Token.TipoToken.VOID, null), ContextoSintactico.DECLARACION_FUNCION);
      tipo = new TipoBasico(TipoBasico.Basicos.VACIO);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.DECLARACION_FUNCION));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Producto A() throws IOException {
    Producto tipo = null;
    if(Set.of(new Token(Token.TipoToken.INT, null),
                    new Token(Token.TipoToken.FLOAT, null),
                    new Token(Token.TipoToken.BOOLEAN, null),
                    new Token(Token.TipoToken.STRING, null))
            .contains(this.next_tok)) {
      this.parse.add(59);
      Pair<TipoBasico, Integer> attrs_t = this.T();
      TipoBasico t_tipo = attrs_t.getFirst();
      int t_ancho = attrs_t.getSecond();
      int id_pos = (int) this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, null), ContextoSintactico.DECLARACION_FUNCION);
      Tipo k_tipo = this.K();

      this.tablaSimbolos.insertarTipoBasicoTS(id_pos, t_tipo);
      this.tablaSimbolos.insertarDesplazamientoTS(id_pos);
      this.tablaSimbolos.incrementarDesp(t_ancho);

      if(k_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
        tipo = new Producto(t_tipo);
      } else {
        Producto k_tipo_producto = (Producto) k_tipo;
        k_tipo_producto.agregarTipo(t_tipo);
        tipo = k_tipo_producto;
      }
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.VOID) {
      this.parse.add(60);
      this.equipara(new Token(Token.TipoToken.VOID, null), ContextoSintactico.DECLARACION_FUNCION);
      tipo = new Producto(); // void
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.DECLARACION_FUNCION));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Pair<TipoBasico, Integer> T() throws IOException {
    TipoBasico tipo = null;
    int ancho = 0;
    if(this.next_tok.getTipoToken() == Token.TipoToken.INT) {
      this.parse.add(52);
      this.equipara(new Token(Token.TipoToken.INT, null), ContextoSintactico.DECLARACION_TIPO);
      tipo = new TipoBasico(TipoBasico.Basicos.ENTERO);
      ancho = 1;
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.FLOAT) {
      this.parse.add(53);
      this.equipara(new Token(Token.TipoToken.FLOAT, null), ContextoSintactico.DECLARACION_TIPO);
      tipo = new TipoBasico(TipoBasico.Basicos.REAL);
      ancho = 2;
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.STRING) {
      this.parse.add(55);
      this.equipara(new Token(Token.TipoToken.STRING, null), ContextoSintactico.DECLARACION_TIPO);
      tipo = new TipoBasico(TipoBasico.Basicos.CADENA);
      ancho = 64;
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.BOOLEAN){
      this.parse.add(54);
      this.equipara(new Token(Token.TipoToken.BOOLEAN, null), ContextoSintactico.DECLARACION_TIPO);
      tipo = new TipoBasico(TipoBasico.Basicos.LOGICO);
      ancho = 1;
    } else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.DECLARACION_TIPO));
      this.gestorErrores.pararEnError();
    }
    return new Pair<>(tipo, ancho);
  }

  private TipoInterno C(Tipo tipoRet) throws IOException {
    TipoInterno tipo = null;
    if(Set.of(new Token(Token.TipoToken.SWITCH, null),
            new Token(Token.TipoToken.READ, null),
            new Token(Token.TipoToken.WRITE, null),
            new Token(Token.TipoToken.IF, null),
            new Token(Token.TipoToken.RETURN, null),
            new Token(Token.TipoToken.LET, null),
            new Token(Token.TipoToken.IDENTIFICADOR, 0)).contains(this.next_tok)) {
      this.parse.add(63);
      TipoInterno b_tipo = this.B(tipoRet);
      TipoInterno c1_tipo = this.C(tipoRet);
      if(b_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
        tipo = c1_tipo;
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(this.next_tok.equals(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      this.parse.add(64);
      // lambda
      tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.DECLARACION_FUNCION));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private TipoInterno S(Tipo tipoRet) throws IOException {
    TipoInterno s_tipo = null;
    if(this.next_tok.getTipoToken() == Token.TipoToken.IDENTIFICADOR) {
      this.parse.add(28);
      int id_pos = (int) this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, null), ContextoSintactico.SENTENCIA_SIMPLE);
      Pair<Tipo, Boolean> attrs_s2 = this.S2();
      Tipo s2_tipo = attrs_s2.getFirst();
      boolean s2_llamada = attrs_s2.getSecond();

      Tipo id_tipo = this.tablaSimbolos.buscarTipoTS(id_pos);
      if(id_tipo instanceof Funcion func) {
        if(s2_llamada && s2_tipo.equals(func.getParametros())) {
          s_tipo = new TipoInterno(TipoInterno.Internos.OK);
        } else {
          s_tipo = new TipoInterno(TipoInterno.Internos.ERROR);
        }
      } else {
        if(!s2_llamada && s2_tipo.equals(id_tipo)) {
          s_tipo = new TipoInterno(TipoInterno.Internos.OK);
        } else {
          s_tipo = new TipoInterno(TipoInterno.Internos.ERROR);
        }
      }
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.WRITE) {
      this.parse.add(29);
      this.equipara(new Token(Token.TipoToken.WRITE, null), ContextoSintactico.SENTENCIA_SIMPLE);
      this.E();
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null), ContextoSintactico.SENTENCIA_SIMPLE);
      s_tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.READ) {
      this.parse.add(30);
      this.equipara(new Token(Token.TipoToken.READ, null), ContextoSintactico.SENTENCIA_SIMPLE);
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0), ContextoSintactico.SENTENCIA_SIMPLE);
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null), ContextoSintactico.SENTENCIA_SIMPLE);
      s_tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.RETURN) {
      this.parse.add(31);
      this.equipara(new Token(Token.TipoToken.RETURN, null), ContextoSintactico.SENTENCIA_SIMPLE);
      Tipo r1_tipo = this.R1();
      if(tipoRet.equals(new TipoInterno(TipoInterno.Internos.ERROR))) {
        // TODO: return sin funcion
        s_tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      } else {
        // TODO: Lanzar error correcto

        s_tipo = r1_tipo.equals(tipoRet) ? new TipoInterno(TipoInterno.Internos.OK) : new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.SENTENCIA_SIMPLE));
      this.gestorErrores.pararEnError();
    }
    return s_tipo;
  }

  private Pair<Tipo, Boolean> S2() throws IOException {
    Tipo tipo = null;
    boolean llamada = false;
    if(this.next_tok.getTipoToken() == Token.TipoToken.ASIGNACION) {
      this.parse.add(32);
      this.equipara(new Token(Token.TipoToken.ASIGNACION, null), ContextoSintactico.SENTENCIA_SIMPLE);
      Tipo e_tipo = this.E();
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null), ContextoSintactico.SENTENCIA_SIMPLE);
      tipo = e_tipo;
      llamada = false;
    }
    else if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA))) {
      this.parse.add(33);
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA), ContextoSintactico.SENTENCIA_SIMPLE);
      Tipo l_tipo = this.L();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE), ContextoSintactico.SENTENCIA_SIMPLE);
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null), ContextoSintactico.SENTENCIA_SIMPLE);
      tipo = l_tipo;
      llamada = true;
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.SENTENCIA_SIMPLE));
      this.gestorErrores.pararEnError();
    }
    return new Pair<>(tipo, llamada);
  }

  private Tipo R1() throws IOException {
    Tipo tipo = null;
    if(Set.of(new Token(Token.TipoToken.CTE_LOGICA, null),
            new Token(Token.TipoToken.IDENTIFICADOR, 0),
            new Token(Token.TipoToken.CTE_CADENA, ""),
            new Token(Token.TipoToken.CTE_ENTERA, 0),
            new Token(Token.TipoToken.CTE_REAL, 0.0),
            new Token(Token.TipoToken.NOT, null),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA),
            new Token(Token.TipoToken.AUTOINCREMENTO, null),
            new Token(Token.TipoToken.OP_RESTA, null))
            .contains(this.next_tok)) {
      this.parse.add(34);
      Tipo e_tipo = this.E();
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null), ContextoSintactico.SENTENCIA_RETORNO);
      tipo = e_tipo;
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.PUNTO_COMA) {
      this.parse.add(35);
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null), ContextoSintactico.SENTENCIA_RETORNO);
      tipo = new TipoBasico(TipoBasico.Basicos.VACIO);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.SENTENCIA_RETORNO));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Producto L() throws IOException {
    Producto tipo = null;
    if(Set.of(new Token(Token.TipoToken.CTE_LOGICA, null),
                    new Token(Token.TipoToken.IDENTIFICADOR, 0),
                    new Token(Token.TipoToken.CTE_CADENA, ""),
                    new Token(Token.TipoToken.CTE_ENTERA, 0),
                    new Token(Token.TipoToken.CTE_REAL, 0.0),
                    new Token(Token.TipoToken.NOT, null),
                    new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA),
                    new Token(Token.TipoToken.AUTOINCREMENTO, null),
                    new Token(Token.TipoToken.OP_RESTA, null))
            .contains(this.next_tok)) {
      this.parse.add(36);
      Tipo e_tipo = this.E();
      Tipo q_tipo = this.Q();
      if(q_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
        tipo = new Producto(e_tipo);
      } else {
        Producto q_tipo_producto = (Producto) q_tipo;
        q_tipo_producto.agregarTipo(e_tipo);
        tipo = q_tipo_producto;
      }
    }
    else if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      this.parse.add(37);
      // lambda
      tipo = new Producto(); // void
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.SENTENCIA_SIMPLE));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Tipo Q() throws IOException {
    Tipo tipo = null;
    if(this.next_tok.getTipoToken() == Token.TipoToken.COMA) {
      this.parse.add(38);
      this.equipara(new Token(Token.TipoToken.COMA, null), ContextoSintactico.SENTENCIA_SIMPLE);
      Tipo e_tipo = this.E();
      Tipo q1_tipo = this.Q();
      if(q1_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
        tipo = new Producto(e_tipo);
      } else {
        Producto q1_tipo_producto = (Producto) q1_tipo;
        q1_tipo_producto.agregarTipo(e_tipo);
        tipo = q1_tipo_producto;
      }
    }
    else if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      this.parse.add(39);
      // lambda
      tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.SENTENCIA_SIMPLE));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private TipoInterno SW(Tipo tipoRet) throws IOException {
    TipoInterno tipo = null;
    if(this.next_tok.getTipoToken() == Token.TipoToken.CASE) {
      this.parse.add(46);
      this.equipara(new Token(Token.TipoToken.CASE, null), ContextoSintactico.SENTENCIA_SWITCH);
      this.equipara(new Token(Token.TipoToken.CTE_ENTERA, 0), ContextoSintactico.SENTENCIA_SWITCH);
      this.equipara(new Token(Token.TipoToken.DOS_PUNTOS, null), ContextoSintactico.SENTENCIA_SWITCH);
      TipoInterno swc_tipo = this.SWC(tipoRet);
      TipoInterno sw1_tipo = this.SW(tipoRet);
      if(swc_tipo.equals(new TipoInterno(TipoInterno.Internos.OK)) && sw1_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
        tipo = new TipoInterno(TipoInterno.Internos.OK);
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.DEFAULT) {
      this.parse.add(47);
      this.equipara(new Token(Token.TipoToken.DEFAULT, null), ContextoSintactico.SENTENCIA_SWITCH);
      this.equipara(new Token(Token.TipoToken.DOS_PUNTOS, null), ContextoSintactico.SENTENCIA_SWITCH);
      TipoInterno swc_tipo = this.SWC(tipoRet);
      TipoInterno sw1_tipo = this.SW(tipoRet);
      if(swc_tipo.equals(new TipoInterno(TipoInterno.Internos.OK)) && sw1_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
        tipo = new TipoInterno(TipoInterno.Internos.OK);
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(this.next_tok.equals(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      this.parse.add(48);
      // lambda
      tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.SENTENCIA_SWITCH));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private TipoInterno SWC(Tipo tipoRet) throws IOException {
    TipoInterno tipo = null;
    if(Set.of(Token.TipoToken.SWITCH, Token.TipoToken.LET, Token.TipoToken.IDENTIFICADOR,
            Token.TipoToken.WRITE, Token.TipoToken.RETURN, Token.TipoToken.READ, Token.TipoToken.IF)
            .contains(this.next_tok.getTipoToken())) {
      this.parse.add(49);
      TipoInterno b_tipo = this.B(tipoRet);
      TipoInterno swc1_tipo = this.SWC(tipoRet);
      if(b_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
        tipo = swc1_tipo;
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.BREAK) {
      this.parse.add(50);
      this.equipara(new Token(Token.TipoToken.BREAK, null), ContextoSintactico.SENTENCIA_SWITCH);
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null), ContextoSintactico.SENTENCIA_SWITCH);
      TipoInterno swc1_tipo = this.SWC(tipoRet);
      tipo = swc1_tipo;
    }
    else if(Set.of(new Token(Token.TipoToken.CASE, null), new Token(Token.TipoToken.DEFAULT, null),
            new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE))
            .contains(this.next_tok)) {
      this.parse.add(51);
      // lambda
      tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.SENTENCIA_SWITCH));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Tipo K() throws IOException {
    Tipo tipo = null;
    if(this.next_tok.getTipoToken() == Token.TipoToken.COMA) {
      this.parse.add(61);
      this.equipara(new Token(Token.TipoToken.COMA, null), ContextoSintactico.DECLARACION_FUNCION);
      Pair<TipoBasico, Integer> attrs_t = this.T();
      TipoBasico t_tipo = attrs_t.getFirst();
      int t_ancho = attrs_t.getSecond();
      int id_pos = (int) this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0), ContextoSintactico.DECLARACION_FUNCION);
      Tipo k1_tipo = this.K();

      this.tablaSimbolos.insertarTipoBasicoTS(id_pos, t_tipo);
      this.tablaSimbolos.insertarDesplazamientoTS(id_pos);
      this.tablaSimbolos.incrementarDesp(t_ancho);
      if(k1_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
        tipo = new Producto(t_tipo);
      } else {
        Producto k1_tipo_producto = (Producto) k1_tipo;
        k1_tipo_producto.agregarTipo(t_tipo);
        tipo = k1_tipo_producto;
      }
    }
    else if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      this.parse.add(62);
      // lambda
      tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.DECLARACION_FUNCION));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Tipo E() throws IOException {
    Tipo tipo;
    this.parse.add(1);
    Tipo r_tipo = this.R();
    Tipo e2_tipo = this.E2();
    if(e2_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
      tipo = r_tipo;
    } else {
      if(r_tipo.equals(new TipoBasico(TipoBasico.Basicos.LOGICO)) && !e2_tipo.equals(new TipoInterno(TipoInterno.Internos.ERROR))) {
        tipo = new TipoBasico(TipoBasico.Basicos.LOGICO);
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    return tipo;
  }

  private Tipo E2() throws IOException {
    Tipo tipo = null;
    if(this.next_tok.getTipoToken() == Token.TipoToken.AND) {
      this.parse.add(2);
      this.equipara(new Token(Token.TipoToken.AND, null), ContextoSintactico.EXPRESION);
      Tipo r_tipo = this.R();
      Tipo e2_1_tipo = this.E2();

      if((e2_1_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))
      || e2_1_tipo.equals(r_tipo))
      && r_tipo.equals(new TipoBasico(TipoBasico.Basicos.LOGICO))) {
        tipo = r_tipo;
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(Set.of(new Token(Token.TipoToken.COMA, null),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.PUNTO_COMA, null))
            .contains(this.next_tok)) {
      this.parse.add(3);
      // lambda
      tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.EXPRESION));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Tipo R() throws IOException {
    Tipo tipo;
    this.parse.add(4);
    Tipo u_tipo = this.U();
    Tipo r2_tipo = this.R2();

    if(r2_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
      tipo = u_tipo;
    } else {
      if(Set.of(new TipoBasico(TipoBasico.Basicos.ENTERO), new TipoBasico(TipoBasico.Basicos.REAL))
              .contains(u_tipo) && u_tipo.equals(r2_tipo)) {
        tipo = new TipoBasico(TipoBasico.Basicos.LOGICO);
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    return tipo;
  }

  private Tipo R2() throws IOException {
    Tipo tipo = null;
    if(this.next_tok.getTipoToken() == Token.TipoToken.IGUALDAD) {
      this.parse.add(5);
      this.equipara(new Token(Token.TipoToken.IGUALDAD, null), ContextoSintactico.EXPRESION);
      Tipo u_tipo = this.U();
      Tipo r2_1_tipo = this.R2();

      if((r2_1_tipo.equals(u_tipo) || r2_1_tipo.equals(new TipoInterno(TipoInterno.Internos.OK)))
              && Set.of(new TipoBasico(TipoBasico.Basicos.ENTERO), new TipoBasico(TipoBasico.Basicos.REAL))
              .contains(u_tipo)) {
        tipo = u_tipo;
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(Set.of(new Token(Token.TipoToken.PUNTO_COMA, null),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.AND, null),
            new Token(Token.TipoToken.COMA, null))
            .contains(this.next_tok)) {
      this.parse.add(6);
      // lambda
      tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.EXPRESION));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Tipo U() throws IOException {
    Tipo tipo ;
    this.parse.add(7);
    Tipo v_tipo = this.V();
    Tipo u2_1_tipo = this.U2();

    if(u2_1_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
      tipo = v_tipo;
    } else {
      if(Set.of(new TipoBasico(TipoBasico.Basicos.ENTERO), new TipoBasico(TipoBasico.Basicos.REAL))
              .contains(v_tipo) && v_tipo.equals(u2_1_tipo)) {
        tipo = new TipoBasico(TipoBasico.Basicos.LOGICO);
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    return tipo;
  }

  private Tipo U2() throws IOException {
    Tipo tipo = null;
    if(this.next_tok.getTipoToken() == Token.TipoToken.MENOR_ESTRICTO) {
      this.parse.add(8);
      this.equipara(new Token(Token.TipoToken.MENOR_ESTRICTO, null), ContextoSintactico.EXPRESION);
      Tipo v_tipo = this.V();
      Tipo u2_1_tipo = this.U2();

      if((u2_1_tipo.equals(new TipoInterno(TipoInterno.Internos.OK)) || u2_1_tipo.equals(v_tipo))
      && Set.of(new TipoBasico(TipoBasico.Basicos.ENTERO), new TipoBasico(TipoBasico.Basicos.REAL))
              .contains(v_tipo)) {
        tipo = v_tipo;
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(Set.of(new Token(Token.TipoToken.AND, null),
            new Token(Token.TipoToken.PUNTO_COMA, null),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.IGUALDAD, null),
            new Token(Token.TipoToken.COMA, null))
            .contains(this.next_tok)) {
      this.parse.add(9);
      // lambda
      tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.EXPRESION));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Tipo V() throws IOException {
    Tipo tipo;
    this.parse.add(10);
    Tipo w_tipo = this.W();
    Tipo v2_tipo = this.V2();

    if(v2_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
      tipo = w_tipo;
    } else {
      if(v2_tipo.equals(w_tipo) && Set.of(new TipoBasico(TipoBasico.Basicos.ENTERO), new TipoBasico(TipoBasico.Basicos.REAL))
              .contains(w_tipo)) {
        tipo = w_tipo;
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }

    return tipo;
  }

  private Tipo V2() throws IOException {
    Tipo tipo = null;
    if(this.next_tok.getTipoToken() == Token.TipoToken.OP_RESTA) {
      this.parse.add(11);
      this.equipara(new Token(Token.TipoToken.OP_RESTA, null), ContextoSintactico.EXPRESION);
      Tipo w_tipo = this.W();
      Tipo v2_1_tipo = this.V2();

      if((v2_1_tipo.equals(new TipoInterno(TipoInterno.Internos.OK)) || v2_1_tipo.equals(w_tipo))
      && Set.of(new TipoBasico(TipoBasico.Basicos.ENTERO), new TipoBasico(TipoBasico.Basicos.REAL)).contains(w_tipo)) {
        tipo = w_tipo;
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(Set.of(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.COMA, null),
            new Token(Token.TipoToken.MENOR_ESTRICTO, null),
            new Token(Token.TipoToken.PUNTO_COMA, null),
            new Token(Token.TipoToken.IGUALDAD, null),
            new Token(Token.TipoToken.AND, null))
            .contains(this.next_tok)) {
      this.parse.add(12);
      // lambda
      tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.EXPRESION));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Tipo W() throws IOException {
    Tipo tipo;
    this.parse.add(13);
    Tipo x_tipo = this.X();
    Tipo w2_tipo = this.W2();

    if(w2_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
      tipo = x_tipo;
    } else {
      if(w2_tipo.equals(x_tipo) && Set.of(new TipoBasico(TipoBasico.Basicos.ENTERO), new TipoBasico(TipoBasico.Basicos.REAL))
              .contains(x_tipo)) {
        tipo = x_tipo;
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    return tipo;
  }

  private Tipo W2() throws IOException {
    Tipo tipo = null;
    if(this.next_tok.getTipoToken() == Token.TipoToken.OP_MODULO) {
      this.parse.add(14);
      this.equipara(new Token(Token.TipoToken.OP_MODULO, null), ContextoSintactico.EXPRESION);
      Tipo x_tipo = this.X();
      Tipo w2_1_tipo = this.W2();

      if((w2_1_tipo.equals(new TipoInterno(TipoInterno.Internos.OK)) || w2_1_tipo.equals(x_tipo))
              && Set.of(new TipoBasico(TipoBasico.Basicos.ENTERO), new TipoBasico(TipoBasico.Basicos.REAL))
              .contains(x_tipo)) {
        tipo = x_tipo;
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(Set.of(new Token(Token.TipoToken.PUNTO_COMA, null),
            new Token(Token.TipoToken.COMA, null),
            new Token(Token.TipoToken.IGUALDAD, null),
            new Token(Token.TipoToken.MENOR_ESTRICTO, null),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.OP_RESTA, null),
            new Token(Token.TipoToken.AND, null))
            .contains(this.next_tok)) {
      this.parse.add(15);
      // lambda
      tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.EXPRESION));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Tipo X() throws IOException {
    Tipo tipo = null;
    if(this.next_tok.getTipoToken() == Token.TipoToken.NOT) {
      this.parse.add(16);
      this.equipara(new Token(Token.TipoToken.NOT, null), ContextoSintactico.EXPRESION);
      Tipo x1_tipo = this.X();
      if(x1_tipo.equals(new TipoBasico(TipoBasico.Basicos.LOGICO))) {
        tipo = new TipoBasico(TipoBasico.Basicos.LOGICO);
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.AUTOINCREMENTO) {
      this.parse.add(17);
      this.equipara(new Token(Token.TipoToken.AUTOINCREMENTO, null), ContextoSintactico.EXPRESION);
      int id_pos = (int) this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0), ContextoSintactico.EXPRESION);

      if(this.tablaSimbolos.buscarTipoTS(id_pos).equals(new TipoBasico(TipoBasico.Basicos.ENTERO))) {
        tipo = new TipoBasico(TipoBasico.Basicos.ENTERO);
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.OP_RESTA) {
      this.parse.add(18);
      this.equipara(new Token(Token.TipoToken.OP_RESTA, null), ContextoSintactico.EXPRESION);
      Tipo x1_tipo = this.X();
      if(Set.of(new TipoBasico(TipoBasico.Basicos.ENTERO), new TipoBasico(TipoBasico.Basicos.REAL))
              .contains(x1_tipo)) {
        tipo = x1_tipo;
      } else {
        tipo = new TipoInterno(TipoInterno.Internos.ERROR);
      }
    }
    else if(Set.of(new Token(Token.TipoToken.CTE_LOGICA, false),
            new Token(Token.TipoToken.CTE_REAL, 0.0),
            new Token(Token.TipoToken.CTE_ENTERA, 0),
            new Token(Token.TipoToken.CTE_CADENA, ""),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA),
            new Token(Token.TipoToken.IDENTIFICADOR, 0))
            .contains(this.next_tok)) {
      this.parse.add(19);
      Tipo y_tipo = this.Y();
      tipo = y_tipo;
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.EXPRESION));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Tipo Y() throws IOException {
    Tipo tipo = null;
    if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA))) {
      this.parse.add(21);
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA), ContextoSintactico.EXPRESION);
      Tipo e_tipo = this.E();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE), ContextoSintactico.EXPRESION);
      tipo = e_tipo;
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.CTE_ENTERA) {
      this.parse.add(22);
      this.equipara(new Token(Token.TipoToken.CTE_ENTERA, null), ContextoSintactico.EXPRESION);
      tipo = new TipoBasico(TipoBasico.Basicos.ENTERO);
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.CTE_CADENA) {
      this.parse.add(23);
      this.equipara(new Token(Token.TipoToken.CTE_CADENA, null), ContextoSintactico.EXPRESION);
      tipo = new TipoBasico(TipoBasico.Basicos.CADENA);
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.CTE_REAL) {
      this.parse.add(24);
      this.equipara(new Token(Token.TipoToken.CTE_REAL, null), ContextoSintactico.EXPRESION);
      tipo = new TipoBasico(TipoBasico.Basicos.REAL);
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.CTE_LOGICA) {
      this.parse.add(25);
      this.equipara(new Token(Token.TipoToken.CTE_LOGICA, null), ContextoSintactico.EXPRESION);
      tipo = new TipoBasico(TipoBasico.Basicos.LOGICO);
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.IDENTIFICADOR) {
      this.parse.add(20);
      int id_pos = (int) this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0), ContextoSintactico.EXPRESION);
      Tipo y2_tipo = this.Y2();

      Tipo id_tipo = this.tablaSimbolos.buscarTipoTS(id_pos);
      if(id_tipo instanceof Funcion func) {
        if(y2_tipo.equals(func.getParametros())) {
          tipo = func.getTipoRetorno();
        } else {
          tipo = new TipoInterno(TipoInterno.Internos.ERROR);
        }
      } else {
        if(y2_tipo.equals(new TipoInterno(TipoInterno.Internos.OK))) {
          tipo = id_tipo;
        } else {
          tipo = new TipoInterno(TipoInterno.Internos.ERROR);
        }
      }
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.EXPRESION));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  private Tipo Y2() throws IOException {
    Tipo tipo = null;
    if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA))) {
      this.parse.add(27);
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA), ContextoSintactico.EXPRESION);
      Producto l_tipo = this.L();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE), ContextoSintactico.EXPRESION);

      tipo = l_tipo;
    }
    else if(Set.of(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.PUNTO_COMA, null),
            new Token(Token.TipoToken.MENOR_ESTRICTO, null),
            new Token(Token.TipoToken.IGUALDAD, null),
            new Token(Token.TipoToken.OP_RESTA, null),
            new Token(Token.TipoToken.COMA, null),
            new Token(Token.TipoToken.OP_MODULO, null),
            new Token(Token.TipoToken.AND, null))
            .contains(this.next_tok)) {
      this.parse.add(26);
      // lambda
      tipo = new TipoInterno(TipoInterno.Internos.OK);
    }
    else {
      this.gestorErrores.agregarError(new ErrorTokenNoEsperado(this.next_tok, ContextoSintactico.EXPRESION));
      this.gestorErrores.pararEnError();
    }
    return tipo;
  }

  public ArrayList<Token> getListaTokens() {
    return this.listaTokens;
  }

  public ArrayList<Integer> getParse() {
    return this.parse;
  }
}
