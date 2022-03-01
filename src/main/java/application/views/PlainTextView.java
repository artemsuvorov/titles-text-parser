package application.views;

public class PlainTextView extends ResponseBodyBuilder {

    private final String text;

    public PlainTextView(String text) {
        this.text = text;
    }

    @Override
    protected void viewResponseBody(StringBuilder htmlBuilder) {
        var lines = text.split("(\r\n|\n)");
        for (var line : lines) {
            if (line.isBlank())
                htmlBuilder.append(br());
            else
                htmlBuilder.append(paragraph(line));
        }
    }

}