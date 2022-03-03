package application.controllers;

import application.models.TitleText;
import application.storage.Storage;
import application.views.PlainTextView;
import application.views.TitleTextView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilesController {

    private final Storage storage;

    @Autowired
    public FilesController(Storage storage) {
        this.storage = storage;
    }

    @GetMapping("/files/{filename:.+}")
    public String serveFileContents(@PathVariable String filename) {
        try {
            var text = getTextOfFile(filename);
            return new PlainTextView(text).buildResponseBody();
        } catch (Exception ex) {
            return "Failed to upload '" + filename + "' => Error message: " + ex.getMessage();
        }
    }

    @GetMapping("/files/parsed/{filename:.+}")
    public String serveParsedFile(@PathVariable String filename) {
        try {
            var text = TitleText.parse(getTextOfFile(filename));
            return new TitleTextView(text).buildResponseBody();
        } catch (Exception ex) {
            return "Failed to upload '" + filename + "' => Error message: " + ex.getMessage();
        }
    }

    @GetMapping("/files/parsed/json/{filename:.+}")
    public TitleText serveParsedFileAsJson(@PathVariable String filename) {
        try {
            var text = getTextOfFile(filename);
            return TitleText.parse(text);
        } catch (Exception ex) {
            // todo: fixme
            return TitleText.parse("Failed to upload '" + filename + "' => Error message: " + ex.getMessage());
            //return "Failed to upload '" + filename + "' => Error message: " + ex.getMessage();
        }
    }

    private String getTextOfFile(String filename) throws Exception {
        var file = storage.load(filename);
        try (var stream = file.getInputStream()) {
            return new String(stream.readAllBytes());
        }
    }

}