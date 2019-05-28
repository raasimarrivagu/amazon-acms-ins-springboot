package org.amazon.ins.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.amazon.ins.exception.ResourceNotFoundException;
import org.amazon.ins.model.HelpDocument;
import org.amazon.ins.model.Issue;
import org.amazon.ins.model.TempTicket;
import org.amazon.ins.model.Ticket;
import org.amazon.ins.model.User;
import org.amazon.ins.nosqlrepository.HelpDocumentRepository;
import org.amazon.ins.repository.IssueRepository;
import org.amazon.ins.repository.TicketRepository;
import org.amazon.ins.repository.UserRepository;
import org.amazon.ins.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Each method verifies if the user exists in the database and 
 * also if a valid issue is being reported.
 * 
 * @author Raasi 
 *
 */

@RestController
@RequestMapping("/ticket")
@CrossOrigin
public class TicketController {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	TicketRepository ticketRepo; 
	
	@Autowired
	IssueRepository issueRepo;
	
	@Autowired
	HelpDocumentRepository helpDocumentRepo;
	
	@Autowired
	MailService mailService; 
	
	/**
	 * This method returns all the tickets saved in the database
	 * 
	 * @return list of saved tickets
	 */
	@GetMapping("/getTickets")
	public List<Ticket> getAllTickets(){
		return ticketRepo.findAll();
	}
	
	/**
	 * This method returns tickets based on the issueCategory.
	 * 
	 * @param issueCategory input is the issue category
	 * @return list of tickets registered for the corresponding issue category
	 */
	@GetMapping("/getTicketsByCategory/{issueCategory}")
	public List<Ticket> getTicketsByCategory(@PathVariable String issueCategory){
		Issue issue = issueRepo.findByIssueCategory(issueCategory);
		return ticketRepo.findAllByIssueId(issue.getId());
		
	}
	
	/**
	 * This method returns all the tickets registered by a particular user
	 * If the user does not exist it throws a ResourceNotFoundException with the message "Invalid User"
	 * else it returns the list of tickets
	 * 
	 * @param userName 
	 * @return list of registered tickets 
	 */
	@GetMapping("/getAllTickets/{id}")
	public List<Ticket> getAllTicketsByUserName(@PathVariable(name="id") String userName){
		if(!userRepo.existsByEmail(userName)){
			throw new ResourceNotFoundException("Invalid user");
		}else {
			Long userId = userRepo.findByEmail(userName).getId();
			return ticketRepo.findAllByUserId(userId);
		}
	}
	
	/**
	 * This end-point is used to achieve one of the main work flow of the project.
	 * The data is requested from the user into a response model TempTicket
	 * Response model is used to validate the input received
	 * Then the input is verified by checking if the resources(user and issue) exist.
	 * After retrieving the issue and user the ticket is generated
	 * and a mail is sent to the user after saving the ticket to the database 
	 * and retrieving the help document from MongoDB.
	 * 
	 * @param tempTicket request from user
	 * @return saved ticket
	 */
	@PostMapping("/createTicket")
	public Ticket createTicket(@RequestBody TempTicket tempTicket){
		
		//if the user does not exist returns a 404 Not Found error with the message "Invalid user"
		if(!userRepo.existsByEmail(tempTicket.getUserName())){
			throw new ResourceNotFoundException("Invalid user");
		}
		
		//if the issue is not recognized returns a 404 Not Found error with the respective message
		if(!issueRepo.existsByIssueCategory(tempTicket.getIssueCategory())){
			throw new ResourceNotFoundException("Issue Category : "+tempTicket.getIssueCategory()+" does not exist.");
		}
		
		Issue issue = issueRepo.findByIssueCategory(tempTicket.getIssueCategory());
		User user=userRepo.findByEmail(tempTicket.getUserName());
		
		Ticket t=new Ticket();
		t.setIssueId(issue.getId());
		t.setStatus("started");
		t.setUser(user);
		t.setPriority(tempTicket.getPriority());
		
		Ticket finalTicket = ticketRepo.save(t);
		
		HelpDocument helpDocument = helpDocumentRepo.findByIssueCategory(tempTicket.getIssueCategory());
		
		mailService.sendMail(user,helpDocument,finalTicket);
		
		return finalTicket;
	}
	
	/**
	 * This end-point is used to delete a ticket based on the userId and the ticketId
	 * After verification the corresponding ticket is deleted from the database
	 * 
	 * @param userId	user ID
	 * @param ticketId	ticket ID
	 * @return 	ResponseEntity
	 */
	@DeleteMapping("/user/{uid}/ticket/{tid}")
	public ResponseEntity<?> deleteTicket(@PathVariable(value="uid") Long userId,@PathVariable(value="tid")Long ticketId){
		
		if(!userRepo.existsById(userId)){
			throw new ResourceNotFoundException("UserId "+userId+" not found.");
		}
		
		if(!ticketRepo.existsById(ticketId)){
			throw new ResourceNotFoundException("TicketId "+ticketId+" not found.");
		}
		
		return ticketRepo.findByIdAndUserId(ticketId, userId).map(ticket->{
			ticketRepo.delete(ticket);
			return ResponseEntity.ok().build(); 
		}).orElseThrow(()->new ResourceNotFoundException("Resource not found."));
	}
	
	/**
	 * This end-point is used to check the status of a ticket
	 * The input is sent in the form of a Map object
	 * The input is then verified and the ticket status is returned as a response
	 * 
	 * @param requestForStatus Map object with corresponding data
	 * @return Ticket is returned as the response
	 */
	@PostMapping("/getStatus")
	public Optional<Ticket> getStatus(@RequestBody Map<String,Object> requestForStatus) {
		
		String userName = requestForStatus.get("userName").toString();
		Long ticketId = Long.parseLong(requestForStatus.get("ticketId").toString()); 
		
		if(!userRepo.existsByEmail(userName)){
			throw new ResourceNotFoundException("Invalid user");
		}
		
		if(!ticketRepo.existsById(ticketId)){
			throw new ResourceNotFoundException("Ticket with id: "+ticketId+" does not exist");
		}
		
		User user = userRepo.findByEmail(userName);
		
		return ticketRepo.findByIdAndUserId(ticketId, user.getId()).map(ticket->{
			return ticketRepo.findById(ticketId);
		}).orElseThrow(()->new ResourceNotFoundException("No ticket registered with this ID : "+ticketId));
		
	}
	
	/**
	 * This end-point is used to update the status of the ticket 
	 * the data is request in the form of Map object
	 * the data is then verified
	 * and the status of the corresponding ticket is updated to the requested value
	 * after appropriate verification of the issue category and email are done
	 * the updated ticket is saved to the database and an email is sent to the user 
	 * indicating the update
	 * 
	 * @param requestBody ticketId,status(updated status)
	 * @return updated ticket
	 */
	@PutMapping("/updateStatus")
	public Ticket updateStatus(@RequestBody Map<String,Object> requestBody){
		
		Long ticketId = Long.parseLong(requestBody.get("ticketId").toString());
		String status = requestBody.get("status").toString();
		
		if(!ticketRepo.existsById(ticketId)){
			throw new ResourceNotFoundException("ticket not found");
		}
		
		return ticketRepo.findById(ticketId).map(ticket->{
			
			Optional<Issue> issue = issueRepo.findById(ticket.getIssueId());
			Optional<User> user = userRepo.findById(ticket.getUser().getId());
			
			if(issue.isPresent() && user.isPresent()){
				String issueCategory = issue.get().getIssueCategory();
				String userEmail = user.get().getEmail();
				ticket.setStatus(status);
				Ticket finalTicket = ticketRepo.save(ticket);
				mailService.sendMailForStatusUpdates(userEmail, ticket, issueCategory);
				return finalTicket;
			}else {
				throw new ResourceNotFoundException("Invalid request");
			}
			
			
		}
		).orElseThrow(()->new ResourceNotFoundException("Status not updated"));
	}
	
	/**
	 * This end-point returns the list of tickets registered by a user corresponding to 
	 * whether the ticket is pending(started or in-progress) or resolved
	 * 
	 * @param status 
	 * @param userName
	 * @return list of corresponding tickets
	 */
	@GetMapping("/getUserTicketsByStatus/{status}/{userName}")
	public List<Ticket> getTicketsByStatus(@PathVariable(name="status") String status,@PathVariable(name="userName") String userName){
		
		if(!userRepo.existsByEmail(userName)){
			throw new ResourceNotFoundException("Invalid user");
		}
		
		Long userId = userRepo.findByEmail(userName).getId();
		if(status.equals("pending")){
			List<Ticket> resultList = ticketRepo.findAllByUserIdAndStatus(userId,"started");
			resultList.addAll(ticketRepo.findAllByUserIdAndStatus(userId,"in-progress"));
			return resultList;
		}else {
			return ticketRepo.findAllByUserIdAndStatus(userId,"resolved");
		}
	}
	
	/**
	 * This end-point returns the count of the tickets corresponding to the status 
	 * for the requested username
	 * 
	 * @param userName
	 * @return list containing count for ("started","in-progress","resolved")
	 */
	@GetMapping("/getCountOfTicketsByStatus/{userName}")
	public List<Integer> getCount(@PathVariable(name="userName") String userName){
		
		int[] result = new int[3];
		
		Long userId = userRepo.findByEmail(userName).getId();
		
		result[0] = ticketRepo.findAllByUserIdAndStatus(userId, "started").size();
		result[1] = ticketRepo.findAllByUserIdAndStatus(userId, "in-progress").size();
		result[2] = ticketRepo.findAllByUserIdAndStatus(userId, "resolved").size();
		
		List<Integer> list=new ArrayList<>();
		
		
		list.add(result[0]);
		list.add(result[1]);
		list.add(result[2]);
		
		return list;
		
	}
	
}
