# amazon-acms-ins-springboot

## Issue Notification System

INS is a platform which provides the clients functionality to report various dependency issues/failures. 

## Packages

### Controller
  - TicketController
  - UserController
  - IssueController
  
### Repository (JPA)
  - TicketRepository
  - UserRepository
  - IssueRepository
  
### Model
  - Ticket
  - User
  - Issue
  - HelpDocument
  - TempTicket (Validates the Response Body for createTicket API)
  
### Service
  - MailService
  
## Work Flow

Once a REST API is called the Controller uses the Repositories to fetch the data from the MySQL database validates and verifies the request and sends an email notification
for actions like ticket creation and status updates on tickets using the MailService.
 
