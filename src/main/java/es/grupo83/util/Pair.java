package es.grupo83.util;

public class Pair<F, S> {
  private F first;
  private S second;

  public Pair(F first, S second) {
    this.first = first;
    this.second = second;
  }

  public F getFirst() {
    return this.first;
  }

  public void setFirst(F elem) {
    this.first = elem;
  }

  public S getSecond() {
    return this.second;
  }

  public void setSecond(S elem) {
    this.second = elem;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null) {
      return false;
    }
    if(this == obj) {
      return true;
    }

    if(!(obj instanceof Pair<?, ?>)) {
      return false;
    }

    Pair<?, ?> other = (Pair<?, ?>) obj;
    return this.first.equals(other.first) && this.second.equals(other.second);
  }

  @Override
  public int hashCode() {
    return this.first.hashCode() * 10 + this.second.hashCode();
  }
}
