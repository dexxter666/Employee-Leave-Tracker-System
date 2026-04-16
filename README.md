Employee Leave Tracker System
-

Description
-

This is a Java command-line program used to manage employees and their 
leave requests. The system allows adding employees,
submitting leave requests, approving or rejecting them, and 
storing data in CSV files.

Objectives
- 
- Apply OOP principles
- Implement CRUD operations
- Store data using input
- Validate user input

Requirements
-
1. CRUD operations
2. Command-line interface
3. Input validation
4. File storage (CSV)
5. Modular design
6. Error handling
7. Documentation
8. Encapsulation
9. Inheritance
10. Polymorphism

System Design
-
- Model: Employee, LeaveRequest
- Service: EmployeeService, LeaveService
- Storage: FileStorageService
- Validation: InputValidator

OOP Concepts
-
- Encapsulation: private fields with getters/setters
- Inheritance: Employee - FullTimeEmployee, PartTimeEmployee
- Polymorphism: method overriding
- Abstraction: abstract Employee class

Key Features
-
- Manage employees
- Submit leave requests
- Approve/reject requests
- Track leave balance
- Export data to CSV

Algorithms
- 

The system uses simple algorithms to handle operations and validation:

Employee search: Linear search is used to find employees by ID or name by iterating through the list.

Leave validation: The system checks:
if the employee exists;
if start date is before end date;
if leave balance is enough;
if there are overlapping requests.

Leave balance calculation: The system sums all approved leave days and subtracts from total allowance.
Data Structures

The project mainly uses:

ArrayList → to store employees and leave requests.
String & LocalDate → to store text and date values.
CSV files → to store data permanently.

These structures were chosen because they are simple and efficient for this type of application.

Functions / Modules
-


The system is divided into different classes:

Employee & subclasses → store employee data. 
LeaveRequest → store leave request details. 
EmployeeService → handles employee CRUD operations.
LeaveService → handles leave request logic.
FileStorageService → handles saving and loading data.
Validation methods → ensure correct user input.

This modular design makes the system easier to manage and extend.

Challenges Faced
-

Handling file storage and converting objects to CSV format;

Validating user input (dates, numbers, formats);

Managing multiple classes and keeping code organized;

Preventing errors like duplicate IDs and invalid requests



