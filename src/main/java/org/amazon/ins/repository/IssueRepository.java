package org.amazon.ins.repository;

import java.util.List;

import org.amazon.ins.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Raasi
 *
 */

@Repository
public interface IssueRepository extends JpaRepository<Issue,Long>{

	Issue findByIssueCategory(String issueCategory);
	List<Issue> findByImpact(String impact);
	boolean existsByIssueCategory(String issueCategory);
	
}
