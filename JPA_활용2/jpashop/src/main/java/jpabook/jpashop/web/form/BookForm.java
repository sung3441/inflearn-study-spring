package jpabook.jpashop.web.form;

import jpabook.jpashop.domain.item.Book;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    private String author;

    private String isbn;

    public BookForm() {
    }

    public Book toBook() {
        Book book = new Book();
        book.setId(getId());
        book.setName(getName());
        book.setPrice(getPrice());
        book.setStockQuantity(getStockQuantity());
        book.setAuthor(getAuthor());
        book.setIsbn(getIsbn());
        return book;
    }
}
