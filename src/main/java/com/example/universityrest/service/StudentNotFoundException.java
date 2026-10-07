package com.example.universityrest.service;

public class StudentNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    public StudentNotFoundException(Long id) {
        super("Étudiant introuvable avec l'id " + id);
    }
}
