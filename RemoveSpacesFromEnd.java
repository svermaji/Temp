/**
 * Created by 44085037 on 16-Oct-17
 */
public class RemoveSpacesFromEnd extends BaseProcessor {

    public RemoveSpacesFromEnd(MyLogger logger) {
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

        String fn = new StringBuilder(args.getFileNameNoExtn()).reverse().toString();
        int c;
        for (c = fn.length(); c > 0; c--) {
            if (fn.charAt(c) != ' ') {
                break;
            }
        }
        return args.getFileNameNoExtn().substring(0, c);
    }

}
