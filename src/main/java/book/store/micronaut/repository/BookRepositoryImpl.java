package book.store.micronaut.repository;

import book.store.micronaut.ApplicationConfiguration;
import book.store.micronaut.model.Book;
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;

    public BookRepositoryImpl(@CurrentSession EntityManager entityManager,
                              ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    @Transactional
    public Book save(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    @Transactional
    public void deleteById(@NotNull Long id) {
        findById(id).ifPresent(book -> entityManager.remove(book));
    }

    private final static List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "title");

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll(@NotNull SortingAndOrderArguments args) {
        String qlString = "SELECT b FROM book as b";
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
            qlString += " ORDER BY g." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
        }
        TypedQuery<Book> query = entityManager.createQuery(qlString, Book.class);
        query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);
        return query.getResultList();
    }
}
