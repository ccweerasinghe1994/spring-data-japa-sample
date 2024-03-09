package guru.springframework.orderservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderHeaderTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void orderHeaderEqualsShouldReturnTrueWhenComparingSameId() {
        // given
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setId(1L);

        OrderHeader orderHeader2 = new OrderHeader();
        orderHeader2.setId(1L);
        // when
        // then
        assertEquals(orderHeader, orderHeader2);
    }

    @Test
    void orderHeaderEqualsShouldReturnFalseWhenComparingDifferentId() {
        // given
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setId(1L);

        OrderHeader orderHeader2 = new OrderHeader();
        orderHeader2.setId(2L);
        // when
        // then
        assertFalse(orderHeader.equals(orderHeader2));
    }

    @Test
    void orderHeaderEqualsShouldReturnTrueWhenComparingSameObject() {
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setCustomer("Customer1");

        assertTrue(orderHeader.equals(orderHeader));
    }

    @Test
    void equalsShouldReturnFalseWhenComparingWithNull() {
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setCustomer("Customer1");

        assertFalse(orderHeader.equals(null));
    }

    //
    @Test
    void equalsShouldReturnFalseWhenComparingWithDifferentClass() {
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setCustomer("Customer1");

        assertNotEquals(orderHeader, new String());
    }

    @Test
    void equalsShouldReturnFalseWhenComparingWithDifferentCustomer() {
        OrderHeader orderHeader1 = new OrderHeader();
        orderHeader1.setCustomer("Customer1");

        OrderHeader orderHeader2 = new OrderHeader();
        orderHeader2.setCustomer("Customer2");

        assertFalse(orderHeader1.equals(orderHeader2));
    }

    @Test
    void equalsShouldReturnTrueWhenComparingWithSameCustomer() {
        OrderHeader orderHeader1 = new OrderHeader();
        orderHeader1.setCustomer("Customer1");

        OrderHeader orderHeader2 = new OrderHeader();
        orderHeader2.setCustomer("Customer1");

        assertTrue(orderHeader1.equals(orderHeader2));
    }

}