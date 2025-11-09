package com.mipt.nikolyakhachatryan.TextFileAnalyzer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;

public class FileProcessor {
  public List<Path> splitFile(String sourcePath, String outputDir, int partSize) throws IOException {
    if (partSize <= 0) {
      throw new IllegalArgumentException("partSize must be > 0");
    }

    Path source = Paths.get(sourcePath);
    if (!Files.exists(source)) {
      throw new IOException("Source file not found: " + source);
    }

    Path outputDirPath = Paths.get(outputDir);
    Files.createDirectories(outputDirPath);

    long fileSize = Files.size(source);
    String fileName = source.getFileName().toString();
    List<Path> parts = new ArrayList<>();

    // Обработка пустого файла
    if (fileSize == 0) {
      Path partFile = outputDirPath.resolve(fileName + ".part1");
      Files.createFile(partFile);
      parts.add(partFile);
      return parts;
    }

    try (FileChannel inChannel = FileChannel.open(source, READ)) {
      long position = 0;
      int partIndex = 1;

      while (position < fileSize) {
        long remaining = fileSize - position;
        int currentPartSize = (int) Math.min(partSize, remaining);

        ByteBuffer buffer = ByteBuffer.allocate(currentPartSize);
        inChannel.read(buffer, position);
        buffer.flip();

        Path partFile = outputDirPath.resolve(fileName + ".part" + partIndex);
        try (FileChannel outChannel = FileChannel.open(partFile, CREATE, WRITE, TRUNCATE_EXISTING)) {
          outChannel.write(buffer);
        }

        parts.add(partFile);
        position += currentPartSize;
        partIndex++;
      }
    }

    return parts;
  }

  public void mergeFiles(List<Path> partPaths, String outputPath) throws IOException {
    if (partPaths == null || partPaths.isEmpty()) {
      throw new IllegalArgumentException("partPaths must not be empty");
    }

    List<Path> sortedParts = new ArrayList<>(partPaths);
    sortedParts.sort(Comparator.comparing(p -> p.getFileName().toString()));

    Path output = Paths.get(outputPath);
    Path parent = output.getParent();
    if (parent != null) {
      Files.createDirectories(parent);
    }

    try (FileChannel outChannel = FileChannel.open(output, CREATE, WRITE, TRUNCATE_EXISTING)) {
      for (Path part : sortedParts) {
        if (!Files.exists(part)) {
          throw new IOException("Part file missing: " + part);
        }
        try (FileChannel inChannel = FileChannel.open(part, READ)) {
          long position = 0;
          long size = Files.size(part);
          while (position < size) {
            long transferred = inChannel.transferTo(position, size - position, outChannel);
            if (transferred <= 0) break;
            position += transferred;
          }
        }
      }
    }
  }
}