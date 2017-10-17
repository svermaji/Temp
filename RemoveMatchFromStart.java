/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveMatchFromStart extends BaseProcessor {

    public RemoveMatchFromStart(MyLogger logger) {
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
            printMsg(log + "***Parameter explaining matching string is null.");
            return Utils.EMPTY;
        }

        if (fileName.indexOf(extraPrm1) == 0)
            fileName = fileName.substring(extraPrm1.length());

        printMsg(log + "Finishing process with parameters: fileName [" + fileName + "], extraPrm1 [" + extraPrm1 + "] and extraPrm2 [" + extraPrm2 + "]");
        return fileName;*/
        return null;
    }

}
