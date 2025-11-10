package es.grupo83.afd;

import es.grupo83.util.*;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

/**
 * Implementacion de un AFD.
 * <p>
 * Dado un estado, la busqueda de la transicion correspondiente se hace en tres pasos:
 * <p>
 * 1º. Se busca en la matriz de transiciones del estado actual con el caracter dado.
 * <p>
 * 2º. Si no se encuentra, se ejecuta la funcion proporcionada para el estado actual.
 * <ul>
 *  <li>Esta funcion, que puede ser distinta para cada estado, calcula la transicion a realizar</li>
 *  <li>Si no existe dicha funcion, este paso no se realiza</li>
 * </ul>
 * 3º. Por ultimo, se devuelve la transicion de Otro Caracter. Si esta transicion no existe, se devuelve null.
 */
public class AutomataFinito {
  private HashMap<Integer, Terna<HashMap<Character, Transicion>, Function<Character, Transicion>, Transicion>>
          transiciones;

  private int estado;

  private int estadoInicial;

  private Set<Integer> estadosFinales;

  public static final char EOF = '\0';

  public static final char[] DIGITOS = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

  public static final char[] ALFABETO = ("abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz".toUpperCase())
          .toCharArray();

  public static final char[] DELIMITADORES = new char[] {' ', '\t', '\n', '\r'};

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
    var data = this.transiciones.get(estadoInicial);
    // Caso en el que estado ya tiene transiciones asociadas
    if(data != null) { 
      HashMap<Character, Transicion> columna = data.getFirst();
      columna.put(chr, tr);
    }
    // Caso en el que estado no tiene transiciones
    else { 
      HashMap<Character, Transicion> nuevaColumna = new HashMap<>();
      nuevaColumna.put(chr, tr);
      Terna<HashMap<Character, Transicion>, Function<Character, Transicion>, Transicion> fila
              = new Terna<>(nuevaColumna, null, null);
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
   * Asigna una funcion de calculo de transiciones a un estado del AFD
   *
   * @param estadoInicial El estado origen de las transiciones que genere la funcion,
   * @param fn La funcion que se usara para el calculo de las transiciones que partan del estado dado.
   *           <p>Debe verificar:
   *           <ol>
   *           <li> Si existe una transicion desde el estadoInical por el caracter recibido como parametro,
   *           debe devolver dicha transicion </li>
   *           <li> Si no es posible dicha transicion o se usa la de Otro Caracter, devolvera null </li>
   *           </ol>
   */
  public void asignarFuncionCalculo(int estadoInicial, Function<Character, Transicion> fn) {
    Terna<?, Function<Character, Transicion>, ?> columna = this.transiciones.get(estadoInicial);
    if(columna != null) {
      columna.setSecond(fn);
    } else {
      Terna<HashMap<Character, Transicion>, Function<Character, Transicion>, Transicion> data
              = new Terna<>(new HashMap<>(), fn, null);
      this.transiciones.put(estadoInicial, data);
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
    Terna<?, ?, Transicion> columna = this.transiciones.get(estadoInicial);
    if(columna != null) {
      columna.setThird(tr);
    }
    else {
      Terna<HashMap<Character, Transicion>, Function<Character, Transicion>, Transicion> data
              = new Terna<>(new HashMap<>(), null, tr);
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
    var fila = this.transiciones.get(this.estado);

    if(fila == null) {
      return null;
    }

    // Paso 1: Buscar en la matriz de transiciones
    HashMap<Character, Transicion> columna = fila.getFirst();
    Transicion tr = columna.get(chr);

    if(tr != null) {
      this.estado = tr.getEstadoFinal();
      return tr;
    }
    // Paso 2: Usamos la funcion de calculo de la transicion para el estado actual y el caracter dado
    Function<Character, Transicion> calculo = fila.getSecond();
    if(calculo != null) {
      Transicion trAlternativa = calculo.apply(chr);
      if(trAlternativa != null) {
        this.estado = trAlternativa.getEstadoFinal();
        return trAlternativa;
      }
    }

    // Paso 3: Usamos la transicion de Otro Caracter
    Transicion trOtroCaracter = fila.getThird();
    if(trOtroCaracter != null) {
      this.estado = trOtroCaracter.getEstadoFinal();
      return trOtroCaracter;
    }

    // No existe transicion posible
    return null;
  }
}