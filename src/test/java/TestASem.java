import es.grupo83.AnalizadorLexico;
import es.grupo83.AnalizadorSintactico;
import es.grupo83.TablaSimbolos;
import es.grupo83.afd.AutomataFinito;
import es.grupo83.afd.ImplementacionAFD;

import es.grupo83.error.ErrorRedeclaracionVariable;
import es.grupo83.error.GestorErrores;

import es.grupo83.semantica.TipoBasico;

import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TestASem {
  private GestorErrores testearRecurso(String nombreRecurso) throws IOException {
    new MockUp<System>() {
      @Mock
      public void exit(int status) {
        throw new TestExitException();
      }
    };

    File recurso = new File(this.getClass().getResource("ASem/" + nombreRecurso).getPath());
    FileReader file = new FileReader(recurso);

    GestorErrores gestorErrores = new GestorErrores();
    TablaSimbolos tablaSimbolos = new TablaSimbolos(gestorErrores);
    AutomataFinito afd = ImplementacionAFD.getAutomataFinito();

    AnalizadorLexico alex = new AnalizadorLexico(gestorErrores, tablaSimbolos, afd, file);
    AnalizadorSintactico asint = new AnalizadorSintactico(gestorErrores, tablaSimbolos, alex);

    try {
      asint.run();
    }
    catch(TestExitException e) {
      // Tras parar en GestorErrores.pararEnError, se devuelve el control al test.
      // Permitimos que el analizador pare inmediatamente sin abortar el programa
    }
    return gestorErrores;
  }

  @Test
  void test1() throws IOException {
    final String RECURSO = "PIdG83-1.txt";

    GestorErrores errores = testearRecurso(RECURSO);

    assertFalse(errores.getErrores().iterator().hasNext());
  }

  @Test
  void test2() throws IOException {
    final String RECURSO = "PIdG83-2.txt";

    GestorErrores errores = testearRecurso(RECURSO);

    assertFalse(errores.getErrores().iterator().hasNext());
  }

  @Test
  void test3() throws IOException {
    final String RECURSO = "PIdG83-3.txt";

    GestorErrores errores = testearRecurso(RECURSO);

    assertFalse(errores.getErrores().iterator().hasNext());
  }

  @Test
  void test4() throws IOException {
    final String RECURSO = "PIdG83-4.txt";

    GestorErrores errores = testearRecurso(RECURSO);

    GestorErrores expected = new GestorErrores();
    TablaSimbolos expectedTabla = new TablaSimbolos(expected);

    expectedTabla.crearTS();
    expectedTabla.inicioZonaDecl();
    int pos = expectedTabla.agregarORecuperarEntrada("b");
    expectedTabla.finZonaDecl();
    expectedTabla.insertarTipoBasicoTS(pos, new TipoBasico(TipoBasico.Basicos.LOGICO));
    ErrorRedeclaracionVariable err = new ErrorRedeclaracionVariable(pos, true);
    err.rellenarConTS(expectedTabla);

    expected.setLinea(9);
    expected.agregarError(err);
    expectedTabla.finTablaSimbolos();

    assertIterableEquals(expected.getErrores(), errores.getErrores());
  }

  @Test
  void test5() throws IOException {
    final String RECURSO = "PIdG83-5.txt";

    GestorErrores errores = testearRecurso(RECURSO);

    assertFalse(errores.getErrores().iterator().hasNext());
  }

  @Test
  void test6() throws IOException {
    final String RECURSO = "PIdG83-6.txt";

    GestorErrores errores = testearRecurso(RECURSO);

    assertFalse(errores.getErrores().iterator().hasNext());
  }

  @Test
  void test7() throws IOException {
    final String RECURSO = "PIdG83-7.txt";

    GestorErrores errores = testearRecurso(RECURSO);

    assertFalse(errores.getErrores().iterator().hasNext());
  }
}
