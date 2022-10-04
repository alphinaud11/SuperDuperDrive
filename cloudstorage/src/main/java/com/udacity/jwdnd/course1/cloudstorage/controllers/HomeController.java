package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;

    public HomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping()
    public String homeView() {
        return "home";
    }

    @PostMapping("/file-upload")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload, Model model) throws IOException {
        String error = null;
        Integer userid = userService.getUserid(authentication.getName());

        if (fileUpload.isEmpty()) {
            error = "You have to choose a file to upload.";
        }

        if (error == null) {
            boolean isFilenameAvailable = fileService.isFilenameAvailable(userid, fileUpload.getOriginalFilename());
            if (!isFilenameAvailable) {
                error = "There is a file with the same name.";
            }
        }

        if (error == null) {
            int rowsAdded = fileService.addFile(new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(), Long.toString(fileUpload.getSize()), userid, fileUpload.getBytes()));
            if (rowsAdded < 0) {
                error = "There was an error saving this file. Please try again.";
            }
        }

        if (error == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", error);
        }

        return "result";
    }

    @PostMapping("/note-upload-edit")
    public String uploadNote(Authentication authentication, @ModelAttribute Note note, Model model) {
        String error = null;
        note.setUserid(userService.getUserid(authentication.getName()));

        if (note.getNoteid() == null) {
            int rowsAdded = noteService.addNote(note);
            if (rowsAdded < 0) {
                error = "There was an error saving this note. Please try again.";
            }
        } else {
            noteService.editNote(note);
        }

        if (error == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", error);
        }

        return "result";
    }

    @PostMapping("/credential-upload-edit")
    public String uploadCredential(Authentication authentication, @ModelAttribute Credential credential, Model model) {
        String error = null;
        credential.setUserid(userService.getUserid(authentication.getName()));

        if (credential.getCredentialid() == null) {
            int rowsAdded = credentialService.addCredential(credential);
            if (rowsAdded < 0) {
                error = "There was an error saving this credential. Please try again.";
            }
        } else {
            credentialService.editCredential(credential);
        }

        if (error == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", error);
        }

        return "result";
    }

    @GetMapping("/file-download")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "fileid") Integer fileid) {
        File file = fileService.getFile(fileid);
        Resource resource = new ByteArrayResource(file.getFiledata());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(resource);
    }

    @GetMapping("/file-delete")
    public String deleteFile(@RequestParam(value = "fileid") Integer fileid, Model model) {
        fileService.deleteFile(fileid);
        model.addAttribute("success", true);
        return "result";
    }

    @GetMapping("/note-delete")
    public String deleteNote(@RequestParam(value = "noteid") Integer noteid, Model model) {
        noteService.deleteNote(noteid);
        model.addAttribute("success", true);
        return "result";
    }

    @GetMapping("/credential-delete")
    public String deleteCredential(@RequestParam(value = "credentialid") Integer credentialid, Model model) {
        credentialService.deleteCredential(credentialid);
        model.addAttribute("success", true);
        return "result";
    }

    @ModelAttribute("files")
    public List<File> files(Authentication authentication) {
        return fileService.getAllFiles(userService.getUserid(authentication.getName()));
    }

    @ModelAttribute("notes")
    public List<Note> notes(Authentication authentication) {
        return noteService.getAllNotes(userService.getUserid(authentication.getName()));
    }

    @ModelAttribute("credentials")
    public List<Credential> credentials(Authentication authentication) {
        return credentialService.getAllCredentials(userService.getUserid(authentication.getName()));
    }
}
