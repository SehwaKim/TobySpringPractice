package springbook.calc;

public interface CalcStrategy<T> {
    T doSomethingWithLine(String line, T value);
}