/**
 * Created by 44085037 on 16-Oct-17
 */
public class AppendStringInEnd extends BaseProcessor {

    /**
     * Remove all occurrence of any digit in file name
     *
     * @param args Object of type Arguments
     * @return String modified file name
     */
    @Override
    protected String process(Arguments args) {
        return args.getFile().getName() + args.getParam1();
    }

}
