package guru.springframework.orderservice.domain;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Created by jt on 12/5/21.
 */
@Entity
@AttributeOverrides({
        @AttributeOverride(name = "shippingAddress.address", column = @Column(name = "shipping_address")),
        @AttributeOverride(name = "shippingAddress.city", column = @Column(name = "shipping_city")),
        @AttributeOverride(name = "shippingAddress.state", column = @Column(name = "shipping_state")),
        @AttributeOverride(name = "shippingAddress.zipCode", column = @Column(name = "shipping_zip_code")),
        @AttributeOverride(name = "billiToAddress.address", column = @Column(name = "bill_to_address")),
        @AttributeOverride(name = "billiToAddress.city", column = @Column(name = "bill_to_city")),
        @AttributeOverride(name = "billiToAddress.state", column = @Column(name = "bill_to_state")),
        @AttributeOverride(name = "billiToAddress.zipCode", column = @Column(name = "bill_to_zip_code"))
})
public class OrderHeader extends BaseEntity {
    private String customer;
    private Address shippingAddress;
    private Address billiToAddress;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBilliToAddress() {
        return billiToAddress;
    }

    public void setBilliToAddress(Address billiToAddress) {
        this.billiToAddress = billiToAddress;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OrderHeader that = (OrderHeader) o;

        if (!Objects.equals(customer, that.customer)) return false;
        if (!Objects.equals(shippingAddress, that.shippingAddress))
            return false;
        if (!Objects.equals(billiToAddress, that.billiToAddress))
            return false;
        return orderStatus == that.orderStatus;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (shippingAddress != null ? shippingAddress.hashCode() : 0);
        result = 31 * result + (billiToAddress != null ? billiToAddress.hashCode() : 0);
        result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
        return result;
    }
}
