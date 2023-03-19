package jpabook.jpashop.domain.item;

import jpabook.jpashop.web.form.BookForm;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
@Getter
@Setter
public class Book extends Item {

    private String author;
    private String isbn;

    public BookForm toBookForm() {
        BookForm form = new BookForm();
        form.setId(getId());
        form.setName(getName());
        form.setPrice(getPrice());
        form.setStockQuantity(getStockQuantity());
        form.setAuthor(getAuthor());
        form.setIsbn(getIsbn());
        return form;
    }
}
