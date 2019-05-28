package org.amazon.ins.repository;

import java.util.List;
import java.util.Optional;

import org.amazon.ins.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Raasi 
 *
 */

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long>{

	Optional<Ticket> findByIdAndUserId(Long userId,Long ticketId);
	List<Ticket> findAllByIssueId(Long issueId);
	List<Ticket> findAllByUserId(Long userId);
	List<Ticket> findAllByUserIdAndStatus(Long userId,String status);
	
}
