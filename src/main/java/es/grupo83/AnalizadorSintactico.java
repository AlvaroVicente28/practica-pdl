package es.grupo83;

import es.grupo83.error.GestorErrores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class AnalizadorSintactico {
  private AnalizadorLexico alex;

  private GestorErrores gestorErrores;

  private ArrayList<Token> listaTokens = new ArrayList<>();

  private Token next_tok;

  public AnalizadorSintactico(GestorErrores gestorErrores, AnalizadorLexico alex) {
    this.alex = alex;
    this.gestorErrores = gestorErrores;
    this.next_tok = null;
  }

  public void run() throws IOException {
    Token token;
    do {
      token = this.alex.nextToken();
      this.listaTokens.add(token);        
    }
    while(token.getTipoToken() != Token.TipoToken.EOF);
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
      this.B();
      this.P();
    }
    if(first2.contains(next_tok)) {
      this.F();
      this.P();
    }
    if(follow.contains(next_tok)) {

    }
    else {
      // TODO: Error
    }
  }

  private void B() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.IF) {
      this.equipara(new Token(Token.TipoToken.IF, null));
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA));
      this.E();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE));
      this.S();
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.LET) {
      this.equipara(new Token(Token.TipoToken.LET, null));
      this.T();
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, null));
      this.B2();
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.SWITCH) {
      this.equipara(new Token(Token.TipoToken.SWITCH, null));
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA));
      this.E();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE));
      this.equipara(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.APERTURA));
      this.SW();
      this.equipara(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE));
    }
    if(Set.of(new Token(Token.TipoToken.RETURN, null),
            new Token(Token.TipoToken.WRITE, null),
            new Token(Token.TipoToken.READ, null),
            new Token(Token.TipoToken.IDENTIFICADOR, 0))
            .contains(this.next_tok)) {
      this.S();
    }
    else {
      // TODO: Error
    }
  }

  private void B2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.PUNTO_COMA) {
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.ASIGNACION) {
      this.equipara(new Token(Token.TipoToken.ASIGNACION, null));
      this.E();
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
    }
    else {
      // TODO: Error
    }
  }

  private void F() throws IOException {
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
      this.T();
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.VOID) {
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
      this.T();
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, null));
      this.K();
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.VOID) {
      this.equipara(new Token(Token.TipoToken.VOID, null));
    }
    else {
      // TODO: Error
    }
  }

  private void T() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.INT) {
      this.equipara(new Token(Token.TipoToken.INT, null));
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.FLOAT) {
      this.equipara(new Token(Token.TipoToken.FLOAT, null));
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.STRING) {
      this.equipara(new Token(Token.TipoToken.STRING, null));
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.BOOLEAN){
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
      this.B();
      this.C();
    }
    if(this.next_tok.equals(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      // lambda
    }
    else {
      // TODO: Err
    }
  }

  private void S() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.IDENTIFICADOR) {
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, null));
      this.S2();
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.WRITE) {
      this.equipara(new Token(Token.TipoToken.WRITE, null));
      this.E();
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.READ) {
      this.equipara(new Token(Token.TipoToken.READ, null));
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0));
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.RETURN) {
      this.equipara(new Token(Token.TipoToken.RETURN, null));
      this.R1();
    }
    else {
      // TODO: Err
    }
  }

  private void S2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.ASIGNACION) {
      this.equipara(new Token(Token.TipoToken.ASIGNACION, null));
      this.E();
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
    }
    if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA))) {
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
      this.E();
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.PUNTO_COMA) {
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
      this.E();
      this.Q();
    }
    if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      // lambda
    }
    else {
      // TODO: err
    }
  }

  private void Q() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.COMA) {
      this.equipara(new Token(Token.TipoToken.COMA, null));
      this.E();
      this.Q();
    }
    if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      // lambda
    }
    else {
      // TODO: err
    }
  }

  private void SW() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.CASE) {
      this.equipara(new Token(Token.TipoToken.CASE, null));
      this.equipara(new Token(Token.TipoToken.CTE_ENTERA, 0));
      this.equipara(new Token(Token.TipoToken.DOS_PUNTOS, null));
      this.SWC();
      this.SW();
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.DEFAULT) {
      this.equipara(new Token(Token.TipoToken.DEFAULT, null));
      this.equipara(new Token(Token.TipoToken.DOS_PUNTOS, null));
      this.SWC();
      this.SW();
    }
    if(this.next_tok.equals(new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE))) {
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
      this.B();
      this.SWC();
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.BREAK) {
      this.equipara(new Token(Token.TipoToken.BREAK, null));
      this.equipara(new Token(Token.TipoToken.PUNTO_COMA, null));
      this.SWC();
    }
    if(Set.of(new Token(Token.TipoToken.CASE, null), new Token(Token.TipoToken.DEFAULT, null),
            new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE))
            .contains(this.next_tok)) {
      // lambda
    }
    else {
      // TODO: Err
    }
  }

  private void K() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.COMA) {
      this.equipara(new Token(Token.TipoToken.COMA, null));
      this.T();
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0));
      this.K();
    }
    if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE))) {
      // lambda
    }
    else {
      // TODO: Error
    }
  }

  private void E() throws IOException {
    this.R();
    this.E2();
  }

  private void E2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.AND) {
      this.equipara(new Token(Token.TipoToken.AND, null));
      this.R();
      this.E2();
    }
    if(Set.of(new Token(Token.TipoToken.COMA, null),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.PUNTO_COMA, null))
            .contains(this.next_tok)) {
      // lambda
    }
    else {
      // TODO
    }
  }

  private void R() throws IOException {
    this.U();
    this.R2();
  }

  private void R2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.IGUALDAD) {
      this.equipara(new Token(Token.TipoToken.IGUALDAD, null));
      this.U();
      this.R2();
    }
    if(Set.of(new Token(Token.TipoToken.PUNTO_COMA, null),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.AND, null),
            new Token(Token.TipoToken.COMA, null))
            .contains(this.next_tok)) {
      // lambda
    }
    else {
      // TODO: err
    }
  }

  private void U() throws IOException {
    this.V();
    this.U2();
  }

  private void U2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.MENOR_ESTRICTO) {
      this.equipara(new Token(Token.TipoToken.MENOR_ESTRICTO, null));
      this.V();
      this.U2();
    }
    if(Set.of(new Token(Token.TipoToken.AND, null),
            new Token(Token.TipoToken.PUNTO_COMA, null),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.IGUALDAD, null),
            new Token(Token.TipoToken.COMA, null))
            .contains(this.next_tok)) {
      // lambda
    }
    else {
      // TODO: Err
    }
  }

  private void V() throws IOException {
    this.W();
    this.V2();
  }

  private void V2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.OP_RESTA) {
      this.equipara(new Token(Token.TipoToken.OP_RESTA, null));
      this.W();
      this.V2();
    }
    if(Set.of(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.COMA, null),
            new Token(Token.TipoToken.MENOR_ESTRICTO, null),
            new Token(Token.TipoToken.PUNTO_COMA, null),
            new Token(Token.TipoToken.IGUALDAD, null),
            new Token(Token.TipoToken.AND, null))
            .contains(this.next_tok)) {
      // lambda
    }
    else {
      // TODO
    }
  }

  private void W() throws IOException {
    this.X();
    this.W2();
  }

  private void W2() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.OP_MODULO) {
      this.equipara(new Token(Token.TipoToken.OP_MODULO, null));
      this.X();
      this.W2();
    }
    if(Set.of(new Token(Token.TipoToken.PUNTO_COMA, null),
            new Token(Token.TipoToken.COMA, null),
            new Token(Token.TipoToken.IGUALDAD, null),
            new Token(Token.TipoToken.MENOR_ESTRICTO, null),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.OP_RESTA, null),
            new Token(Token.TipoToken.AND))) {
      // lambda
    }
    else {
      // TODO: err
    }
  }

  private void X() throws IOException {
    if(this.next_tok.getTipoToken() == Token.TipoToken.NOT) {
      this.equipara(new Token(Token.TipoToken.NOT, null));
      this.X();
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.AUTOINCREMENTO) {
      this.equipara(new Token(Token.TipoToken.AUTOINCREMENTO, null));
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0));
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.OP_RESTA) {
      this.equipara(new Token(Token.TipoToken.OP_RESTA, null));
      this.X();
    }
    if(Set.of(new Token(Token.TipoToken.CTE_LOGICA, false),
            new Token(Token.TipoToken.CTE_REAL, 0.0),
            new Token(Token.TipoToken.CTE_ENTERA, 0),
            new Token(Token.TipoToken.CTE_CADENA, ""),
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA),
            new Token(Token.TipoToken.IDENTIFICADOR, 0))
            .contains(this.next_tok)) {
      this.Y();
    }
    else {
      // TODO
    }
  }

  private void Y() throws IOException {
    if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA))) {
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA));
      this.E();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE));
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.CTE_ENTERA) {
      this.equipara(new Token(Token.TipoToken.CTE_ENTERA, null));
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.CTE_REAL) {
      this.equipara(new Token(Token.TipoToken.CTE_REAL, null));
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.CTE_CADENA) {
      this.equipara(new Token(Token.TipoToken.CTE_CADENA, null));
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.CTE_LOGICA) {
      this.equipara(new Token(Token.TipoToken.CTE_LOGICA, null));
    }
    if(this.next_tok.getTipoToken() == Token.TipoToken.IDENTIFICADOR) {
      this.equipara(new Token(Token.TipoToken.IDENTIFICADOR, 0));
      this.Y2();
    }
    else {
      // TODO
    }
  }

  private void Y2() throws IOException {
    if(this.next_tok.equals(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA))) {
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA));
      this.L();
      this.equipara(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE));
    }
    if(Set.of(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            new Token(Token.TipoToken.PUNTO_COMA, null),
            new Token(Token.TipoToken.MENOR_ESTRICTO, null),
            new Token(Token.TipoToken.IGUALDAD, null),
            new Token(Token.TipoToken.OP_RESTA, null),
            new Token(Token.TipoToken.COMA, null),
            new Token(Token.TipoToken.OP_MODULO, null),
            new Token(Token.TipoToken.AND, null))) {
      // lambda
    }
    else {
      // TODO: err
    }
  }

  public ArrayList<Token> getListaTokens() {
    return this.listaTokens;
  }
}
