package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemUpdateService {


    @Autowired
    EntityManager entityManager;

    @Test
    public void updateTest() {
        Book book = entityManager.find(Book.class, 1L);

        // TX
        book.setName("test");

        // TX commit
    }
}
