package application.controllers;

import application.storage.Storage;
import application.storage.StorageException;

import application.views.PlainTextView;
import application.views.TitleTextView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private String storeFileWithStatusMessage(MultipartFile file) {
        var filename = file.getOriginalFilename();
        if (file.isEmpty())
            return "You cannot upload an empty file.";
        try {
            storage.store(file, uploadFilename);
            return "You've successfully uploaded '" + filename + "' as a '" + uploadFilename + "'";
        } catch (StorageException ex) {
            return "Failed to upload '" + filename + "' => Error message: " + ex.getMessage();
        }
    }

}