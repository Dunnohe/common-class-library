package common.guava;

import com.google.common.io.Closer;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class _9IoCloser {
    /**
     * guava
     * close io
     */
    static String readFirstLineFromFile(String path) throws IOException {
        Closer closer = Closer.create();
        BufferedReader br = null;
        try {
            br = closer.register(new BufferedReader(new FileReader(path)));
            return br.readLine();
        } finally {
            closer.close();
        }
    }

    /**
     * jdk7 try-with-resources
     * close io
     */
    static String readFirstLineFromFile2(String path) throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new FileReader(path))) {
            return br.readLine();
        }
    }

}
