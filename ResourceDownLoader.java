import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class will help in downloading bunch of urls that
 * may point to resource like images, pdfs etc.
 */
public class ResourceDownLoader extends AppFrame {

    private Label lblSource, lblDest;
    private TextField txtDest, txtSource;
    private Button btnDownload;
    private MyLogger logger;
    private final String DEFAULT = "default (current folder)";

    /**
     * This method initializes the form.
     */
    private void initComponents() {

        logger = MyLogger.createLogger("resource-downloader.log");
        Container container = getContentPane();

        setTitle("Resource Downloader");

        lblSource = new Label("Download from");
        txtSource = new TextField();
        btnDownload = new Button("DownLoad");
        lblDest = new Label("Location To Save");
        txtDest = new TextField(DEFAULT);

        container.setLayout(new FlowLayout());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitForm(evt);
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

    private void setToCenter() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Exit the Application
     */
    private void exitForm(WindowEvent evt) {
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

class AppFrame extends JFrame {

    private Font baseFont = new Font("Dialog", Font.PLAIN, 12);

    public AppFrame() {
        setFont(baseFont);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);
        setForeground(Color.black);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Font getBaseFont() {
        return baseFont;
    }
}