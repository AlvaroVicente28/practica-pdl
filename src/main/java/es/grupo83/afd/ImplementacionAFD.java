package es.grupo83.afd;

import java.util.Set;

public class ImplementacionAFD {
  private ImplementacionAFD() { }

  public static AutomataFinito getAutomataFinito() {
    Set<Integer> estadosFinales = Set.of(2, 7, 5, 9, 15, 16, 18, 20, 21, 22, 23, 24, 25,
            26, 27, 28, 29, 30, 31, 32);
    AutomataFinito afd = new AutomataFinito(0, estadosFinales);
    // Delimitadores
    afd.agregarTransicionGrupo(0, 0, AutomataFinito.DELIMITADORES, AccionSemantica.SOLO_LEER);

    // Identificadores
    afd.agregarTransicionGrupo(0, 1, AutomataFinito.ALFABETO, AccionSemantica.INICIAR_CONCATENAR_ID);
    afd.agregarTransicion(0, 1, '_', AccionSemantica.INICIAR_CONCATENAR_ID);
    afd.agregarTransicionGrupo(1, 1, AutomataFinito.ALFABETO, AccionSemantica.CONCATENAR_ID);
    afd.agregarTransicionGrupo(1, 1, AutomataFinito.DIGITOS, AccionSemantica.CONCATENAR_ID);
    afd.agregarTransicion(1, 1, '_', AccionSemantica.CONCATENAR_ID);
    afd.agregarTransicionOtroCaracter(1, 2, AccionSemantica.GENERAR_TOKEN_IDENTIFICADOR);

    // Numeros
    afd.agregarTransicionGrupo(0, 3, AutomataFinito.DIGITOS, AccionSemantica.INICIAR_NUMERO_ENTERO);
    afd.agregarTransicionGrupo(3, 3, AutomataFinito.DIGITOS, AccionSemantica.CONCATENAR_NUMERO_ENTERO);
    afd.agregarTransicion(3, 4, '.', AccionSemantica.INICIAR_NUMERO_REAL);
    afd.agregarTransicionOtroCaracter(3, 5, AccionSemantica.GENERAR_TOKEN_CTE_ENTERA);
    afd.agregarTransicionOtroCaracter(4, 401, AccionSemantica.GEN_ERROR_REAL_MAL_ESCRITO);
    afd.agregarTransicionGrupo(4, 6, AutomataFinito.DIGITOS, AccionSemantica.CONCATENAR_NUMERO_REAL);
    afd.agregarTransicionGrupo(6, 6, AutomataFinito.DIGITOS, AccionSemantica.CONCATENAR_NUMERO_REAL);
    afd.agregarTransicionOtroCaracter(6, 7, AccionSemantica.GENERAR_TOKEN_CTE_REAL);

    // Cadenas
    afd.agregarTransicion(0, 8, '\"', AccionSemantica.INICIAR_CADENA);
    afd.agregarTransicionGrupo(8, 8, AutomataFinito.ASCII_IMPRIMIBLES, AccionSemantica.CONCATENAR_CADENA);
    afd.agregarTransicion(8, 10, '\\', AccionSemantica.SOLO_LEER);
    afd.agregarTransicionGrupo(10, 8, new char[]{'n', '"', '\\'}, AccionSemantica.MANEJAR_SECUENCIA_ESCAPE);
    afd.agregarTransicionOtroCaracter(10, 402, AccionSemantica.GEN_ERROR_SECUENCIA_ESCAPE_INVALIDO);
    afd.agregarTransicion(8, 9, '"', AccionSemantica.GENERAR_TOKEN_CTE_CADENA);

    // Comentarios
    afd.agregarTransicion(0, 11, '/', AccionSemantica.SOLO_LEER);
    afd.agregarTransicion(11, 12, '*', AccionSemantica.SOLO_LEER);
    afd.agregarTransicion(12, 403, AutomataFinito.EOF, AccionSemantica.GEN_ERROR_COMENTARIO_SIN_FIN);
    afd.agregarTransicionOtroCaracter(12, 12, AccionSemantica.SOLO_LEER);
    afd.agregarTransicion(12, 13, '*', AccionSemantica.SOLO_LEER);
    afd.agregarTransicion(13, 13, '*', AccionSemantica.SOLO_LEER);
    afd.agregarTransicion(13, 0, '/', AccionSemantica.SOLO_LEER);
    afd.agregarTransicion(13, 403, AutomataFinito.EOF, AccionSemantica.GEN_ERROR_COMENTARIO_SIN_FIN);
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

    return afd;
  }
}
