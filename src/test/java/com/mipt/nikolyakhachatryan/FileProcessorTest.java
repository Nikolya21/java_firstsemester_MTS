package com.mipt.nikolyakhachatryan;

import com.mipt.nikolyakhachatryan.TextFileAnalyzer.FileProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorTest {

  @TempDir
  Path tempDir;

  @Test
  void testSplitAndMergeFile() throws IOException {
    FileProcessor processor = new FileProcessor();

    Path testFile = tempDir.resolve("test.dat");
    byte[] testData = new byte[1500];
    new Random(42).nextBytes(testData);
    Files.write(testFile, testData);

    Path partsDir = tempDir.resolve("parts");
    List<Path> parts = processor.splitFile(testFile.toString(), partsDir.toString(), 500);

    assertEquals(3, parts.size());
    for (int i = 0; i < 3; i++) {
      assertTrue(Files.exists(parts.get(i)));
      assertEquals(500, Files.size(parts.get(i)));
    }

    Path mergedFile = tempDir.resolve("merged.dat");
    processor.mergeFiles(parts, mergedFile.toString());

    assertArrayEquals(testData, Files.readAllBytes(mergedFile));
    assertEquals(1500, Files.size(mergedFile));

    byte[] part1 = Files.readAllBytes(parts.get(0));
    byte[] part2 = Files.readAllBytes(parts.get(1));
    byte[] part3 = Files.readAllBytes(parts.get(2));

    assertArrayEquals(java.util.Arrays.copyOfRange(testData, 0, 500), part1);
    assertArrayEquals(java.util.Arrays.copyOfRange(testData, 500, 1000), part2);
    assertArrayEquals(java.util.Arrays.copyOfRange(testData, 1000, 1500), part3);
  }

  @Test
  void testSplitFile_EmptyFile() throws IOException {
    FileProcessor processor = new FileProcessor();

    Path emptyFile = tempDir.resolve("empty.txt");
    Files.createFile(emptyFile);

    Path partsDir = tempDir.resolve("empty_parts");
    List<Path> parts = processor.splitFile(emptyFile.toString(), partsDir.toString(), 1024);

    assertEquals(1, parts.size());
    assertEquals(0, Files.size(parts.get(0)));
  }
}