package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("UserA", "서울", "12-1", "123");
            em.persist(member);

            Book book1 = createBook("JPA1", 10_000, 10);
            em.persist(book1);

            Book book2 = createBook("JPA2", 20_000, 20);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10_000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20_000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("UserB", "제주도", "111", "999");
            em.persist(member);

            Book book1 = createBook("spring1", 30_000, 10);
            em.persist(book1);

            Book book2 = createBook("spring2", 40_000, 20);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10_000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20_000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private static Book createBook(String name, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }

        private static Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private static Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }
}
