package springbook.calc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    private <T>T template(String numFilepath, CalcStrategy calcStrategy, T initValue) throws IOException{
        try(BufferedReader br = new BufferedReader(new FileReader(numFilepath))){
            T res = initValue;
            String line = null;
            while((line = br.readLine()) != null){
                res = (T) calcStrategy.doSomethingWithLine(line, res);
            }

            return res;
        }
    }

    public Integer calcSum(String numFilepath) throws IOException {
        CalcStrategy<Integer> calcStrategy = (line, value)-> value + Integer.valueOf(line);
        return template(numFilepath, calcStrategy, 0);
    }

    public Integer calcMultiply(String numFilepath) throws IOException{
        CalcStrategy<Integer> calcStrategy = (line, value)-> value * Integer.valueOf(line);
        return template(numFilepath, calcStrategy, 1);
    }

    public String concatenate(String filepath) throws IOException {
        CalcStrategy<String> calcStrategy = (line, value)-> value + line;
        return template(filepath, calcStrategy, "");
    }
}
