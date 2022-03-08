package application.models.serialization;

import application.models.TextHeader;
import application.models.TextLine;
import application.models.TitleText;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class TitleTextJsonSerializer extends JsonSerializer<TitleText> {

    private TextHeader previousHeader = null;
    private int depth = 0;

    private boolean isAddingParagraphs = false;
    private boolean isAddingSubheadings = false;

    @Override
    public void serialize(TitleText text, JsonGenerator json, SerializerProvider serializer) throws IOException {
        json.writeStartArray();
        for (var line : text) {
            if (line instanceof TextHeader header) {
                handleHeader(json, header);
                previousHeader = header;
            } else {
                handleParagraph(json, line);
            }
        }
        writeEnd(json);
        json.writeEndArray();
        clearState();
    }

    private void handleHeader(JsonGenerator json, TextHeader header) throws IOException {
        if (isAddingParagraphs)
            writeEndParagraphs(json);
        if (previousHeader != null && header.level() > previousHeader.level()) {
            writeStartSubheadings(json);
            depth++;
        } else if (previousHeader != null && header.level() < previousHeader.level()) {
            writeEnd(json);
        } else if (previousHeader != null) {
            writeEndHeader(json);
        }
        writeStartHeader(json, header);
    }

    private void handleParagraph(JsonGenerator json, TextLine line) throws IOException {
        if (line.isBlank())
            return;
        if (isAddingSubheadings)
            writeEndSubheadings(json);
        if (previousHeader != null && !isAddingParagraphs)
            writeStartParagraphs(json);
        writeTextLine(json, line);
    }

    private void writeEnd(JsonGenerator json) throws IOException {
        if (isAddingParagraphs)
            writeEndParagraphs(json);
        if (previousHeader != null)
            writeEndHeader(json);
        while (depth > 0) {
            writeEndSubheadings(json);
            writeEndHeader(json);
            depth--;
        }
    }

    private void writeTextLine(JsonGenerator json, TextLine line) throws IOException {
        json.writeStartObject();
        json.writeStringField("paragraph", line.text());
        json.writeEndObject();
    }

    private void writeStartHeader(JsonGenerator json, TextHeader header) throws IOException {
        json.writeStartObject();
        json.writeStringField("heading", header.text());
        isAddingParagraphs = false;
        isAddingSubheadings = false;
    }

    private void writeEndHeader(JsonGenerator json) throws IOException {
        json.writeEndObject();
        isAddingParagraphs = false;
        isAddingSubheadings = false;
    }

    private void writeStartParagraphs(JsonGenerator json) throws IOException {
        json.writeArrayFieldStart("paragraphs");
        isAddingParagraphs = true;
    }

    private void writeEndParagraphs(JsonGenerator json) throws IOException {
        json.writeEndArray();
        isAddingParagraphs = false;
    }

    private void writeStartSubheadings(JsonGenerator json) throws IOException {
        json.writeArrayFieldStart("subheadings");
        isAddingSubheadings = true;
    }

    private void writeEndSubheadings(JsonGenerator json) throws IOException {
        json.writeEndArray();
        isAddingSubheadings = false;
    }

    private void clearState() {
        previousHeader = null;
        depth = 0;
        isAddingParagraphs = false;
        isAddingSubheadings = false;
    }

}