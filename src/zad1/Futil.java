package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {
    public static void processDir(String dirName, String resultFileName) {
        Path dir = Paths.get(dirName);
        Path res = Paths.get(resultFileName);
        try {
            FileChannel fc = FileChannel.open(res, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            Files.walkFileTree(dir, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    FileChannel opn = FileChannel.open(file, StandardOpenOption.READ);
                    ByteBuffer bb = ByteBuffer.allocate(256);
                    CharsetDecoder decoder = Charset.forName("Cp1250").newDecoder();
                    CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
                    while(opn.read(bb) != -1){
                        bb.flip();
                        CharBuffer cb = decoder.decode(bb);
                        ByteBuffer out = encoder.encode(cb);
                        out.rewind();
                        fc.write(out);
                        bb.rewind();
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
