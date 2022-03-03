package application.views;

import application.models.TextHeader;
import application.models.TextLine;
import application.models.TitleText;

public final class TitleTextView extends ResponseBodyBuilder {

    private final TitleText text;

    public TitleTextView(TitleText text) {
        this.text = text;
    }

    @Override
    protected void viewResponseBody(StringBuilder htmlBuilder) {
        navigation(htmlBuilder);
        for (var line : text) {
            htmlBuilder.append(line(line));
        }
    }

    private String line(TextLine line) {
        if (line instanceof TextHeader header)
            return header(header.level(), header.text(), underscore(header.text()));
        else if (!line.isBlank())
            return paragraph(line.text());
        else
            return "";
    }

    private void navigation(StringBuilder htmlBuilder) {
        if (!text.hasHeaders()) return;
        htmlBuilder.append(beginNavigation());
        htmlBuilder.append(beginOrderedList());
        populateNavigationItems(htmlBuilder);
        htmlBuilder.append(endOrderedList());
        htmlBuilder.append(endNavigation());
    }

    private void populateNavigationItems(StringBuilder htmlBuilder) {
        TextHeader previous = null;
        for (var line : text) {
            if (line instanceof TextHeader header) {
                htmlBuilder.append(depth(previous, header));
                htmlBuilder.append(navigationItem(header));
                previous = header;
            }
        }
    }

    private String depth(TextHeader previous, TextHeader header) {
        if (previous != null && previous.level() < header.level()) {
            var depth = header.level() - previous.level();
            return beginOrderedList().repeat(depth);
        } else if (previous != null && previous.level() > header.level()) {
            var depth = previous.level() - header.level();
            return endOrderedList().repeat(depth);
        }
        return "";
    }

    private String navigationItem(TextHeader header) {
        return "<li><a href=\"#" + underscore(header.text()) + "\">" + header.text() + "</a></li>\n";
    }

    private String beginNavigation() {
        return """
                <div>
                <div><p2>Navigation</h2></div>
                """;
    }

    private String endNavigation() {
        return "</div>";
    }

    private String beginOrderedList() {
        return "<ol>\n";
    }

    private String endOrderedList() {
        return "</ol>\n";
    }

}