import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Aug 28, 2006
 * Time: 3:30:57 PM
 * <p>
 * Change file names performs different operations
 * on file names by renaming them.
 * <p>
 * View usage for help.
 */
public class ChangeFileNames extends AppFrame {

    private static final String MAIN = "main";
    private MyLogger logger = null;

    private JLabel lblFolder, lblAction, lblParam1, lblParam2, lblExt;
    private JTextField txtFolder, txtParam1, txtParam2, txtExt;
    private JCheckBox jcSubFolder, jcProcessFolders, jcOverwrite, jcAppendFolder, jcUpdateID3v2Tag;
    private JCheckBox jcModifyTitle;
    private JComboBox<Choices> jcb;
    private JTextArea taStatus;
    private MyButton btnChange, btnBrowse, btnClear, btnUsage, btnExit;
    private JPanel jpSouth;
    private JPanel jpNorth;

    private static int totalProcessedFiles = 0;
    private static int successfullyProcessedFiles = 0;
    private static int unprocessedFiles = 0;

    private String title = "Change File Names";

    private ChangeFileNames() throws Exception {
        logger = MyLogger.createLogger("ChangeFileNames.log");
        setTitle(title);
        initComponents();
    }

    private void initComponents() throws Exception {

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitApp();
            }
        });

        lblFolder = new JLabel("Folder");
        lblAction = new JLabel("Action");
        lblParam1 = new JLabel("Param 1");
        lblParam2 = new JLabel("Param 2");
        lblExt = new JLabel("Process files with ext.");

        txtFolder = new JTextField("E:\\Songs\\2017", 20);
        txtParam1 = new JTextField(Utils.EMPTY, 5);
        txtParam2 = new JTextField(Utils.EMPTY, 5);
        txtExt = new JTextField("mp3", 5);

        Choices[] allChoices = Choices.values();
        Arrays.sort(allChoices, new ChoicesComparator());
        jcb = new JComboBox<>(allChoices); //drop down Options
        jcb.setSelectedIndex(6);
        jcSubFolder = new JCheckBox("Process sub-folders", false);
        jcSubFolder.setToolTipText("Process sub-folders if selected else only files of the folder");
        jcProcessFolders = new JCheckBox("Process folder names also", false);
        jcProcessFolders.setToolTipText("Will process folder names also and convert them to title case if selected (only for CONVERT_TO_TITLE_CASE Action)");
        jcOverwrite = new JCheckBox("Overwrite MP3 tag info", true);
        jcOverwrite.setToolTipText("Overwrite MP3 tag info (overwrites Album and title info; only for UPDATE_MP3_TAGS Action)");
        jcAppendFolder = new JCheckBox("Append folder name", false);
        jcAppendFolder.setToolTipText("Append folder's name before the file name (only for CONVERT_TO_TITLE_CASE Action)");
        jcUpdateID3v2Tag = new JCheckBox("Update ID3v2Tag also", true);
        jcUpdateID3v2Tag.setToolTipText("Append folder's name before the file name (only for CONVERT_TO_TITLE_CASE Action)");
        jcModifyTitle = new JCheckBox("Modify Title Tag", false);
        jcModifyTitle.setToolTipText("Modify MP3 Title tag (only for UPDATE_MP3_TAGS Action)");
        taStatus = new JTextArea(Utils.EMPTY);
        taStatus.setAutoscrolls(true);
        btnChange = new MyButton("Change");
        btnBrowse = new MyButton("Browse");
        btnClear = new MyButton("Clear Status");
        btnUsage = new MyButton("Usage");
        btnExit = new MyButton("Exit");

        drawUI();

        printUsage();
        setSize(new Dimension(1200, 500));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void exitApp() {
        logger.log("Goodbye");
        System.exit(0);
    }

    private void drawUI() {
        getContentPane().setLayout(new BorderLayout());

        JPanel jpUp = new JPanel();
        JPanel jpDown = new JPanel();
        JPanel jpMid = new JPanel();
        jpSouth = new JPanel();
        jpNorth = new JPanel();
        jpNorth.setLayout(new GridLayout(3, 1));
        jpUp.add(lblFolder);
        jpUp.add(txtFolder);
        jpUp.add(btnBrowse);
        jpUp.add(lblExt);
        jpUp.add(txtExt);
        jpUp.add(lblAction);
        jpUp.add(jcb);
        jpDown.add(lblParam1);
        jpDown.add(txtParam1);
        jpDown.add(lblParam2);
        jpDown.add(txtParam2);
        jpDown.add(btnChange);
        jpDown.add(btnExit);
        jpMid.add(jcSubFolder);
        jpMid.add(jcProcessFolders);
        jpMid.add(jcOverwrite);
        jpMid.add(jcAppendFolder);
        jpMid.add(jcUpdateID3v2Tag);
        jpMid.add(jcModifyTitle);
        jpSouth.add(btnClear);
        jpSouth.add(btnUsage);

        jpNorth.add(jpUp);
        jpNorth.add(jpMid);
        jpNorth.add(jpDown);

        add(jpNorth, BorderLayout.NORTH);
        add(jpSouth, BorderLayout.SOUTH);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.WEST);
        add(new JScrollPane(taStatus), BorderLayout.CENTER);
    }

    /**
     * Operates on file names after accessing
     * and tries to ignore errors upto maximum
     * extends.
     *
     * @param args parameters for processing filenames
     */
    private void process(Arguments args) {
        final String log = "process: ";

        printMsg(log + "Starting process.");

        if (args.getFileType().equals("ALL")) {
            args.setFileType(null);
        }

        ChangeFileNamesFilter filter = new ChangeFileNamesFilter(args.getFileType());
        File srcDir = new File(args.getSourceDir());
        File[] fileList = srcDir.listFiles(filter);
        int len = (fileList != null) ? fileList.length : 0;
        printMsg(log + "Files obtained after applying filter are [" + len + "]");

        if (len != 0) {
            try {
                BaseProcessor processor =
                    (BaseProcessor) Class.forName(args.getChoice().getClazz())
                        .getConstructor(logger.getClass())
                        .newInstance(logger);

                int cnt = 1;
                for (File file : fileList) {
                    updateTitle(((cnt * 100) / len) + "%");
                    cnt++;
                    printMsg("is directory [" + file.isDirectory() + "], is file [" + file.isFile() + "]");
                    if (args.isProcessSubFolder() && file.isDirectory()) {
                        String[] tempArgs = null;
                        try {
                            printMsg("Collecting parameters for directory [" + file.getCanonicalPath() + "]");
                            // TODO: need to revisit this nesting functionality
                            //tempArgs = new String[args.length];
                            System.arraycopy(args, 0, tempArgs, 0, tempArgs.length);
                            tempArgs[0] = file.getCanonicalPath();
                        } catch (Exception e) {
                            printMsg(log + "Error: " + e.getMessage());
                            e.printStackTrace();
                        }

                        printMsg("Calling nested process.");
                        //TODO: analyze
                        //process(tempArgs);
                        if (jcProcessFolders.isSelected() && args.getChoice().equals(Choices.CONVERT_TO_TITLE_CASE)) {
                            String folderName = file.getName();
                            args.setFile(file);

                            String returnVal = processor.execute(args);
                            printMsg(log + "modified file name obtained as [" + returnVal + "]");

                            if (Utils.hasValue(returnVal) && !returnVal.equals(folderName)) {
                                boolean status = file.renameTo(new File(file.getParent() + "\\" + returnVal));
                                printMsg(log + "The operation status for renaming [" + folderName + "] to [" + returnVal + "] " +
                                    "for file.getParent() [" + file.getParent() + "] is [" + status + "]");
                            } else {
                                printMsg(log + "folder [" + folderName + "] does not require conversion.");
                            }
                        }
                    } else {

                        if (!file.isFile()) {
                            continue;
                        }

                        args.setFile(file);

                        String actualFileName = file.getName();
                        //excluding extension
                        String extension = actualFileName.substring(actualFileName.lastIndexOf(".") + 1);
                        printMsg(log + "extension = " + extension);
                        actualFileName = actualFileName.substring(0, actualFileName.lastIndexOf("."));
                        printMsg(log + "actual file name is [" + actualFileName + "]");

//                        Object[] arguments = new Object[]{actualFileName, EXTRA_PARAM1, EXTRA_PARAM2, file};
                        //String returnVal = (String) method.invoke(this, arguments);
                        String returnVal = processor.execute(args);
                        printMsg(log + "modified file name obtained as [" + returnVal + "]");

                        if (Utils.hasValue(returnVal) && !returnVal.equals(actualFileName)) {
                            boolean status = file.renameTo(new File(file.getParent() + "\\" + returnVal + "." + extension));
                            printMsg(log + "The operation status for renaming [" + actualFileName + "] to [" + returnVal + "] " +
                                "for file.getParent() [" + file.getParent() + "] is [" + status + "]");
                        } else {
                            printMsg(log + "file [" + actualFileName + "] does not require conversion.");
                        }

                        totalProcessedFiles++;
                        successfullyProcessedFiles++;
                    }
                }
                updateTitle("Done");

            } catch (Exception e) {
                throwExcp(log + "Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            printMsg("No file to process.");
        }

        printMsg("Processing COMPLETE. Processing summary.total processed files [" + totalProcessedFiles +
            "], total processed successfully files [" + successfullyProcessedFiles +
            "], total processed failed files [" + unprocessedFiles + "]");

        resetProcessedFileCounters();
    }

    private void resetProcessedFileCounters() {
        totalProcessedFiles = 0;
        successfullyProcessedFiles = 0;
        unprocessedFiles = 0;
    }

    /**
     * Prints message on console and/or text area
     *
     * @param s message to print
     */
    private void printMsg(String s) {
        taStatus.append(s + System.lineSeparator());
        s = "[" + new Date() + "]" + s;
        logger.log(s);
    }

    private String getFolderName(File file) throws Exception {
        return getFolderName(file, true);
    }

    private String getFolderName(File file, boolean modifyLongName) throws Exception {
        String log = "getFolderName: ";
        String folderName = Utils.EMPTY;
        if (file.isFile()) {
            folderName = file.getParent();
            if (folderName.contains("\\")) {
                folderName = folderName.substring(folderName.lastIndexOf("\\") + 1);
            } else if (folderName.contains("/")) {
                folderName = folderName.substring(folderName.lastIndexOf("/") + 1);
            }

            // excluding chars other than Aa-Zz
            String temp = Utils.EMPTY;
            for (int i = 0; i < folderName.length(); i++) {
                boolean include = Character.isLetter(folderName.charAt(i)) || folderName.charAt(i) == Utils.SPACE.charAt(0);
                temp += include ? folderName.charAt(i) + Utils.EMPTY : Utils.EMPTY;
            }
            if (Utils.hasValue(temp)) {
                folderName = temp;
            }

            // if length > 17 chars then take first char of spacing folder name
            if (modifyLongName && folderName.length() > 17) {
                temp = Utils.EMPTY + ((Character.isLetter(folderName.charAt(0))) ? folderName.charAt(0) + Utils.EMPTY : Utils.EMPTY);
                for (int i = 0; i < folderName.length(); i++) {
                    if (folderName.charAt(i) == ' ') {
                        temp += ((Character.isLetter(folderName.charAt(i + 1))) ? folderName.charAt(i + 1) + Utils.EMPTY : Utils.EMPTY);
                    }
                }
                if (Utils.hasValue(temp)) {
                    folderName = temp;
                }
            }
        }
        folderName = folderName.trim();
        printMsg(log + "Returning folder name as [" + folderName + "]");
        return folderName;
    }

    private String getModified(String str, int limit) {
        String log = "getModified: ";
        printMsg(log + "Processing with parameter str [" + str + "] and limit [" + limit + "].");
        while (str.length() > limit) {
            str = str.substring(0, limit);
            if (str.contains(Utils.SPACE)) {
                str = str.substring(0, str.lastIndexOf(Utils.SPACE));
            }
        }
        printMsg(log + "Finished with parameter str as [" + str + "] and limit [" + limit + "].");
        return str;
    }

    /**
     * Validate all the arguments.
     *
     * @param args arguments
     * @return boolean as status
     * @throws Exception on error
     */
    private boolean validateAllArgs(Arguments args) throws Exception {
        return (validateArgs(args) && validateDir(args.getSourceDir()) && validateChoice(args.getChoice()));
    }

    /**
     * checks whether used the available choice of used
     * some thing else.
     *
     * @param choice the option
     * @return boolean status of operation
     */
    private boolean validateChoice(Choices choice) {
        return choice != null;
    }

    /**
     * Validate the arguments against not null condition.
     *
     * @param args parameters for processing filenames
     * @return boolean status of operation
     */
    private boolean validateArgs(Arguments args) {
        final String log = "validateArgs: ";

        if (!Utils.hasValue(args.getSourceDir())) {
            return false;
        }
        if (args.getChoice() == null) {
            return false;
        }
        if (!Utils.hasValue(args.getFileType())) {
            return false;
        }

        printMsg(log + "Successfully validate arguments");
        return true;
    }

    /**
     * checks if passed dir name exists and accessible
     *
     * @param dir directory name
     * @return boolean status of operation
     * @throws Exception on errors
     */
    private boolean validateDir(String dir) throws Exception {
        final String log = "validateDir: ";
        printMsg(log + "Parameter obtained as dir [" + dir + "]");
        File srcDir = new File(dir);
        if (!(srcDir.exists() && srcDir.isDirectory())) {
            throwExcp(log + "Either path for directory [" + dir + "] does not exists or it is not a directory.");
            return false;
        }
        printMsg(log + "Validating directory successful.");
        return true;
    }

    /**
     * print the usage of class
     */
    private void printUsage() {
        String usage = "javac ChangeFileNames <srcDir> <include-sub-folders> <file-filter-extension> <option> [<extra-param>]"
            + "\nwhere"
            + "\n <srcDir> - source directory whose filenames are to be processed."
            + "\n <include-sub-folders> - TRUE to include and FALSE to exclude."
            + "\n <file-filter-extension> - the specific files (like .html or .class etc) to be processed. \"ALL\" for all files."
            + "\n <option> - the operation to be performed on files, also <extra-param> is optional and required by some of the operations of <option>. The valid options are:"
            + "\n     1.  REMOVE_NUMBERS_FROM_FILE_NAMES"
            + "\n     2.  REMOVE_NUMBERS_FROM_START"
            + "\n     3.  REMOVE_NUMBERS_FROM_END"
            + "\n     4.  APPEND_STRING_IN_START <string>"
            + "\n     5.  APPEND_STRING_IN_END <string>"
            + "\n     6.  REMOVE_CHARS_FROM_START <number-of-chars>"
            + "\n     7.  REMOVE_CHARS_FROM_END <number-of-chars>"
            + "\n     8.  REMOVE_SPACES_FROM_START"
            + "\n     9.  REMOVE_SPACES_FROM_END"
            + "\n     10. REMOVE_SPACES_FROM_BOTH_SIDES"
            + "\n     11. REMOVE_MATCH_FROM_START <string>"
            + "\n     12. REMOVE_MATCH_FROM_END <string>"
            + "\n     13. REMOVE_MATCH <string>"
            + "\n     14. REPLACE_MATCH_FROM_START <search-string> <replacement-string>"
            + "\n     15. REPLACE_MATCH_FROM_END <search-string> <replacement-string>"
            + "\n     16. REPLACE_MATCH <search-string> <replacement-string>"
            + "\n     17. CONVERT_TO_TITLE_CASE"
            + "\n     18. UPDATE_MP3_TAGS"
            + "\n\n example 1. javac ChangeFileNames c:/test FALSE mp3 REMOVE_NUMBERS_FROM_START"
            + "\n example 2. javac ChangeFileNames c:/test FALSE ALL REMOVE_NUMBERS_FROM_FILE_NAMES"
            + "\n example 3. javac ChangeFileNames c:/test FALSE java APPEND_STRING_IN_START prefix"
            + "\n example 4. javac ChangeFileNames c:/test FALSE class REMOVE_CHARS_FROM_END 4"
            + "\n example 5. javac ChangeFileNames c:/test FALSE class REMOVE_SPACES_FROM_END"
            + "\n example 6. javac ChangeFileNames c:/test FALSE class REPLACE_MATCH abc xyz";

        taStatus.setText("Usage = " + usage);
    }

    /**
     * print the usage and then throws the exception with the
     * given message.
     *
     * @param msg string for exception message
     */
    private void throwExcp(String msg) {
        printMsg(msg);
    }

    /**
     * Filter chile class
     */
    private class ChangeFileNamesFilter implements FileFilter {
        String filter;

        /**
         * Constructor to initialize class parameters
         *
         * @param filter value of extension
         */
        ChangeFileNamesFilter(String filter) {
            this.filter = filter;
        }

        /**
         * over rider accept method
         *
         * @param file object of type File
         * @return boolean status of operation
         */
        public boolean accept(File file) {
            boolean status = false;
            final String log = "accept: ";
            if (!Utils.hasValue(filter)) {
                status = true;
            }
            if ((!status && file.getName().endsWith(filter)) || file.isDirectory()) {
                status = true;
            }

            printMsg(log + "Applying filter [" + filter + "] for file [" + file.getName() + "] and status result is [" + status + "]");
            return status;
        }
    }

    /**
     * entry unit point
     *
     * @param args parameters for processing filenames
     * @throws Exception thrown on errors.
     */
    public static void main(String[] args) throws Exception {
        ChangeFileNames c = new ChangeFileNames();
        //c.startProcessing(args);
    }

    private Arguments prepareArguments() {
        Arguments arguments = new Arguments();

        arguments.setSourceDir(txtFolder.getText());
        arguments.setFileType(txtExt.getText());
        arguments.setChoice(((Choices) (jcb.getSelectedItem())));
        arguments.setParam1(Utils.hasValue(txtParam1.getText()) ? txtParam1.getText() : Utils.SPACE);
        arguments.setParam2(Utils.hasValue(txtParam2.getText()) ? txtParam2.getText() : Utils.SPACE);

        arguments.setAppendFolder(jcAppendFolder.isSelected());
        arguments.setOverwrite(jcOverwrite.isSelected());
        arguments.setProcessFolders(jcProcessFolders.isSelected());
        arguments.setProcessSubFolder(jcSubFolder.isSelected());
        arguments.setUpdateID3v2Tag(jcUpdateID3v2Tag.isSelected());

        printMsg("prepareArguments: " + arguments.toString());
        return arguments;
    }

    /**
     * This method provides support to execute it from command line.
     * As of thought removing this support from command line on 16-Oct-2017
     *
     * @param args of type {@link Arguments}
     * @throws Exception
     */
    private void startProcessing(Arguments args) throws Exception {
        if (args != null && validateAllArgs(args)) {
            process(args);
        } else {
            printMsg("Unable to start process.");
        }
    }

    private class MyButton extends JButton implements ActionListener {
        MyButton(String text) {
            super(text);
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            String log = "actionPerformed: ";
            try {
                if (e.getSource() == btnBrowse) {
                    JFileChooser jfc = new JFileChooser(txtFolder.getText(), FileSystemView.getFileSystemView());
                    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    jfc.showDialog(this, "Select");
                    try {
                        if (jfc.getSelectedFile() != null) {
                            txtFolder.setText(jfc.getSelectedFile().getCanonicalPath());
                        }
                    } catch (Exception e1) {
                        throwExcp(log + "Error: " + e1.getMessage());
                        e1.printStackTrace();
                    }
                } else if (e.getSource() == btnExit) {
                    exitApp();
                } else if (e.getSource() == btnChange) {
                    taStatus.setText(Utils.EMPTY);
                    handleControls(false);
                    startProcessing(prepareArguments());
                    handleControls(true);
                } else if (e.getSource() == btnUsage) {
                    printUsage();
                } else if (e.getSource() == btnClear) {
                    taStatus.setText(Utils.EMPTY);
                }
            } catch (Exception e1) {
                throwExcp(log + "Error: " + e1.getMessage());
                e1.printStackTrace();
            }
        }
    }

    private void handleControls(boolean enable) {
        if (!enable) {
            Icon icon = new ImageIcon("loading.gif");
            lblParam1.setIcon(icon);
            lblParam1.repaint();
        } else {
            lblParam1.setIcon(null);
            lblParam1.repaint();
        }
        jpNorth.setEnabled(enable);
        jpSouth.setEnabled(enable);
    }

    public void updateTitle(String addlInfo) {
        setTitle(Utils.hasValue(addlInfo) ? title + Utils.SP_DASH_SP + addlInfo : title);
    }
}

enum Choices {
    REMOVE_NUMBERS_FROM_FILE_NAMES("Remove numbers from file names", "removeNumbersFromFileNames", "RemoveNumbersFromFileNames"),
    REMOVE_NUMBERS_FROM_START("Remove numbers from start", "removeNumbersFromStart", "RemoveNumbersFromStart"),
    REMOVE_NUMBERS_FROM_END("Remove numbers from end", "removeNumbersFromEnd", "RemoveNumbersFromEnd"),
    APPEND_STRING_IN_START("Append string in start", "appendStringInStart", "AppendStringInStart"),
    APPEND_STRING_IN_END("Append string in end", "appendStringInEnd", "AppendStringInEnd"),
    REMOVE_CHARS_FROM_START("Remove chars from start", "removeCharsFromStart", "RemoveCharsFromStart"),
    REMOVE_CHARS_FROM_END("Remove chars from end", "removeCharsFromEnd", "RemoveCharsFromEnd"),
    REMOVE_SPACES_FROM_START("Remove spaces from start", "removeSpacesFromStart", "RemoveSpacesFromStart"),
    REMOVE_SPACES_FROM_END("Remove spaces from end", "removeSpacesFromEnd", "RemoveSpacesFromEnd"),
    REMOVE_SPACES_FROM_BOTH_SIDES("Remove spaces from both sides", "removeSpacesFromBothSides", "RemoveSpacesFromBothSides"),
    REMOVE_MATCH_FROM_START("Remove match from start", "removeMatchFromStart", "RemoveMatchFromStart"),
    REMOVE_MATCH_FROM_END("Remove match from end", "removeMatchFromEnd", "RemoveMatchFromEnd"),
    REMOVE_MATCH("Remove match", "removeMatch", "RemoveMatch"),
    REPLACE_MATCH_FROM_START("Replace match from start", "replaceMatchFromStart", "ReplaceMatchFromStart"),
    REPLACE_MATCH_FROM_END("Replace match from end", "replaceMatchFromEnd", "ReplaceMatchFromEnd"),
    REPLACE_MATCH("Replace match", "replaceMatch", "ReplaceMatch"),
    CONVERT_TO_TITLE_CASE("Convert to title case", "convertToTitleCase", "ConvertToTitleCase");

    private String label, value, clazz;

    Choices(String label, String value, String clazz) {
        this.label = label;
        this.value = value;
        this.clazz = clazz;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Getter for property 'clazz'.
     *
     * @return Value for property 'clazz'.
     */
    public String getClazz() {
        return clazz;
    }

    public String toString() {
        return getLabel();
    }
}

class ChoicesComparator implements java.util.Comparator<Choices> {
    public int compare(Choices left, Choices right) {
        return left.getLabel().compareTo(right.getLabel());
    }
}