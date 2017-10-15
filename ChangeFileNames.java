import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Method;
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
public class ChangeFileNames extends AppFrame {//JFrame {

    private static final String SEPARATOR = ": ";
    private static final String MAIN = "main";
    private Writer logWriter = null;
    private String currentMethod = MAIN + SEPARATOR;

    private enum Choices {
        REMOVE_NUMBERS_FROM_FILE_NAMES("Remove numbers from file names", "removeNumbersFromFileNames"),
        REMOVE_NUMBERS_FROM_START("Remove numbers from start", "removeNumbersFromStart"),
        REMOVE_NUMBERS_FROM_END("Remove numbers from end", "removeNumbersFromEnd"),
        APPEND_STRING_IN_START("Append string in start", "appendStringInStart"),
        APPEND_STRING_IN_END("Append string in end", "appendStringInEnd"),
        REMOVE_CHARS_FROM_START("Remove chars from start", "removeCharsFromStart"),
        REMOVE_CHARS_FROM_END("Remove chars from end", "removeCharsFromEnd"),
        REMOVE_SPACES_FROM_START("Remove spaces from start", "removeSpacesFromStart"),
        REMOVE_SPACES_FROM_END("Remove spaces from end", "removeSpacesFromEnd"),
        REMOVE_SPACES_FROM_BOTH_SIDES("Remove spaces from both sides", "removeSpacesFromBothSides"),
        REMOVE_MATCH_FROM_START("Remove match from start", "removeMatchFromStart"),
        REMOVE_MATCH_FROM_END("Remove match from end", "removeMatchFromEnd"),
        REMOVE_MATCH("Remove match", "removeMatch"),
        REPLACE_MATCH_FROM_START("Replace match from start", "replaceMatchFromStart"),
        REPLACE_MATCH_FROM_END("Replace match from end", "replaceMatchFromEnd"),
        REPLACE_MATCH("Replace match", "replaceMatch"),
        CONVERT_TO_TITLE_CASE("Convert to title case", "convertToTitleCase");

        private String label, value;

        Choices(String label, String value) {
            this.label = label;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

        public String toString() {
            return getLabel();
        }
    }

    private String[] dropdownOptions = {
        "REMOVE_NUMBERS_FROM_FILE_NAMES",   // 0
        "REMOVE_NUMBERS_FROM_START",        // 1
        "REMOVE_NUMBERS_FROM_END",          // 2
        "APPEND_STRING_IN_START",           // 3
        "APPEND_STRING_IN_END",             // 4
        "REMOVE_CHARS_FROM_START",          // 5
        "REMOVE_CHARS_FROM_END",            // 6
        "REMOVE_SPACES_FROM_START",         // 7
        "REMOVE_SPACES_FROM_END",           // 8
        "REMOVE_SPACES_FROM_BOTH_SIDES",    // 9
        "REMOVE_MATCH_FROM_START",          // 10
        "REMOVE_MATCH_FROM_END",            // 11
        "REMOVE_MATCH",                     // 12
        "REPLACE_MATCH_FROM_START",         // 13
        "REPLACE_MATCH_FROM_END",           // 14
        "REPLACE_MATCH",                    // 15
        "CONVERT_TO_TITLE_CASE",            // 16
        "UPDATE_MP3_TAGS"                   // 17
    };

    private JLabel lblFolder, lblAction, lblParam1, lblParam2, lblExt;
    private JTextField txtFolder, txtParam1, txtParam2, txtExt;
    private JCheckBox jcSubFolder, jcProcessFolders, jcOverwrite, jcAppendFolder, jcUpdateID3v2Tag;
    private JCheckBox jcModifyTitle;
    private JComboBox<Choices> jcb;
    private JTextArea taStatus;
    private MyButton btnChange, btnBrowse, btnClear, btnUsage;

    private static final String DOUBLE_SPACE = "  ";
    private static final char SPACE_CHAR = ' ';
    private static final String SPACE = " ";
    private static final String DASH = "-";
    private static final String SP_DASH_SP = SPACE + DASH + SPACE;

    private static final int FILENAME_LEN = 30;
    private static int totalProcessedFiles = 0;
    private static int successfullyProcessedFiles = 0;
    private static int unprocessedFiles = 0;

    private ChangeFileNames(String title) throws Exception {
        setTitle(title);
        initComponents();
    }

    private void initComponents() throws Exception {

        createLogFile("ChangeFileNames.log");

        lblFolder = new JLabel("Folder");
        lblAction = new JLabel("Action");
        lblParam1 = new JLabel("Param 1");
        lblParam2 = new JLabel("Param 2");
        lblExt = new JLabel("Process files with ext.");

        txtFolder = new JTextField("E:\\Songs\\2017", 20);
        txtParam1 = new JTextField("", 5);
        txtParam2 = new JTextField("", 5);
        txtExt = new JTextField("mp3", 5);

        jcb = new JComboBox<>(Choices.values()); //dropdownOptions
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
        taStatus = new JTextArea("");
        taStatus.setAutoscrolls(true);
        btnChange = new MyButton("Change");
        btnBrowse = new MyButton("Browse");
        btnClear = new MyButton("Clear Status");
        btnUsage = new MyButton("Usage");

        drawUI();

        printUsage();
        setSize(new Dimension(1200, 500));
        setLocation(0, 0);
    }

    private void drawUI() {
        getContentPane().setLayout(new BorderLayout());

        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(2, 1));
        JPanel jpUp = new JPanel();
        JPanel jpDown = new JPanel();
        JPanel jpMid = new JPanel();
        JPanel jpSouth = new JPanel();
        JPanel jpNorth = new JPanel();
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
        // for 1.5 use getContentPane
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
    private void process(String[] args) {
        final String log = "process: ";
        String SRC_DIR = args[0];
        String INCLUDE_SUB_FOLDERS = args[1];
        boolean includeSubDir = false;
        try {
            includeSubDir = Utils.getBoolean(INCLUDE_SUB_FOLDERS, false);
        } catch (Exception e) {
            printMsg(log + "Error: " + e.getMessage());
        }
        String EXTENSION = args[2];
        String OPTION = args[3];
        String EXTRA_PARAM1 = "";
        String EXTRA_PARAM2 = "";
        this.currentMethod = OPTION + SEPARATOR;

        if (args.length > 4 && args[4] != null)
            EXTRA_PARAM1 = args[4];

        if (args.length > 5 && args[5] != null)
            EXTRA_PARAM2 = args[5];

        printMsg(log + "Starting process.");

        printMsg(log + "Argument SRC_DIR [" + SRC_DIR + "]");
        printMsg(log + "Argument INCLUDE_SUB_FOLDERS [" + INCLUDE_SUB_FOLDERS + "]");
        printMsg(log + "Argument OPTION [" + OPTION + "]");
        printMsg(log + "Argument EXTENSION [" + EXTENSION + "]");
        printMsg(log + "Argument EXTRA_PARAM1 [" + EXTRA_PARAM1 + "]");
        printMsg(log + "Argument EXTRA_PARAM2 [" + EXTRA_PARAM2 + "]");

        if (EXTENSION.equals("ALL"))
            EXTENSION = null;

        ChangeFileNamesFilter filter = new ChangeFileNamesFilter(EXTENSION);
        File srcDir = new File(SRC_DIR);
        File[] fileList = srcDir.listFiles(filter);
        int len = fileList.length;
        printMsg(log + "Files obtained after applying filter are [" + len + "]");

        if (len != 0) {
            try {
                Class<ChangeFileNames> thisClass = ChangeFileNames.class;
                Class argumentsType[] = new Class[4];
                argumentsType[0] = String.class;
                argumentsType[1] = String.class;
                argumentsType[2] = String.class;
                argumentsType[3] = File.class;
                Method method = thisClass.getMethod(OPTION, argumentsType);

                for (File file : fileList) {
                    printMsg("includeSubDir [" + includeSubDir + "]");
                    printMsg("is directory [" + file.isDirectory() + "]");
                    printMsg("is file [" + file.isFile() + "]");
                    if (includeSubDir && file.isDirectory()) {
                        String[] tempArgs = null;
                        try {
                            printMsg("Collecting parameters for directory [" + file.getCanonicalPath() + "]");
                            tempArgs = new String[args.length];
                            System.arraycopy(args, 0, tempArgs, 0, tempArgs.length);
                            tempArgs[0] = file.getCanonicalPath();
                        } catch (Exception e) {
                            printMsg(log + "Error: " + e.getMessage());
                            e.printStackTrace();
                        }

                        printMsg("Calling nested process.");
                        process(tempArgs);
                        if (jcProcessFolders.isSelected() && OPTION.equals(Choices.CONVERT_TO_TITLE_CASE.getValue())) {
                            String folderName = file.getName();
                            //getting folder name

                            Object[] arguments = new Object[]{folderName, EXTRA_PARAM1, EXTRA_PARAM2, file};
                            String returnVal = (String) method.invoke(this, arguments);
                            printMsg(log + "modified file name obtained as [" + returnVal + "]");


                            if (Utils.hasValue(returnVal) && !returnVal.equals(folderName)) {
                                printMsg(log + "folder [" + folderName + "] renamed to fileName [" + returnVal + "]");
                                printMsg(log + "file.getParent () = " + file.getParent());
                                boolean status = file.renameTo(new File(file.getParent() + "\\" + returnVal));
                                printMsg(log + "The operation status for [" + folderName + "] is [" + status + "]");
                            } else
                                printMsg(log + "folder [" + folderName + "] does not require conversion.");
                        }
                    } else {

                        if (!file.isFile())
                            continue;

                        String actualFileName = file.getName();
                        //excluding extension
                        String extension = actualFileName.substring(actualFileName.lastIndexOf(".") + 1);
                        printMsg(log + "extension = " + extension);
                        actualFileName = actualFileName.substring(0, actualFileName.lastIndexOf("."));
                        printMsg(log + "actual file name is [" + actualFileName + "]");

                        Object[] arguments = new Object[]{actualFileName, EXTRA_PARAM1, EXTRA_PARAM2, file};
                        String returnVal = (String) method.invoke(this, arguments);
                        printMsg(log + "modified file name obtained as [" + returnVal + "]");


                        if (Utils.hasValue(returnVal) && !returnVal.equals(actualFileName)) {
                            printMsg(log + "file [" + actualFileName + "] renamed to fileName [" + returnVal + "]");
                            printMsg("file.getParent () = " + file.getParent());
                            boolean status = file.renameTo(new File(file.getParent() + "\\" + returnVal + "." + extension));
                            printMsg(log + "The operation status for [" + actualFileName + "] is [" + status + "]");
                        } else
                            printMsg(log + "file [" + actualFileName + "] does not require conversion.");

                        totalProcessedFiles++;
                        successfullyProcessedFiles++;
                    }
                }
            } catch (Exception e) {
                throwExcp(log + "Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else
            printMsg("No file to process.");

        printMsg("Processing COMPLETE. Processing summary.total processed files [" + totalProcessedFiles +
            "], total processed successfully files [" + successfullyProcessedFiles +
            "], total processed failed files [" + unprocessedFiles + "]");

        //resetProcessedFileCounters ();
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
        write(s);
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     */
    private String removeNumbersFromFileNames(String fileName, String extraPrm1, String extraPrm2, File file) {
        StringBuilder sb = new StringBuilder("");
        for (int c = 0; c < fileName.length(); c++) {
            char ch = fileName.charAt(c);
            if (!Utils.isNumeric(ch))
                sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     */
    private String removeNumbersFromStart(String fileName, String extraPrm1, String extraPrm2, File file) {
        boolean meetAlphabet = false;
        StringBuilder sb = new StringBuilder("");
        for (int c = 0; c < fileName.length(); c++) {
            char ch = fileName.charAt(c);
            if (!Utils.isNumeric(ch) || meetAlphabet) {
                meetAlphabet = true;
                sb.append(ch);
            }

        }
        return sb.toString();
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     */
    public String removeNumbersFromEnd(String fileName, String extraPrm1, String extraPrm2, File file) {
        String log = currentMethod;
        printMsg(log + "Initialising process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        fileName = new StringBuilder(fileName).reverse().toString();
        StringBuilder sb = new StringBuilder(removeNumbersFromStart(fileName, extraPrm1, extraPrm2, file));

        printMsg(log + "Finishing process with parameters: fileName [" + sb.reverse().toString() + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return sb.reverse().toString();
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     */
    public String appendStringInStart(String fileName, String extraPrm1, String extraPrm2, File file) {
        return extraPrm1 + fileName;
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     */
    public String appendStringInEnd(String fileName, String extraPrm1, String extraPrm2, File file) {
        return fileName + extraPrm1;
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     * @throws Exception on error
     */
    private String removeCharsFromStart(String fileName, String extraPrm1, String extraPrm2, File file) throws Exception {
        String log = currentMethod;
        printMsg(log + "Initialising process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        if (!Utils.hasValue(extraPrm1)) {
            printMsg(log + "***Parameter explaining how many characters to remove is null.");
            return "";
        }
        int numChars = 0;
        try {
            numChars = Integer.parseInt(extraPrm1);
            if (numChars >= fileName.length()) {
                printMsg(log + "***Number of characters exceeding file name length.");
            }
        } catch (NumberFormatException e) {
            throwExcp(log + "Error: " + e.getMessage());
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder("");
        for (int c = 0; c < fileName.length(); c++) {
            char ch = fileName.charAt(c);
            if (c >= numChars)
                sb.append(ch);
        }
        printMsg(log + "Finishing process with parameters: fileName [" + sb + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return sb.toString();
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of File
     * @return String modified file name
     * @throws Exception on error
     */
    public String convertToTitleCase(String fileName, String extraPrm1, String extraPrm2, File file) throws Exception {
        String log = currentMethod;
        printMsg(log + "Initialising process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        if (Utils.hasValue(fileName)) {
            fileName = fileName.toLowerCase();
            for (int i = 0; i < fileName.length(); i++) {
                char ch = fileName.charAt(i);
                //if (ch == '-' || ch == ' ' || ch == '_')
                if (ch == ' ' || ch == '_') {
                    if (i != fileName.length() - 1) {
                        fileName = fileName.substring(0, i) + SPACE + Character.toString(fileName.charAt(i + 1)).toUpperCase() + fileName.substring(i + 2);
                    }
                    //} else if (ch == '(' || ch == '[')
                } else if (ch == '(') {
                    if (i != fileName.length() - 1) {
                        fileName = fileName.substring(0, i + 1) + Character.toString(fileName.charAt(i + 1)).toUpperCase() + fileName.substring(i + 2);
                    }
                }
            }
            fileName = fileName.replaceAll("_", "");
        }
        fileName = Character.toString(fileName.charAt(0)).toUpperCase() + fileName.substring(1);

        while (fileName.contains(DOUBLE_SPACE))
            fileName = fileName.replaceAll(DOUBLE_SPACE, SPACE);

        if (jcAppendFolder.isSelected()) {
            String folderName = getFolderName(file);
            // removing any numbers if present in folder name
            folderName = removeNumbersFromFileNames(folderName.trim(), "", "", null);
            folderName = removeSpacesFromBothSides(folderName.trim(), "", "", null);
            if (Utils.hasValue(folderName) && !fileName.toLowerCase().startsWith(folderName.toLowerCase() + SP_DASH_SP)) {
                if (fileName.toLowerCase().startsWith(folderName.toLowerCase()))
                    fileName = fileName.substring(0, folderName.length()) + SP_DASH_SP + fileName.substring(folderName.length());
                else
                    fileName = folderName + SP_DASH_SP + fileName;

                fileName = fileName.replaceAll(DOUBLE_SPACE, SPACE);
            }
        } else {
            String folderName = getFolderName(file);
            // removing any numbers if present in folder name
            folderName = removeNumbersFromFileNames(folderName.trim(), "", "", null);
            folderName = removeSpacesFromBothSides(folderName.trim(), "", "", null);
            if (Utils.hasValue(folderName) && fileName.toLowerCase().startsWith(folderName.toLowerCase())) {
                fileName = fileName.substring(folderName.length());
            }
            if (Utils.hasValue(folderName) && fileName.toLowerCase().startsWith(SP_DASH_SP)) {
                fileName = fileName.substring(SP_DASH_SP.length());
            }

            // trying same thing with non-modified folder name
            folderName = getFolderName(file, false);
            // removing any numbers if present in folder name
            folderName = removeNumbersFromFileNames(folderName.trim(), "", "", null);
            folderName = removeSpacesFromBothSides(folderName.trim(), "", "", null);
            if (Utils.hasValue(folderName) && fileName.toLowerCase().startsWith(folderName.toLowerCase())) {
                fileName = fileName.substring(folderName.length());
            }
            if (Utils.hasValue(folderName) && fileName.toLowerCase().startsWith(SP_DASH_SP)) {
                fileName = fileName.substring(SP_DASH_SP.length());
            }
        }

        if (file.isFile())
            while (fileName.length() > FILENAME_LEN) {
                fileName = fileName.substring(0, FILENAME_LEN);
                if (fileName.contains(SPACE)) {
                    fileName = fileName.substring(0, fileName.lastIndexOf(SPACE));
                }
            }

        fileName = removeSpacesFromBothSides(fileName, "", "", null);
        printMsg(log + "Finishing process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return fileName;
    }

    private String getFolderName(File file) throws Exception {
        return getFolderName(file, true);
    }

    private String getFolderName(File file, boolean modifyLongName) throws Exception {
        String log = "getFolderName: ";
        String folderName = "";
        if (file.isFile()) {
            folderName = file.getParent();
            if (folderName.contains("\\"))
                folderName = folderName.substring(folderName.lastIndexOf("\\") + 1);
            else if (folderName.contains("/"))
                folderName = folderName.substring(folderName.lastIndexOf("/") + 1);

            // excluding chars other than Aa-Zz
            String temp = "";
            for (int i = 0; i < folderName.length(); i++) {
                boolean include = Character.isLetter(folderName.charAt(i)) || folderName.charAt(i) == SPACE_CHAR;
                temp += include ? folderName.charAt(i) + "" : "";
            }
            if (Utils.hasValue(temp)) folderName = temp;

            // if length > 17 chars then take first char of spacing folder name
            if (modifyLongName && folderName.length() > 17) {
                temp = "" + ((Character.isLetter(folderName.charAt(0))) ? folderName.charAt(0) + "" : "");
                for (int i = 0; i < folderName.length(); i++) {
                    if (folderName.charAt(i) == ' ')
                        temp += ((Character.isLetter(folderName.charAt(i + 1))) ? folderName.charAt(i + 1) + "" : "");
                }
                if (Utils.hasValue(temp)) folderName = temp;
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
            if (str.contains(SPACE)) {
                str = str.substring(0, str.lastIndexOf(SPACE));
            }
        }
        printMsg(log + "Finished with parameter str as [" + str + "] and limit [" + limit + "].");
        return str;
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     * @throws Exception on error
     */
    public String removeCharsFromEnd(String fileName, String extraPrm1, String extraPrm2, File file) throws Exception {
        String log = currentMethod;
        printMsg(log + "Initialising process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        if (!Utils.hasValue(extraPrm1)) {
            printMsg(log + "***Parameter explaining how many characters to remove is null.");
            return "";
        }
        fileName = new StringBuilder(fileName).reverse().toString();
        StringBuilder sb = new StringBuilder(removeCharsFromStart(fileName, extraPrm1, extraPrm2, file));
        String returnFileName = sb.reverse().toString();

        printMsg(log + "Finishing process with parameters: fileName [" + returnFileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return returnFileName;
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     * @throws Exception on error
     */
    private String removeSpacesFromStart(String fileName, String extraPrm1, String extraPrm2, File file) throws Exception {
        String log = currentMethod;
        printMsg(log + "Initialising process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        boolean meetAlphabet = false;
        StringBuilder sb = new StringBuilder("");
        for (int c = 0; c < fileName.length(); c++) {
            char ch = fileName.charAt(c);
            if (ch != ' ' || meetAlphabet) {
                meetAlphabet = true;
                sb.append(ch);
            }
        }
        printMsg(log + "Finishing process with parameters: fileName [" + sb + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return sb.toString();
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     * @throws Exception on error
     */
    private String removeSpacesFromEnd(String fileName, String extraPrm1, String extraPrm2, File file) throws Exception {
        String log = currentMethod;
        printMsg(log + "Initialising process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        fileName = new StringBuilder(fileName).reverse().toString();
        StringBuilder sb = new StringBuilder(removeSpacesFromStart(fileName, extraPrm1, extraPrm2, file));
        String returnFileName = sb.reverse().toString();

        printMsg(log + "Finishing process with parameters: fileName [" + returnFileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return returnFileName;
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     * @throws Exception on error
     */
    private String removeSpacesFromBothSides(String fileName, String extraPrm1, String extraPrm2, File file) throws Exception {
        fileName = removeSpacesFromStart(fileName, extraPrm1, extraPrm2, file);
        fileName = removeSpacesFromEnd(fileName, extraPrm1, extraPrm2, file);
        return fileName;
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     * @throws Exception on error
     */
    private String removeMatchFromStart(String fileName, String extraPrm1, String extraPrm2, File file) throws Exception {
        String log = currentMethod;
        printMsg(log + "Initialising process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        if (!Utils.hasValue(extraPrm1)) {
            printMsg(log + "***Parameter explaining matching string is null.");
            return "";
        }

        if (fileName.indexOf(extraPrm1) == 0)
            fileName = fileName.substring(extraPrm1.length());

        printMsg(log + "Finishing process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return fileName;
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     * @throws Exception on error
     */
    public String removeMatchFromEnd(String fileName, String extraPrm1, String extraPrm2, File file) throws Exception {
        String log = currentMethod;
        printMsg(log + "Initialising process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        if (!Utils.hasValue(extraPrm1)) {
            printMsg(log + "***Parameter explaining matching string is null.");
            return "";
        }

        fileName = new StringBuilder(fileName).reverse().toString();
        String revPrm1 = new StringBuilder(extraPrm1).reverse().toString();
        String revPrm2 = new StringBuilder(extraPrm2).reverse().toString();
        StringBuilder sb = new StringBuilder(removeMatchFromStart(fileName, revPrm1, revPrm2, file));
        String returnFileName = sb.reverse().toString();

        printMsg(log + "Finishing process with parameters: fileName [" + returnFileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return returnFileName;
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     * @throws Exception on error
     */
    public String removeMatch(String fileName, String extraPrm1, String extraPrm2, File file) throws Exception {
        String log = currentMethod;
        printMsg(log + "Initialising process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        if (!Utils.hasValue(extraPrm1)) {
            printMsg(log + "***Parameter explaining matching string is null.");
            return "";
        }

        fileName = fileName.replaceAll(extraPrm1, "");
        printMsg(log + "Finishing process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return fileName;
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     * @throws Exception on error
     */
    private String replaceMatchFromStart(String fileName, String extraPrm1, String extraPrm2, File file) throws Exception {
        String log = currentMethod;
        printMsg(log + "Initialising process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        if (!Utils.hasValue(extraPrm1)) {
            printMsg(log + "***Parameter to search is null.");
            return "";
        }
        if (!Utils.hasValue(extraPrm2)) {
            printMsg(log + "***Parameter to replace is null.");
            return "";
        }

        if (fileName.indexOf(extraPrm1) == 0)
            fileName = fileName.replaceFirst(extraPrm1, extraPrm2);

        printMsg(log + "Finishing process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return fileName;
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     * @throws Exception on error
     */
    public String replaceMatchFromEnd(String fileName, String extraPrm1, String extraPrm2, File file) throws Exception {
        String log = currentMethod;
        printMsg(log + "Initialising process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        if (!Utils.hasValue(extraPrm1)) {
            printMsg(log + "***Parameter to search is null.");
            return "";
        }
        if (!Utils.hasValue(extraPrm2)) {
            printMsg(log + "***Parameter to replace is null.");
            return "";
        }

        fileName = new StringBuilder(fileName).reverse().toString();
        String revPrm1 = new StringBuilder(extraPrm1).reverse().toString();
        String revPrm2 = new StringBuilder(extraPrm2).reverse().toString();
        StringBuilder sb = new StringBuilder(replaceMatchFromStart(fileName, revPrm1, revPrm2, file));
        String returnFileName = sb.reverse().toString();

        printMsg(log + "Finishing process with parameters: fileName [" + returnFileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return returnFileName;
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param fileName  source file name to modify
     * @param extraPrm1 extra parameter 1 to be used by other methods (due to use of reflection)
     * @param extraPrm2 extra parameter 2 to be used by other methods (due to use of reflection)
     * @param file      object of java.io.File
     * @return String modified file name
     * @throws Exception on error
     */
    public String replaceMatch(String fileName, String extraPrm1, String extraPrm2, File file) throws Exception {
        String log = currentMethod;
        printMsg(log + "Initialising process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        if (!Utils.hasValue(extraPrm1)) {
            printMsg(log + "***Parameter to search is null.");
            return "";
        }
        if (!Utils.hasValue(extraPrm2)) {
            printMsg(log + "***Parameter to replace is null.");
            return "";
        }

        fileName = fileName.replaceAll(extraPrm1, extraPrm2);
        printMsg(log + "Finishing process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return fileName;
    }

    /**
     * Validate all the arguments.
     *
     * @param args arguments
     * @return boolean as status
     * @throws Exception on error
     */
    private boolean validateAllArgs(String[] args) throws Exception {
        return (validateArgs(args) && validateDir(args[0]) && validateOption(args[3]));
    }

    /**
     * checks whether used the available options of used
     * some thing else.
     *
     * @param arg the option
     * @return boolean status of operation
     */
    private boolean validateOption(String arg) {
        Choices choices = ((Choices) jcb.getSelectedItem());
        boolean validVal = choices.getValue().equals(arg);
        printMsg("validateOption: Returning result [" + validVal + "] for argument [" + choices.getLabel() + "]");
        return validVal;
    }

    /**
     * Validate the arguments against not null condition.
     *
     * @param args parameters for processing filenames
     * @return boolean status of operation
     * @throws Exception on error
     */
    private boolean validateArgs(String[] args) throws Exception {
        final String log = "validateArgs: ";
        int totalArgsExpected = 4;
        int argsLength = args.length;

        if (argsLength < totalArgsExpected) {
            printMsg("Insufficient arguments length. Exiting.");
            return false;
        } else {
            for (String arg1 : args) {
                if (!Utils.hasValue(arg1)) {
                    printMsg(log + "Null argument(s) passed.");
                    return false;
                }
                printMsg(log + "Successfully validate argument [" + arg1 + "]");
            }
        }
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


    public void dispose() {
        super.dispose();
        try {
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            if (!Utils.hasValue(filter))
                status = true;
            if ((!status && file.getName().endsWith(filter)) || file.isDirectory())
                status = true;

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
        ChangeFileNames c = new ChangeFileNames("Change File Names");
        c.startProcessing(args);
    }

    private void startProcessing(String[] args) throws Exception {
        if (args != null && args.length > 0 && validateAllArgs(args))
            process(args);
        else
            printMsg("Unable to start process.");
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
                        if (jfc.getSelectedFile() != null)
                            txtFolder.setText(jfc.getSelectedFile().getCanonicalPath());
                    } catch (Exception e1) {
                        throwExcp(log + "Error: " + e1.getMessage());
                        e1.printStackTrace();
                    }
                } else if (e.getSource() == btnChange) {
                    //TODO: //handleControl (); // percentage
                    taStatus.setText("");
                    String args[] = {
                        txtFolder.getText(),
                        Boolean.toString(jcSubFolder.isSelected()),
                        txtExt.getText(),
                        ((Choices) (jcb.getSelectedItem())).getValue(),
                        Utils.hasValue(txtParam1.getText()) ? txtParam1.getText() : SPACE,
                        Utils.hasValue(txtParam2.getText()) ? txtParam2.getText() : SPACE
                    };
                    startProcessing(args);
                    //TODO: //handleControl ();
                } else if (e.getSource() == btnUsage) {
                    printUsage();
                } else if (e.getSource() == btnClear) {
                    taStatus.setText("");
                }
            } catch (Exception e1) {
                throwExcp(log + "Error: " + e1.getMessage());
                e1.printStackTrace();
            }
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
                e.printStackTrace();
            }
        }
    }


    /**
     * Writes the debug statement in log file.
     * If log file could not be initialized
     * thn output would be redirected to console.
     *
     * @param message - debug statement
     */
    private void write(String message) {
        try {
            if (logWriter != null) {
                synchronized (logWriter) {
                    logWriter.write(message + "\r\n");
                    logWriter.flush();
                }
            } else {
                System.out.println(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
