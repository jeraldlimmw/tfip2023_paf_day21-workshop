package sg.edu.nus.iss.day21workshop.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.day21workshop.model.Customer;
import sg.edu.nus.iss.day21workshop.model.Order;
import sg.edu.nus.iss.day21workshop.repository.CustomerRepository;

@RestController
@RequestMapping(path = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerRestController {
    
    @Autowired
    CustomerRepository custRepo;

    // //customer injection, alternative to @autowired
    // public CustomerRestController(CustomerRepository custRepo) {
    //     this.customerRepository = custRepo;
    // }

    @GetMapping()
    public ResponseEntity<String> getAllCustomers (@RequestParam(required = false) String offset, @RequestParam(required = false) String limit) {
        
        if (Objects.isNull(offset)) offset = "0";
        if (Objects.isNull(limit)) limit = "5";

        List<Customer> customers = custRepo.getAllCustomer(Integer.parseInt(offset), Integer.parseInt(limit));

        // method to convert to JSON
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        
        for (Customer c : customers) {
            arrBuilder.add(c.toJson());
        }

        JsonArray result = arrBuilder.build();
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }

    @GetMapping(path="/{customerId}")
    public ResponseEntity<String> getCustomerById(@PathVariable Integer customerId) {

        JsonObject result = null;
        try{
            Customer customer = custRepo.findCustomerById(customerId);
            result = Json.createObjectBuilder()
                .add("customer", customer.toJson())
                .build();
        } catch (IndexOutOfBoundsException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error_msg\" :\"record not found\"}");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }

    @GetMapping(path="/{customerId}/orders")
    public ResponseEntity<String> getCustomerOrders(@PathVariable Integer customerId) {

        List<Order> orders = new ArrayList<>();

        orders = custRepo.getCustomerOrders(customerId);

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        for (Order o : orders) {
            arrBuilder.add(o.toJson());
        }
        
        JsonArray result = arrBuilder.build();

        if (result.size() == 0) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error_msg\" :\"record not found\"}");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    } 
}
