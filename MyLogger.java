import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;

/**
 * Created by svg on 11-Oct-2017
 */
public class MyLogger {

    private Writer logWriter = null;
    private static MyLogger logger = null;

    public static MyLogger createLogger(String logFilename) {
        if (logger == null) {
            logger = new MyLogger();
            try {
                logger.createLogFile(Utils.hasValue(logFilename) ? logFilename : "test.log");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logger;
    }

    private MyLogger() {
    }

    public void dispose() {
        try {
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

    /**
     * Writes the debug statement in log file.
     * If log file could not be initialized
     * thn output would be redirected to console.
     *
     * @param message - debug statement
     */
    public void log(String message) {
        try {
            if (logWriter != null) {
                synchronized (logWriter) {
                    //logWriter.write ( message + "\r\n");
                    logWriter.write(getTime() + message + System.lineSeparator());
                    logWriter.flush();
                }
            } else {
                System.out.println(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createLogFile(String logFile) throws IOException {
        String logFileName = "test.log";
        if (Utils.hasValue(logFile)) {
            logFileName = logFile;
        }

        if (logWriter == null) {
            try {
                logWriter = new BufferedWriter(new FileWriter(logFileName));
            } catch (IOException e) {
                logWriter = null;
                throw new IOException(e.getMessage());
            } catch (Exception e) {
                logWriter = null;
            }
        }
    }

    private String getTime() {
        return "["+LocalDateTime.now()+"]: ";
    }
}
