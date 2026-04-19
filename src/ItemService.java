import java.time.LocalDate;
import java.util.Map;
import java.util.ArrayList;

public class ItemService {

    private FoundItemStorage foundStorage;
    private LostItemStorage lostStorage;

    // counters for system report on dashboard
    private int totalFoundRegistered = 0;
    private int totalLostRegistered  = 0;
    private int totalClaimed         = 0;

    public ItemService(FoundItemStorage foundStorage, LostItemStorage lostStorage) {
        this.foundStorage = foundStorage;
        this.lostStorage  = lostStorage;
    }

    // ── counter setters (called from Main after loading saved data) ──
    public void setTotalFoundRegistered(int val) { this.totalFoundRegistered = val; }
    public void setTotalLostRegistered(int val)  { this.totalLostRegistered  = val; }
    public void setTotalClaimed(int val)         { this.totalClaimed         = val; }

    // ── counter getters (used by GUI for dashboard) ──
    public int getTotalFoundRegistered() { return totalFoundRegistered; }
    public int getTotalLostRegistered()  { return totalLostRegistered;  }
    public int getTotalClaimed()         { return totalClaimed;         }

    public FoundItem createFoundItem(int choice) {
        return switch (choice) {
            case 1 -> new Category02(); // Electronics
            case 2 -> new Category04(); // Accessories
            case 3 -> new Category01(); // Clothing
            case 4 -> new Category03(); // Documents
            case 5 -> new Category01(); // Keys
            case 6 -> new Category01(); // Bags
            case 7 -> new Category04(); // Jewelry
            case 8 -> new Category03(); // Books
            default -> null;
        };
    }

    public void setBasicFields(FoundItem item, String name, String location, String category,
                                String finderContact, String finderName) {
        item.setName(name);
        item.setLocation(location);
        item.setCategory(category);
        item.setDate(LocalDate.now());
        item.setFinderName(finderName);
        item.setFinderContact(finderContact);
    }

    public void registerFoundItem(Map<String, String> answers, FoundItem item) {
        item.registerDetails(answers);
        foundStorage.addFoundItem(item);
        totalFoundRegistered++; // increment counter
    }

    public ArrayList<FoundItem> getAvailableFoundItems() {
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
            totalClaimed++; // increment counter
            return "THE CLAIM IS APPROVED";
        }
        return "THE CLAIM IS REJECTED";
    }

    public ArrayList<FoundItem> getClaimedItems() {
        return foundStorage.getClaimedItems();
    }

    // ── Lost item methods ──
    public LostItem registerLostItem(String name, String location, String category,
                                      String ownerName, String ownerContact,
                                      String description, String imagePath) {
        LostItem item = new LostItem(name, location, category, ownerName, ownerContact, description, imagePath);
        lostStorage.addLostItem(item);
        totalLostRegistered++; // increment counter
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
