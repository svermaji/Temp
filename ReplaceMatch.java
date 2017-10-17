/**
 * Created by 44085037 on 16-Oct-17
 */
public class ReplaceMatch extends BaseProcessor {

    public ReplaceMatch(MyLogger logger) {
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
        /*if (!Utils.hasValue(extraPrm1)) {
            printMsg(log + "***Parameter to search is null.");
            return Utils.EMPTY;
        }
        if (!Utils.hasValue(extraPrm2)) {
            printMsg(log + "***Parameter to replace is null.");
            return Utils.EMPTY;
        }

        fileName = fileName.replaceAll(extraPrm1, extraPrm2);
        return fileName;*/
        return null;
    }

}
