package com.sowa.halil57493.controller;

import com.sowa.halil57493.model.Note;
import com.sowa.halil57493.model.User;
import com.sowa.halil57493.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final com.sowa.halil57493.repository.UserRepository userRepository;

    @GetMapping
    public String listNotes(
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
            Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        List<Note> notes = noteService.getNotesByUser(user);
        model.addAttribute("notes", notes);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole().name());
        return "notes";
    }

    @PostMapping
    public String createNote(
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
            @RequestParam String title,
            @RequestParam String content) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        noteService.createNote(title, content, user);
        return "redirect:/notes";
    }

    @PostMapping("/delete/{id}")
    public String deleteNote(
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
            @PathVariable Long id) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        noteService.deleteNote(id, user);
        return "redirect:/notes";
    }
}
