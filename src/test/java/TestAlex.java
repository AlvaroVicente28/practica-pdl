import es.grupo83.AnalizadorLexico;
import es.grupo83.AnalizadorSintactico;
import es.grupo83.TablaSimbolos;
import es.grupo83.Token;
import es.grupo83.afd.AutomataFinito;
import es.grupo83.afd.ImplementacionAFD;
import es.grupo83.error.*;
import es.grupo83.util.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestAlex {
  Pair<Iterable<Token>, GestorErrores> testearRecurso(String nombreRecurso) throws IOException {
    new MockUp<System>() {
      @Mock
      public void exit(int status) {
        throw new TestExitException();
      }
    };

    File recurso = new File(this.getClass().getResource(nombreRecurso).getPath());
    FileReader file = new FileReader(recurso);

    GestorErrores gestorErrores = new GestorErrores();
    TablaSimbolos tablaSimbolos = new TablaSimbolos();
    AutomataFinito afd = ImplementacionAFD.getAutomataFinito();

    AnalizadorLexico alex = new AnalizadorLexico(gestorErrores, tablaSimbolos, afd, file);
    AnalizadorSintactico asint = new AnalizadorSintactico(gestorErrores, alex);

    try {
      asint.run();
    }
    catch(TestExitException e) {
      // Tras parar en GestorErrores.pararEnError, se devuelve el control al test.
      // Permitimos que el ALex pare inmediatamente sin abortar el programa
    }
    return new Pair<>(asint.getListaTokens(), gestorErrores);
  }

  @Test
  void test1() throws IOException {
    GestorErrores current = testearRecurso("PidG83-1.txt").getSecond();

    GestorErrores expected = new GestorErrores();
    expected.setLinea(14);
    expected.agregarError(new ErrorRealMalEscrito(99, '\t'));

    assertEquals(expected.mensajes(), current.mensajes());
  }

  @Test
  void test2() throws IOException {
    Pair<Iterable<Token>, GestorErrores> current = testearRecurso("PidG83-2.txt");

    assertTrue(current.getSecond().mensajes().isEmpty());
    // TODO: Comprobar tokens
  }

  @Test
  void test3() throws IOException {
    Pair<Iterable<Token>, GestorErrores> current = testearRecurso("PidG83-3.txt");

    assertTrue(current.getSecond().mensajes().isEmpty());
    // TODO: Comprobar tokens
  }
}
