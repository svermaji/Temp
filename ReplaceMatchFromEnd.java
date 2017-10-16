/**
 * Created by 44085037 on 16-Oct-17
 */
public class ReplaceMatchFromEnd extends BaseProcessor {

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

        fileName = new StringBuilder(fileName).reverse().toString();
        String revPrm1 = new StringBuilder(extraPrm1).reverse().toString();
        String revPrm2 = new StringBuilder(extraPrm2).reverse().toString();
        StringBuilder sb = new StringBuilder(replaceMatchFromStart(fileName, revPrm1, revPrm2, file));
        String returnFileName = sb.reverse().toString();

        return returnFileName;*/
        return null;
    }

}
