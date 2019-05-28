package org.amazon.ins.repository;

import org.amazon.ins.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Raasi 
 *
 */

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

	User findByEmail(String email);
	boolean existsByEmail(String email);
	
}
