
package com.kiet.librarymanagementsystem.controller;

import com.kiet.librarymanagementsystem.model.Book;
import com.kiet.librarymanagementsystem.repository.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository repository;

    // 1. Add Book
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {

        book.setId(null);

        Book savedBook = repository.save(book);

        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    // 2. Get All Books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {

        return ResponseEntity.ok(repository.findAll());
    }

    // 3. Get Book By ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
            @PathVariable Integer id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Update Book
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Integer id,
            @RequestBody Book updatedBook) {

        return repository.findById(id).map(book -> {

            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setCategory(updatedBook.getCategory());
            book.setAvailableCopies(
                    updatedBook.getAvailableCopies()
            );

            return ResponseEntity.ok(repository.save(book));

        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. Delete Book
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(
            @PathVariable Integer id) {

        if (repository.existsById(id)) {

            repository.deleteById(id);

            return ResponseEntity.ok(
                    "Book deleted successfully!"
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Book not found with ID: " + id);
    }
}
