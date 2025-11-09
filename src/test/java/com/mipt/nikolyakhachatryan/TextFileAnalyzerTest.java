package com.mipt.nikolyakhachatryan;

import com.mipt.nikolyakhachatryan.TextFileAnalyzer.TextFileAnalyzer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    Files.write(testFile, Arrays.asList("Hello world!", "This is test."));

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    assertEquals(2, result.getLineCount());
    assertEquals(5, result.getWordCount());
    assertEquals(27, result.getCharCount());

    Map<Character, Long> freq = result.getCharFrequency();
    assertEquals(1L, freq.get('H'));
    assertEquals(1L, freq.get('\n'));
    assertTrue(freq.containsKey('!'));
    assertTrue(freq.containsKey('.'));
  }

  @Test
  void testSaveAnalysisResult() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    TextFileAnalyzer.AnalysisResult result = new TextFileAnalyzer.AnalysisResult(
      2, 5, 27,
      Map.of('H', 1L, 'e', 2L, '\n', 2L, '!', 1L, '.', 1L)
    );

    Path outputFile = tempDir.resolve("analysis.txt");
    analyzer.saveAnalysisResult(result, outputFile.toString());

    assertTrue(Files.exists(outputFile));
    assertTrue(Files.size(outputFile) > 0);

    String content = Files.readString(outputFile, java.nio.charset.StandardCharsets.UTF_8);
    assertTrue(content.contains("Количество строк: 2"));
    assertTrue(content.contains("Количество слов: 5"));
    assertTrue(content.contains("Количество символов: 27"));
    assertTrue(content.contains("'H': 1"));
    assertTrue(content.contains("'\\n': 2"));
  }
}