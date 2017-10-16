/**
 * Created by 44085037 on 16-Oct-17
 */
public abstract class BaseProcessor {

    private MyLogger logger;

    public BaseProcessor(MyLogger logger) {
        this.logger = logger;
    }

    private void printParameters(Arguments args) {
        printParameters(args, true);
    }

    private void printParameters(Arguments args, boolean isInitMsg) {
        logger.log(this.getClass().getName() + Utils.SP_DASH_SP
            + (isInitMsg ? "Initialising" : "Finishing") + " process with parameters: fileName ["
            + ((args.getFile() != null) ? args.getFile().getName() : "") + "] and [" + args.toString() + "]");
    }

    public String execute(Arguments args) {
        printParameters(args);
        String result = process(args);
        printParameters(args, false);
        return result;
    }

    public void log(String msg) {
        logger.log(this.getClass().getName() + Utils.SP_DASH_SP + msg);
    }

    protected abstract String process(Arguments args);

}
