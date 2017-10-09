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
public class ResourceDownLoader extends JFrame {

    private TextArea txtAreaStatus;
    private Label lblStatus, lblPath, lblLocToSave;
    private TextField txtLocToSave, txtPath;
    private Button btnDownload;
    private Font baseFont = new Font("Dialog", Font.PLAIN, 12);

    /**
     * This method initializes the form.
     */
    private void initComponents() {

        setFont(baseFont);

        setTitle("Resource Downloader");
        setBounds(100, 75, 550, 380);
        setVisible(true);

        lblPath = new Label();
        txtPath = new TextField();
        btnDownload = new Button();
        txtAreaStatus = new TextArea();
        lblStatus = new Label();
        lblLocToSave = new Label();
        txtLocToSave = new TextField();

        getContentPane().setLayout(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitForm(evt);
            }
        });

        lblPath.setForeground(new Color(102, 0, 0));
        lblPath.setText("Path");
        getContentPane().add(lblPath);
        lblPath.setBounds(50, 10, 40, 23);

        getContentPane().add(txtPath);
        txtPath.setText("paths-to-download.txt");
        txtPath.setBounds(100, 10, 290, 20);

        btnDownload.setLabel("DownLoad");
        btnDownload.addActionListener(evt -> startDownLoad(txtPath.getText()));
        getContentPane().add(btnDownload);
        btnDownload.setBounds(430, 10, 80, 24);

        getContentPane().add(txtAreaStatus);
        txtAreaStatus.setBounds(100, 80, 280, 170);

        lblStatus.setForeground(new Color(0, 0, 153));
        lblStatus.setName("null");
        lblStatus.setText("Status");
        getContentPane().add(lblStatus);
        lblStatus.setBounds(110, 50, 70, 20);

        lblLocToSave.setForeground(new Color(0, 0, 153));
        lblLocToSave.setText("Location To Save");
        getContentPane().add(lblLocToSave);
        lblLocToSave.setBounds(100, 260, 100, 20);

        getContentPane().add(txtLocToSave);
        txtLocToSave.setBounds(210, 260, 170, 20);

        pack();
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
            //System.out.println("IT is download"+path);
            FileOutputStream fos = new FileOutputStream(path);
            String p1 = txtPath.getText();
            int li = p1.lastIndexOf("/");
            String sitename = p1.substring(0, li);
            System.out.println("opening url: " + sitename + "/" + str);
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
            //list1.add("\n" + str);

        } catch (Exception e) {
        }
    }

    private void extractFileName(String s) {
        StringTokenizer st = new StringTokenizer(s, "/");
        int ct = st.countTokens();
        String s1[] = new String[ct];
        ////System.out.println(ct);
        for (int i = 0; i < ct; i++) {
            s1[i] = st.nextToken();

        }

        String path1 = txtLocToSave.getText();
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
        String path = txtLocToSave.getText();
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
