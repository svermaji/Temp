import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by 44085037 on 19-Jan-18
 */
public class TestLargeHM {

    public static void main(String[] args) {
//        testHashMap ();
//        testStringBytes ();
        writeBigFile();
    }

    private static void writeBigFile() {
        System.out.println("--------writeBigFile-----------");
        long nanoTime = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        String fn = "big-file.txt";
        boolean approach1 = false;
        System.out.println("Approach = " + (approach1 ? "approach-1":"approach-2"));
        int numLines = 20_000_000;
        try {
            if (approach1) {
                //Approach 1 -- for 2 crore lines takes 4.5 seconds with 180 mb file size
                approach1(fn, numLines);
            } else {
                //Approach 2 -- for 2 crore lines takes nearly 2 to 2.5 seconds with 180 mb file size
                approach2(fn, numLines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Completed file writing in sec = " + TimeUnit.SECONDS.convert((System.nanoTime() - nanoTime), TimeUnit.NANOSECONDS));
    }

    private static void approach2(String fn, int numLines) throws IOException {
        StringBuilder sb = new StringBuilder();
        FileChannel rwChannel = new RandomAccessFile(fn, "rw").getChannel();
        ByteBuffer wrBuf = null;

        int pos = 0;
        for (int i = 1; i <= numLines; i++) {
            sb.append(i).append(System.lineSeparator());
            if (i % 100000 == 0) {
                wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, pos, sb.length());
                pos += sb.length();
                wrBuf.put(sb.toString().getBytes());
                sb = new StringBuilder();
            }
        }
        if (sb.length() > 0) {
            wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, pos, sb.length());
            wrBuf.put(sb.toString().getBytes());
        }
        rwChannel.close();
    }

    private static void approach1(String fn, int numLines) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= numLines; i++) {
            sb.append(i).append(System.lineSeparator());
        }
        FileWriter fileWriter = new FileWriter(fn);
        fileWriter.write(sb.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    private static void testStringBytes() {
        System.out.println("--------testStringBytes-----------");
        String s = "abcd";
        try {
            System.out.println(s + ", length=" + s.length() + " and bytes in utf 16 = " + s.getBytes("UTF-16").length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("hashCodes......");
        System.out.println(s.hashCode());
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaa".hashCode());
        System.out.println("aaaaaaaaaaabbaaaaaaaaaa".hashCode());//same
        System.out.println("aaaaaaaaaaabbaaaaaaaaaa".hashCode());//same
        System.out.println("aaaaaaaaaaabbbaaaaaaaaa".hashCode());
        System.out.println("aaaaaaaaaaabbaaaccaaaaa".hashCode());
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaa".hashCode());
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".hashCode());
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaxxxxxxxxxxxxxxxxxxxxxxdfghdghxxxxxxxxxxxxxxxxxxxxx".hashCode());
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaxxxxxxxxxxxxdfghdfghxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".hashCode());
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaxxxxxxxxxxxxxxxxxxxxxxxxxxggggggggggggxxxxxxxxxxxx".hashCode());
    }

    private static void testHashMap() {
        System.out.println("--------testHashMap-----------");
        int MAX_KEYS = 10_000_000; // 1 crore elements
        int KB = 1024;
        int CALC = KB * KB;
        int TRACK = 500000;

        long milis = System.nanoTime();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < MAX_KEYS; i++) {
            map.put((i + "").hashCode(), i);
            if (i % TRACK == 0) {
                System.out.println(TRACK + " entered, total = " + map.size() + " and memory = "
                        + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / CALC) + " MB");
                //System.gc();
            }
        }
        System.out.println(MAX_KEYS + " elements, time to fill in sec = " + TimeUnit.SECONDS.convert((System.nanoTime() - milis), TimeUnit.NANOSECONDS));

        print((500 + "").hashCode(), map);
        print((5000 + "").hashCode(), map);
        print((50000 + "").hashCode(), map);
        print((500000 + "").hashCode(), map);
        print((9999999 + "").hashCode(), map);
    }

    private static void print(int i, Map<Integer, Integer> map) {
        long milis = System.nanoTime();
        System.out.println(i + "=hashcode and element = " + map.get(i) + ", Time in ms = " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - milis), TimeUnit.NANOSECONDS));
    }
}
