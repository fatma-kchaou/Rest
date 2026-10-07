package com.example.universityrest.web;

import com.example.universityrest.model.Student;
import com.example.universityrest.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // GET ALL
    @GetMapping
    public CollectionModel<EntityModel<Student>> getAll() {

        List<EntityModel<Student>> students =
                studentService.getAll()
                        .stream()
                        .map(student -> EntityModel.of(
                                student,

                                linkTo(methodOn(StudentController.class)
                                        .getById(student.getId()))
                                        .withSelfRel(),

                                linkTo(methodOn(StudentController.class)
                                        .getAll())
                                        .withRel("students")
                        ))
                        .toList();

        return CollectionModel.of(
                students,

                linkTo(methodOn(StudentController.class)
                        .getAll())
                        .withSelfRel()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public EntityModel<Student> getById(@PathVariable Long id) {

        Student student = studentService.getById(id);

        return EntityModel.of(
                student,

                linkTo(methodOn(StudentController.class)
                        .getById(id))
                        .withSelfRel(),

                linkTo(methodOn(StudentController.class)
                        .getAll())
                        .withRel("students")
        );
    }

    // POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<Student> create(
            @Valid @RequestBody Student student) {

        Student created = studentService.create(student);

        return EntityModel.of(
                created,

                linkTo(methodOn(StudentController.class)
                        .getById(created.getId()))
                        .withSelfRel(),

                linkTo(methodOn(StudentController.class)
                        .getAll())
                        .withRel("students")
        );
    }

    // PUT
    @PutMapping("/{id}")
    public EntityModel<Student> update(
            @PathVariable Long id,
            @Valid @RequestBody Student student) {

        Student updated = studentService.update(id, student);

        return EntityModel.of(
                updated,

                linkTo(methodOn(StudentController.class)
                        .getById(id))
                        .withSelfRel(),

                linkTo(methodOn(StudentController.class)
                        .getAll())
                        .withRel("students")
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        studentService.delete(id);
    }
}