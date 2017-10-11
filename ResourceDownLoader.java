import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

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

        setToCenter ();
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


    private void downLoad(String str, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            String p1 = txtSource.getText();
            int li = p1.lastIndexOf("/");
            String sitename = p1.substring(0, li);
            if (!str.startsWith("http")) str = sitename + "/" + str;
            URL u = new URL(str);
            URLConnection uc = u.openConnection();

            InputStream in = uc.getInputStream();
            byte b[] = new byte[500];
            int i = in.read(b);
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

    private void extractFileName(String s) {
        StringTokenizer st = new StringTokenizer(s, "/");
        int ct = st.countTokens();
        String s1[] = new String[ct];
        for (int i = 0; i < ct; i++) {
            s1[i] = st.nextToken();

        }

        String path1 = txtDest.getText();
        for (int i = 0; i < ct - 1; i++) {
            path1 = path1 + "/" + s1[i];

        }
        File f = new File(path1);
        f.mkdirs();

        //System.out.println("path in extralink"+path1);
        downLoad(s, path1 + "/" + s1[ct - 1]);
    }

    private void startDownLoad(String str) {

        //System.out.println("DownLOAD :"+str);
        String path = txtDest.getText();
        //System.out.println(" user specified :"+path);
        String str1 = str.substring(6, str.length());
        StringTokenizer st = new StringTokenizer(str, "/");
        int ct = st.countTokens();
        String ap[] = new String[ct];
        for (int i = 0; i < ct; i++) {
            ap[i] = st.nextToken();

        }
        for (int i = 2; i < ct - 1; i++) {
            path = path + "/" + ap[i];
        }
        //System.out.println(" dir " +path);
        File f = new File(path);
        f.mkdir();
        path = path + "/" + ap[ct - 1];

        //System.out.println("path"+path);
        downLoad(str, path);
        //System.out.println("STRSTr"+str);
    }
}

class AppFrame extends JFrame {

    private Font baseFont = new Font("Dialog", Font.PLAIN, 12);

    public AppFrame () {
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