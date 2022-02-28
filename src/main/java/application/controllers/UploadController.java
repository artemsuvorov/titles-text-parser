package application.controllers;

import application.storage.Storage;
import application.storage.StorageException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class UploadController {

    private final Storage storage;
    private final String uploadFilename = "text.txt";

    @Autowired
    public UploadController(Storage storage) {
        this.storage = storage;
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs) {
        String status = storeFileWithStatusMessage(file);
        redirectAttrs.addFlashAttribute("status", status);
        return "redirect:/";
    }

    @GetMapping("/files/{filename:.+}")
    public @ResponseBody String serveFile(@PathVariable String filename) {
        try {
            var file = storage.load(filename);
            try (var stream = file.getInputStream()) {
                return new String(stream.readAllBytes()).replaceAll("(\r\n|\n)", "<br/>");
            }
        } catch (Exception ex) {
            return "Failed to upload '" + filename + "' => Error message: " + ex.getMessage();
        }
    }

    private String storeFileWithStatusMessage(MultipartFile file) {
        var filename = file.getOriginalFilename();
        if (file.isEmpty())
            return "You cannot upload an empty file '" + filename + "'";
        try {
            storage.store(file, uploadFilename);
            return "You've successfully uploaded '" + filename + "' as a '" + uploadFilename + "'";
        } catch (StorageException ex) {
            return "Failed to upload '" + filename + "' => Error message: " + ex.getMessage();
        }
    }

}