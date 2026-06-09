package com.basweti.basweti_books.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.basweti.basweti_books.controller.entity.Book;
import com.basweti.basweti_books.request.BookRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

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


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/allbooks")
    public List<Book> getAllBooks() {
        return books;
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Book> getBooks(@RequestParam(required = false) String category) {
        if (category == null) {
            return books;
        }
        
        return books.stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable @Min(value = 1, message = "ID must be a positive integer") long id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }


    @ResponseStatus(HttpStatus.OK)
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

    // @PostMapping
    // public void createBook(@RequestBody Book newBook) {

    //     for (Book book : books) {
    //         if (book.getTitle().equalsIgnoreCase(newBook.getTitle())) {
    //             return;
    //         }
    //     }

    //     books.add(newBook);
    // }


    // Create Book Using Non-matching Title
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public String createBook(@Valid @RequestBody BookRequest bookRequest) {
        // long id;

        // if (books.isEmpty()) {
        //     id = 1;
        // } else {
        //     id = books.get(books.size() - 1).getId() + 1;
        // }

        // use ternary operator to generate id
        long id = books.isEmpty() ? 1 : books.get(books.size() - 1).getId() + 1;

        Book newBook = convertToBook(id, bookRequest);
        books.add(newBook);
        return "Book created successfully with non-matching title";
    }


    // update book by title using @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/update/{id}")
    public String updateBook(@PathVariable @Min(value = 1, message = "ID must be a positive integer") long id, @Valid @RequestBody BookRequest bookRequest) {
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getId() == id) {
                Book updatedBook = convertToBook(id, bookRequest);
                books.set(i, updatedBook);
                return "Book updated successfully";
            }
        }
        return "Book not found";
    }


    // delete book by title using @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable @Min(value = 1, message = "ID must be a positive integer") long id) {
        books.removeIf(book -> book.getId() == id);
        return "Book deleted successfully";
    }


    private Book convertToBook(long id, BookRequest bookRequest) {
        return new Book(id, bookRequest.getTitle(), bookRequest.getAuthor(), bookRequest.getCategory(), bookRequest.getRating());
    }

}
