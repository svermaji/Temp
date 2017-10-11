import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will help in downloading bunch of urls that
 * may point to resource like images, pdfs etc.
 */
public class ResourceDownLoader extends AppFrame {

    private Label lblSource, lblDest;
    private TextField txtDest, txtSource;
    private Button btnDownload;

    /**
     * This method initializes the form.
     */
    private void initComponents() {

        Container container = getContentPane();

        setTitle("Resource Downloader");

        lblSource = new Label("Download from");
        txtSource = new TextField();
        btnDownload = new Button("DownLoad");
        lblDest = new Label("Location To Save");
        txtDest = new TextField("Same as source");

        container.setLayout(new FlowLayout());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitForm(evt);
            }
        });

        container.add(lblSource);
        container.add(txtSource);
        txtSource.setText("paths-to-download.txt");

        btnDownload.addActionListener(evt -> startDownLoad(txtSource.getText()));
        container.add(lblDest);
        container.add(txtDest);
        container.add(btnDownload);

        setToCenter();
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
        try {
            URL u = new URL(httpUrl);
            URLConnection uc = u.openConnection();
            InputStream in = uc.getInputStream();
            byte b[] = new byte[500];
            int i = in.read(b);
            FileOutputStream fos = new FileOutputStream(getDestPath(httpUrl));
            while (i != -1) {
                fos.write(b, 0, i);
                i = in.read(b);
            }
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isHttpUrl(String s) {
        return s.startsWith("http");
    }

    private String getDestPath(String httpUrl) {
        return httpUrl.substring(httpUrl.lastIndexOf("/") + 1);
    }

    private void startDownLoad(String srcPath) {

        java.util.List<String> urlsToDownload = new ArrayList<>();
        urlsToDownload.add(srcPath);

        if (!isHttpUrl(srcPath)) {
            urlsToDownload = getUrlsFromFile(srcPath);
        }

        urlsToDownload.forEach(this::downLoad);
    }

    private List<String> getUrlsFromFile(String path) {
        return null;
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
    }

    public Font getBaseFont() {
        return baseFont;
    }
}