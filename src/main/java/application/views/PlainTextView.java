package application.views;

import application.models.TitleText;

public class PlainTextView extends ResponseBodyBuilder {

    private final TitleText text;

    public PlainTextView(String text) {
        this.text = TitleText.parse(text);
    }

    @Override
    protected void viewResponseBody(StringBuilder htmlBuilder) {
        for (var line : text) {
            if (!line.isBlank())
                htmlBuilder.append(paragraph(line.text()));
        }
    }

}