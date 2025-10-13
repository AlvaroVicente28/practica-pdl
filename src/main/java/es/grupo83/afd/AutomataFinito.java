package es.grupo83.afd;

import es.grupo83.util.*;

import java.util.HashMap;
import java.util.Set;

public class AutomataFinito {
  private HashMap<Integer, Pair<HashMap<Character, Transicion>, Transicion>> transiciones;

  private int estado;

  private int estadoInicial;

  private Set<Integer> estadosFinales;

  public static final char EOF = '\0';

  public static final char[] DIGITOS = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

  public static final char[] ALFABETO = ("abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz".toUpperCase())
          .toCharArray();

  public static final char[] DELIMITADORES = new char[] {' ', '\t', '\n', '\r'};

  public static final char[] ASCII_IMPRIMIBLES = {
          ' ', '\t', '!', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
          '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
          ':', ';', '<', '=', '>', '?', '@',
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
          'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
          '[', ']', '^', '_', '`',
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
          'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
          '{', '|', '}', '~'
  };

  public AutomataFinito(int estadoIncial, Set<Integer> estadosFinales) {
    this.transiciones = new HashMap<>();
    this.estadoInicial = estadoIncial;
    this.estadosFinales = estadosFinales;
    this.estado = estadoIncial;
  }

  /**  
   * Devuelve el estado actual del autómata
   * 
   * @return el estado actual del autómata
   */
  public int getEstado() {
    return this.estado;
  }

  /**  
   * Indica si el autómata se encuentra en un estado final
   * 
   * @return true si el autómata se encuentra en un estado final, false en caso contrario
   */
  public boolean esFinal() {
    return this.estadosFinales.contains(this.estado);
  }

  /**  
   * Devuelve el autómata al estado inicial
   */
  public void reset() {
    this.estado = this.estadoInicial;
  }

  /**  
   * Agrega una transición al autómata finito
   * 
   * @param estadoInicial Estado inicial del que se transita
   * @param estadoFinal Estado final al que se transita
   * @param chr El caracter de la transición
   * @param accion La acción semántica asociada a la transición
   */
  public void agregarTransicion(int estadoInicial, int estadoFinal, char chr, AccionSemantica accion) {
    Transicion tr = new Transicion(estadoFinal, accion);
    Pair<HashMap<Character, Transicion>, ?> data = this.transiciones.get(estadoInicial);
    //Caso en el que estado ya tiene transiciones asociadas
    if(data != null) { 
      HashMap<Character, Transicion> columna = data.getFirst();
      columna.put(chr, tr);
    }
    //Caso en el que estado no tiene transiciones
    else { 
      HashMap<Character, Transicion> nuevaColumna = new HashMap<>();
      nuevaColumna.put(chr, tr);
      Pair<HashMap<Character, Transicion>, Transicion> fila = new Pair<>(nuevaColumna, null);
      this.transiciones.put(estadoInicial, fila);
    }
  }

  /**
   * Agrega un conjunto de transiciones al autómata finito
   * 
   * @param estadoInicial Estado inicial del que se transita
   * @param estadoFinal Estado final al que se transita
   * @param chrs Array de caracteres con los que se realiza la transición
   * @param accion La acción semántica asociada a la transición
   */
  public void agregarTransicionGrupo(int estadoInicial, int estadoFinal, char[] chrs, AccionSemantica accion) {
    for(char chr : chrs) {
      this.agregarTransicion(estadoInicial, estadoFinal, chr, accion);
    }
  }

  /**
   * Agrega un conjunto de transiciones al autómata finito
   * 
   * @param estadoInicial Estado inicial del que se transita
   * @param estadoFinal Estado final al que se transita
   * @param chrs Iterable de caracteres con los que se realiza la transición
   * @param accion La acción semántica asociada a la transición
   */
  public void agregarTransicionGrupo(int estadoInicial, int estadoFinal, Iterable<Character> chrs, AccionSemantica accion) {
    for(char chr : chrs) {
      this.agregarTransicion(estadoInicial, estadoFinal, chr, accion);
    }
  }

  /**
   * 
   * 
   * @param estadoInicial Estado inicial del que se transita
   * @param estadoFinal Estado final al que se transita
   * @param accion La acción semántica asociada a la transición
   */
  public void agregarTransicionOtroCaracter(int estadoInicial, int estadoFinal, AccionSemantica accion) {
    Transicion tr = new Transicion(estadoFinal, accion);
    Pair<?, Transicion> columna = this.transiciones.get(estadoInicial);
    if(columna != null) {
      columna.setSecond(tr);
    }
    else {
      Pair<HashMap<Character, Transicion>, Transicion> data = new Pair<>(new HashMap<>(), tr);
      this.transiciones.put(estadoInicial, data);
    }
  }

  /**
   * Realiza la transición del autómata al siguiente estado
   * 
   * @param chr El caracter a leer
   * @return la transición realizada, o null si no existe ninguna transición posible
   */
  public Transicion transicionar(char chr) {
    Pair<HashMap<Character, Transicion>, Transicion> fila = this.transiciones.get(this.estado);

    if(fila == null) {
      return null;
    }

    HashMap<Character, Transicion> columna = fila.getFirst();
    Transicion tr = columna.get(chr);

    if(tr != null) {
      this.estado = tr.getEstadoFinal();
      return tr;
    }
    Transicion trOtroCaracter = fila.getSecond();
    if(trOtroCaracter != null) {
      this.estado = trOtroCaracter.getEstadoFinal();
      return trOtroCaracter;
    }

    return null;
  }
}