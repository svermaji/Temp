import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 44085037 on 02-Nov-17
 */
public class TestHref {

    String domain = "https://www.w3schools.com/";
    String domainToChk = "w3schools.com";

    public static void main(String[] args) {
        TestHref t = new TestHref();
        t.test();
    }

    private void test() {
        String html = "<a href=\"url\">link text</a>...<a   href = \"https://www.w3schools.com/html/\">Visit our HTML tutorial</a>" +
                "<a href=\"https://www.google.com/xyz.asp\">HTML Images</a> <a   href = \"\">HTML Images</a>" +
                "<a href=\"html_images.asp\">HTML Images</a> <a   href = \"\">HTML Images</a>" +
                "<a href=\"https://www.w3schools.com/\" target=\"_blank\">Visit W3Schools!</a>" +
                "<a href=\"default.asp\">" +
                "  <img src=\"smiley.gif\" alt=\"HTML tutorial\" style=\"width:42px;height:42px;border:0;\">" +
                "</a>";
        Pattern linkPattern = Pattern.compile("<a[\\h]+href[\\h]*=[\\h]*\"(.*?)\"",  Pattern.CASE_INSENSITIVE| Pattern.DOTALL);
        Matcher pageMatcher = linkPattern.matcher(html);
        ArrayList<String> links = new ArrayList<>();
        while(pageMatcher.find()){
            String url = formatUrl (extractUrl(pageMatcher.group()));
            if (isEligible(url)) {
                links.add(url);
            }
        }
        System.out.println("total links = " + links.size());
        System.out.println("links = " + links);
    }

    private String formatUrl(String url) {
        return hasValue(url) && !(url.startsWith("http://") || url.startsWith("https://")) ? domain + url : url;
    }

    private boolean isEligible(String url) {
        return hasValue(url) && url.contains(domainToChk);
    }

    private String extractUrl(String href) {
        // although as per pattern only those matching strings
        // will come out
        String DQ = "\"";
        int fIdx = href.indexOf(DQ);
        if (fIdx > -1) {
            int sIdx = href.indexOf(DQ, fIdx+1);
            if (sIdx > -1 && sIdx > fIdx) {
                return href.substring(fIdx+1, sIdx);
            }
        }
        return "";
    }

    public static boolean hasValue ( String item )
    {
        return( (item != null) && (item.length() > 0) );
    }

}
