import java.util.ArrayList;
/*Purpose of this class:
1) Adds an item to the FindItem list
2) find the item by id
3) Display the list of found item that were ever there, even those who got claimed
4) To set the items claimed as claimed items 
5) To access the Un-claimed items in found

*/
public class FoundItemStorage {
    //Declaration:
    // original record that will be changed 
    ArrayList<FoundItem> foundItemsarray= new ArrayList<>(); //for founditem storage
    // permanent record : a copy that will not be changed to keep rack of original
    ArrayList<FoundItem> historyArray = new ArrayList<>();

    //METHODS
    //1) Adds an item to the FindItem list
    public void addFoundItem(FoundItem founditem){
        foundItemsarray.add(founditem);
        historyArray.add(founditem); //adds in both lists
    }

    //2) To find the item using id
    public FoundItem findbyID(int id){
        for (FoundItem item : foundItemsarray ){ //enhanced for loop
            if(id == item.getItemID()){
                return item;  //searches only the original copy 
            }
        }
            //no item with this id exists.
            return null;
        }

    //3) Display the list of found item that were ever there, even those that got claimed
    public ArrayList<FoundItem> historyoffoundItem(){
        return new ArrayList<>(historyArray); //makes a copy of the history arraylist
    }

    //4) To set the items claimed as claimed items 
    public boolean markAsClaimed(int id){
        FoundItem item = findbyID(id);
        if(!(item == null)){
            item.setIsClaimed(true);
            foundItemsarray.remove(item); //removes that item from found list
            return true;
        }
        return false;
    }

    //5) To access the Un-claimed items in found
    public ArrayList<FoundItem> getAllItems() {
        return foundItemsarray; //showing the original
    }
}
