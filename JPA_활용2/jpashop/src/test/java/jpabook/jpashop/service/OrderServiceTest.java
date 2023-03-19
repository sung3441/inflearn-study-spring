package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
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
public class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() {
        String bookName = "이펙티브 자바";
        int itemPrice = 10_000;
        int itemStockQuantity = 10;
        int orderCount = 5;

        Member member = createMember();

        Item book = createBook(bookName, itemPrice, itemStockQuantity);

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order order = orderRepository.findOne(orderId);

        assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, order.getStatus());
        assertEquals("상품 종류 수", 1, order.getOrderItems().size());
        assertEquals("주문 총 가격", itemPrice * orderCount, order.getTotalPrice());
        assertEquals("주문 후 재고 수량", 5, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() {
        String bookName = "이펙티브 자바";
        int itemPrice = 10_000;
        int itemStockQuantity = 10;
        int orderCount = 20;

        Member member = createMember();

        Item book = createBook(bookName, itemPrice, itemStockQuantity);

        orderService.order(member.getId(), book.getId(), orderCount);

        fail("재고 수량 부족 예외 발생해야됨");
    }

    @Test
    public void 주문취소() {
        int itemStockQuantity = 10;
        Member member = createMember();
        Item book = createBook("이펙티브 자바", 10_000, 10);
        Long orderId = orderService.order(member.getId(), book.getId(), 5);

        orderService.cancelOrder(orderId);

        assertEquals("주문 취소 시 CANCEL 상태", OrderStatus.CANCEL, orderRepository.findOne(orderId).getStatus());
        assertEquals("주문 취소된 상품은 재고 수량 원복", itemStockQuantity, book.getStockQuantity());
    }

    private Item createBook(String bookName, int itemPrice, int stockQuantity) {
        Item book = new Book();
        book.setName(bookName);
        book.setPrice(itemPrice);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("memberA");
        member.setAddress(new Address("seoul", "", ""));
        em.persist(member);
        return member;
    }
}