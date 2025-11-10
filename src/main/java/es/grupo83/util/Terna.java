package es.grupo83.util;

/**
 * Tipo que representa una tupla de tres elementos
 * @param <S> Tipo del primer elemento
 * @param <T> Tipo del segundo elemento
 * @param <U> Tipo del tercer elemento
 */
public class Terna<S, T, U> {
  private S first;
  private T second;
  private U third;

  public Terna(S first, T second, U third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  public S getFirst() {
    return first;
  }

  public void setFirst(S first) {
    this.first = first;
  }

  public T getSecond() {
    return second;
  }

  public void setSecond(T second) {
    this.second = second;
  }

  public U getThird() {
    return third;
  }

  public void setThird(U third) {
    this.third = third;
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj) {
      return true;
    }
    if(obj == null) {
      return false;
    }

    if(!(obj instanceof Terna<?,?,?>)) {
      return false;
    }
    Terna<?, ?, ?> other = (Terna<?, ?, ?>) obj;
    return this.first.equals(other.first) && this.second.equals(other.second) && this.third.equals(other.third);
  }

  @Override
  public int hashCode() {
    return this.first.hashCode() * 100 + this.second.hashCode() * 10 + this.third.hashCode();
  }
}
