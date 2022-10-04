package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getAllNotes(Integer userid) {
        return noteMapper.getAllNotes(userid);
    }

    public int addNote(Note note) {
        return noteMapper.addNote(note);
    }

    public void editNote(Note note) {
        noteMapper.editNote(note);
    }

    public void deleteNote(Integer noteid) {
        noteMapper.deleteNote(noteid);
    }
}
