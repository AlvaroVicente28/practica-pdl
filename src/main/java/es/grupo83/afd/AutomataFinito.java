package es.grupo83.afd;

import es.grupo83.util.*;

import java.util.HashMap;

public class AutomataFinito {
  private HashMap<Integer, Pair<HashMap<Character, Transicion>, Transicion>> transiciones;

  private int estado;

  private int estadoInicial;

  private int[] estadosFinales;

  public static final char EOF = '\0';

  public static final char[] DIGITOS = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

  public static final char[] ALFABETO = ("abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz".toUpperCase())
          .toCharArray();

  public static final char[] DELIMITADORES = new char[] {' ', '\t', '\n', '\r'};

  public AutomataFinito(int estadoIncial, int[] estadosFinales) {
    this.transiciones = new HashMap<>();
    this.estadoInicial = estadoIncial;
    this.estadosFinales = estadosFinales;
    this.estado = estadoIncial;
  }

  public int getEstado() {
    return this.estado;
  }

  public boolean esFinal() {
    for(int es : this.estadosFinales) {
      if(this.estadoInicial == es) {
        return true;
      }
    }
    return false;
  }

  public void reset() {
    this.estado = this.estadoInicial;
  }

  public void agregarTransicion(int estadoInicial, int estadoFinal, char chr, AccionSemantica accion) {
    Transicion tr = new Transicion(estadoFinal, accion);
    if(this.transiciones.containsKey(estadoInicial)) {
      HashMap<Character, Transicion> columna = this.transiciones.get(estadoInicial).getFirst();
      columna.put(chr, tr);
    }
    else {
      HashMap<Character, Transicion> columna = new HashMap<>();
      columna.put(chr, tr);
      Pair<HashMap<Character, Transicion>, Transicion> fila = new Pair<>(columna, null);
      this.transiciones.put(estadoInicial, fila);
    }
  }

  public void agregarTransicionGrupo(int estadoInicial, int estadoFinal, char[] chrs, AccionSemantica accion) {
    for(char chr : chrs) {
      this.agregarTransicion(estadoInicial, estadoFinal, chr, accion);
    }
  }

  public void agregarTransicionGrupo(int estadoInicial, int estadoFinal, Iterable<Character> chrs, AccionSemantica accion) {
    for(char chr : chrs) {
      this.agregarTransicion(estadoInicial, estadoFinal, chr, accion);
    }
  }

  public void agregarTransicionOtroCaracter(int estadoInicial, int estadoFinal, AccionSemantica accion) {
    Transicion tr = new Transicion(estadoFinal, accion);
    if(this.transiciones.containsKey(estadoInicial)) {
      Pair<?, Transicion> columna = this.transiciones.get(estadoInicial);
      columna.setSecond(tr);
    }
    else {
      Pair<HashMap<Character, Transicion>, Transicion> data = new Pair<>(new HashMap<>(), tr);
      this.transiciones.put(estadoInicial, data);
    }
  }

  /**
   * @param chr El caracter a leer
   * @return null si no existe transcion posible
   */
  public Transicion transicionar(char chr) {
    if(!this.transiciones.containsKey(this.estado)) {
      return null;
    }
    Pair<HashMap<Character, Transicion>, Transicion> fila = this.transiciones.get(this.estado);

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
