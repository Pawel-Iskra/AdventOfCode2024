package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public final class MyUtils {

    public static List<String> getInputLines(String pathToFile) {
        try (FileReader fileReader = new FileReader(pathToFile);
             BufferedReader br = new BufferedReader(fileReader)) {
            return br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MyUtils() {
    }
}

/*
  ** try-with-resources **
  1. Automatic closure: No finally block or manual close() call is required.
  2. AutoCloseable interface: Every resource used in this pattern must implement AutoCloseable (or Closeable e.g. FileReader, BufferedReader).
  3. Order: Resources are closed in the reverse order in which they were opened.
  4. Safety: Helps prevent resource leaks, such as unclosed files or other system resources.
 */