package es.grupo83.afd;

import es.grupo83.util.*;

import java.util.ArrayList;
import java.util.HashMap;

public class AutomataFinito {
  private ArrayList<Pair<HashMap<Character, Transicion>, Transicion>> transiciones;

  private int estado;

  private int estadoInicial;

  public static final char EOF = '\0';

  public static final char[] DIGITOS = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

  public static final char[] ALFABETO = ("abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz".toUpperCase())
          .toCharArray();


  public AutomataFinito(int estadoIncial) {
    this.transiciones = new ArrayList<>();
    this.estadoInicial = estadoIncial;
    this.estado = estadoIncial;
  }

  public int getEstado() {
    return this.estado;
  }

  public void reset() {
    this.estado = this.estadoInicial;
  }

  public void agregarTransicion(int estadoInicial, int estadoFinal, char chr, int accion) {
    Transicion tr = new Transicion(estadoFinal, accion);
    if(this.transiciones.size() > estadoInicial) {
      // TODO: Puede devolver null
      HashMap<Character, Transicion> columna = this.transiciones.get(estadoInicial).getFirst();
      columna.put(chr, tr);
    }
    else {
      HashMap<Character, Transicion> columna = new HashMap<>();
      columna.put(chr, tr);
      Pair<HashMap<Character, Transicion>, Transicion> data = new Pair<>(columna, null);
      this.transiciones.set(estadoInicial, data);
    }
  }

  public void agregarTransicionGrupo(int estadoInicial, int estadoFinal, char[] chrs, int accion) {
    for(char chr : chrs) {
      this.agregarTransicion(estadoInicial, estadoFinal, chr, accion);
    }
  }

  public void agregarTransicionOtroCaracter(int estadoInicial, int estadoFinal, int accion) {
    Transicion tr = new Transicion(estadoFinal, accion);
    if(this.transiciones.size() > estadoInicial) {
      // TODO: Puede devolver null
      Pair<?, Transicion> columna = this.transiciones.get(estadoInicial);
      columna.setSecond(tr);
    }
    else {
      Pair<HashMap<Character, Transicion>, Transicion> data = new Pair<>(new HashMap<>(), tr);
      this.transiciones.set(estadoInicial, data);
    }
  }

  /**
   * @param chr El caracter a leer
   * @return null si no existe transcion posible
   */
  public Transicion transicionar(char chr) {
    if(this.transiciones.size() <= this.estado) {
      return null;
    }
    Pair<HashMap<Character, Transicion>, Transicion> data = this.transiciones.get(this.estado);
    // TODO: data puede ser null
    HashMap<Character, Transicion> columna = data.getFirst();
    Transicion tr = columna.get(chr);

    return tr != null ? tr : data.getSecond();
  }
}
