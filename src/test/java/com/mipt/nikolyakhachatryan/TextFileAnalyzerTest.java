package com.mipt.nikolyakhachatryan;

import com.mipt.nikolyakhachatryan.TextFileAnalyzer.TextFileAnalyzer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TextFileAnalyzerTest {

  @TempDir
  Path tempDir;

  @Test
  void testAnalyzeFile() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Path testFile = tempDir.resolve("test.txt");
    Files.write(testFile, Arrays.asList("Hello world!", "This is test."), java.nio.charset.StandardCharsets.UTF_8);

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    assertEquals(2, result.getLineCount());
    assertEquals(5, result.getWordCount());
    assertEquals(27, result.getCharCount());

    Map<Character, Long> freq = result.getCharFrequency();
    assertEquals(1L, freq.get('H'));
    assertEquals(2L, freq.get('o'));
    assertEquals(3L, freq.get('l'));
    assertEquals(2L, freq.get('\n'));
    assertEquals(1L, freq.get('!'));
    assertEquals(1L, freq.get('.'));
  }

  @Test
  void testSaveAnalysisResult() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    java.util.Map<Character, Long> freq = new java.util.HashMap<>();
    freq.put('H', 1L);
    freq.put('e', 2L);
    freq.put('l', 3L);
    freq.put('o', 2L);
    freq.put(' ', 3L);
    freq.put('w', 1L);
    freq.put('r', 1L);
    freq.put('d', 1L);
    freq.put('!', 1L);
    freq.put('T', 1L);
    freq.put('h', 1L);
    freq.put('i', 2L);
    freq.put('s', 3L);
    freq.put('t', 2L);
    freq.put('.', 1L);
    freq.put('\n', 2L);

    TextFileAnalyzer.AnalysisResult result = new TextFileAnalyzer.AnalysisResult(2, 5, 27, freq);

    Path outputFile = tempDir.resolve("analysis.txt");
    analyzer.saveAnalysisResult(result, outputFile.toString());

    assertTrue(Files.exists(outputFile));
    assertTrue(Files.size(outputFile) > 0);

    String content = Files.readString(outputFile, java.nio.charset.StandardCharsets.UTF_8);
    assertTrue(content.contains("Строк: 2"));
    assertTrue(content.contains("Слов: 5"));
    assertTrue(content.contains("Символов: 27"));
    assertTrue(content.contains("'H': 1"));
    assertTrue(content.contains("'o': 2"));
    assertTrue(content.contains("'\\n': 2"));
  }
}