/**
 * Created by 44085037 on 16-Oct-17
 */
public class AppendStringInEnd extends BaseProcessor {

    public AppendStringInEnd(MyLogger logger) {
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
        return args.getFileNameNoExtn() + args.getParam1();
    }

}
