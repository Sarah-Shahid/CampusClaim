//this serves as the class that will link gui to our logic, because with gui, you cant use
//console and scanner and println statements.

//this is the class consoleUI uses to access data and storage.
//will also be used by GUI class using the same methods defined here
//which are also being called by consoleUI class

//FOR EXAMPLE: for claimItem Menu:
//1. "Enter item ID:"                 → TALK TO USER   → ConsoleUI
// 2. read ID from scanner            → TALK TO USER   → ConsoleUI
// 3. find item by ID                 → LOGIC          → ItemService
// 4. check if already claimed        → LOGIC          → ItemService
// 5. get validation questions        → LOGIC          → ItemService
// 6. show questions, collect answers → TALK TO USER   → ConsoleUI
// 7. compare answers                 → LOGIC          → ItemService
// 8. mark as claimed                 → LOGIC          → ItemService
// 9. show result                     → TALK TO USER   → ConsoleUI

import java.time.LocalDate;
import java.util.Map;
import java.util.ArrayList;

public class ItemService {

    //make storage for both found items and lost items.
    FoundItemStorage foundStorage = new FoundItemStorage();
    LostItemStorage lostStorage = new LostItemStorage();


    //GUI buttons will later call these methods directly.

    public FoundItem createFoundItem (int choice) {
        return switch (choice) {
            case 1 -> new Category02(); // Electronics
            case 2 -> new Category04(); // accessories
            case 3 -> new Category01(); // clothing
            case 4 -> new Category03(); // documents
            case 5 -> new Category01(); // keys
            case 6 -> new Category01(); // bags
            case 7 -> new Category04(); // jewelry
            case 8 -> new Category03(); // books
            default -> null;
        };
    }

    public void setBasicFields (FoundItem item, String name, String location, String category,String finderContact, String finderName) {
        //ID STILL LEFT??
        item.setName(name);
        item.setLocation(location);
        item.setCategory(category);
        item.setDate(LocalDate.now());
        item.setFinderName(finderName);
        item.setFinderContact(finderContact);
    }

    public void registerFoundItem (Map <String , String> answers, FoundItem item) {
        item.registerDetails(answers);
        foundStorage.addFoundItemToLists(item); //item will be added to both arrayLists.
    }

    //calling the alr made methods
    public ArrayList <FoundItem> getAvailableFoundItems () {
        return foundStorage.getAllItems();
    }
    
    public FoundItem findFoundItemByID(int id) {
    return foundStorage.findbyID(id); 
    }

    public String processClaim(int id, Map<String, String> claimantAnswers) {
    FoundItem item = foundStorage.findbyID(id); // directly using yours

    // validation checks

    if (item == null)        return "Item is not found!";
    if (item.getIsClaimed()) return "The item is already claimed!";
        
    //to see the approval or rejection of validation questions

    boolean approved = item.verifyClaims(claimantAnswers);

    if (approved) {
        foundStorage.markAsClaimed(id); 
        return "THE CLAIM IS APPROVED";
    }
    return "THE CLAIM IS REJECTED";
}
    
}
