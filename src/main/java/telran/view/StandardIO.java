package telran.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class StandardIO implements InputOutput {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    PrintStream writer = System.out;

    @Override
    public String readString(String prompt) {
        writer.println(prompt);
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void writeString(String str) {
        writer.println(str);
    }
}
