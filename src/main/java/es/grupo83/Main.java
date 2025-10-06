package es.grupo83;

import es.grupo83.afd.AutomataFinito;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    String filePath = args[0];

    // Calcular

    AutomataFinito afd = new AutomataFinito(0);
    afd.agregarTransicionGrupo(0, 1, AutomataFinito.ALFABETO, 0);
    afd.agregarTransicion(0, 1, '_', 0);

    afd.agregarTransicionGrupo(0, 1, AutomataFinito.ALFABETO, 1);
    afd.agregarTransicionGrupo(0, 1, AutomataFinito.DIGITOS, 1);
    afd.agregarTransicion(0, 1, '_', 1);

    afd.agregarTransicionOtroCaracter(1, 2, 2);

    afd.agregarTransicionGrupo(0, 3, AutomataFinito.DIGITOS, 3);

    afd.agregarTransicionGrupo(3, 3, AutomataFinito.DIGITOS, 4);

    afd.agregarTransicion(3, 4, '.', 5);

    afd.agregarTransicionOtroCaracter(3, 5, 6);

    afd.agregarTransicionGrupo(4, 6, AutomataFinito.DIGITOS, 7);

    afd.agregarTransicionGrupo(6, 6, AutomataFinito.DIGITOS, 8);

    afd.agregarTransicionOtroCaracter(6, 7, 9);

//    afd.agregarTransicion(0, 8, '\"', 10);

    // TODO: Comillas

    afd.agregarTransicion(0, 11, '/', 15);

    afd.agregarTransicion(11, 12, '*', 16);

    // TODO: Mirar si EOF es valido en comentario
    afd.agregarTransicionOtroCaracter(12, 12, 17);

    afd.agregarTransicion(12, 13, '*', 18);

    afd.agregarTransicion(13, 13, '*', 19);
    
    afd.agregarTransicion(13, 0, '/', 20);

    // TODO: Mirar si EOF es valido en comentario
    afd.agregarTransicionOtroCaracter(13, 12, 21);

    afd.agregarTransicion(0, 14, '=', 22);

    afd.agregarTransicion(14, 15, '=', 23);

    afd.agregarTransicionOtroCaracter(14, 16, 24);

    afd.agregarTransicion(0, 17, '+', 25);

    afd.agregarTransicion(17, 18, '+', 26);

    afd.agregarTransicion(0, 19, '&', 27);

    afd.agregarTransicion(19, 20, '&', 28);

    afd.agregarTransicion(0, 21, ',', 29);

    afd.agregarTransicion(0, 22, '{', 30);

    afd.agregarTransicion(0, 23, '}', 30);

    afd.agregarTransicion(0, 24, '(', 31);

    afd.agregarTransicion(0, 25, ')', 32);

    afd.agregarTransicion(0, 26, '<', 33);

    afd.agregarTransicion(0, 27, '-', 34);

    afd.agregarTransicion(0, 28, '%', 35);

    afd.agregarTransicion(0, 29, '!', 36);

    afd.agregarTransicion(0, 30, ':', 37);

    afd.agregarTransicion(0, 31, ';', 38);

    // TODO: Manejar DEL

    try {
      AnalizadorLexico alex = new AnalizadorLexico(afd, filePath);
      AnalizadorSintactico asint = new AnalizadorSintactico(alex);
      asint.run();
    }
    catch (FileNotFoundException e) {
      System.err.println("Archivo no encontrado: " + filePath);
    }
    catch (IOException e) {
      System.err.println("Error al leer archivo: " + e.getMessage());
      e.printStackTrace();
    }

  }
}
