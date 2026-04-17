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

    // storage passed in from Main — not created here
    private FoundItemStorage foundStorage;
    private LostItemStorage lostStorage;

    public ItemService(FoundItemStorage foundStorage, LostItemStorage lostStorage) {
        this.foundStorage = foundStorage;
        this.lostStorage  = lostStorage;
    }

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

    public void setBasicFields (FoundItem item, String name, String location, String category, String finderContact, String finderName) {
        item.setName(name);
        item.setLocation(location);
        item.setCategory(category);
        item.setDate(LocalDate.now());
        item.setFinderName(finderName);
        item.setFinderContact(finderContact);
    }

    public void registerFoundItem (Map <String , String> answers, FoundItem item) {
        item.registerDetails(answers);
        foundStorage.addFoundItem(item); //item will be added to both arrayLists.
    }

    //calling the alr made methods
    public ArrayList <FoundItem> getAvailableFoundItems () {
        return foundStorage.getAllItems();
    }
    
    public FoundItem findFoundItemByID(int id) {
        return foundStorage.findbyID(id); 
    }

    public String processClaim(int id, Map<String, String> claimantAnswers) {
        FoundItem item = foundStorage.findbyID(id);

        if (item == null)        return "Item is not found!";
        if (item.getIsClaimed()) return "The item is already claimed!";

        boolean approved = item.verifyClaims(claimantAnswers);

        if (approved) {
            foundStorage.markAsClaimed(id); 
            return "THE CLAIM IS APPROVED";
        }
        return "THE CLAIM IS REJECTED";
    }

    public ArrayList<FoundItem> getClaimedItems() {
        return foundStorage.getClaimedItems();
    }

    // lost item methods
    public LostItem registerLostItem(String name, String location, String category,
                                      String ownerName, String ownerContact,
                                      String description, String imagePath) {
        LostItem item = new LostItem(name, location, category, ownerName, ownerContact, description, imagePath);
        lostStorage.addLostItem(item);
        return item;
    }

    public ArrayList<LostItem> getActiveLostItems() {
        return lostStorage.getActiveItem();
    }

    public ArrayList<LostItem> getExpiredLostItems() {
        return lostStorage.getExpiredItem();
    }

    public ArrayList<LostItem> getActiveLostByCategory(String category) {
        return lostStorage.getActiveByCategory(category);
    }

    public ArrayList<LostItem> getExpiredLostByCategory(String category) {
        return lostStorage.getExpiredByCategory(category);
    }
}