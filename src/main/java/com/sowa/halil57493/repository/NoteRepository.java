package com.sowa.halil57493.repository;

import com.sowa.halil57493.model.Note;
import com.sowa.halil57493.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(User user);
}
