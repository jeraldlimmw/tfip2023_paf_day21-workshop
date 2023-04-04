package sg.edu.nus.iss.day21workshop.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day21workshop.model.Customer;
import sg.edu.nus.iss.day21workshop.model.Order;

import static sg.edu.nus.iss.day21workshop.repository.DBQueries.*;

@Repository
public class CustomerRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public List<Customer> getAllCustomer(Integer offset, Integer limit) {
        List<Customer> customers = new ArrayList<>();

        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_ALL_CUSTOMERS, offset, limit);

        while(rs.next()) {
            customers.add(Customer.create(rs));
        }

        return customers;
    }

    public Customer findCustomerById(int id) {
        List<Customer> customers = jdbcTemplate.query(SELECT_CUSTOMER_BY_ID, 
                new CustomerRowMapper(), new Object[]{id});
        
        return customers.get(0);
    }

    public List<Order> getCustomerOrders(Integer id) {     
        List<Order> orders = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_ORDERS_FOR_CUSTOMER, id);

        while(rs.next()) {
            orders.add(Order.create(rs));
        };
        
        return orders;
    }
}
