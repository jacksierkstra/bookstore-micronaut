package book.store.micronaut.controller;

import book.store.micronaut.model.Book;
import book.store.micronaut.repository.BookRepository;
import book.store.micronaut.repository.SortingAndOrderArguments;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller("/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Get
    public HttpResponse<List<Book>> getBooks() {
        return HttpResponse.ok(this.bookRepository.findAll());
    }

    @Get("/{bookId}")
    public HttpResponse<Optional<Book>> getBook(Long bookId) {
        return HttpResponse.ok(this.bookRepository.findById(bookId));
    }

    @Post
    public HttpResponse<Book> addBook(@Body @Valid Book book) {
        Book createdBook = this.bookRepository.save(book);

        return HttpResponse.created(URI.create(String.format("/api/books/%s", createdBook.id)));
    }

    @Delete("/{bookId}")
    public void deleteBook(Long bookId) {
        this.bookRepository.deleteById(bookId);
    }
}
