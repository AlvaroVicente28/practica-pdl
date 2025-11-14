package es.grupo83;

import es.grupo83.error.GestorErrores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class AnalizadorSintactico {
  private AnalizadorLexico alex;

  private GestorErrores gestorErrores;

  private ArrayList<Token> listaTokens;

  private ArrayList<Integer> parse;

  private Token next_tok;

  public AnalizadorSintactico(GestorErrores gestorErrores, AnalizadorLexico alex) {
    this.alex = alex;
    this.gestorErrores = gestorErrores;
    this.listaTokens = new ArrayList<>();
    this.parse = new ArrayList<>();
    this.next_tok = null;
  }

  public void run() throws IOException {
    this.next_tok = this.alex.nextToken();
    this.P();
    if(this.next_tok.getTipoToken() != Token.TipoToken.EOF) {
      throw new RuntimeException("Hay un error - Faltan pulir los errores");
      // TODO: Error
    }
  }

  private void equipara(Token token) throws IOException {
    if(this.next_tok.getTipoToken() == token.getTipoToken()) {
      this.next_tok = this.alex.nextToken();
      this.listaTokens.add(this.next_tok);
    }
    // TODO: Error
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
      this.B();
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
      // TODO: Error
    }
  }

  private void B() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.IF) {
      this.parse.add(40);
      this.equipara(new Token(Token.TipoToken.IF, null));
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA));
      this.E();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE));
      this.S();
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.LET) {
      this.parse.add(41);
      this.equipara(new Token(Token.TipoToken.LET, null));
      this.T();
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, null));
      this.B2();
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.SWITCH) {
      this.parse.add(45);
      this.equipara(new Token(Token.TipoToken.SWITCH, null));
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA));
      this.E();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE));
      this.equipara(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.APERTURA));
      this.SW();
      this.equipara(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE));
    }
    else if(Set.of(new Token(Token.TipoToken.RETURN, null),
            new Token(Token.TipoToken.WRITE, null),
            new Token(Token.TipoToken.READ, null),
            new Token(Token.TipoToken.IDENTIFICADOR, 0))
            .contains(this.next_tok)) {
      this.parse.add(44);
      this.S();
    }
    else {
      // TODO: Error
    }
  }

  private void B2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.PUNTO_COMA) {
      this.parse.add(42);
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.ASIGNACION) {
      this.parse.add(43);
      this.equipara(new Token(Token.TipoToken.ASIGNACION, null));
      this.E();
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
    }
    else {
      // TODO: Error
    }
  }

  private void F() throws IOException {
    this.parse.add(56);
    this.equipara(new Token(Token.TipoToken.FUNCTION, null));
    this.H();
    this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0));
    this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA));
    this.A();
    this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE));
    this.equipara(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.APERTURA));
    this.C();
    this.equipara(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE));
  }

  private void H() throws IOException {
    if(Set.of(new Token(Token.TipoToken.INT, null),
            new Token(Token.TipoToken.FLOAT, null),
            new Token(Token.TipoToken.BOOLEAN, null),
            new Token(Token.TipoToken.STRING, null))
            .contains(this.next_tok)) {
      this.parse.add(57);
      this.T();
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.VOID) {
      this.parse.add(58);
      this.equipara(new Token(Token.TipoToken.VOID, null));
    }
    else {
      // TODO: Error
    }
  }

  private void A() throws IOException {
    if(Set.of(new Token(Token.TipoToken.INT, null),
                    new Token(Token.TipoToken.FLOAT, null),
                    new Token(Token.TipoToken.BOOLEAN, null),
                    new Token(Token.TipoToken.STRING, null))
            .contains(this.next_tok)) {
      this.parse.add(59);
      this.T();
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, null));
      this.K();
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.VOID) {
      this.parse.add(60);
      this.equipara(new Token(Token.TipoToken.VOID, null));
    }
    else {
      // TODO: Error
    }
  }

  private void T() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.INT) {
      this.parse.add(52);
      this.equipara(new Token(Token.TipoToken.INT, null));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.FLOAT) {
      this.parse.add(53);
      this.equipara(new Token(Token.TipoToken.FLOAT, null));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.STRING) {
      this.parse.add(55);
      this.equipara(new Token(Token.TipoToken.STRING, null));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.BOOLEAN){
      this.parse.add(54);
      this.equipara(new Token(Token.TipoToken.BOOLEAN, null));
    } else {
      // TODO: Err
    }
  }

  private void C() throws IOException {
    if(Set.of(new Token(Token.TipoToken.SWITCH, null),
            new Token(Token.TipoToken.READ, null),
            new Token(Token.TipoToken.WRITE, null),
            new Token(Token.TipoToken.IF, null),
            new Token(Token.TipoToken.RETURN, null),
            new Token(Token.TipoToken.LET, null),
            new Token(Token.TipoToken.IDENTIFICADOR, 0)).contains(this.next_tok)) {
      this.parse.add(63);
      this.B();
      this.C();
    }
    else if(this.next_tok.equals(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      this.parse.add(64);
      // lambda
    }
    else {
      // TODO: Err
    }
  }

  private void S() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.IDENTIFICADOR) {
      this.parse.add(28);
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, null));
      this.S2();
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.WRITE) {
      this.parse.add(31);
      this.equipara(new Token(Token.TipoToken.WRITE, null));
      this.E();
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.READ) {
      this.parse.add(32);
      this.equipara(new Token(Token.TipoToken.READ, null));
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0));
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.RETURN) {
      this.parse.add(33);
      this.equipara(new Token(Token.TipoToken.RETURN, null));
      this.R1();
    }
    else {
      // TODO: Err
    }
  }

  private void S2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.ASIGNACION) {
      this.parse.add(29);
      this.equipara(new Token(Token.TipoToken.ASIGNACION, null));
      this.E();
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
    }
    else if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA))) {
      this.parse.add(30);
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA));
      this.L();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE));
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
    }
    else {
      // TODO: Err
    }
  }

  private void R1() throws IOException {
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
      this.E();
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.PUNTO_COMA) {
      this.parse.add(35);
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
    }
    else {
      // TODO: Err
    }
  }

  private void L() throws IOException {
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
      this.E();
      this.Q();
    }
    else if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      this.parse.add(37);
      // lambda
    }
    else {
      // TODO: err
    }
  }

  private void Q() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.COMA) {
      this.parse.add(38);
      this.equipara(new Token(Token.TipoToken.COMA, null));
      this.E();
      this.Q();
    }
    else if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      this.parse.add(39);
      // lambda
    }
    else {
      // TODO: err
    }
  }

  private void SW() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.CASE) {
      this.parse.add(46);
      this.equipara(new Token(Token.TipoToken.CASE, null));
      this.equipara(new Token(Token.TipoToken.CTE_ENTERA, 0));
      this.equipara(new Token(Token.TipoToken.DOS_PUNTOS, null));
      this.SWC();
      this.SW();
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.DEFAULT) {
      this.parse.add(47);
      this.equipara(new Token(Token.TipoToken.DEFAULT, null));
      this.equipara(new Token(Token.TipoToken.DOS_PUNTOS, null));
      this.SWC();
      this.SW();
    }
    else if(this.next_tok.equals(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      this.parse.add(48);
      // lambda
    }
    else {
      // TODO: Err
    }
  }

  private void SWC() throws IOException {
    if(Set.of(Token.TipoToken.SWITCH, Token.TipoToken.LET, Token.TipoToken.IDENTIFICADOR,
            Token.TipoToken.WRITE, Token.TipoToken.RETURN, Token.TipoToken.READ, Token.TipoToken.IF)
            .contains(this.next_tok.getTipoToken())) {
      this.parse.add(49);
      this.B();
      this.SWC();
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.BREAK) {
      this.parse.add(50);
      this.equipara(new Token(Token.TipoToken.BREAK, null));
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
      this.SWC();
    }
    else if(Set.of(new Token(Token.TipoToken.CASE, null), new Token(Token.TipoToken.DEFAULT, null),
            new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE))
            .contains(this.next_tok)) {
      this.parse.add(51);
      // lambda
    }
    else {
      // TODO: Err
    }
  }

  private void K() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.COMA) {
      this.parse.add(61);
      this.equipara(new Token(Token.TipoToken.COMA, null));
      this.T();
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0));
      this.K();
    }
    else if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      this.parse.add(62);
      // lambda
    }
    else {
      // TODO: Error
    }
  }

  private void E() throws IOException {
    this.parse.add(1);
    this.R();
    this.E2();
  }

  private void E2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.AND) {
      this.parse.add(2);
      this.equipara(new Token(Token.TipoToken.AND, null));
      this.R();
      this.E2();
    }
    else if(Set.of(new Token(Token.TipoToken.COMA, null),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.PUNTO_COMA, null))
            .contains(this.next_tok)) {
      this.parse.add(3);
      // lambda
    }
    else {
      // TODO
    }
  }

  private void R() throws IOException {
    this.parse.add(4);
    this.U();
    this.R2();
  }

  private void R2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.IGUALDAD) {
      this.parse.add(5);
      this.equipara(new Token(Token.TipoToken.IGUALDAD, null));
      this.U();
      this.R2();
    }
    else if(Set.of(new Token(Token.TipoToken.PUNTO_COMA, null),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.AND, null),
            new Token(Token.TipoToken.COMA, null))
            .contains(this.next_tok)) {
      this.parse.add(6);
      // lambda
    }
    else {
      // TODO: err
    }
  }

  private void U() throws IOException {
    this.parse.add(7);
    this.V();
    this.U2();
  }

  private void U2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.MENOR_ESTRICTO) {
      this.parse.add(8);
      this.equipara(new Token(Token.TipoToken.MENOR_ESTRICTO, null));
      this.V();
      this.U2();
    }
    else if(Set.of(new Token(Token.TipoToken.AND, null),
            new Token(Token.TipoToken.PUNTO_COMA, null),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.IGUALDAD, null),
            new Token(Token.TipoToken.COMA, null))
            .contains(this.next_tok)) {
      this.parse.add(9);
      // lambda
    }
    else {
      // TODO: Err
    }
  }

  private void V() throws IOException {
    this.parse.add(10);
    this.W();
    this.V2();
  }

  private void V2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.OP_RESTA) {
      this.parse.add(11);
      this.equipara(new Token(Token.TipoToken.OP_RESTA, null));
      this.W();
      this.V2();
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
    }
    else {
      // TODO
    }
  }

  private void W() throws IOException {
    this.parse.add(13);
    this.X();
    this.W2();
  }

  private void W2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.OP_MODULO) {
      this.parse.add(14);
      this.equipara(new Token(Token.TipoToken.OP_MODULO, null));
      this.X();
      this.W2();
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
    }
    else {
      // TODO: err
    }
  }

  private void X() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.NOT) {
      this.parse.add(16);
      this.equipara(new Token(Token.TipoToken.NOT, null));
      this.X();
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.AUTOINCREMENTO) {
      this.parse.add(17);
      this.equipara(new Token(Token.TipoToken.AUTOINCREMENTO, null));
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.OP_RESTA) {
      this.parse.add(18);
      this.equipara(new Token(Token.TipoToken.OP_RESTA, null));
      this.X();
    }
    else if(Set.of(new Token(Token.TipoToken.CTE_LOGICA, false),
            new Token(Token.TipoToken.CTE_REAL, 0.0),
            new Token(Token.TipoToken.CTE_ENTERA, 0),
            new Token(Token.TipoToken.CTE_CADENA, ""),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA),
            new Token(Token.TipoToken.IDENTIFICADOR, 0))
            .contains(this.next_tok)) {
      this.parse.add(19);
      this.Y();
    }
    else {
      // TODO
    }
  }

  private void Y() throws IOException {
    if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA))) {
      this.parse.add(21);
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA));
      this.E();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.CTE_ENTERA) {
      this.parse.add(22);
      this.equipara(new Token(Token.TipoToken.CTE_ENTERA, null));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.CTE_REAL) {
      this.parse.add(24);
      this.equipara(new Token(Token.TipoToken.CTE_REAL, null));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.CTE_CADENA) {
      this.parse.add(23);
      this.equipara(new Token(Token.TipoToken.CTE_CADENA, null));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.CTE_LOGICA) {
      this.parse.add(25);
      this.equipara(new Token(Token.TipoToken.CTE_LOGICA, null));
    }
    else if(this.next_tok.getTipoToken() == Token.TipoToken.IDENTIFICADOR) {
      this.parse.add(20);
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0));
      this.Y2();
    }
    else {
      // TODO
    }
  }

  private void Y2() throws IOException {
    if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA))) {
      this.parse.add(27);
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA));
      this.L();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE));
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
    }
    else {
      // TODO: err
    }
  }

  public ArrayList<Token> getListaTokens() {
    return this.listaTokens;
  }

  public ArrayList<Integer> getParse() {
    return this.parse;
  }
}
