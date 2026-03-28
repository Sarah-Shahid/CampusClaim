//import java.time.LocalDate;

public class LostItem extends Item {
    //lost items cannot be claimed.
    //lost item should have hidden details. make it either using another class or use hashmap class.
    private User owner; //owner is the person who is posting their lost item
    //                    they shud also post a picture.

    public Object[] getsummarydata()
    {
        //display those details here that would be visible in the list on mainmenu
        return new Object[] {
            getItemID(),
            getName(),
            getCategory(),
            getLocation(),
            getDate(),
        };
    }

    public Object[] getdetails()
    {
        //return those details here that would be visible when an item is clicked
        //return image, owner name and his contact info and maybe email?
        return new Object[] {
            getImagePath(), //for showing image of the lost item.
            owner.getName(),
            owner.getContact(),
            owner.getEmail()
        };
    }
}
