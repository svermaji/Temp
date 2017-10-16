import java.io.File;

public class Arguments {
    private String sourceDir, fileType, choice, param1, param2;
    private boolean processSubFolder, processFolders, overwrite, appendFolder, updateID3v2Tag;

    private File file;

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public boolean isProcessSubFolder() {
        return processSubFolder;
    }

    public void setProcessSubFolder(boolean processSubFolder) {
        this.processSubFolder = processSubFolder;
    }

    public boolean isProcessFolders() {
        return processFolders;
    }

    public void setProcessFolders(boolean processFolders) {
        this.processFolders = processFolders;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public boolean isAppendFolder() {
        return appendFolder;
    }

    public void setAppendFolder(boolean appendFolder) {
        this.appendFolder = appendFolder;
    }

    public boolean isUpdateID3v2Tag() {
        return updateID3v2Tag;
    }

    public void setUpdateID3v2Tag(boolean updateID3v2Tag) {
        this.updateID3v2Tag = updateID3v2Tag;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Params except file
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Arguments{" +
                "sourceDir='" + sourceDir + '\'' +
                ", fileType='" + fileType + '\'' +
                ", choice='" + choice + '\'' +
                ", param1='" + param1 + '\'' +
                ", param2='" + param2 + '\'' +
                ", processSubFolder=" + processSubFolder +
                ", processFolders=" + processFolders +
                ", overwrite=" + overwrite +
                ", appendFolder=" + appendFolder +
                ", updateID3v2Tag=" + updateID3v2Tag +
                '}';
    }
}
