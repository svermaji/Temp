/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveSpacesFromStart extends BaseProcessor {

    public RemoveSpacesFromStart(MyLogger logger) {
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
        int c;
        String fn = args.getFileNameNoExtn();
        for (c = 0; c < fn.length(); c++) {
            if (fn.charAt(c) != ' ') {
                break;
            }
        }
        return args.getFileNameNoExtn().substring(c);
    }

}
