package org.amazon.ins.controller;

import java.util.List;

import org.amazon.ins.model.Issue;
import org.amazon.ins.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The IssueController class includes two get methods to list the issues
 * and to list the issues based on impact.
 * It also includes a post method to add issues to the database.
 * 
 * @author Raasi
 *
 */

@RestController
@RequestMapping("/issue")
@CrossOrigin
public class IssueController {

	@Autowired
	IssueRepository issueRepo;
	
	/**
	 * This end-point returns a list of all the saved issues
	 * @return list of saved issues
	 */
	@GetMapping("/getIssues")
	public List<Issue> getIssues(){
		return issueRepo.findAll();
	}
	
	/**
	 * This end-point saves a list of issues to the database.
	 * 
	 * @param issueList list of issues to be saved
	 * @return list of issues after saving
	 */
	@PostMapping("/addIssues")
	public List<Issue> addIssues(@RequestBody List<Issue> issueList){
		return issueRepo.saveAll(issueList);
	}
	
	/**
	 * This end-point returns a list of all issues by impact.
	 * impact = {"critical","significant","minor"}
	 * 
	 * @param impact issue impact
	 * @return list of corresponding issues
	 */
	@GetMapping("/{impact}")
	public List<Issue> getByImpact(@PathVariable(name="impact") String impact){
		return issueRepo.findByImpact(impact);
	}
}
