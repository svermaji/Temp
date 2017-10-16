/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveCharsFromStart extends BaseProcessor {

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param args Object of type Arguments
     * @return String modified file name
     */
    @Override
    protected String process(Arguments args) {
        /*if (!Utils.hasValue(args.getParam1())) {
            printMsg(log + "***Parameter explaining how many characters to remove is null.");
            return Utils.EMPTY;
        }
        int numChars = 0;
        String fileName = args.getFile().getName();
        try {
            numChars = Integer.parseInt(args.getParam1());
            if (numChars >= fileName.length()) {
                printMsg(log + "***Number of characters exceeding file name length.");
            }
        } catch (NumberFormatException e) {
            throwExcp(log + "Error: " + e.getMessage());
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder(Utils.EMPTY);
        for (int c = 0; c < fileName.length(); c++) {
            char ch = fileName.charAt(c);
            if (c >= numChars)
                sb.append(ch);
        }
        return sb.toString();*/
        return null;
    }

}
