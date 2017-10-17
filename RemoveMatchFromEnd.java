/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveMatchFromEnd extends BaseProcessor {

    public RemoveMatchFromEnd(MyLogger logger) {
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
//            printMsg(log + "***Parameter explaining matching string is null.");
            return Utils.EMPTY;
        }

        // TODO: revisit
        //fileName = new StringBuilder(fileName).reverse().toString();
        String revPrm1 = new StringBuilder(args.getParam1()).reverse().toString();
        String revPrm2 = new StringBuilder(args.getParam1()).reverse().toString();
        // TODO: revisit
//        StringBuilder sb = new StringBuilder(removeMatchFromStart(args));
//        String returnFileName = sb.reverse().toString();

//        return returnFileName;
        return null;
    }

}
