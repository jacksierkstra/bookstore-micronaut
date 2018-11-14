package book.store.micronaut.repository;

import book.store.micronaut.model.Book;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Optional<Book> findById(@NotNull Long id);

    Book save(Book book);

    void deleteById(@NotNull Long id);

    List<Book> findAll(@NotNull SortingAndOrderArguments args);
}
