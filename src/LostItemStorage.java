import java.util.ArrayList;
/* purpose of this class:
1) Stores lost items in two specific categories: Active and Expired.
2) Automatically moves items from Active to Expired based on the 30-day rule.

*/
public class LostItemStorage {
    //lost items that are still active
    private ArrayList<LostItem> activeItems = new ArrayList<>(); 
    
    //lost items that have expired after 30 days
    private ArrayList<LostItem> expiredItems = new ArrayList<>();

    //adding the lost item in active items
    public void addLostItem(LostItem item) {
        activeItems.add(item);
    }
    public void refreshLists() {
        //A temporary list to hold items that need to be move. It will match the date 
        ArrayList<LostItem> itemsToMove = new ArrayList<>(); //new list

        for (LostItem item : activeItems) {
            //checks if item is expired, if yes it moved in refresh list
            if (item.isExpired()) {
                itemsToMove.add(item);
            }
        }
        //removing expired item from active list and move to expired list
        for (LostItem item : itemsToMove) {
            activeItems.remove(item);
            expiredItems.add(item);
        }
    }
    //get only items reported within 30 days
    public ArrayList<LostItem> getActiveItem(){
        //always refresh the list first 
        refreshLists();
        return activeItems;
    }
    //get only items reported after 30 days
    public ArrayList<LostItem> getExpiredItem(){
        //always refresh the list first 
        refreshLists();
        return expiredItems;
    }

    //-----------filter active items by category--------------
     public ArrayList<LostItem> getActiveByCategory(String category) {

        // always refresh first so expired items are already moved out
        refreshLists();

        ArrayList<LostItem> matchingActive = new ArrayList<>();

        // loop through every active item
        for (LostItem item : activeItems) {

            // check if category matches what is requested
            // equalsIgnoreCase handles "electronics" and "Electronics"
            if (item.getCategory().equalsIgnoreCase(category)) {

                //ifmatch found add it to result
                matchingActive.add(item);
            }
            //if no match then skip to next item
        }
        return matchingActive;
    }

    //-----------filter expired items by category--------------
    public ArrayList<LostItem> getExpiredByCategory(String category) {
        refreshLists();

        // list to hold matching expired items
        ArrayList<LostItem> matchingExpired = new ArrayList<>();

        // loop through expired items
        for (LostItem item : expiredItems) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                matchingExpired.add(item);
            }
        }

        return matchingExpired;
    }
}