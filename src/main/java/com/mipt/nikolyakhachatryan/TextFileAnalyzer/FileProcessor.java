package com.mipt.nikolyakhachatryan.TextFileAnalyzer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;

public class FileProcessor {
  public List<Path> splitFile(String sourcePath, String outputDir, int partSize) throws IOException {
    if (partSize <= 0) {
      throw new IllegalArgumentException("partSize must be > 0");
    }

    Path source = Paths.get(sourcePath);
    if (!Files.exists(source)) {
      throw new IOException("Source file does not exist: " + source);
    }
    if (!Files.isReadable(source)) {
      throw new IOException("Source file is not readable: " + source);
    }

    Path outputDirectory = Paths.get(outputDir);
    if (!Files.exists(outputDirectory)) {
      Files.createDirectories(outputDirectory);
    }

    String fileName = source.getFileName().toString();
    List<Path> parts = new ArrayList<>();
    long fileSize = Files.size(source);

    try (FileChannel channel = FileChannel.open(source, READ)) {
      long position = 0;
      int partIndex = 1;

      while (position < fileSize) {
        long remaining = fileSize - position;
        int currentPartSize = (int) Math.min(partSize, remaining);

        ByteBuffer buffer = ByteBuffer.allocate(currentPartSize);
        int bytesRead = channel.read(buffer, position);
        if (bytesRead <= 0) break;

        buffer.flip();

        Path partFile = outputDirectory.resolve(fileName + ".part" + partIndex);
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

    Path output = Paths.get(outputPath);
    Path outputDir = output.getParent();
    if (outputDir != null && !Files.exists(outputDir)) {
      Files.createDirectories(outputDir);
    }

    try (FileChannel outChannel = FileChannel.open(output, CREATE, WRITE, TRUNCATE_EXISTING)) {
      for (Path part : partPaths) {
        if (!Files.exists(part)) {
          throw new IOException("Part file does not exist: " + part);
        }
        try (FileChannel inChannel = FileChannel.open(part, READ)) {
          long position = 0;
          long count = Files.size(part);
          while (position < count) {
            long transferred = inChannel.transferTo(position, count - position, outChannel);
            if (transferred <= 0) break;
            position += transferred;
          }
        }
      }
    }
  }
}