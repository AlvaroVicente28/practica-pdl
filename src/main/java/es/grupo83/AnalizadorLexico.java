package es.grupo83;

import java.util.HashMap;

import es.grupo83.afd.AutomataFinito;
import es.grupo83.afd.Transicion;
import es.grupo83.afd.AccionSemantica;
import es.grupo83.error.*;

import java.io.FileReader;
import java.io.IOException;

public class AnalizadorLexico {
  private GestorErrores gestorErrores;

  private TablaSimbolos tablaSimbolos;

  private AutomataFinito afd;

  private FileReader lectorFichero;

  private char chrActual;

  private HashMap<String, Token> palabrasReservadas;

  public AnalizadorLexico(GestorErrores gestorErrores, TablaSimbolos tablaSimbolos,
                          AutomataFinito afd, String filePath) throws IOException {
    this(gestorErrores, tablaSimbolos, afd, new FileReader(filePath));
  }

  public AnalizadorLexico(GestorErrores gestorErrores, TablaSimbolos tablaSimbolos,
                          AutomataFinito afd, FileReader file) throws IOException {
    this.palabrasReservadas = new HashMap<>();
    palabrasReservadas.put("true", new Token(Token.TipoToken.CTE_LOGICA, true));
    palabrasReservadas.put("false", new Token(Token.TipoToken.CTE_LOGICA, false));
    palabrasReservadas.put("int", new Token(Token.TipoToken.INT, null));
    palabrasReservadas.put("float", new Token(Token.TipoToken.FLOAT, null));
    palabrasReservadas.put("boolean", new Token(Token.TipoToken.BOOLEAN, null));
    palabrasReservadas.put("string", new Token(Token.TipoToken.STRING, null));
    palabrasReservadas.put("void", new Token(Token.TipoToken.VOID, null));
    palabrasReservadas.put("let", new Token(Token.TipoToken.LET, null));
    palabrasReservadas.put("write", new Token(Token.TipoToken.WRITE, null));
    palabrasReservadas.put("read", new Token(Token.TipoToken.READ, null));
    palabrasReservadas.put("return", new Token(Token.TipoToken.RETURN, null));
    palabrasReservadas.put("function", new Token(Token.TipoToken.FUNCTION, null));
    palabrasReservadas.put("if", new Token(Token.TipoToken.IF, null));
    palabrasReservadas.put("switch", new Token(Token.TipoToken.SWITCH, null));
    palabrasReservadas.put("case", new Token(Token.TipoToken.CASE, null));
    palabrasReservadas.put("default", new Token(Token.TipoToken.DEFAULT, null));
    palabrasReservadas.put("break", new Token(Token.TipoToken.BREAK, null));

    this.tablaSimbolos = tablaSimbolos;
    this.gestorErrores = gestorErrores;
    this.afd = afd;

    this.lectorFichero = file;

    int read = this.lectorFichero.read();
    this.chrActual = read == -1 ? AutomataFinito.EOF : (char) read;
  }

  public Token nextToken() throws IOException {
    this.afd.reset();
    // Estado interno
    String lexemaId = "";
    int contadorLongitudId = 0;
    long entero = 0;
    int contadorDecimales = 0;
    String cadena = "";
    int contadorCaracteresCadena = 0;

    while(!this.afd.esFinal()) {
      if(this.chrActual == '\n') {
        this.gestorErrores.pasarLinea();
      }
      Transicion tr = this.afd.transicionar(this.chrActual);
      if(tr == null) {
        this.gestorErrores.agregarError(new ErrorCaracterNoEsperado(this.chrActual));
        this.gestorErrores.pararEnError();
      }
      AccionSemantica accionSem = tr.getAccionSemantica();
      switch (accionSem) {
        case SOLO_LEER -> this.leer();
        case INICIAR_CONCATENAR_ID -> {
          lexemaId = "" + this.chrActual;
          contadorLongitudId = 1;
          this.leer();
        }
        case CONCATENAR_ID -> {
          lexemaId += "" + this.chrActual;
          ++contadorLongitudId;
          this.leer();
        }
        case GENERAR_TOKEN_IDENTIFICADOR -> {
          Token pr = this.palabrasReservadas.get(lexemaId);
          if(pr != null) {
            return pr;
          }
          return new Token(Token.TipoToken.IDENTIFICADOR, this.tablaSimbolos.agregarORecuperarEntrada(lexemaId));
        }
        case INICIAR_NUMERO_ENTERO -> {
          entero = (long) this.chrActual - '0';
          this.leer();
        }
        case CONCATENAR_NUMERO_ENTERO -> {
          entero *= 10;
          entero += this.chrActual - '0';
          this.leer();
        }
        case GENERAR_TOKEN_CTE_ENTERA -> {
          if(entero > 32767) {
            this.gestorErrores.agregarError(new ErrorEnteroFueraRango(entero));
            this.gestorErrores.pararEnError();
          }
          return new Token(Token.TipoToken.CTE_ENTERA, (int) entero);
        }
        case INICIAR_NUMERO_REAL -> {
          contadorDecimales = 0;
          this.leer();
        }
        case CONCATENAR_NUMERO_REAL -> {
          entero *= 10;
          entero += this.chrActual - '0';
          ++contadorDecimales;
          this.leer();
        }
        case GENERAR_TOKEN_CTE_REAL -> {
          double real = entero * Math.pow(10, (double) -1 * contadorDecimales);
          if(real > 117549436.0) {
            this.gestorErrores.agregarError(new ErrorRealFueraRango(real));
            this.gestorErrores.pararEnError();
          }
          return new Token(Token.TipoToken.CTE_REAL, (float) real);
        }
        case INICIAR_CADENA -> {
          cadena = "";
          contadorCaracteresCadena = 0;
          this.leer();
        }
        case CONCATENAR_CADENA -> {
          cadena += "" + this.chrActual;
          ++contadorCaracteresCadena;
          this.leer();
        }
        case MANEJAR_SECUENCIA_ESCAPE -> {
          if(this.chrActual == 'n') {
            cadena += "\n";
          }
          if(this.chrActual == '\\') {
            cadena += "\\";
          }
          if(this.chrActual == '"') {
            cadena += "\"";
          }
          ++contadorCaracteresCadena;
          this.leer();
        }
        case GENERAR_TOKEN_CTE_CADENA -> {
          if(contadorCaracteresCadena > 64) {
            this.gestorErrores.agregarError(new ErrorCadenaDemasiadoLarga(cadena));
            this.gestorErrores.pararEnError();
          }
          Token res = new Token(Token.TipoToken.CTE_CADENA, cadena);
          this.leer();
          return res;
        }
        case GENERAR_TOKEN_OPERADOR_IGUALDAD -> {
          this.leer();
          return new Token(Token.TipoToken.IGUALDAD, null);
        }
        case GENERAR_TOKEN_ASIG -> {
          return new Token(Token.TipoToken.ASIGNACION, null);
        }
        case GENERAR_TOKEN_AUTOINCREMENTO -> {
          this.leer();
          return new Token(Token.TipoToken.AUTOINCREMENTO, null);
        }
        case GENERAR_TOKEN_AND -> {
          this.leer();
          return new Token(Token.TipoToken.AND, null);
        }
        case GENERAR_TOKEN_COMA -> {
          this.leer();
          return new Token(Token.TipoToken.COMA, null);
        }
        case GENERAR_TOKEN_APERTURA_LLAVES -> {
          this.leer();
          return new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.APERTURA);
        }
        case GENERAR_TOKEN_CIERRE_LLAVES -> {
          this.leer();
          return new Token(Token.TipoToken.LLAVES, Token.ORIENTACION_SIMBOLO.CIERRE);
        }
        case GENERAR_TOKEN_APERTURA_PAR -> {
          this.leer();
          return new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.APERTURA);
        }
        case GENERAR_TOKEN_CIERRE_PAR -> {
          this.leer();
          return new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE);
        }
        case GENERAR_TOKEN_MENOR_ESTRICTO -> {
          this.leer();
          return new Token(Token.TipoToken.MENOR_ESTRICTO, null);
        }
        case GENERAR_TOKEN_MENOS -> {
          this.leer();
          return new Token(Token.TipoToken.OP_RESTA, null);
        }
        case GENERAR_TOKEN_MODULO -> {
          this.leer();
          return new Token(Token.TipoToken.OP_MODULO, null);
        }
        case GENERAR_TOKEN_NOT -> {
          this.leer();
          return new Token(Token.TipoToken.NOT, null);
        }
        case GENERAR_TOKEN_DOS_PUNTOS -> {
          this.leer();
          return new Token(Token.TipoToken.DOS_PUNTOS, null);
        }
        case GENERAR_TOKEN_PUNTO_COMA -> {
          this.leer();
          return new Token(Token.TipoToken.PUNTO_COMA, null);
        }
        case GENERAR_TOKEN_EOF -> {
          return new Token(Token.TipoToken.EOF, null);
        }
        case GEN_ERROR_REAL_MAL_ESCRITO -> {
          this.gestorErrores.agregarError(new ErrorRealMalEscrito(entero, this.chrActual));
          this.gestorErrores.pararEnError();
        }
        case GEN_ERROR_SECUENCIA_ESCAPE_INVALIDO -> {
          this.gestorErrores.agregarError(new ErrorSecuenciaEscapeInvalida(this.chrActual));
          this.gestorErrores.pararEnError();
        }
        case GEN_ERROR_COMENTARIO_SIN_FIN -> {
          this.gestorErrores.agregarError(new ErrorComentarioSinFin());
          this.gestorErrores.pararEnError();
        }
      }
    }

    throw new RuntimeException("No se retorna un token!");
  }

  private void leer() throws IOException {
    int read = this.lectorFichero.read();
    this.chrActual = read == -1 ? AutomataFinito.EOF : (char) read;
  }

}
