package org.amazon.ins.service;

import org.amazon.ins.model.HelpDocument;
import org.amazon.ins.model.Ticket;
import org.amazon.ins.model.User;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * This Service class is used to send notifications to the user
 * for issue registration and status updates
 * 
 * @author Raasi 
 *
 */

@Service
public class MailService {

	private JavaMailSender javaMailSender;
	
	public MailService(JavaMailSender javaMailSender){
		
		this.javaMailSender=javaMailSender;
	}
	
	public void sendMail(User user,HelpDocument helpDocument,Ticket ticket) throws MailException{
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		
		simpleMailMessage.setTo(user.getEmail());
		
		simpleMailMessage.setSubject("Issue registered successfully");
		
		String messageBody = "Your issue has been registered successfully.\nYour tracking ID is "+ ticket.getId()
								+"\nIssue Category : "+helpDocument.getIssueCategory()
								+"\nCreated On : "+ticket.getCreatedOn().toString()
								+"\nSuggested Steps for resolving : "+ helpDocument.getHelpText();
		
		simpleMailMessage.setText(messageBody);
		
		javaMailSender.send(simpleMailMessage);
	}
	
	public void sendMailForStatusUpdates(String userEmail, Ticket ticket, String issueCategory) throws MailException {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		
		simpleMailMessage.setTo(userEmail);
		
		simpleMailMessage.setSubject("Ticket status update");
		
		String messageBody = "The ticket holding ID "+ticket.getId()
								+"\ncreated on "+ticket.getCreatedOn()
								+"\nregarding "+issueCategory
								+"\nhas been updated to "
								+ticket.getStatus()
								+"\non "+ticket.getUpdatedOn();
		
		simpleMailMessage.setText(messageBody);
		
		System.out.println("mail sent");
		
		javaMailSender.send(simpleMailMessage);
	}
}
