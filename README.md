# SEMESTER PROJECT FINAL REPORT


---

## Campus Claim: A Lost and Found Management System

The system uses OOP to achieve this reporting and claiming of items effectively. Everything from all item types to pictures and owners’ details are all stored in the system files. To develop this, we worked together on different modules and approached this project with a modular approach, making separate modules for each feature we wanted to implement.

### MAJOR CLASSES
* **GUI Class:** Contains complete GUI structure including the picture handling.
* **Item Class:** The parent class inherited by Lost Item, Found Item and Claimed Item classes.
* **File Handling Class:** Contains the complete logic of storing and reading the data.
* **Category Class:** Contains methods and questions related to the categories.
* **Main Class:** contains the overall flow of the project.

### STRUCTURE:
The user gets options on the main dashboard for viewing and reporting data. Categories are made for each item for the user’s ease which are used for filtering and asking specific questions. To view and report any kind (Lost, Found, and Claim) of item, data is read from and written in the file. To claim an item, validation process is set up. Lost items pictures are also stored in a folder. The system report displays the overall efficiency of the project.

---

### KEY FEATURES
* **IMAGE UPLOAD:** The user can upload a picture when reporting the lost item. This has been achieved through picture handling process. The pictures are copied in a folder and assigned specific names.
* **CLAIM VALIDATION:** User cannot claim an item without first passing the validation test. Each category has been assigned specific questions. Questions have to be correctly answered for the approval.
* **CATEGORY FILTERS:** The user can filter items according to the categories they belong to while viewing the list of items. It makes it easier to locate the items.
* **PERFORMANCE REPORT:** A performance report block has been displayed on the dashboard showing the usage of the project along with the percentage of users who benefited from it.
* **GUI:** The whole project is GUI based which is made using JavaFx.

---

### FUTURE IMPROVEMENTS
We were unable to achieve a few features in the given time frame which we hope to add in future.
* **USER REGISTRATION AND LOGIN:** Since the user registration requires complex handling, either through email or ID, it could not be achieved by us. In future we wish to add this feature to make it more reliable and secure for the users.
* **ADMIN PANEL:** We aim to add this feature which will supervise the entries, usage and the requirements of the user. The admin will keep the project up to date.
* **VERSATILE PROJECT:** We aim to convert this into a complete app which can be used by any organization, park, institute or mall.
* **ADDITION OF QUERY BOX:** To make it user-friendly, we can add a query box where user can seek assistance from the AI linked.

---

### OOP CONCEPTS USED
* **Classes and Objects:** A class is the blueprint for the objects. Item is the main class through which all the classes extend. We have created classes such as “Item” and its further subclasses “Lost Item” and “Found Item”. The objects are the items that are reported in lost or found items when the details are given by the user and he clicks the submit button, an object is created such as Calculator.
* **Encapsulation:** Some information is hidden from other classes to ensure smooth flow of the project and keep the information organized. It makes sure that limited access is given to other classes. Getters are used to access the private attributes. It is used so that some other class such as GUI.java should not change specific attributes like the ID of the lost or found item.
* **Inheritance:** It is an important feature of OOP. It allows the reusability of the code and prevents the repetition of the same code. In our code Item is the parent class and the lost item class inherits from the item class. Similarly, found item class inherits from item class and further all the category classes inherit from found item class. This is because the item class has some attributes that every child class uses too.
* **Polymorphism:** It allows the program to use the method with same name differently. In our project the method of verifying claims is overridden in all the category classes. But in all the classes different attributed of the item are verified such as in Category 2, the name and model of gadgets are verified and in Category 3, the name of the documents is verified.
* **Abstraction:** It is like a template. The code never has a generic item because the Item class is made abstract. The object is always a specific category. Similarly in the method of questions for item claim, we do not give them questions but it is forced to make questions for specific categories.

---

### TEAM MEMBERS & RESPONSIBILITIES

**SUMMIYA AURANGZEB (546507)** GUI.  
Made the prototype.  
Assisted in image handling.  
Made logic for found item class.  
Logic to store and display found items.  
Mentioned basic flow and features in document.

**SARAH SHAHID (546132)** Made category classes.  
Logic to claim found items.  
File handling for permanent storage.  
Made the flowchart.  
Made the UML diagram.

**ZOHA AHSAN (545764)** Made the logic for Lost Item class.  
Made the logic to store the lost items in different categories and filter them by active and expired items.  
Mentioned the OOP concepts and prototype in the document.  
Logic for image handling.  
Maintained Read-me in GitHub.

---

### PROJECT DELIVERABLES
* **Final Report:** Final Project report with detailed overview of the project. It includes key features, OOP principles, basic structure and future improvements that can be made in the project. Flowchart, UML diagram and the prototype of final project are also added in the report.
* **Plagiarism Report:** To ensure the academic integrity of the project, the final report was tested for plagiarism.
* **Code:** GitHub repo link is shared that contains all the source files. It has a detailed readme to ensure smooth user experience with the project. It has series of commits so the progress of the team members can be monitored.
