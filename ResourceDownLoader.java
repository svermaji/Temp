import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will help in downloading bunch of urls that
 * may point to resource like images, pdfs etc.
 */
public class ResourceDownLoader extends AppFrame {

    private TextField txtDest, txtSource;
    private MyLogger logger;
    private final String DEFAULT = "default (current folder)";
    private TrustManager[] trustAllCerts;

    private void createTrustManager() {
        trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };
    }

    /**
     * This method initializes the form.
     */
    private void initComponents() {

        createTrustManager();
        trustAllHttps ();
        logger = MyLogger.createLogger("resource-downloader.log");
        Container container = getContentPane();

        setTitle("Resource Downloader");

        Label lblSource = new Label("Download from");
        txtSource = new TextField();
        Button btnDownload = new Button("DownLoad");
        Label lblDest = new Label("Location To Save");
        txtDest = new TextField(DEFAULT);

        container.setLayout(new FlowLayout());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitForm();
            }
        });

        container.add(lblSource);
        container.add(txtSource);
//        txtSource.setText("http://slideplayer.com/slide/4380452/14/images/13/The+finalize()+Method.jpg");
        txtSource.setText("paths-to-download.txt");

        btnDownload.addActionListener(evt -> startDownLoad(txtSource.getText()));
        container.add(lblDest);
        container.add(txtDest);
        container.add(btnDownload);

        setToCenter();
        logger.log("Program initialized");
    }

    private void trustAllHttps() {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            logger.log(e.getMessage());
            e.printStackTrace();
        }
    }

    private void setToCenter() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Exit the Application
     */
    private void exitForm() {
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ResourceDownLoader().initComponents();
    }

    private void downLoad(String httpUrl) {
        logger.log("Trying url [" + httpUrl + "]");
        long startTime = System.currentTimeMillis();
        try {
            int KB = 1024;
            URL u = new URL(httpUrl);
            HttpURLConnection uc = (HttpURLConnection) u.openConnection();
            int fileSize = uc.getContentLength();
            logger.log("Url resource size is [" + fileSize + "] Bytes i.e. [" + (fileSize / KB) + "] KB");
            /*InputStream in = uc.getInputStream();
            int chunkSize = 1024;
            byte b[] = new byte[chunkSize];
            int i = in.read(b);
            FileOutputStream fos = new FileOutputStream(getDestPath(httpUrl));
            int chunks = 1;
            while (i != -1) {
                fos.write(b, 0, i);
                i = in.read(b);
                chunks++;
            }*/
            ReadableByteChannel rbc = Channels.newChannel(u.openStream());
            FileOutputStream fos = new FileOutputStream(getDestPath(httpUrl));
            fos.getChannel().transferFrom(rbc, 0, fileSize);
            long diffTime = (System.currentTimeMillis() - startTime);
            long diffTimeInSec = diffTime / 1000;

            fos.flush();
            fos.close();
//            logger.log("Download complete. Total chunks of size [" + chunkSize + "] downloaded are [" + chunks + "] in ["
            logger.log("Download complete in ["
                + diffTimeInSec + "] seconds with speed [" + (fileSize / diffTime) + "] KB/s");
        } catch (Exception e) {
            logger.log(e.getMessage());
            e.printStackTrace();
        }
        logger.log("File downloaded");
    }

    private boolean isHttpUrl(String s) {
        return s.startsWith("http");
    }

    private String getDestPath(String httpUrl) {
        String destFolder = txtDest.getText();
        if (!Utils.hasValue(destFolder) || destFolder.contentEquals(new StringBuffer(DEFAULT))) {
            destFolder = ".";
        }
        String path = destFolder + httpUrl.substring(httpUrl.lastIndexOf("/"));
        logger.log("Destination path is [" + path + "]");
        return path;
    }

    private void startDownLoad(String srcPath) {

        java.util.List<String> urlsToDownload = new ArrayList<>();
        urlsToDownload.add(srcPath);

        if (!isHttpUrl(srcPath)) {
            urlsToDownload = getUrlsFromFile(srcPath);
        }
        logger.log("Downloading urls are " + urlsToDownload);

        urlsToDownload.forEach(this::downLoad);
    }

    private List<String> getUrlsFromFile(String filePath) {
        logger.log("Current directory is: " + System.getProperty("user.dir"));
        Path path = FileSystems.getDefault().getPath(filePath);
        logger.log(path.toString());
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            logger.log(e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

