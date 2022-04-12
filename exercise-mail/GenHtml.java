import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenHtml {

    private String ln = System.lineSeparator();
    private List<String> templateLines = new ArrayList<>();
    private List<String> gifFiles = null;
    private Map<String, List<String>> images = new LinkedHashMap<>();
    private String htmlPre = "<html>" +
            "<title>Come On!! Let's exercise</title>" + ln +
            "<body>" + ln +
            "<style>" + ln +
            "table {" + ln +
            "\tborder-collapse:collapse;" + ln +
            "}" + ln +
            "</style>" + ln +
            "<table width=\"100%\" border=\"1\">";
    private String htmlPost = "</table>" + ln +
            "</body>" + ln +
            "</html>";
    String trHtml = "  <tr>" +
            "    <td>[HEADER]</td>" +
            "    <td><img src=\"IMG_NAME\" title=\"[IMG_NAME_NO_EXTN]\" alt=\"[IMG_NAME_NO_EXTN]\" width=\"36\" height=\"36\" border=\"0\"><br><font size=1>IMG_NAME_NO_EXTN</font></td>" +
            "  </tr>";
    String tdHtml = "    <td><img src=\"IMG_NAME\" title=\"IMG_NAME\" alt=\"IMG_NAME\" width=\"200\" height=\"200\" border=\"0\"><br><font size=2>IMG_NAME</font></td>";

    public static void main(String[] args) {
        new GenHtml();
    }

    public GenHtml() {
        genHtml();
    }

    private void genHtml() {
        readHtmlTemplate();
        readGifImages();
        generate();
    }

    private void readGifImages() {
        try {
            gifFiles = Files.list(Paths.get("."))
                    .filter(f -> !f.toFile().isDirectory())
                    .map(p -> p.getFileName().toString())
                    .filter(f -> f.endsWith(".gif"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(gifFiles);
        gifFiles.forEach(p -> {
            String nm = p.split("-")[0].trim();
            if (images.containsKey(nm)) {
                List<String> l = images.get(nm);
                l.add(p);
            } else {
                List<String> l = new ArrayList<>();
                l.add(p);
                images.put(nm, l);
            }
        });

        System.out.println(images);
    }

    private void generate() {
        StringBuilder html = new StringBuilder();
        images.forEach((k, v) -> {
            html.append("<tr>").append(ln);
            html.append("<td>").append(k).append("</td>").append(ln);
            html.append("</tr>").append(ln);
            v.forEach(f -> {
                html.append("<tr>").append(ln);
                html.append("<td>").append(tdHtml.replaceAll("IMG_NAME", f)).append("</td>").append(ln);
                html.append("</tr>").append(ln);
            });
        });
        String finalHtml = htmlPre + html.toString() + htmlPost;
        System.out.println(finalHtml);
        try {
            Files.write(Paths.get("exercise.html"), finalHtml.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readHtmlTemplate() {
        try {
            templateLines = Files.readAllLines(Paths.get("exercise.html.template"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}