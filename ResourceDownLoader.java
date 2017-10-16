import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class will help in downloading bunch of urls that
 * may point to resource like images, pdfs etc.
 */
public class ResourceDownLoader extends AppFrame {

    private TextField txtDest, txtSource;
    private Button btnDownload, btnExit;

    private MyLogger logger;
    private final String DEFAULT = "default (current folder)";
    private TrustManager[] trustAllCerts;
    private String title = "Resource Downloader";
    private DownloadStatus downloadStatus;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(1);

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

        setTitle(title);

        Label lblSource = new Label("Download from");
        txtSource = new TextField();
        btnDownload = new Button("DownLoad");
        btnExit = new Button("Exit");
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

        container.add(lblDest);
        container.add(txtDest);
        btnDownload.addActionListener(evt -> startDownLoad(txtSource.getText()));
        container.add(btnDownload);
        btnExit.addActionListener(evt -> exitForm());
        container.add(btnExit);

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
        logger.log("Goodbye");
        setVisible(false);
        dispose();
        System.exit(0);
    }

    public void updateTitle (String addlInfo) {
        setTitle(Utils.hasValue(addlInfo) ?  title + " - " + addlInfo : title);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ResourceDownLoader().initComponents();
    }

    private void downLoad(String url) {
        logger.log("Trying url [" + url + "]");
        long startTime = System.currentTimeMillis();
        try {
            int KB = 1024;
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            FileInfo fileInfo = new FileInfo(url, getDestPath(url), uc.getContentLength());
            logger.log("Url resource size is [" + fileInfo.getSize() + "] Bytes i.e. [" + (fileInfo.getSize() / KB) + "] KB");

            ReadableByteChannel rbc = Channels.newChannel(u.openStream());
            FileOutputStream fos = new FileOutputStream(fileInfo.getDest());
            startUpdatingDownloadInfo (fileInfo);
            fos.getChannel().transferFrom(rbc, 0, fileInfo.getSize());

            long diffTime = (System.currentTimeMillis() - startTime);
            long diffTimeInSec = diffTime / 1000;

            fos.flush();
            fos.close();
            rbc.close();
            stopUpdatingDownloadInfo ();
            logger.log("Download complete in ["
                + diffTimeInSec + "] seconds with speed [" + (fileInfo.getSize() / diffTime) + "] KB/s");
        } catch (Exception e) {
            logger.log(e.getMessage());
            e.printStackTrace();
        }
        logger.log("File downloaded");
    }

    private void startUpdatingDownloadInfo(FileInfo fileInfo) {
        downloadStatus = new DownloadStatus (this, fileInfo);
        threadPool.submit(downloadStatus);
    }

    private void stopUpdatingDownloadInfo() {
        updateTitle("");
    }

    private boolean isHttpUrl(String s) {
        return s.startsWith("http");
    }

    private String getDestPath(String url) {
        String destFolder = txtDest.getText();
        if (!Utils.hasValue(destFolder) || destFolder.contentEquals(new StringBuffer(DEFAULT))) {
            destFolder = ".";
        }
        String path = destFolder + url.substring(url.lastIndexOf("/"));
        logger.log("Destination path is [" + path + "]");
        return path;
    }

    private void startDownLoad(String srcPath) {

        handleControls (false);
        java.util.List<String> urlsToDownload = new ArrayList<>();
        urlsToDownload.add(srcPath);

        if (!isHttpUrl(srcPath)) {
            urlsToDownload = getUrlsFromFile(srcPath);
        }
        logger.log("Downloading urls are " + urlsToDownload);

        urlsToDownload.forEach(this::downLoad);
        handleControls (true);
    }

    private void handleControls(boolean enable) {
        txtSource.setEnabled(enable);
        txtDest.setEnabled(enable);
        btnDownload.setEnabled(enable);
    }

    private java.util.List<String> getUrlsFromFile(String filePath) {
        logger.log("Current directory is: " + System.getProperty("user.dir"));
        Path path = Utils.createPath(filePath);
        logger.log("Path created as "+path.toString());
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            logger.log(e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    static class DownloadStatus implements Callable {

        private FileInfo fileInfo;
        private ResourceDownLoader resourceDownLoader;

        DownloadStatus(ResourceDownLoader resourceDownLoader, FileInfo fileInfo) {
            this.fileInfo = fileInfo;
            this.resourceDownLoader = resourceDownLoader;
        }

        @Override
        public Object call() throws Exception {
            long size = Files.size(Utils.createPath(fileInfo.getDest()));
            int percent = (int) ((size*100)/fileInfo.getSize());
            resourceDownLoader.updateTitle(percent+"%");
            Thread.sleep(50);
            return percent;
        }
    }

    static class FileInfo {
        private String src, dest;
        private long size;

        FileInfo(String src, String dest, int size) {
            setSrc(src);
            setDest(dest);
            setSize(size);
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getDest() {
            return dest;
        }

        public void setDest(String dest) {
            this.dest = dest;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }
}
