package com.sowa.halil57493.service;

import com.sowa.halil57493.model.Note;
import com.sowa.halil57493.model.User;
import com.sowa.halil57493.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    public List<Note> getNotesByUser(User user) {
        return noteRepository.findByUser(user);
    }

    public Note createNote(String title, String content, User user) {
        Note note = Note.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
        return noteRepository.save(note);
    }

    public void deleteNote(Long noteId, User user) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this note");
        }

        noteRepository.delete(note);
    }
}
