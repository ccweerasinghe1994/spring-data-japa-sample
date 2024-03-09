## 123 - Introduction

![img.png](img.png)

## 124 - JPA Inheritance

![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)
![img_4.png](img_4.png)
![img_5.png](img_5.png)
![img_6.png](img_6.png)
![img_7.png](img_7.png)
![img_8.png](img_8.png)
![img_9.png](img_9.png)
![img_10.png](img_10.png)
![img_11.png](img_11.png)
![img_12.png](img_12.png)
![img_13.png](img_13.png)
![img_14.png](img_14.png)
![img_15.png](img_15.png)
![img_16.png](img_16.png)
![img_17.png](img_17.png)
![img_18.png](img_18.png)

## 125 - JPA Mapped Super Class

let's create a BaseEntity class.

```java
package guru.springframework.orderservice.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

```

since we have added the `@MappedSuperclass` annotation, this class will not be created in the database.
since we moved the `@Id` and `@GeneratedValue` annotations to the `BaseEntity` class, we can remove them from
the `OrderHeader` class.

```java
package guru.springframework.orderservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

/**
 * Created by jt on 12/5/21.
 */
@Entity
public class OrderHeader extends BaseEntity {


    private String customer;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        OrderHeader that = (OrderHeader) o;
        return Objects.equals(customer, that.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customer);
    }
}

```

let's test the code.

```java
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
```

## 126 - Embedded Types

## 127 - Java Enumerated Types

## 128 - Hibernate Created Date

## 129 - Hibernate Update Date

        

