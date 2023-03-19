package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 아이템_저장() {
        Book book = new Book();

        Long saveId = itemService.saveItem(book);

        assertEquals(book, itemRepository.findOne(saveId));
    }

    @Test
    public void 아이템_수정() {
        Book book = new Book();
        book.setPrice(10_000);
        itemService.saveItem(book); // 10_000 저장

        int newPrice = 20_000;
        book.setPrice(newPrice);

        Long saveId = itemService.saveItem(book); // 20_000 저장

        assertEquals(newPrice, itemRepository.findOne(saveId).getPrice());
    }

    @Test
    public void 수량_증가() {
        Book book = new Book();
        book.setStockQuantity(100);
        itemService.saveItem(book);

        book.addStock(100);
        Long saveId = itemService.saveItem(book);

        assertEquals(200, itemRepository.findOne(saveId).getStockQuantity());
    }

    @Test
    public void 수량_감소_성공() {
        Book book = new Book();
        book.setStockQuantity(100);
        itemService.saveItem(book);

        book.removeStock(50);
        Long saveId = itemService.saveItem(book);

        assertEquals(50, itemRepository.findOne(saveId).getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 수량_감소_실패() {
        Book book = new Book();
        book.setStockQuantity(100);
        book.removeStock(200);
    }
}