import es.grupo83.AnalizadorLexico;
import es.grupo83.AnalizadorSintactico;
import es.grupo83.TablaSimbolos;
import es.grupo83.Token;
import es.grupo83.afd.AutomataFinito;
import es.grupo83.afd.ImplementacionAFD;
import es.grupo83.error.ErrorTokenNoEsperado;
import es.grupo83.error.GestorErrores;

import es.grupo83.util.Pair;
import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class TestASint {
  private Pair<Iterable<Integer>, GestorErrores> testearRecurso(String nombreRecurso) throws IOException {
    new MockUp<System>() {
      @Mock
      public void exit(int status) {
        throw new TestExitException();
      }
    };

    File recurso = new File(this.getClass().getResource("ASint/" + nombreRecurso).getPath());
    FileReader file = new FileReader(recurso);

    GestorErrores gestorErrores = new GestorErrores();
    TablaSimbolos tablaSimbolos = new TablaSimbolos(gestorErrores);
    AutomataFinito afd = ImplementacionAFD.getAutomataFinito();

    AnalizadorLexico alex = new AnalizadorLexico(gestorErrores, tablaSimbolos, afd, file);
    AnalizadorSintactico asint = new AnalizadorSintactico(gestorErrores, alex);

    try {
      asint.run();
    }
    catch(TestExitException e) {
      // Tras parar en GestorErrores.pararEnError, se devuelve el control al test.
      // Permitimos que el analizador pare inmediatamente sin abortar el programa
    }
    return new Pair<>(asint.getParse(), gestorErrores);
  }

  private Iterable<Integer> leerSolucion(String nombreRecurso) throws IOException {
    ArrayList<Integer> res = new ArrayList<>();

    File file = new File(this.getClass().getResource("ASint/soluciones/" + nombreRecurso).getPath());
    String fileStr = Files.readString(Path.of(file.getPath()));

    for(String regla : fileStr.split(" ")) {
      if(regla.contains("D")) {
        continue;
      }
      res.add(Integer.parseInt(regla));
    }

    return res;
  }

  @Test
  void test1() throws IOException {
    final String RECURSO = "PIdG83-1.txt";

    Pair<Iterable<Integer>, GestorErrores> current = testearRecurso(RECURSO);
    Iterable<Integer> expectedParse = leerSolucion(RECURSO);

    assertTrue(current.getSecond().mensajes().isEmpty());
    assertIterableEquals(expectedParse, current.getFirst());
  }

  @Test
  void test2() throws IOException {
    final String RECURSO = "PIdG83-2.txt";

    Pair<Iterable<Integer>, GestorErrores> current = testearRecurso(RECURSO);
    Iterable<Integer> expectedParse = leerSolucion(RECURSO);

    assertTrue(current.getSecond().mensajes().isEmpty());
    assertIterableEquals(expectedParse, current.getFirst());
  }

  @Test
  void test3() throws IOException {
    final String RECURSO = "PIdG83-3.txt";

    Pair<Iterable<Integer>, GestorErrores> current = testearRecurso(RECURSO);
    Iterable<Integer> expectedParse = leerSolucion(RECURSO);

    assertTrue(current.getSecond().mensajes().isEmpty());
    assertIterableEquals(expectedParse, current.getFirst());
  }

  @Test
  void test4() throws IOException {
    final String RECURSO = "PIdG83-4.txt";

    Pair<Iterable<Integer>, GestorErrores> current = testearRecurso(RECURSO);
    Iterable<Integer> expectedParse = leerSolucion(RECURSO);

    assertTrue(current.getSecond().mensajes().isEmpty());
    assertIterableEquals(expectedParse, current.getFirst());
  }

  @Test
  void test5() throws IOException {
    final String RECURSO = "PIdG83-5.txt";

    GestorErrores current = testearRecurso(RECURSO).getSecond();

    GestorErrores expected = new GestorErrores();
    expected.setLinea(26);
    expected.agregarError(
            new ErrorTokenNoEsperado(new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
                    ErrorTokenNoEsperado.ContextoSintactico.DECLARACION_FUNCION)
    );

    assertIterableEquals(expected.getErrores(), current.getErrores());
  }

  @Test
  void test6() throws IOException {
    final String RECURSO = "PIdG83-6.txt";

    Pair<Iterable<Integer>, GestorErrores> current = testearRecurso(RECURSO);
    Iterable<Integer> expectedParse = leerSolucion(RECURSO);

    assertTrue(current.getSecond().mensajes().isEmpty());
    assertIterableEquals(expectedParse, current.getFirst());
  }

  @Test
  void test7() throws IOException {
    final String RECURSO = "PIdG83-7.txt";

    Pair<Iterable<Integer>, GestorErrores> current = testearRecurso(RECURSO);
    Iterable<Integer> expectedParse = leerSolucion(RECURSO);

    assertTrue(current.getSecond().mensajes().isEmpty());
    assertIterableEquals(expectedParse, current.getFirst());
  }

  @Test
  void test8() throws IOException {
    final String RECURSO = "PIdG83-8.txt";

    GestorErrores current = testearRecurso(RECURSO).getSecond();

    GestorErrores expected = new GestorErrores();
    expected.setLinea(32);
    expected.agregarError(new ErrorTokenNoEsperado(
            new Token(Token.TipoToken.PARENTESIS, Token.ORIENTACION_SIMBOLO.CIERRE),
            ErrorTokenNoEsperado.ContextoSintactico.SENTENCIA_SIMPLE
    ));

    assertIterableEquals(expected.getErrores(), current.getErrores());
  }
}
