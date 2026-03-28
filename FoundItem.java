

public class FoundItem extends Item {

    User finder;
    private Boolean isClaimed = false; //default value of allposted items in the found menu is false.
    //when this flag become true, move it to the claimed class.


    //found item should have hidden details. make it either using another class or use hashmap class.
    public Object[] getsummarydata()
    {
        //display those details here that would be visible in the list on mainmenu
        return new Object[] {
            getItemID(),
            getName(),
            getCategory(),
            getLocation(),
            getDate()
            //all items in founditems are available for claim as claim items are moved it claimed class.
        };
    }

    public Object[] getdetails()
    {
        //return those details here that would be visible when an item is clicked
        //return image, owner name and his contact info and maybe email?
        return new Object[] {
            finder.getName(),
            finder.getContact(),
            finder.getEmail()
            //there would be a button to claim this found item.
            //when it is clicked, the other classes that will validate that claim will be used.
        };
    }

}
