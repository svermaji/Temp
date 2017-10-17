/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveCharsFromStart extends BaseProcessor {

    public RemoveCharsFromStart(MyLogger logger) {
        super(logger);
    }

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param args Object of type Arguments
     * @return String modified file name
     */
    @Override
    protected String process(Arguments args) {
        if (!Utils.hasValue(args.getParam1())) {
            log("***Parameter explaining how many characters to remove is null.");
            return Utils.EMPTY;
        }
        int numChars;
        String fileName = args.getFileNameNoExtn();
        try {
            numChars = Integer.parseInt(args.getParam1());
            if (numChars >= fileName.length()) {
                log("***Number of characters exceeding file name length.");
            }
        } catch (NumberFormatException e) {
            log("Unable to convert parameter to number.");
            e.printStackTrace();
            numChars = 0;
        }

        return fileName.substring(numChars);
    }

}
