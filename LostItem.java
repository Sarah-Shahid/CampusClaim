import java.time.LocalDate;
public class LostItem extends Item {
    //private attributes
   private String ownerName;
   private String ownerContact;
   private String description;
   private String status; //either "Lost" or "Expired"
   private String imagePath;
   
   //constant attributes
   public static final String Status_Lost = "Lost";
   public static final String Status_Expired = "Expired";

   // ....................Constructors........................
   //Empty constructor(will be used later for filr handling)
   public LostItem(){
    super();
    this.status = Status_Lost;
   }
   
   //constructor with attributes
   public LostItem(String name, String location, String category, String ownerName, String ownerContact, String description, String status, String imagePath){
    super(name, location, category);
    this.ownerName = ownerName;
    this.ownerContact = ownerContact;
    this.description = description;
    this.status = Status_Lost; //always starts as "Lost"
    this.imagePath = imagePath;

   }
   
    // .....................Getters............................
    public String getOwnerName(){
        return ownerName;
    }
    public String getOwnerContact(){
        return ownerContact;
    }
    public String getDescription(){
        return description;
    }
    public String getStatus(){
        return status;
    }
    public String getImagePath(){
        return imagePath;
    }

    // .....................Setters.........................................
    public void setOwnerName(String ownerName){
         this.ownerName = ownerName;
    }
    public void setOwnerContact(String ownerContact){
         this.ownerContact = ownerContact;
    }
    public void setDescription(String  description){
         this.description = description;
    }
    public void setImagePath(String imagePath){
         this.imagePath = imagePath;
    }
    
    // .................Expiry............................
    // checks if 30 days have passed since the item was reported
    // returns true if expired, false if still active
    public boolean isExpired() {
        return LocalDate.now().isAfter(getDate().plusDays(30));
    }
    
    // used when the list will be displayed. Only shows item if it was reported less than 30 days ago
    public boolean isActive() {
        return !isExpired(); //active means not expired
    }
  
 @Override 
     public String getsummarydata() {
        //if 30 days have passed it will show "Expired" otherwise the status is still "Lost"
        String displayStatus = isExpired() ? Status_Expired : Status_Lost;
               return "LOST  | ID: " + getItemID()
              + " | Name: "        + getName() 
              + " | Category: " + getCategory()
              + " | Lost at: "  + getLocation()
              + " | Date: "     + getDate()
              + " | Status: "      + displayStatus
              + " | Owner: "    + ownerName
              + " | Contact: "  + ownerContact
              + " | Description: "     + getDescription();
     } 

     // full details shown when item is clicked in console menu. Each field has its own line
    public String getDetails() {
    return "  ID      : " + getItemID()  + "\n"
         + "  Name    : " + getName()    + "\n"
         + "  Owner   : " + ownerName    + "\n"
         + "  Contact : " + ownerContact + "\n"
         + "  Image   : " + (imagePath != null ? imagePath : "None");
    }
}