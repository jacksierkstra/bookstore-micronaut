package book.store.micronaut.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "book")
public class Book {

    @Id
    @GeneratedValue
    public Long id;

    public String title;
}
