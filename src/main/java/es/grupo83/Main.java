package es.grupo83;

import es.grupo83.afd.AutomataFinito;
import es.grupo83.afd.AccionSemantica;

import es.grupo83.error.GestorErrores;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {
  public static void main(String[] args) {
    String filePath = args[0];

    // Calcular caracteres imprimibles
    char[] asciiImprimibles = {
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

    AutomataFinito afd = new AutomataFinito(0, new int[] {2, 7, 5, 9, 15, 16, 18, 20, 21, 22, 23, 24, 25,
                                                                    26, 27, 28, 29, 30, 31, 32});
    afd.agregarTransicionGrupo(0, 0, AutomataFinito.DELIMITADORES, AccionSemantica.SOLO_LEER);

    afd.agregarTransicionGrupo(0, 1, AutomataFinito.ALFABETO, AccionSemantica.INICIAR_CONCATENAR_ID);
    afd.agregarTransicion(0, 1, '_', AccionSemantica.INICIAR_CONCATENAR_ID);

    afd.agregarTransicionGrupo(1, 1, AutomataFinito.ALFABETO, AccionSemantica.CONCATENAR_ID);
    afd.agregarTransicionGrupo(1, 1, AutomataFinito.DIGITOS, AccionSemantica.CONCATENAR_ID);
    afd.agregarTransicion(1, 1, '_', AccionSemantica.CONCATENAR_ID);

    afd.agregarTransicionOtroCaracter(1, 2, AccionSemantica.GENERAR_TOKEN_IDENTIFICADOR);

    afd.agregarTransicionGrupo(0, 3, AutomataFinito.DIGITOS, AccionSemantica.INICIAR_NUMERO_ENTERO);

    afd.agregarTransicionGrupo(3, 3, AutomataFinito.DIGITOS, AccionSemantica.CONCATENAR_NUMERO_ENTERO);

    afd.agregarTransicion(3, 4, '.', AccionSemantica.INICIAR_NUMERO_REAL);

    afd.agregarTransicionOtroCaracter(3, 5, AccionSemantica.GENERAR_TOKEN_CTE_ENTERA);

    afd.agregarTransicionGrupo(4, 6, AutomataFinito.DIGITOS, AccionSemantica.CONCATENAR_NUMERO_REAL);

    afd.agregarTransicionGrupo(6, 6, AutomataFinito.DIGITOS, AccionSemantica.CONCATENAR_NUMERO_REAL);

    afd.agregarTransicionOtroCaracter(6, 7, AccionSemantica.GENERAR_TOKEN_CTE_REAL);

    afd.agregarTransicion(0, 8, '\"', AccionSemantica.INICIAR_CADENA);
    afd.agregarTransicionGrupo(8, 9, asciiImprimibles, AccionSemantica.CONCATENAR_CADENA);
    afd.agregarTransicion(8, 10, '\\', AccionSemantica.SOLO_LEER);
    afd.agregarTransicionGrupo(10, 8, new char[] {'n', '"', '\\'}, AccionSemantica.MANEJAR_SECUENCIA_ESCAPE);
    afd.agregarTransicion(8, 11, '"', AccionSemantica.GENERAR_TOKEN_CTE_CADENA);

    afd.agregarTransicion(0, 11, '/', AccionSemantica.SOLO_LEER);
    afd.agregarTransicion(11, 12, '*', AccionSemantica.SOLO_LEER);
    // TODO: Mirar si EOF es valido en comentario
    afd.agregarTransicionOtroCaracter(12, 12, AccionSemantica.SOLO_LEER);
    afd.agregarTransicion(12, 13, '*', AccionSemantica.SOLO_LEER);
    afd.agregarTransicion(13, 13, '*', AccionSemantica.SOLO_LEER);
    afd.agregarTransicion(13, 0, '/', AccionSemantica.SOLO_LEER);
    // TODO: Mirar si EOF es valido en comentario
    afd.agregarTransicionOtroCaracter(13, 12, AccionSemantica.SOLO_LEER);

    afd.agregarTransicion(0, 14, '=', AccionSemantica.SOLO_LEER);

    afd.agregarTransicion(14, 15, '=', AccionSemantica.GENERAR_TOKEN_OPERADOR_IGUALDAD);

    afd.agregarTransicionOtroCaracter(14, 16, AccionSemantica.GENERAR_TOKEN_ASIG);

    afd.agregarTransicion(0, 17, '+', AccionSemantica.SOLO_LEER);

    afd.agregarTransicion(17, 18, '+', AccionSemantica.GENERAR_TOKEN_AUTOINCREMENTO);

    afd.agregarTransicion(0, 19, '&', AccionSemantica.SOLO_LEER);

    afd.agregarTransicion(19, 20, '&', AccionSemantica.GENERAR_TOKEN_AND);

    afd.agregarTransicion(0, 21, ',', AccionSemantica.GENERAR_TOKEN_COMA);

    afd.agregarTransicion(0, 22, '{', AccionSemantica.GENERAR_TOKEN_APERTURA_LLAVES);

    afd.agregarTransicion(0, 23, '}', AccionSemantica.GENERAR_TOKEN_CIERRE_LLAVES);

    afd.agregarTransicion(0, 24, '(', AccionSemantica.GENERAR_TOKEN_APERTURA_PAR);

    afd.agregarTransicion(0, 25, ')', AccionSemantica.GENERAR_TOKEN_CIERRE_PAR);

    afd.agregarTransicion(0, 26, '<', AccionSemantica.GENERAR_TOKEN_MENOR_ESTRICTO);

    afd.agregarTransicion(0, 27, '-', AccionSemantica.GENERAR_TOKEN_MENOS);

    afd.agregarTransicion(0, 28, '%', AccionSemantica.GENERAR_TOKEN_MODULO);

    afd.agregarTransicion(0, 29, '!', AccionSemantica.GENERAR_TOKEN_NOT);

    afd.agregarTransicion(0, 30, ':', AccionSemantica.GENERAR_TOKEN_DOS_PUNTOS);

    afd.agregarTransicion(0, 31, ';', AccionSemantica.GENERAR_TOKEN_PUNTO_COMA);

    afd.agregarTransicion(0, 32, AutomataFinito.EOF, AccionSemantica.GENERAR_TOKEN_EOF);

    GestorErrores gestorErrores = new GestorErrores();

    try {
      AnalizadorLexico alex = new AnalizadorLexico(gestorErrores, afd, filePath);
      AnalizadorSintactico asint = new AnalizadorSintactico(gestorErrores, alex);
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
