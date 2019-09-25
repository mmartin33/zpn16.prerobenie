/*
 * Decompiled with CFR 0_119.
 */
package sk.zpn.zaklad;

public class Html {
    public static String html4(String title, String content, String css) {
        return "<html><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"><style>body{font-family:Tahoma;} "
                + css + "</style>" + "<head><meta charset=\"UTF-8\"><title>" + title + "</title></head><body>" + content
                + "</body></html>";
    }

    public static String br() {
        return "<br>";
    }

    public static String br(int count) {
        String s = "";
        for (int i = 0; i < count; ++i) {
            s = s + "<br>";
        }
        return s;
    }

    public static String br(String content) {
        return "<br>" + content;
    }

    public static String b(String content) {
        return "<b>" + content + "</b>";
    }

    public static String p(String content) {
        return "<p>" + content + "</p>";
    }

    public static String p(String content, String style) {
        return "<p style='" + style + "'>" + content + "</p>";
    }

    public static String pc(String content, String clazz) {

        return "<p class='" + clazz + "'>" + content + "</p>";
    }

    public static String span(String content) {
        return "<span>" + content + "</span>";
    }

    public static String span(String content, String style) {
        return "<span style='" + style + "'>" + content + "</span>";
    }

    public static String spanc(String content, String clazz) {
        return "<span class='" + clazz + "'>" + content + "</span>";
    }

    public static String div(String content) {
        return "<div>" + content + "</div>";
    }

    public static String div(String content, String style) {
        return "<div style='" + style + "'>" + content + "</div>";
    }

    public static String divc(String content, String clazz) {
        return "<div class='" + clazz + "'>" + content + "</div>";
    }

    public static String h1(String content) {
        return "<h1>" + content + "</h1>";
    }

    public static String h2(String content) {
        return "<h2>" + content + "</h2>";
    }

    public static String h3(String content) {
        return "<h3>" + content + "</h3>";
    }

    public static String ol(String content) {
        return "<ol>" + content + "</ol>";
    }

    public static String ul(String content) {
        return "<ul>" + content + "</ul>";
    }

    public static String li(String content) {
        return "<li>" + content + "</li>";
    }

    public static String ahref(String href, String text) {
        return "<a href=\"" + href + "\" target=\"_blank\">" + text + "</a>";
    }

    public static String ahref(String href, String text, String target) {
        return "<a href=\"" + href + "\" target=\"" + target + "\">" + text + "</a>";
    }

    public static String img(String src, String height, String width) {
        return "<img src=\"" + src + "\" height=\"" + height + "\" width=\"" + width + "\">";
    }

    public static String v(String content) {
        return Html.br(Html.span(content, "font-size:1em;color:yellow;padding-right:10px;"));
    }

    public static String small(String content) {
        return Html.span(content, "font-size: 12px;");
    }

    public static String large(String content) {
        return Html.span(content, "font-size: large;");
    }

    public static String xlarge(String content) {
        return Html.span(content, "font-size: x-large;");
    }

    public static String value(String content) {
        return Html.spanc(content, "value");
    }
}
