package org.amazon.ins.nosqlrepository;

import org.amazon.ins.model.HelpDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 
 * @author Sravanthi
 *
 */

public interface HelpDocumentRepository extends MongoRepository<HelpDocument,String> {

	HelpDocument findByIssueCategory(String issueCategory);
}
