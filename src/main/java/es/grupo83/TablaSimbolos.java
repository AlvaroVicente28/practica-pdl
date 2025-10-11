package es.grupo83;

import java.util.ArrayList;

// TODO: Cuando tengamos el A. Semantico, podremos continuar la impl
public class TablaSimbolos {
  private ArrayList<String> lexemas;

  public TablaSimbolos() {
    this.lexemas = new ArrayList<>();
  }

  public int agregarORecuperarEntrada(String lexema) {
    int index = this.lexemas.indexOf(lexema);
    if(index != -1) {
      return index;
    }
    this.lexemas.add(lexema);
    return this.lexemas.size() - 1;
  }
}
