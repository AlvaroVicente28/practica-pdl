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
import java.nio.file.Files;
import java.nio.file.Path;

import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestAlex {
  private Pair<Iterable<Token>, GestorErrores> testearRecurso(String nombreRecurso) throws IOException {
    new MockUp<System>() {
      @Mock
      public void exit(int status) {
        throw new TestExitException();
      }
    };

    File recurso = new File(this.getClass().getResource("ALex/" + nombreRecurso).getPath());
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

  private String leerSolucion(String nombreRecurso) throws IOException {
    File file = new File(this.getClass().getResource("ALex/soluciones/" + nombreRecurso).getPath());
    return Files.readString(Path.of(file.getPath())).replace("\r", "");
  }

  private String tokensToString(Iterable<Token> tokens) {
    String res = "";

    for(Token token : tokens) {
      Object attr = token.getAttr();
      String atributeStr;
      if (attr == null) {
        atributeStr = "";
      }
      else if (attr instanceof Token.ORIENTACION_SIMBOLO) {
        atributeStr = ((Token.ORIENTACION_SIMBOLO) attr).ordinal() + "";
      }
      else if (token.getTipoToken() == Token.TipoToken.CTE_CADENA) {
        atributeStr = "\"" + attr + "\"";
      }
      else {
        atributeStr = attr.toString();
      }
      res += "<" + token.getTipoToken().ordinal() + ", " + atributeStr + ">\n";
    }

    return res;
  }

  @Test
  void test1() throws IOException {
    final String RECURSO = "PIdG83-1.txt";
    GestorErrores current = testearRecurso(RECURSO).getSecond();

    GestorErrores expected = new GestorErrores();
    expected.setLinea(14);
    expected.agregarError(new ErrorRealMalEscrito(99, '\t'));

    assertIterableEquals(expected.getErrores(), current.getErrores());
  }

  @Test
  void test2() throws IOException {
    final String RECURSO = "PIdG83-2.txt";
    Pair<Iterable<Token>, GestorErrores> current = testearRecurso(RECURSO);

    assertTrue(current.getSecond().mensajes().isEmpty());
    // TODO: Comprobar tokens
  }

  @Test
  void test3() throws IOException {
    final String RECURSO = "PIdG83-3.txt";

    Pair<Iterable<Token>, GestorErrores> current = testearRecurso(RECURSO);

    assertTrue(current.getSecond().mensajes().isEmpty());

    String actual = tokensToString(current.getFirst());

    String expected = leerSolucion(RECURSO);

    assertEquals(expected.trim(), actual.trim());
  }

  @Test
  void test4() throws IOException {
    final String RECURSO = "PIdG83-4.txt";

    Pair<Iterable<Token>, GestorErrores> current = testearRecurso(RECURSO);

    String expectedTokens = leerSolucion(RECURSO);

    assertTrue(current.getSecond().mensajes().isEmpty());
    assertEquals(expectedTokens.trim(), tokensToString(current.getFirst()).trim());
  }

  @Test
  void test5() throws IOException {
    final String RECURSO = "PIdG83-5.txt";

    Pair<Iterable<Token>, GestorErrores> current = testearRecurso(RECURSO);

    String expectedTokens = leerSolucion(RECURSO);

    assertTrue(current.getSecond().mensajes().isEmpty());
    assertEquals(expectedTokens.trim(), tokensToString(current.getFirst()).trim());
  }

  @Test
  void test6() throws IOException {
    final String RECURSO = "PIdG83-6.txt";

    GestorErrores actual = testearRecurso(RECURSO).getSecond();

    GestorErrores expected = new GestorErrores();
    expected.setLinea(20);
    expected.agregarError(new ErrorEnteroFueraRango(32769));

    assertIterableEquals(expected.getErrores(), actual.getErrores());
  }

  @Test
  void test7() throws IOException {
    final String RECURSO = "PIdG83-7.txt";

    Pair<Iterable<Token>, GestorErrores> actual = testearRecurso(RECURSO);
    String expectedTokens = leerSolucion(RECURSO);

    assertTrue(actual.getSecond().mensajes().isEmpty());
    assertEquals(expectedTokens, tokensToString(actual.getFirst()));
  }
}
