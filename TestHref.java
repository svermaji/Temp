import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 44085037 on 02-Nov-17
 */
public class TestHref {

    //String domain = "https://www.w3schools.com";
    String domain = "http://www.serosoft.in";
    String HTTP = "http://";
    String HTTPS = "https://";
    String WWW = "www";
    String domainToChk = domain;

    public static void main(String[] args) {
        TestHref t = new TestHref();
        t.test();
    }

    private void test() {
        //Pattern linkPattern = Pattern.compile("<link[\\h]*(rel)*=\"(.+?)\"(href)+",  Pattern.CASE_INSENSITIVE| Pattern.DOTALL);
        Pattern linkPattern = Pattern.compile("*<link",  Pattern.CASE_INSENSITIVE| Pattern.DOTALL);
        Matcher alinkMatcher = linkPattern.matcher(html);
        ArrayList<String> links = new ArrayList<>();
        while(alinkMatcher.find()){
            String url = formatUrl (extractUrl(alinkMatcher.group()));
            if (isEligible(url) && !links.contains(url)) {
                links.add(url);
            }
        }
        System.out.println("links = " + links);

    }
    private void test1() {
        setDomainToCheck ();
        /*String html = "<a href=\"url\">link text</a>...<a   href = \"https://www.w3schools.com/html/\">Visit our HTML tutorial</a>" +
                "<a href=\"https://www.google.com/xyz.asp\">HTML Images</a> <a   href = \"\">HTML Images</a>" +
                "<a href=\"html_images.asp\">HTML Images</a> <a   href = \"\">HTML Images</a>" +
                "<a href=\"https://www.w3schools.com/\" target=\"_blank\">Visit W3Schools!</a>" +
                "<a href=\"default.asp\">" +
                "  <img src=\"smiley.gif\" alt=\"HTML tutorial\" style=\"width:42px;height:42px;border:0;\">" +
                "</a>";*/
        Pattern ahrefPattern = Pattern.compile("<a[\\h]+href[\\h]*=[\\h]*\"(.*?)\"",  Pattern.CASE_INSENSITIVE| Pattern.DOTALL);
        Pattern linkPattern = Pattern.compile("<link(.*?)[\\h]+[href]+[\\h]*=[\\h]*\"(.*?)\"",  Pattern.CASE_INSENSITIVE| Pattern.DOTALL);
        Matcher ahrefMatcher = ahrefPattern.matcher(html);
        Matcher alinkMatcher = linkPattern.matcher(html);
        ArrayList<String> links = new ArrayList<>();
        while(ahrefMatcher.find()){
            String url = formatUrl (extractUrl(ahrefMatcher.group()));
            if (isEligible(url) && !links.contains(url)) {
                links.add(url);
            }
        }
        while(alinkMatcher.find()){
            String url = formatUrl (extractUrl(alinkMatcher.group()));
            if (isEligible(url) && !links.contains(url)) {
                links.add(url);
            }
        }
        System.out.println("total links = " + links.size());
        System.out.println("links = " + links);
    }

    private void setDomainToCheck() {
        if (domain.startsWith(HTTP) || domain.startsWith(HTTPS)) {
            if (domain.contains(WWW)) {
                domainToChk = domain.substring(domain.indexOf(WWW)+4);
            } else {
                domainToChk = domain.startsWith(HTTP) ? domain.substring(domain.indexOf(HTTP)+HTTP.length()) : domain.substring(domain.indexOf(HTTPS)+HTTPS.length());
            }
        }
        //domain = domain.toLowerCase()+"/";
        System.out.println("domain = " + domain);
        System.out.println("domainToChk = " + domainToChk);
    }

    private String formatUrl(String url) {
        return hasValue(url) && !(url.startsWith("http://") || url.startsWith("https://")) ? domain + url : url;
    }

    private boolean isEligible(String url) {
        return hasValue(url) && url.contains(domainToChk);
    }

    private String extractUrl(String href) {
        href = href.toLowerCase();
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

    String html = "  <link rel=\"stylesheet\" href=\"/media/k2/assets/css/k2.fonts.css?v2.7.1\" type=\"text/css\" />";
    String html1 = "\n" +
            "<!DOCTYPE html>\n" +
            "<html prefix=\"og: http://ogp.me/ns#\" xml:lang=\"en-gb\" lang=\"en-gb\" dir=\"ltr\" class=\"bootstrap3 homepage com_content view-featured\">\n" +
            "<head>\n" +
            "  <script>\n" +
            "  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n" +
            "  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n" +
            "  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n" +
            "  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');\n" +
            "\n" +
            "  ga('create', 'UA-80654435-1', 'auto');\n" +
            "  ga('send', 'pageview');\n" +
            "\n" +
            "</script>\n" +
            "  <base href=\"http://www.serosoft.in/\" />\n" +
            "  <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n" +
            "  <meta name=\"keywords\" content=\"Software Development Company in Indore, Java Company in Indore, software testing in Indore, ERP Company jobs Indore, it companies in India, software companies in Indore, Software Development Company in Indore, Educational Software &amp; Solutions Company, Education ERP Company\" />\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\" />\n" +
            "  <meta name=\"description\" content=\"\n" +
            "We are a leading educational software and solutions company, which is powering scores of institutions - globally- to drive campus automation and efficiency.\" />\n" +
            "  <title>Leading Educational Software &amp; Solutions Company India - IT/Software Company Indore</title>\n" +
            "  <link href=\"http://www.serosoft.in/\" rel=\"canonical\" />\n" +
            "  <link href=\"/?format=feed&amp;type=rss\" rel=\"alternate\" type=\"application/rss+xml\" title=\"RSS 2.0\" />\n" +
            "  <link href=\"/?format=feed&amp;type=atom\" rel=\"alternate\" type=\"application/atom+xml\" title=\"Atom 1.0\" />\n" +
            "  <link href=\"/templates/baseline/favicon.ico\" rel=\"shortcut icon\" type=\"image/vnd.microsoft.icon\" />\n" +
            "  <link rel=\"stylesheet\" href=\"/media/k2/assets/css/k2.fonts.css?v2.7.1\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"/components/com_k2/css/k2.css?v2.7.1\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"http://www.serosoft.in/modules/mod_bt_contentslider/tmpl/css/btcontentslider.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"/media/system/css/modal.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"http://www.serosoft.in/modules/mod_yjme/css/stylesheet.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"/modules/mod_favslider/theme/css/favslider.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"/plugins/system/yjsg/assets/css/font-awesome.min.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"/templates/baseline/css_compiled/bootstrap-style1.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"/plugins/system/yjsg/legacy/css/template.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"/templates/baseline/css/menus.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"/templates/baseline/css/layout.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"/templates/baseline/css/style1.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"/plugins/system/yjsg/legacy/css/yjresponsive.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"/templates/baseline/css/custom_responsive.css\" type=\"text/css\" />\n" +
            "  <link rel=\"stylesheet\" href=\"http://fonts.googleapis.com/css?family=Fjalla+One\" type=\"text/css\" />\n" +
            "  <style type=\"text/css\">\n" +
            "body{font-size:13px;}#logo{width:20%;height:105px;}#logo a{height:105px;}.yjsgsitew{width:1200px;}.yjsgheadergw{width:80%;}#midblock{width:100%;}#insetsholder_2t,#insetsholder_2b{width:0%;}\n" +
            ".horiznav li li,.horiznav ul ul a, .horiznav li ul,.YJSG_listContainer{width:200px;}\n" +
            ".horiznav li ul ul,.subul_main.group_holder ul.subul_main ul.subul_main, .subul_main.group_holder ul.subul_main ul.subul_main ul.subul_main, .subul_main.group_holder ul.subul_main ul.subul_main ul.subul_main ul.subul_main,.horiznav li li li:hover ul.dropline{margin-top: -32px!important;margin-left:95%!important;}.top_menu ul.subul_main.dropline.group_holder li.holdsgroup > ul.subul_main{margin:0!important;padding-top:10px!important;padding-bottom:10px!important;}\n" +
            "h1,h2,h3,h4,h5,h6,.article_title,.module_title,.pagetitle,.showtext,.trialLink a{font-family:Fjalla One,sans-serif;font-weight:normal;}\n" +
            "a{color:#de5d45;}[class*='yjsg-button-color']{background:#de5d45;}[class*='yjsg-button-color']:hover{background:#da4a2f;}#typosticky.yjsg-sticky.fixed{max-width:1200px;margin:0 auto;padding:15px;}.yjsg-sub-heading,.yjsg-sticky-menu a.active-scroll,[data-sticky-block] a.active-scroll:before{border-color:#de5d45;}[class*='facolor'].fa:before{color:#de5d45;}\n" +
            "#adv1.yjsgxhtml{width:100.00%;}\n" +
            "#user1.yjsgxhtml{width:100.00%;}\n" +
            "#user6.yjsgxhtml{width:100.00%;}\n" +
            "#user11.yjsgxhtml{width:100.00%;}\n" +
            "#user16.yjsgxhtml{width:100.00%;}\n" +
            "#user21.yjsgxhtml{width:33.33%;}\n" +
            "#user22.yjsgxhtml{width:33.33%;}\n" +
            "#user23.yjsgxhtml{width:33.33%;}\n" +
            "#btcontentslider120 .bt_handles{top: -50px !important;right: 5px !important}\n" +
            "\t\t@media screen and (max-width: 480px){.bt-cs .bt-row{width:100%!important;}}\n" +
            "  </style>\n" +
            "  <script src=\"/media/jui/js/jquery.min.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/media/jui/js/jquery-noconflict.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/media/jui/js/jquery-migrate.min.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/media/k2/assets/js/k2.frontend.js?v2.7.1&amp;sitepath=/\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/media/system/js/mootools-core.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/media/system/js/core.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/media/system/js/mootools-more.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/plugins/system/yjsg/assets/src/yjsg.jquicustom.min.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/plugins/system/yjsg/assets/bootstrap3/js/bootstrap.min.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/plugins/system/yjsg/legacy/src/yjresponsive.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/plugins/system/yjsg/legacy/src/yjsg.smoothdrop.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/plugins/system/yjsg/assets/src/yjsg.site.plugins.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/plugins/system/yjsg/assets/src/yjsg.site.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/plugins/system/yjsg/assets/src/magnific/yjsg.magnific.popup.min.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"http://www.serosoft.in/modules/mod_bt_contentslider/tmpl/js/slides.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"http://www.serosoft.in/modules/mod_bt_contentslider/tmpl/js/default.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"http://www.serosoft.in/modules/mod_bt_contentslider/tmpl/js/jquery.easing.1.3.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/media/system/js/modal.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/modules/mod_favslider/theme/js/jquery.flexslider.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/modules/mod_favslider/theme/js/jquery.mousewheel.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/modules/mod_favslider/theme/js/jquery.fitvids.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/modules/mod_favslider/theme/js/favslider.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/modules/mod_favslider/theme/js/viewportchecker/viewportchecker.js\" type=\"text/javascript\"></script>\n" +
            "  <script src=\"/templates/baseline/src/styles.js\" type=\"text/javascript\"></script>\n" +
            "  <script type=\"text/javascript\">\n" +
            ";jQuery(function($) {\n" +
            "\t\t\tSqueezeBox.initialize({});\n" +
            "\t\t\tSqueezeBox.assign($('a.modal').get(), {\n" +
            "\t\t\t\tparse: 'rel'\n" +
            "\t\t\t});\n" +
            "\t\t});\n" +
            "  </script>\n" +
            "</head>\n" +
            "<body id=\"stylef6\" class=\"yjsgbody style_style1 yjsgbr-chrome\">\n" +
            "\t<div class=\"top_bg\">\n" +
            "\t\t<div id=\"centertop\" class=\"yjsgsitew\">\n" +
            "\t\t\t\t\t\t <!--header-->\n" +
            "<div id=\"header\">\n" +
            "      <div id=\"logo\">\n" +
            "           <a href=\"http://www.serosoft.in/\"></a>\n" +
            "          </div>\n" +
            "    <!-- end logo -->\n" +
            "   <!--top menu-->\n" +
            "<div id=\"yjsgheadergrid\" class=\"yjsgheadergw\">\n" +
            "    <div id=\"topmenu_holder\" class=\"yjsgmega\">\n" +
            "      <div class=\"top_menu\">\n" +
            "          <div id=\"horiznav\" class=\"horiznav\">\t<ul class=\"megalegacy menunav\">\n" +
            "\t<li id=\"current\" class=\" active item101 level0 first\"><span class=\"mymarg\"><a class=\"yjanchor  first activepath \" href=\"/\"><span class=\"yjm_has_none\"><span class=\"yjm_title\">Home</span></span></a></span></li><li class=\"item302 level0\"><span class=\"mymarg\"><a class=\"yjanchor \" href=\"/company\"><span class=\"yjm_has_none\"><span class=\"yjm_title\">Company</span></span></a></span></li><li class=\"haschild item303 level0\"><span class=\"child\"><a class=\"yjanchor \" href=\"/team\"><span class=\"yjm_has_none\"><span class=\"yjm_title\">Team</span></span></a></span><ul class=\"subul_main level1\"><li class=\"bl\"></li><li class=\"tl\"></li><li class=\"tr\"></li><li class=\"item304 level1 first lilast\"><span class=\"mymarg\"><a class=\"yjanchor  firstlast\" href=\"/team/board-of-advisors\"><span class=\"yjm_has_none\"><span class=\"yjm_title\">Board of Advisors</span></span></a></span></li><li class=\"right\"></li><li class=\"br\"></li></ul></li><li class=\"item305 level0\"><span class=\"mymarg\"><a class=\"yjanchor \" href=\"/why-us\"><span class=\"yjm_has_none\"><span class=\"yjm_title\">WHY US</span></span></a></span></li><li class=\"item306 level0\"><span class=\"mymarg\"><a class=\"yjanchor \" href=\"/services\"><span class=\"yjm_has_none\"><span class=\"yjm_title\">Services</span></span></a></span></li><li class=\"item307 level0\"><span class=\"mymarg\"><a class=\"yjanchor \" href=\"/products\"><span class=\"yjm_has_none\"><span class=\"yjm_title\">Products</span></span></a></span></li><li class=\"haschild item332 level0\"><span class=\"child\"><a class=\"yjanchor \" href=\"/careers\"><span class=\"yjm_has_none\"><span class=\"yjm_title\">Careers</span></span></a></span><ul class=\"subul_main level1\"><li class=\"bl\"></li><li class=\"tl\"></li><li class=\"tr\"></li><li class=\"item334 level1 first lilast\"><span class=\"mymarg\"><a class=\"yjanchor  firstlast\" href=\"/careers/work-life-at-serosoft\"><span class=\"yjm_has_none\"><span class=\"yjm_title\">Work-Life at Serosoft</span></span></a></span></li><li class=\"right\"></li><li class=\"br\"></li></ul></li><li class=\"item319 level0\"><span class=\"mymarg\"><a class=\"yjanchor \" href=\"/blog\"><span class=\"yjm_has_none\"><span class=\"yjm_title\">Blog</span></span></a></span></li><li class=\"haschild item321 level0\"><span class=\"child\"><a class=\"yjanchor \" href=\"/contact-us\"><span class=\"yjm_has_none\"><span class=\"yjm_title\">Contact Us</span></span></a></span><ul class=\"subul_main level1\"><li class=\"bl\"></li><li class=\"tl\"></li><li class=\"tr\"></li><li class=\"item335 level1 first lilast\"><a class=\"yjanchor  firstlast\" href=\"https://www.academiaerp.com/contact/request-demo\" target=\"_blank\" ><span class=\"yjm_has_none\"><span class=\"yjm_title\">Request Demo</span></span></a></li><li class=\"right\"></li><li class=\"br\"></li></ul></li></ul></div>\n" +
            "      </div>\n" +
            "  </div>\n" +
            "   </div>\n" +
            "<!-- end top menu -->\n" +
            "</div>\n" +
            "  <!-- end header -->\n" +
            "\t\t\t\t\t\t\t\t<div id=\"yjsg2\" class=\"yjsg_grid\"><div id=\"adv1\" class=\"yjsgxhtml only_mod\"><div class=\"yjsquare modid121\"><div class=\"yjsquare_in\">\n" +
            "<script type=\"text/javascript\">\n" +
            "\n" +
            "  \n" +
            "    jQuery(document).ready(function() {\n" +
            "\n" +
            "      jQuery('#slider-14577 .layout-effect').addClass(\"favhide\").viewportCheckerfavslider({\n" +
            "        classToAdd: 'favshow layout-effect1', // Class to add to the elements when they are visible\n" +
            "        offset: 0,\n" +
            "      });\n" +
            "\n" +
            "    });\n" +
            "\n" +
            "  \n" +
            "  jQuery(window).load(function() {\n" +
            "    jQuery('.favslider').animate({\n" +
            "      opacity: 1,\n" +
            "    }, 1500 );\n" +
            "  });\n" +
            "\n" +
            "</script>\n" +
            "\n" +
            "<!--[if (IE 7)|(IE 8)]><style type= text/css>.fav-control-thumbs li {width: 24.99%!important;}</style><![endif]-->\n" +
            "\n" +
            "<script type=\"text/javascript\">\n" +
            "  jQuery(window).load(function(){\n" +
            "    jQuery('#slider-14577').favslider({\n" +
            "\t   animation: \"slide\",\n" +
            "\t   directionNav: 1,\n" +
            "\t   keyboardNav: 1,\n" +
            "\t   mousewheel: 0,\n" +
            "\t   slideshow: 1,\n" +
            "\t   slideshowSpeed: 7000,\n" +
            "\t   randomize: 0,\n" +
            "\t   animationLoop: 1,\n" +
            "\t   pauseOnHover: 1,controlNav: 1,\n" +
            "      start: function(slider){\n" +
            "        jQuery('body').removeClass('loading');\n" +
            "      }\n" +
            "    });\n" +
            "  });\n" +
            "\n" +
            "</script> \n" +
            "<style type=\"text/css\">\n" +
            ".favslider .favs li.fav-slider-main, .favslider .favs li.fav-slider-main iframe, .favslider .favs li.fav-slider-main img { height: 350px!important; }\n" +
            "</style>\n" +
            "\n" +
            "\n" +
            "\n" +
            "\t<div id=\"slider-14577\" class=\"favslider dark-arrows\">\n" +
            "\n" +
            "\t\t<ul class=\"favs\">\n" +
            "\n" +
            "\t\t\t\n" +
            "\t\t    <li class=\"fav-slider-main\">\n" +
            "\n" +
            "            \n" +
            "\t\t    \t  \n" +
            "                <img src=\"/media/favslider/speed.jpg\" alt=\"\" />\n" +
            "\n" +
            "            \n" +
            "            \n" +
            "            \n" +
            "            \n" +
            "                <div id=\"fav-caption\" class=\"visible favalign-left favstyle-default favstyle-bg-dark layout-effect\" style=\"width:; height:;\">\n" +
            "\n" +
            "                                          <h3 class=\"favtitle\" style=\"font-family: Roboto; font-size: ; text-transform: none; padding: ; margin: ;\">\n" +
            "                        Speed                      </h3>\n" +
            "                    \n" +
            "                    \n" +
            "                    \n" +
            "                      \n" +
            "                      \n" +
            "                </div>\n" +
            "\n" +
            "            \n" +
            "            \n" +
            "\t\t    </li>\n" +
            "\n" +
            "      \n" +
            "\t\t    <li class=\"fav-slider-main\">\n" +
            "\n" +
            "            \n" +
            "\t\t    \t  \n" +
            "                <img src=\"/media/favslider/efficiency.jpg\" alt=\"\" />\n" +
            "\n" +
            "            \n" +
            "            \n" +
            "            \n" +
            "            \n" +
            "                <div id=\"fav-caption\" class=\"visible favalign-left favstyle-default favstyle-bg-dark layout-effect\" style=\"width:; height:;\">\n" +
            "\n" +
            "                                          <h3 class=\"favtitle\" style=\"font-family: Roboto; font-size: ; text-transform: none; padding: ; margin: ;\">\n" +
            "                        Efficiency                      </h3>\n" +
            "                    \n" +
            "                    \n" +
            "                    \n" +
            "                      \n" +
            "                      \n" +
            "                </div>\n" +
            "\n" +
            "            \n" +
            "            \n" +
            "\t\t    </li>\n" +
            "\n" +
            "      \n" +
            "\t\t    <li class=\"fav-slider-main\">\n" +
            "\n" +
            "            \n" +
            "\t\t    \t  \n" +
            "                <img src=\"/media/favslider/reliablity.jpg\" alt=\"\" />\n" +
            "\n" +
            "            \n" +
            "            \n" +
            "            \n" +
            "            \n" +
            "                <div id=\"fav-caption\" class=\"visible favalign-left favstyle-default favstyle-bg-dark layout-effect\" style=\"width:; height:;\">\n" +
            "\n" +
            "                                          <h3 class=\"favtitle\" style=\"font-family: Roboto; font-size: ; text-transform: none; padding: ; margin: ;\">\n" +
            "                        Reliablity                      </h3>\n" +
            "                    \n" +
            "                    \n" +
            "                    \n" +
            "                      \n" +
            "                      \n" +
            "                </div>\n" +
            "\n" +
            "            \n" +
            "            \n" +
            "\t\t    </li>\n" +
            "\n" +
            "      \n" +
            "\t\t    <li class=\"fav-slider-main\">\n" +
            "\n" +
            "            \n" +
            "\t\t    \t  \n" +
            "                <img src=\"/media/favslider/optimality.jpg\" alt=\"\" />\n" +
            "\n" +
            "            \n" +
            "            \n" +
            "            \n" +
            "            \n" +
            "                <div id=\"fav-caption\" class=\"visible favalign-left favstyle-default favstyle-bg-dark layout-effect\" style=\"width:; height:;\">\n" +
            "\n" +
            "                                          <h3 class=\"favtitle\" style=\"font-family: Roboto; font-size: ; text-transform: none; padding: ; margin: ;\">\n" +
            "                        Optimality                      </h3>\n" +
            "                    \n" +
            "                    \n" +
            "                    \n" +
            "                      \n" +
            "                      \n" +
            "                </div>\n" +
            "\n" +
            "            \n" +
            "            \n" +
            "\t\t    </li>\n" +
            "\n" +
            "      \n" +
            "\t\t</ul>\n" +
            "\n" +
            "\t</div>\n" +
            "\n" +
            "\n" +
            "</section>\n" +
            "</div></div></div></div>\t\t\t\t\t</div>\n" +
            "\t</div>\n" +
            "\t<!-- end centartop-->\n" +
            "\t\t<div class=\"yjsg3_before\"><div id=\"yjsg3\" class=\"yjsg_grid yjsgsitew\"><div id=\"user1\" class=\"yjsgxhtml only_mod yjsgsfx-OurServices\"><div class=\"yjsquare OurServices modid102\"><div class=\"h2_holder\"><h3 class=\"module_title\"><span class=\"title_split titlesplit0\">Our</span> <span class=\"title_split titlesplit1\">Services</span></h3></div><div class=\"yjsquare_in\"><div class=\"box_wrapp\">\n" +
            "<div class=\"Content-OurServices\">\n" +
            "<h4><span class=\"icon-book\"><input type=\"hidden\" /></span>Educational Software Products</h4>\n" +
            "<p>We are a leading educational software and solutions company, promoted by a dynamic team of erstwhile I-Bankers, US graduates and MBA’s.</p>\n" +
            "<div style=\"padding-left: 60px;\" align=\"center\"><a class=\"rm_content\" href=\"/products\">Read More</a></div>\n" +
            "</div>\n" +
            "<div class=\"Content-OurServices\">\n" +
            "<h4><span class=\"icon-laptop\"><input type=\"hidden\" /></span>Customized Solutions for Education</h4>\n" +
            "<p>At Serosoft, we rise to meet the challenges of a rapidly evolving educational landscape that includes new technologies, new education business models.</p>\n" +
            "<div style=\"padding-left: 60px;\" align=\"center\"><a class=\"rm_content\" href=\"/customized-solutions-for-education\">Read More</a></div>\n" +
            "</div>\n" +
            "<div class=\"Content-OurServices\">\n" +
            "<h4><span class=\"icon-check\"><input type=\"hidden\" /></span>Consultancy for Education and ERP</h4>\n" +
            "<p>At Serosoft, through our experience of engaging with over a 100 educational clients since our inception, we have the big picture understanding.</p>\n" +
            "<div style=\"padding-left: 60px;\" align=\"center\"><a class=\"rm_content\" href=\"/consultancy-for-education-and-erp\">Read More</a></div>\n" +
            "</div>\n" +
            "<div class=\"Content-OurServices\">\n" +
            "<h4><span class=\"icon-globe\"><input type=\"hidden\" /></span>Web Development &amp; Online Marketing</h4>\n" +
            "<p>We provide top class, professional yet affordable website development and online marketing services.</p>\n" +
            "<p> </p>\n" +
            "<div style=\"padding-left: 60px;\" align=\"center\"><a class=\"rm_content\" href=\"/web-development\">Read More</a></div>\n" +
            "</div>\n" +
            "</div></div></div></div></div></div>\t<div class=\"yjsg4_before\"><div id=\"yjsg4\" class=\"yjsg_grid yjsgsitew\"><div id=\"user6\" class=\"yjsgxhtml only_mod yjsgsfx-getSocial\"><div class=\"yjsquare getSocial modid97\"><div class=\"yjsquare_in\"><h1 style=\"color: #e2e1e1; font-size: 220%; text-align: center; margin-top: -25px; margin-bottom: 25px;\" align=\"center\">Our Leading edge educational products</h1>\n" +
            "<div align=\"center\">\n" +
            "<div class=\"mycirclefloat\">\n" +
            "<div class=\"mycirclehold\">\n" +
            "<div class=\"mycircle\">\n" +
            "<div class=\"showtext\"><a href=\"http://www.academiaerp.com/\" target=\"_blank\"><img title=\"Academia - Institute ERP / Campus Management System\" src=\"/images/academia.png\" alt=\"\" width=\"180px;\" border=\"0\" /></a></div>\n" +
            "</div>\n" +
            "</div>\n" +
            "</div>\n" +
            "<div class=\"mycirclefloat\">\n" +
            "<div class=\"mycirclehold\">\n" +
            "<div class=\"mycircle\">\n" +
            "<div class=\"showtext\"><a href=\"http://www.web-guru.in/\" target=\"_blank\"><img title=\"Webguru - Learning Management System\" src=\"/images/webGuruLogo.png\" alt=\"\" width=\"180px;\" border=\"0\" /></a></div>\n" +
            "</div>\n" +
            "</div>\n" +
            "</div>\n" +
            "<div class=\"mycirclefloat\">\n" +
            "<div class=\"mycirclehold\">\n" +
            "<div class=\"mycircle\">\n" +
            "<div class=\"showtext\"><a href=\"http://www.alumnee.in/\" target=\"_blank\"><img title=\"Alumnee - Alumni Management System\" src=\"/images/alumnee.png\" alt=\"\" width=\"180px;\" border=\"0\" /></a></div>\n" +
            "</div>\n" +
            "</div>\n" +
            "</div>\n" +
            "<div class=\"mycirclefloat\">\n" +
            "<div class=\"mycirclehold\">\n" +
            "<div class=\"mycircle\">\n" +
            "<div class=\"showtext\"><a href=\"http://www.testmaster.in/\" target=\"_blank\"><img title=\"Testmaster - Online Assessment System\" src=\"/images/campus.png\" alt=\"\" width=\"180px;\" border=\"0\" /></a></div>\n" +
            "</div>\n" +
            "</div>\n" +
            "</div>\n" +
            "<div class=\"mycirclefloat\">\n" +
            "<div class=\"mycirclehold\">\n" +
            "<div class=\"mycircle\">\n" +
            "<div class=\"showtext\"><a href=\"http://www.campushire.in/\" target=\"_blank\"><img title=\"Campushire - Placement Automation System\" src=\"/images/campusHireLog.png\" alt=\"\" width=\"180px;\" border=\"0\" /></a></div>\n" +
            "</div>\n" +
            "</div>\n" +
            "</div>\n" +
            "<div> </div>\n" +
            "</div></div></div></div></div></div>\t\t<div id=\"centerbottom\" class=\"yjsgsitew\">\n" +
            "\t\t<!--MAIN LAYOUT HOLDER -->\n" +
            "<div id=\"holder2\" class=\"holders\">\n" +
            "\t<!-- messages -->\n" +
            "\t<div class=\"yjsg-system-msg\">\n" +
            "\t\t\n" +
            "\t</div>\n" +
            "\t<!-- end messages -->\n" +
            "\t\t<!-- MID BLOCK -->\n" +
            "\t<div id=\"midblock\" class=\"sidebars sidebar-main\">\n" +
            "\t\t<div class=\"insidem\">\n" +
            "\t\t\t\t\t\t\t\t\t<!-- component -->\n" +
            "\t\t\t<div class=\"yjsg-newsitems\">\n" +
            "\t<div class=\"yjsg-blog_f\">\n" +
            "\t\t\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\t\t\n" +
            "\t\t\t</div>\n" +
            "</div>\n" +
            "\t\t\t<!-- end component -->\n" +
            "\t\t\t\t\t\t\t\t\t<div class=\"clearm\"></div>\n" +
            "\t\t</div>\n" +
            "\t\t<!-- end mid block insidem class -->\n" +
            "\t</div>\n" +
            "\t<!-- end mid block div -->\n" +
            "\t\t\t\t\t\t</div>\n" +
            "<!-- end holder div -->\n" +
            "\t\t\t</div>\n" +
            "\t<!-- end centerbottom-->\n" +
            "\t\t\n" +
            "\t<div class=\"yjsg5_before\"><div id=\"yjsg5\" class=\"yjsg_grid yjsgsitew\"><div id=\"user11\" class=\"yjsgxhtml only_mod yjsgsfx-yjme team-sample\"><div class=\"yjsquare  yjme team-sample modid104\"><div class=\"h2_holder\"><h3 class=\"module_title\"><span class=\"title_split titlesplit0\">Meet</span> <span class=\"title_split titlesplit1\">The</span> <span class=\"title_split titlesplit2\">Team</span></h3></div><div class=\"yjsquare_in\"><!-- Powered by YJ Module Engine find out more at www.youjoomla.com -->\n" +
            "<div class=\"yjme_holder\">\n" +
            "    <div class=\"yjme_item\">\n" +
            "    <div class=\"yjme_item_in yjmeitem119\">\n" +
            "            <div class=\"imageholder\" style=\"width:208px;height:208px;float:none;\">\n" +
            "        <a class=\"item_image\"  style=\"width:208px;height:208px;\" href=\"/97-meet-the-team/119-arpit-badjatya-ceo-md\" >\n" +
            "          <img src=\"/images/Arpit.JPG\" alt=\"Arpit Badjatya (CEO &amp; MD)\" />\n" +
            "        </a>\n" +
            "      </div>\n" +
            "                  <a class=\"item_title\" href=\"/97-meet-the-team/119-arpit-badjatya-ceo-md\">\n" +
            "       Arpit Badjatya (CEO &amp; MD)      </a>\n" +
            "                  <p class=\"item_intro\">\n" +
            "        \n" +
            "http://www.linkedin.com/in/arpitbadjatya\n" +
            "      </p>\n" +
            "                        <a class=\"item_readmore\" href=\"/97-meet-the-team/119-arpit-badjatya-ceo-md\">\n" +
            "        <span class=\" icon-share\">\n" +
            "        </span>\n" +
            "      </a>\n" +
            "          </div>\n" +
            "  </div>\n" +
            "    <div class=\"yjme_item\">\n" +
            "    <div class=\"yjme_item_in yjmeitem118\">\n" +
            "            <div class=\"imageholder\" style=\"width:208px;height:208px;float:none;\">\n" +
            "        <a class=\"item_image\"  style=\"width:208px;height:208px;\" href=\"/97-meet-the-team/118-ashok-badjatiya-chairman-hindustan-group\" >\n" +
            "          <img src=\"/images/ashokji.png\" alt=\"Ashok Badjatiya (Chairman, Hindustan Group)\" />\n" +
            "        </a>\n" +
            "      </div>\n" +
            "                  <a class=\"item_title\" href=\"/97-meet-the-team/118-ashok-badjatiya-chairman-hindustan-group\">\n" +
            "       Ashok Badjatiya (Chairman, Hindustan Group)      </a>\n" +
            "                  <p class=\"item_intro\">\n" +
            "        \n" +
            "http://www.linkedin.com/pub/ashok-badjatiya/18/791/947\n" +
            "      </p>\n" +
            "                        <a class=\"item_readmore\" href=\"/97-meet-the-team/118-ashok-badjatiya-chairman-hindustan-group\">\n" +
            "        <span class=\" icon-share\">\n" +
            "        </span>\n" +
            "      </a>\n" +
            "          </div>\n" +
            "  </div>\n" +
            "    <div class=\"yjme_item\">\n" +
            "    <div class=\"yjme_item_in yjmeitem115 last\">\n" +
            "            <div class=\"imageholder\" style=\"width:208px;height:208px;float:none;\">\n" +
            "        <a class=\"item_image\"  style=\"width:208px;height:208px;\" href=\"/97-meet-the-team/115-siddharth-badjatiya-coo-director\" >\n" +
            "          <img src=\"/images/sid.png\" alt=\"Siddharth Badjatiya (COO &amp; Director)\" />\n" +
            "        </a>\n" +
            "      </div>\n" +
            "                  <a class=\"item_title\" href=\"/97-meet-the-team/115-siddharth-badjatiya-coo-director\">\n" +
            "       Siddharth Badjatiya (COO &amp; Director)      </a>\n" +
            "                  <p class=\"item_intro\">\n" +
            "        \n" +
            "http://www.linkedin.com/in/siddharthbadjatiya\n" +
            "      </p>\n" +
            "                        <a class=\"item_readmore\" href=\"/97-meet-the-team/115-siddharth-badjatiya-coo-director\">\n" +
            "        <span class=\" icon-share\">\n" +
            "        </span>\n" +
            "      </a>\n" +
            "          </div>\n" +
            "  </div>\n" +
            "  </div></div></div></div></div></div>\t<div class=\"yjsg6_before\"><div id=\"yjsg6\" class=\"yjsg_grid yjsgsitew\"><div id=\"user16\" class=\"yjsgxhtml only_mod\"><div class=\"yjsquare modid120\"><div class=\"h2_holder\"><h3 class=\"module_title\">Testimonials</h3></div><div class=\"yjsquare_in\"><div style=\"text-align:center\" id=\"btcontentslider120\" style=\"display:none;width:auto\" class=\"bt-cs\">\n" +
            "\t\t<a class=\"prev\" href=\"#\">Prev</a><a class=\"next\" href=\"#\">Next</a> \n" +
            "\t\t\t<div class=\"slides_container\" style=\"width:auto;\">\n" +
            "\n" +
            "\t\t\t<div class=\"slide\" style=\"width:auto\">\n" +
            "\t\t\t\t\t<div class=\"bt-row bt-row-first\"  style=\"width:100%\" >\n" +
            "\t\t\t\t<div class=\"bt-inner\">\n" +
            "\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-center\" style=\"text-align:center;\">\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t<img class=\"hovereffect\" src=\"http://www.serosoft.in/cache/mod_bt_contentslider/b59e9d811db906cd5e39e24efd14f150-Testimonial-EDI-Ahmedabad.png\" alt=\"Enterepreneurship Development Institute of India\"  style=\"width:70px;\" title=\"Enterepreneurship Development Institute of India\" /><br/><br/>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-introtext\">\n" +
            "\t\t\t\t\t\n" +
            "\n" +
            "We wish to extend our sincerest appreciation to Serosoft. As a team, we found Serosoft to be very responsive to our requirements. Their implementation team deployed with us worked hard to meet our objectives and deliverables. Serosoft was flexible in accommodating our varied need for institute's automationrequirement and management styles, A word of appreciation is also due to their client interfacing skills. We found them to be good listeners as well as balanced responders. We were regularly updated of the work progress. The Serosoft team has become a valuable extension of EDI team and we look forward to additional opportunities to work together. With regard to the product- Academia ERP, it is with pleasure that we recommend Academia to any educational institute lookingto enhance efficiency and introduce automation. Academia ERP is a robust and feature-rich platform incorporating the innovative  Technologies of today , and we are reaping efficiency and cost benefits on account of Academia implementation at our institute.\n" +
            " \n" +
            " \n" +
            " \n" +
            "~ Enterepreneurship Development Institute India\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t</div>\n" +
            "\t\t\t\t<!--end bt-inner -->\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<!--end bt-row -->\n" +
            "\t\t\t\t\t\t\t\t\t<div style=\"clear: both;\"></div>\n" +
            "\n" +
            "\t\t</div>\n" +
            "\t\t<!--end bt-main-item page\t-->\n" +
            "\t\t\t\t<div class=\"slide\" style=\"width:auto\">\n" +
            "\t\t\t\t\t<div class=\"bt-row bt-row-first\"  style=\"width:100%\" >\n" +
            "\t\t\t\t<div class=\"bt-inner\">\n" +
            "\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-center\" style=\"text-align:center;\">\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t<img class=\"hovereffect\" src=\"http://www.serosoft.in/cache/mod_bt_contentslider/4de4a7eda2a7f12b9f2834fe2f5ca0b9-jecrc.png\" alt=\"ERP-Recommendation-JECRC\"  style=\"width:70px;\" title=\"ERP-Recommendation-JECRC\" /><br/><br/>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-introtext\">\n" +
            "\t\t\t\t\t\n" +
            "\n" +
            "We are happy to state that Serosoft has been associated with us for past One year for our ERP implementation. We are impressed with the energy and dedication of Team Serosoft, and find them to be competent, knowledgeable and industrious. Academia ERP ia a product replete with features that capturethe essence of the processes in an educational organization, thereby helping us to meet our automation and streamlining objectives. We recommend Academia ERP and Serosoft - without reservation - to educational institutes looking to take the automation plunge. Best wishes to Serosoft.\n" +
            " \n" +
            " \n" +
            "~ JECRC University India\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t</div>\n" +
            "\t\t\t\t<!--end bt-inner -->\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<!--end bt-row -->\n" +
            "\t\t\t\t\t\t\t\t\t<div style=\"clear: both;\"></div>\n" +
            "\n" +
            "\t\t</div>\n" +
            "\t\t<!--end bt-main-item page\t-->\n" +
            "\t\t\t\t<div class=\"slide\" style=\"width:auto\">\n" +
            "\t\t\t\t\t<div class=\"bt-row bt-row-first\"  style=\"width:100%\" >\n" +
            "\t\t\t\t<div class=\"bt-inner\">\n" +
            "\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-center\" style=\"text-align:center;\">\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t<img class=\"hovereffect\" src=\"http://www.serosoft.in/cache/mod_bt_contentslider/22e106e376424e844142a0d2a8365c58-isbm-tmn.png\" alt=\"lndian School of Business Management &amp; Administration.\"  style=\"width:70px;\" title=\"lndian School of Business Management &amp; Administration.\" /><br/><br/>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-introtext\">\n" +
            "\t\t\t\t\t\n" +
            "\n" +
            "This letter is written to express our satisfaction and convey our highest recommendation for the Academia ERP software and services provided by Serosoft to support our process and functional automation objectives at ISBM. Serosoft provided Academia with state-of-the-art technology to manage the complex and dynamic needs of our growing organization, particularly in the areas of admissions, fees management, course progression, inventory management and student information management.The versatile Academia ERP is an outstanding platform that we customized to meet our particular needs.Serosoft quickly adapted to our expectations by providing the best customer support I have ever experienced. They were always cheerful, easy to contact, and provided solutions to our needs in quick time. This responsiveness exemplifies their dedication to customer satisfaction and their exceptional technical abilities. All in all, Academia ERP is a top quality product that delivers exceptional value at a competitive price. I highly recommend Serosoft and their Academia ERP for all your education management and automation needs.\n" +
            "~ lndian School of Business Management &amp; Administration.\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t</div>\n" +
            "\t\t\t\t<!--end bt-inner -->\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<!--end bt-row -->\n" +
            "\t\t\t\t\t\t\t\t\t<div style=\"clear: both;\"></div>\n" +
            "\n" +
            "\t\t</div>\n" +
            "\t\t<!--end bt-main-item page\t-->\n" +
            "\t\t\t\t<div class=\"slide\" style=\"width:auto\">\n" +
            "\t\t\t\t\t<div class=\"bt-row bt-row-first\"  style=\"width:100%\" >\n" +
            "\t\t\t\t<div class=\"bt-inner\">\n" +
            "\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-center\" style=\"text-align:center;\">\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t<img class=\"hovereffect\" src=\"http://www.serosoft.in/cache/mod_bt_contentslider/7f1ac95619174551e45e45e96c779063-mits.png\" alt=\"Mahaveer Institute of Technology &amp; Science\"  style=\"width:70px;\" title=\"Mahaveer Institute of Technology &amp; Science\" /><br/><br/>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-introtext\">\n" +
            "\t\t\t\t\t\n" +
            "\n" +
            "It gives me immense pleasure to write this testimonial and appreciate Academia. ERP and the efforts put in by Serosoft Team. A seamlessly integrated solution like Academia ERP is much essential for an Institution like ours. The team has been much supportive, enabling us to A streamline our process and making it efficient at the same time. The solution also provides us with plethora of reports which helps us, especially with the academic activities and gives us a 360 degree overview. Academia’s ability to get seamless &amp; tight integration with Finance module also helps accounting with ease and high level of transparency. Moreover, the reason which made sure that Academia would be a perfect for MITS is that the product could be transformed and customized as per our requirements.\n" +
            " \n" +
            " \n" +
            " \n" +
            " \n" +
            "~ Pravesh Jain (Director)\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t</div>\n" +
            "\t\t\t\t<!--end bt-inner -->\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<!--end bt-row -->\n" +
            "\t\t\t\t\t\t\t\t\t<div style=\"clear: both;\"></div>\n" +
            "\n" +
            "\t\t</div>\n" +
            "\t\t<!--end bt-main-item page\t-->\n" +
            "\t\t\t\t<div class=\"slide\" style=\"width:auto\">\n" +
            "\t\t\t\t\t<div class=\"bt-row bt-row-first\"  style=\"width:100%\" >\n" +
            "\t\t\t\t<div class=\"bt-inner\">\n" +
            "\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-center\" style=\"text-align:center;\">\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t<img class=\"hovereffect\" src=\"http://www.serosoft.in/cache/mod_bt_contentslider/fd478cef9454d494e27aba6f1299ea58-logo_oxford.jpg\" alt=\"Oxford School\"  style=\"width:70px;\" title=\"Oxford School\" /><br/><br/>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-introtext\">\n" +
            "\t\t\t\t\t\n" +
            "We at The Oxford School, Dubai, UAE have used the services of Serosoft Solutions Pvt.Ltd to support on line learning modules for our students through Webguru.They have a highly functional team who accomplished quality work in a short time . The team ofSerosoft has successfully completed the training to our teachers, IT staff and administrators for the easy use of their educational support software. The skill to bridge the gap between the technical and  the functional aspects made the team an excellent facilitator to our staff.We have always been pleased with the results they have given to our Students and Teachers while using their IT related services. Serosoft did offer several option with both pros and cons.This enabled our management to make informed decisions with regard to customizations. Their quality of work consistently exceeded our exception.From start to finish, Serosoft's approach, professionalism, and expertise was without fault. We would certainly hire them as we have found them trustworthy and we highly recommend their services.\n" +
            " \n" +
            "~ Oxford School, Dubai\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t</div>\n" +
            "\t\t\t\t<!--end bt-inner -->\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<!--end bt-row -->\n" +
            "\t\t\t\t\t\t\t\t\t<div style=\"clear: both;\"></div>\n" +
            "\n" +
            "\t\t</div>\n" +
            "\t\t<!--end bt-main-item page\t-->\n" +
            "\t\t\t\t<div class=\"slide\" style=\"width:auto\">\n" +
            "\t\t\t\t\t<div class=\"bt-row bt-row-first\"  style=\"width:100%\" >\n" +
            "\t\t\t\t<div class=\"bt-inner\">\n" +
            "\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-center\" style=\"text-align:center;\">\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t<img class=\"hovereffect\" src=\"http://www.serosoft.in/cache/mod_bt_contentslider/a957028b2cdbf7a44c2c675799499041-technonjr.png\" alt=\"Techno India NJR Institute\"  style=\"width:70px;\" title=\"Techno India NJR Institute\" /><br/><br/>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-introtext\">\n" +
            "\t\t\t\t\t\n" +
            "\n" +
            "Our institute started using Academia ERP in 2012. Most of our faculty have found it very useful. Academia ERP features for students, teachers, officers, management have uniformly organized all our students records/data beginning with registration, to the administrators offices, activities and of course all the different departments. Everyone is on the same page I have not come across many thing that we require that Academia ERP cannot do. The support staff at Serosoft is efficient and accommodating. I can get support from them whenever required. Their email reply time is also quick. Academia ERP is feature-rich, stable, user friendly.\n" +
            " \n" +
            "~ Techno India NJR Institute India\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t</div>\n" +
            "\t\t\t\t<!--end bt-inner -->\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<!--end bt-row -->\n" +
            "\t\t\t\t\t\t\t\t\t<div style=\"clear: both;\"></div>\n" +
            "\n" +
            "\t\t</div>\n" +
            "\t\t<!--end bt-main-item page\t-->\n" +
            "\t\t\t\t<div class=\"slide\" style=\"width:auto\">\n" +
            "\t\t\t\t\t<div class=\"bt-row bt-row-first\"  style=\"width:100%\" >\n" +
            "\t\t\t\t<div class=\"bt-inner\">\n" +
            "\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-center\" style=\"text-align:center;\">\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\t<img class=\"hovereffect\" src=\"http://www.serosoft.in/cache/mod_bt_contentslider/f71af852ee25dfed778e7b2c3ccd1214-logo_apple.jpg\" alt=\"The Apple International School\"  style=\"width:70px;\" title=\"The Apple International School\" /><br/><br/>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\t\t\t\t\t\n" +
            "\t\t\t\t\t\t\t\t\t\t<div class=\"bt-introtext\">\n" +
            "\t\t\t\t\t\n" +
            "\n" +
            "We have been associated with Serosoft as one of our software solution providers for our school They have implemented Webguru and other related services for the benefit of our students and for interaction betweenTeachers, Students and Parents. It is in Operation Successfully and we have an understanding for a long term use of their services.We found the team behind the company is most professional dynamic and approachable whenever in need. Thetraining imparted by them to our teachers and staff is very useful for the product understanding which also made the day to day use easier. We thought use after safe service in one of the most outstanding approach in our association and the term is very cooperative in giving us solutions as and when required . Their frequent visit so our campus made easier forus to inter act with them w.r.t our future requirements.We have no invitation whatever to recommend Serosoft Solution Pvt Ltd for your IT Services and Software products\n" +
            " \n" +
            "~ The Apple International School\n" +
            "\t\t\t\t\t</div>\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t\t\n" +
            "\t\t\t\t</div>\n" +
            "\t\t\t\t<!--end bt-inner -->\n" +
            "\t\t\t</div>\n" +
            "\t\t\t<!--end bt-row -->\n" +
            "\t\t\t\t\t\t\t\t\t<div style=\"clear: both;\"></div>\n" +
            "\n" +
            "\t\t</div>\n" +
            "\t\t<!--end bt-main-item page\t-->\n" +
            "\t\t\t</div>\n" +
            "</div>\n" +
            "<!--end bt-container -->\n" +
            "<div style=\"clear: both;\"></div>\n" +
            "\n" +
            "<script type=\"text/javascript\">\t\n" +
            "\tif(typeof(btcModuleIds)=='undefined'){var btcModuleIds = new Array();var btcModuleOpts = new Array();}\n" +
            "\tbtcModuleIds.push(120);\n" +
            "\tbtcModuleOpts.push({\n" +
            "\t\t\tslideEasing : 'easeInQuad',\n" +
            "\t\t\tfadeEasing : 'easeInQuad',\n" +
            "\t\t\teffect: 'slide,slide',\n" +
            "\t\t\tpreloadImage: 'http://www.serosoft.in//modules/mod_bt_contentslider/tmpl/images/loading.gif',\n" +
            "\t\t\tgeneratePagination: true,\n" +
            "\t\t\tplay: 5000,\t\t\t\t\t\t\n" +
            "\t\t\thoverPause: true,\t\n" +
            "\t\t\tslideSpeed : 500,\n" +
            "\t\t\tautoHeight:true,\n" +
            "\t\t\tfadeSpeed : 500,\n" +
            "\t\t\tequalHeight:true,\n" +
            "\t\t\twidth: 'auto',\n" +
            "\t\t\theight: 'auto',\n" +
            "\t\t\tpause: 100,\n" +
            "\t\t\tpreload: true,\n" +
            "\t\t\tpaginationClass: 'bt_handles',\n" +
            "\t\t\tgenerateNextPrev:false,\n" +
            "\t\t\tprependPagination:true,\n" +
            "\t\t\ttouchScreen:1\t});\n" +
            "</script>\n" +
            "\n" +
            "</div></div></div></div></div>\t\n" +
            "\t\t\n" +
            "\t<div class=\"yjsg7_before\"><div id=\"yjsg7\" class=\"yjsg_grid yjsgsitew\"><div id=\"user21\" class=\"yjsgxhtml first_mod yjsgsfx-Links\"><div class=\"yjsquare Links modid110\"><div class=\"h2_holder\"><h3 class=\"module_title\"><span class=\"title_split titlesplit0\">Media</span> <span class=\"title_split titlesplit1\">Coverage</span> <span class=\"title_split titlesplit2\"></span></h3></div><div class=\"yjsquare_in\"><p> </p>\n" +
            "<div style=\"float: left;\">\n" +
            "<ul class=\"disc\">\n" +
            "<li><a href=\"/serosoft-academia-on-et-now\">Serosoft &amp; Academia on ET NOW</a></li>\n" +
            "<li><a href=\"/serosoft-academia-on-et-now\">Serosoft Management Team</a></li>\n" +
            "<li><a href=\"/serosoft-academia-on-et-now\">Education Times (TOI) covers the story of inception of Serosoft</a></li>\n" +
            "</ul>\n" +
            "<a title=\"Read more\" href=\"http://www.serosoft.in/serosoft-academia-on-et-now\"><span class=\"icon-share\" style=\"display: block; height: 20px; line-height: 1em; padding: 10px 10px 0px 0px; text-align: center; font-size: 20px;\"><input type=\"hidden\" /></span></a></div></div></div></div><div id=\"user22\" class=\"yjsgxhtml\"><div class=\"yjsquare modid111\"><div class=\"h2_holder\"><h3 class=\"module_title\"><span class=\"title_split titlesplit0\">Follow</span> <span class=\"title_split titlesplit1\">us</span></h3></div><div class=\"yjsquare_in\"><div style=\"float: left; padding-left: 130px; font-size: 200%;\" align=\"center\">\n" +
            "<h4><a href=\"https://www.facebook.com/serosoft?_rdr\" target=\"_blank\"><span class=\"icon-facebook-sign\"><input type=\"hidden\" /></span></a></h4>\n" +
            "</div>\n" +
            "<div style=\"float: left; padding-left: 15px; font-size: 200%;\" align=\"center\">\n" +
            "<h4><a href=\"http://www.linkedin.com/company/serosoft-solutions\" target=\"_blank\"><span class=\"icon-linkedin-sign\"><input type=\"hidden\" /> </span></a></h4>\n" +
            "</div>\n" +
            "<div style=\"float: left; padding-left: 15px; font-size: 200%;\" align=\"center\">\n" +
            "<h4><a href=\"https://twitter.com/academiaerp\" target=\"_blank\"><span class=\"icon-twitter-sign\"><input type=\"hidden\" /></span></a></h4>\n" +
            "</div>\n" +
            "<div style=\"float: left; padding-left: 15px; font-size: 200%;\" align=\"center\">\n" +
            "<h4><a href=\"https://plus.google.com/106042884298647483187\" target=\"_blank\"><span class=\"icon-google-plus-sign\"><input type=\"hidden\" /></span></a></h4>\n" +
            "</div></div></div></div><div id=\"user23\" class=\"yjsgxhtml last_mod lastModule\"><div class=\"yjsquare modid117\"><div class=\"h2_holder\"><h3 class=\"module_title\"><span class=\"title_split titlesplit0\">About</span> <span class=\"title_split titlesplit1\">Serosoft</span></h3></div><div class=\"yjsquare_in\"><p class=\"my1\">We are a leading educational software and solutions company, promoted by a dynamic team of erstwhile I-Bankers, US graduates and MBA’s. We are part of the prestigious 50-year old Hindustan Group of Companies (<a href=\"http://www.hindustangroup.com\" target=\"_blank\">www.hindustangroup.com</a>). Our out-of-the-box and customized solutions are helping scores of institutions and corporates - globally- overcome their educational and learning challenges and to drive innovation.</p></div></div></div></div></div>\t<div class=\"footer_holders footer\"><!-- footer -->\n" +
            "<div id=\"footer\" class=\"yjsgsitew\">\n" +
            "  <div id=\"youjoomla\">\n" +
            "        \t<div id=\"cp\">\n" +
            "\t\t<div width=\"40%\" style=\"float:left\">Copyright &copy; <span> 2016 <a href=\"http://www.serosoft.in\">Serosoft Solutions Pvt Ltd</a></span></div><div width=\"40%\" style=\"float:right\"><div style=\"float: left;\">\n" +
            "<ul class=\"disc\">\n" +
            "<li style=\"display: inline; list-style-type: none; padding-right: 10px;\"><a href=\"http://www.serosoft.in/\">Home</a></li>\n" +
            "<li style=\"display: inline; list-style-type: none; padding-right: 10px;\"><a href=\"/company\">Company</a></li>\n" +
            "<li style=\"display: inline; list-style-type: none; padding-right: 10px;\"><a href=\"/team\">Team</a></li>\n" +
            "<li style=\"display: inline; list-style-type: none; padding-right: 10px;\"><a href=\"/why-us\">Why Us</a></li>\n" +
            "<li style=\"display: inline; list-style-type: none; padding-right: 10px;\"><a href=\"/services\">Services</a></li>\n" +
            "<li style=\"display: inline; list-style-type: none; padding-right: 10px;\"><a href=\"/products\">Products</a></li>\n" +
            "<li style=\"display: inline; list-style-type: none; padding-right: 10px;\"><a href=\"/careers\">Careers</a></li>\n" +
            "<li style=\"display: inline; list-style-type: none; padding-right: 10px;\"><a href=\"/contact-us\">Contact Us</a></li>\n" +
            "</ul>\n" +
            "</div></span></div><div width=\"100%\" align=\"center\"><p align=\"center\">An ISO 9001 : 2008 Company<br /><span style=\"margin-left: -275px;\">Part of the <a href=\"http://www.hindustangroup.com\">Hindustan Group</a>.</span></p></span></div>\t\t\t\n" +
            "       </div>\n" +
            "  </div>\n" +
            "</div>\n" +
            "<!-- end footer -->\n" +
            "\t\n" +
            "<script type=\"text/javascript\"> var logo_w = '240'; var site_w = '1200'; var site_f = '13px'; var sp='http://www.serosoft.in/'; var tp ='baseline'; var compileme =0; var fontc ='baseline_78701475155302'; var bootstrapv='bootstrap3'; var yver='3'; var yjsglegacy='1'; var yjsgrtl='2'; var menuanimation='fade';var menuanimationspeed=300; var YJSG_topmenu_font = '13px'; (function($){ $(window).load(function(){ $('.horiznav').SmoothDropJQ({ contpoz:0, horizLeftOffset: 20, horizRightOffset: -20, horizTopOffset: 20, verticalTopOffset:30, verticalLeftOffset: 10, maxOutside: 50 }); }) })(jQuery); </script>\n" +
            "</div>\t\t<div id=\"mmenu_holder\">\n" +
            "  <span class=\"yjmm_select\" id=\"yjmm_selectid\">Home</span>\n" +
            "  <select id=\"mmenu\" class=\"yjstyled\">\n" +
            "            <option value=\"/\" selected=\"selected\">&nbsp;Home</option>\n" +
            "            <option value=\"/company\">&nbsp;Company</option>\n" +
            "            <option value=\"/team\">&nbsp;Team</option>\n" +
            "            <option value=\"/team/board-of-advisors\">&nbsp;--Board of Advisors</option>\n" +
            "            <option value=\"/why-us\">&nbsp;WHY US</option>\n" +
            "            <option value=\"/services\">&nbsp;Services</option>\n" +
            "            <option value=\"/products\">&nbsp;Products</option>\n" +
            "            <option value=\"/careers\">&nbsp;Careers</option>\n" +
            "            <option value=\"/careers/work-life-at-serosoft\">&nbsp;--Work-Life at Serosoft</option>\n" +
            "            <option value=\"/blog\">&nbsp;Blog</option>\n" +
            "            <option value=\"/contact-us\">&nbsp;Contact Us</option>\n" +
            "            <option value=\"https://www.academiaerp.com/contact/request-demo\">&nbsp;--Request Demo</option>\n" +
            "        </select>\n" +
            "</div>\t\t\t\n" +
            "\t\n" +
            "\t <!-- /* <div align=\"middle\" style=\"width: 100%; float:middle; font-size:11px;\"><p align=\"center\">An ISO 9001 : 2008 Company<br /><span style=\"margin-left: -275px;\">Part of the <a href=\"http://www.hindustangroup.com\">Hindustan Group</a>.</span></p></div>*/-->\n" +
            "\t <div class=\"but-campus\" style=\"bottom: 170px;position: fixed;right: 0px;z-index: 999999;\">\n" +
            "        <a href=\"https://www.academiaerp.com/request-demo\" target=\"_blank\">\n" +
            "          <img src=\"http://serosoft.in/images/request_demo.jpg\" alt=\"\">\n" +
            "        </a>\n" +
            "        <div style=\"padding-top:10px;\">&nbsp;</div>\n" +
            "        <a href=\"/careers\">\n" +
            "          <img src=\"http://serosoft.in/images/apply_to_serosoft.jpg\" alt=\"\">\n" +
            "        </a>\n" +
            "      </div>\n" +
            "\t \n" +
            "\t \n" +
            "</body>\n" +
            "</html>";

}
