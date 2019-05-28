package org.amazon.ins.controller;

import java.util.List;

import javax.validation.Valid;

import org.amazon.ins.model.User;
import org.amazon.ins.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The UserController class includes a GetMapping method to view all the users.
 * It also includes 2 PostMapping methods to add users
 *  
 * @author Raasi 
 *
 */

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepo;
	
	/**
	 * This Get mapping give a view of all the users.
	 *  
	 * @return list of users saved in the database
	 */
	@GetMapping("/getUsers")
	public List<User> getAllUsers(){
		return userRepo.findAll();
	}
	
	/**
	 * This Post Mapping API allows addition of a user to the database.
	 *  It validates the request body and throws a 400 Bad Request Error if the request body 
	 *  is not valid.
	 *  Else it adds the user to the database
	 *  
	 * @param user Request body : email,org
	 * @return user saved user
	 */
	@PostMapping("/addUser")
	public User addUser(@Valid @RequestBody User user){
		return userRepo.save(user);
	}
	
	/**
	 * This Post Mapping API allows addition of multiple users into the database.
	 * Throws ConstraintViolationException in case the request body is not valid.
	 * 
	 * @param userList list of valid users
	 * @return list of saved users
	 */
	@PostMapping("/addUsers")
	public List<User> addUsers(@RequestBody List<User> userList){
		return userRepo.saveAll(userList);
	}
	
	
}
