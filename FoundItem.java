/*fields are split into two groups:
 *   PUBLIC  – shown on the listing board (category, location, date)
 *   SECRET  – hidden from claimants- used only during validation
 *             (colour, brand, identityMark)
 * */

public class FoundItem extends Item {

//.............................Attributes............

//..........................hidden from the user for validation..................
    private String colour;           // major colour of the object 
    private String brand;            // what brand/model is it of (none for things without a brand)
    private String identityMark;     // major identity mark to validate 


    
//..........................finder information..................  
    private String finderName;
    private String finderContact;
    private Boolean isClaimed = false;
    
    


//.........................constructors................
// set values of attributes 
public FoundItem(String name, String description, String location, String category, String imagePath,String colour,String brand,String identityMark,String finderName, String finderContact){
    super( name, description, location, category, imagePath);
    this.colour = colour;
    this.brand  = brand;
    this.identityMark = identityMark;
    this.finderName   = finderName;
    this.finderContact   = finderContact;
}

//..........................getters..................
    public String getColour()             { return colour; }
    public String getBrand()              { return brand; }
    public String getidentityMark() { return identityMark; }    
    public String getFinderName()         { return finderName; }
    public String getFinderContact()      { return finderContact; }
    public Boolean getIsClaimed()      { return isClaimed; }

//..........................setters..................
    public void setIsClaimed(Boolean claimed) {  this.isClaimed = claimed; }
    
//..........................methods..................
   
@Override //overrides the abstarct method in item class
    public String getsummarydata() {
        return "FOUND | ID: "     + getItemID()
             + " | Category: "    + getCategory()
             + " | Found at: "    + getLocation()
             + " | Date: "        + getDate()
             + " | Status: "      + getIsClaimed(); //correct it sarah acc to the logic
    }

    /* Full details shown ONLY after passing validation. */
    public String getFullDetails() {
        return "++++++Complete Details Of The Item+++++"
             + "\n  ID          : " + getItemID()
             + "\n Category    : " + getCategory()
             + "\n  Found at    : " + getLocation()
             + "\n  Date        : " + getDate()
             + "\n  Description : " + getDescription()
             + "\n  Colour      : " + getColour()
             + "\n  Brand       : " + getBrand()
             + "\n  Mark        : " + getidentityMark()
             + "\n  Finder      : " + getFinderName()
             + "\n  Status      : " + getIsClaimed();
    }


     //User finder;
    //default value of allposted items in the found menu is false.
    //when this flag become true, move it to the claimed class.


    /*//found item should have hidden details. make it either using another class or use hashmap class.
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
*/
}
