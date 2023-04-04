package sg.edu.nus.iss.day21workshop.model;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonValue;

public class Order {
    private Integer id;
    private String shipName;
    private Double shippingFee;
    private Customer customer;
    
    public Order() {

    }
    
    public Order(Integer id, String shipName, Double shippingFee, Customer customer) {
        this.id = id;
        this.shipName = shipName;
        this.shippingFee = shippingFee;
        this.customer = customer;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getShipName() {
        return shipName;
    }
    public void setShipName(String shipName) {
        this.shipName = shipName;
    }
    public Double getShippingFee() {
        return shippingFee;
    }
    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", shipName=" + shipName + ", shippingFee=" + shippingFee + ", customer=" + customer
                + "]";
    }

    public static Order create(SqlRowSet rs) {
        Order order = new Order();
        Customer customer = new Customer();

        customer.setId(rs.getInt("customer_id"));   
        order.setCustomer(customer);
        order.setId(rs.getInt("id"));
        order.setShipName(rs.getString("ship_name"));
        order.setShippingFee(rs.getDouble("shipping_fee"));

        return order;
    }

    public JsonValue toJson() {
        return Json.createObjectBuilder()
            .add("customer_id", getCustomer().getId())
            .add("company", getId())
            .add("ship_name", getShipName())
            .add("shipping_fee", getShippingFee())
            .build();
    }
    
}
