/**
 * Created by 44085037 on 16-Oct-17
 */
public abstract class BaseProcessor {

    protected MyLogger logger;

    private void printParameters(String log, Arguments args) {
        printParameters(log, args, true);
    }

    private void printParameters(String log, Arguments args, boolean isInitMsg) {
        logger.log(log + (isInitMsg ? "Initialising" : "Finishing") +" process with parameters: fileName ["
                + ((args.getFile()!=null) ? args.getFile().getName() : "") + "] and [" + args.toString() + "]");
    }

    public String execute (String log, Arguments args) {
        printParameters(log, args);
        String result = process(args);
        printParameters(log, args, false);
        return result;
    }

    protected abstract String process (Arguments args);

}
