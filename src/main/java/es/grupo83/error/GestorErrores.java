package es.grupo83.error;

import java.util.ArrayList;

import es.grupo83.afd.AutomataFinito;
import es.grupo83.util.Pair;

public class GestorErrores {
  private static final String COLOR_ROJO = "\u001B[31m";
  private static final String COLOR_RESET = "\u001B[0m";

  private int colInicioLexema;

  private int numeroLinea;

  private int colActual;

  private String lineaActual;

  private ArrayList<Pair<Error, CtxError>> errores;

  public GestorErrores() {
    this.colInicioLexema = 0;
    this.numeroLinea = 1;
    this.colActual = 0;
    this.lineaActual = "";
    this.errores = new ArrayList<>();
  }

  /**
   * Pasa la linea actual
   */
  public void pasarLinea() {
    ++this.numeroLinea;
    this.colActual = 0;
    this.colInicioLexema = 0;
    this.lineaActual = "";
  }

  /**
   * Usado por el ALex para que guarde un caracter en la linea actual
   * @param chr El caracter a guardar
   */
  public void agregarCaracter(char chr) {
    if(chr == '\r' || chr == '\n' || chr == AutomataFinito.EOF) {
      return;
    }
    ++this.colActual;
    this.lineaActual += "" + chr;
  }

  /**
   * Usado por el ALex para comunicar al GE el comienzo de un nuevo lexema
   */
  public void nuevoLexema() {
    this.colInicioLexema = this.colActual - 1;
  }

  /**
   * Usado por los analizadores para guardar un error
   * @param error El error a guardar
   */
  public void agregarError(Error error) {
    CtxError ctx = new CtxError(this.numeroLinea, this.colInicioLexema, this.colActual, this.lineaActual);
    this.errores.add(new Pair<>(error, ctx));
  }

  public String mensajes() {
    String res = "";

    for(Pair<Error, CtxError> error : this.errores) {
      Error lanzado = error.getFirst();
      CtxError ctx = error.getSecond();

      String errorHeader = "ERROR (linea " + ctx.getLinea() + "): " + lanzado;

      // Obtenemos el numero de caracteres que ocupa el numero
      String lineaNum = "" + ctx.getLinea();
      // Como comprimimos la linea de contexto por la izquierda, hay que actualizar los nº de columna
      String ctxArchivoTrimmed = ctx.getCtxArchivo().stripLeading();
      int colInicioTrimmed = ctx.getColInicio() - (ctx.getCtxArchivo().length() - ctxArchivoTrimmed.length());
      if(colInicioTrimmed < 0) {
        colInicioTrimmed = 0;
      }
      int colFinTrimmed = ctx.getColFin() - (ctx.getCtxArchivo().length() - ctxArchivoTrimmed.length());
      if(colFinTrimmed < 0) {
        colFinTrimmed = 0;
      }

      String ctxErrorLinea = "\n" + lineaNum + "|\t" + ctxArchivoTrimmed;

      String subrayado = "";
      for(int i = 0; i < lineaNum.length(); ++i) {
        subrayado += " ";
      }
      subrayado += "|\t";
      for(int i = 0; i < colFinTrimmed; ++i) {
        char caracterEncima = ctxErrorLinea.charAt(i + 1 + lineaNum.length() + 2);
        if(caracterEncima == ' ' || caracterEncima == '\t' || Character.isISOControl(caracterEncima)) {
          // Si el caracter que esta encima es blanco, no se subraya debajo de el
          subrayado += "~";
          continue;
        }
        if(i < colInicioTrimmed) {
          subrayado += "~";
        } else {
          subrayado += "^";
        }
      }
      System.out.println("Col inicio: " + colInicioTrimmed + " Fin: " + colFinTrimmed);
      res += errorHeader + ctxErrorLinea + "\n" + subrayado + "\n";
    }

    return res;
  }

  /**
   * Si existen errores reportados, aborta el programa.
   * EOC, es NO-OP
   */
  public void pararEnError() {
    if(!this.errores.isEmpty()) {
      System.out.println(this.mensajes());
      System.exit(-1);
    }
  }

  /**
   * Se usa para probar el gestor. No se verifica nada sobre la linea dada.
   * @param linea La nueva linea
   */
  public void setLinea(int linea) {
    this.numeroLinea = linea;
  }

  /**
   * Se usa para probar el gestor.
   * @return La lista de errores
   */
  public Iterable<Pair<Error, CtxError>> getErrores() {
    return this.errores;
  }
}
