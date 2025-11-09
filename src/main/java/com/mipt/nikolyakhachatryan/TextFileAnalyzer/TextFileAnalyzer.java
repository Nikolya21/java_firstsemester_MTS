package com.mipt.nikolyakhachatryan.TextFileAnalyzer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TextFileAnalyzer {

  public static class AnalysisResult {
    private final long lineCount;
    private final long wordCount;
    private final long charCount;
    private final Map<Character, Long> charFrequency;

    public AnalysisResult(long lineCount, long wordCount, long charCount, Map<Character, Long> charFrequency) {
      this.lineCount = lineCount;
      this.wordCount = wordCount;
      this.charCount = charCount;
      this.charFrequency = new HashMap<>(charFrequency);
    }

    public long getLineCount() { return lineCount; }
    public long getWordCount() { return wordCount; }
    public long getCharCount() { return charCount; }
    public Map<Character, Long> getCharFrequency() { return new HashMap<>(charFrequency); }

    @Override
    public String toString() {
      return "AnalysisResult{" +
        "lineCount=" + lineCount +
        ", wordCount=" + wordCount +
        ", charCount=" + charCount +
        ", charFrequency=" + charFrequency +
        '}';
    }
  }

  public AnalysisResult analyzeFile(String filePath) throws IOException {
    long lineCount = 0;
    long wordCount = 0;
    long charCount = 0;
    Map<Character, Long> charFrequency = new HashMap<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lineCount++;
        charCount += line.length();
        charCount++;
        for (char c : line.toCharArray()) {
          charFrequency.merge(c, 1L, Long::sum);
        }
        charFrequency.merge('\n', 1L, Long::sum);
        String[] words = line.trim().split("\\s+");
        if (!line.trim().isEmpty()) {
          wordCount += words.length;
        }
      }
    }

    return new AnalysisResult(lineCount, wordCount, charCount, charFrequency);
  }
  public void saveAnalysisResult(AnalysisResult result, String outputPath) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, StandardCharsets.UTF_8))) {
      writer.write("=== Результат анализа текстового файла ===\n");
      writer.write("Количество строк: " + result.getLineCount() + "\n");
      writer.write("Количество слов: " + result.getWordCount() + "\n");
      writer.write("Количество символов: " + result.getCharCount() + "\n");
      writer.write("\nЧастота символов:\n");
      result.getCharFrequency().entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(entry -> {
          try {
            char c = entry.getKey();
            String printable = (c == '\n') ? "\\n" : String.valueOf(c);
            writer.write("'" + printable + "': " + entry.getValue() + "\n");
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
        });
    }
  }
}