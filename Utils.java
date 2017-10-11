/**
 * Created by svg on 11-Oct-2017
 */
public class Utils {

    /**
     * return true if param has non-null value
     *
     * @param item string to be checked
     * @return boolean status of operation
     */
    public static boolean hasValue ( String item )
    {
        return( (item != null) && (item.length() > 0) );
    }

}
