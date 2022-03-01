package application.views;

public abstract class ResponseBodyBuilder {

    public String buildResponseBody() {
        var htmlBuilder = new StringBuilder();
        htmlBuilder.append(beginHtml());

        viewResponseBody(htmlBuilder);

        htmlBuilder.append(endHtml());
        return htmlBuilder.toString();
    }

    protected abstract void viewResponseBody(StringBuilder htmlBuilder);

    protected String beginHtml() {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Titles Text Parser</title>
                </head>
                <body>
                """;
    }

    protected String endHtml() {
        return """
                </body>
                </html>""";
    }

    protected String paragraph(String text) {
        return tag("p", text);
    }

    protected String header(int level, String text, String id) {
        var clamped = Math.max(1, Math.min(level, 6));
        return tag("h" + clamped, text, id);
    }

    protected String header(int level, String text) {
        return header(level, text, "");
    }

    protected String br() {
        return "<br/>";
    }

    protected String underscore(String text) {
        return text.replaceAll("\s+", "_");
    }

    private String tag(String name, String text, String id) {
        var attr = id.isBlank() ? "" : " id=\"" + id + "\"";
        return "<" + name + attr + ">" + text + "</" + name + ">";
    }

    private String tag(String name, String text) {
        return tag(name, text, "");
    }

}
