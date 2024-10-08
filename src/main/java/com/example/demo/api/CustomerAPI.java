package com.example.demo.api;

import java.net.URI;
import java.util.Iterator;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.domain.Customer;
import com.example.demo.repository.CustomersRepository;
import com.example.demo.domain.Customer;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/customers")
public class CustomerAPI {
    // initialize repo
    @Autowired
	CustomersRepository repo;

    @GetMapping 
    public Iterable<Customer> getAll() {
		return repo.findAll();
	}

    // find customer by id
    @GetMapping("/{customerId}")
	public Optional<Customer> getCustomerById(@PathVariable("customerId") long id) {
		return repo.findById(id);
	}


    @PostMapping
    public ResponseEntity<?> addCustomers(@RequestBody Customer newCust, UriComponentsBuilder uri) {
        if (newCust.getEmail() == null || newCust.getName() == null || newCust.getId() != 0) {
            return ResponseEntity.badRequest().build();
        }
         
        newCust = repo.save(newCust);
        URI pathCust = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newCust.getId()).toUri();
        ResponseEntity<?> res = ResponseEntity.created(pathCust).build();
        return res;

        
    }

    @DeleteMapping("/{customerId}")
	public ResponseEntity<?> deleteCustomerById(@PathVariable("customerId") long id) {
		repo.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}	


    @PutMapping("/{customerId}")
	public ResponseEntity<?> putCustomer(
			@RequestBody Customer updatedCust,
			@PathVariable("customerId") long customerId) 
	{
		if (updatedCust.getEmail() == null || updatedCust.getName() == null || updatedCust.getId() != customerId) {
            return ResponseEntity.badRequest().build();
        }
		updatedCust = repo.save(updatedCust);
		return ResponseEntity.ok().build();
	}	

    //lookupCustomerByName GET
	@GetMapping("/byname/{username}")
	public ResponseEntity<?> lookupCustomerByNameGet(@PathVariable("username") String username,
			UriComponentsBuilder uri) {
		
		
		Iterator<Customer> customers = repo.findAll().iterator();
		while(customers.hasNext()) {
			Customer cust = customers.next();
			if(cust.getName().equalsIgnoreCase(username)) {
				ResponseEntity<?> response = ResponseEntity.ok(cust);
				return response;				
			}			
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	//lookupCustomerByName POST
	@PostMapping("/byname")
	public ResponseEntity<?> lookupCustomerByNamePost(@RequestBody String username, UriComponentsBuilder uri) {
		
		Iterator<Customer> customers = repo.findAll().iterator();
		while(customers.hasNext()) {
			Customer cust = customers.next();
			if(cust.getName().equals(username)) {
				ResponseEntity<?> response = ResponseEntity.ok(cust);
				return response;				
			}			
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}	

    










    
}



