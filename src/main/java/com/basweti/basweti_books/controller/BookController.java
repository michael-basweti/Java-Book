package com.basweti.basweti_books.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.basweti.basweti_books.controller.entity.Book;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final List<Book> books = new ArrayList<>();

    private void initializeBooks() {
        books.addAll(List.of(
                new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", "Classic", 5),
                new Book(2, "To Kill a Mockingbird", "Harper Lee", "Classic", 4),
                new Book(3, "1984", "George Orwell", "Dystopian", 3),
                new Book(4, "The Catcher in the Rye", "J.D. Salinger", "Classic", 5),
                new Book(5, "The Hobbit", "J.R.R. Tolkien", "Fantasy", 4),
                new Book(6, "Pride and Prejudice", "Jane Austen", "Romance", 2),
                new Book(7, "The Lord of the Rings", "J.R.R. Tolkien", "Fantasy", 5)
        ));
    }

    public BookController() {
        initializeBooks();
    }

    // @GetMapping("/")
    // public String firstApi() {
    //     return "Hello my name is Basweti and I am a software developer";
    // }


    // @GetMapping("/api/books/second")
    // public String secondApi() {
    //     return "I am currently learning Spring Boot and I am enjoying it";
    // }


    @GetMapping("/allbooks")
    public List<Book> getAllBooks() {
        return books;
    }


    @GetMapping
    public List<Book> getBooks(@RequestParam(required = false) String category) {
        if (category == null) {
            return books;
        }
        
        return books.stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable long id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }


    @GetMapping("/category")
    public List<Book> getBooksByCategory(@RequestParam String category) {
        List<Book> booksByCategory = new ArrayList<>();
        for (Book book : books) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                booksByCategory.add(book);
            }
        }
        return booksByCategory;
    }

    // add book by title, author and category
    // @PostMapping("/api/books")

    @PostMapping
    public void createBook(@RequestBody Book newBook) {

        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(newBook.getTitle())) {
                return;
            }
        }

        books.add(newBook);
    }


    // Create Book Using Non-matching Title
    @PostMapping("/non-matching")
    public String createBookWithNonMatchingTitle(@RequestBody Book newBook) {
        boolean isNewBook = books.stream()
                .noneMatch(book -> book.getTitle().equalsIgnoreCase(newBook.getTitle()));
        if (isNewBook) {
            books.add(newBook);
        } else {
            return "Book with the same title already exists";
        }
        return null;
    }


    // update book by title using @PutMapping
    @PutMapping("/update/{id}")
    public String updateBook(@PathVariable long id, @RequestBody Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getId() == id) {
                books.set(i, updatedBook);
                return "Book updated successfully";
            }
        }
        return "Book not found";
    }


    // delete book by title using @DeleteMapping
    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        books.removeIf(book -> book.getId() == id);
        return "Book deleted successfully";
    }

}
