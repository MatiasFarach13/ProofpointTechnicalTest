package matias44579954.proofpoint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Words {
    private static final String FILE_PATH = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "words.txt").toString();;


    public static void main(String[] args) {

        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));

            // 1. Convert to lowercase
            // 2. Remove punctuation (anything that is NOT a letter is replaced with a space)
            content = content.toLowerCase().replaceAll("[^a-zA-Záéíóúüñ ]", "");

            // Splits the string into words using any number of spaces as separators.
            String[] words = content.split("\\s+");

            Map<String, Integer> wordCounts = new HashMap<>();

            // Counts the frequency of each word
            for (String word : words) {
                if (!word.isEmpty()) {
                    wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                }
            }

            System.out.println("\n10 most frequent words in the text: ");
            System.out.println("--------------------------------------------------------\n");
            //Shows the most frequent words
            wordCounts.entrySet().stream() //Converts the HashMap into a set of key-value pairs and creates a stream.
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue())) //Compares the values (the frequencies) and orders them by frequency (from highest to lowest).
                    .limit(10) // Take only the first 10 words
                    .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue())); //Shows the word and its frequency.
            System.out.println("\n--------------------------------------------------------");

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}