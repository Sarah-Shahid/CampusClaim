// //import java.time.LocalDate;
// //zoha
// public class LostItem extends Item {
//     //lost items cannot be claimed.
//     //lost item should have hidden details. make it either using another class or use hashmap class.
//     private User owner; //owner is the person who is posting their lost item
//     //                    they shud also post a picture.

  
  
//   private String ownerName;
//   private String ownerContact;   // phone or email

// public LostItem(String name, String description, String location, String category, String imagePath,String ownerName,String ownerContact){
//     super( name, description, location, category, imagePath);
//     this.ownerName = ownerName;
//     this.ownerContact  = ownerContact;
// }
//    // .....................Getters.........................................
//     public String getOwnerName()    { return ownerName; }
//     public String getOwnerContact() { return ownerContact; }
  
// @Override 
//     public String getsummarydata() {
//         return "LOST  | ID: " + getItemID() 
//              + " | Category: " + getCategory()
//              + " | Lost at: "  + getLocation()
//              + " | Date: "     + getDate()
//              + " | Owner: "    + ownerName
//              + " | Contact: "  + ownerContact
//              + " | Description: "     + getDescription();
//     }  
  
  
  
  
  
  
  
  
//     /*  @Override
//     public String getsummarydata()
//     {
//         //display those details here that would be visible in the list on mainmenu
//         return new Object[] {
//             getItemID(),
//             getName(),
//             getCategory(),
//             getLocation(),
//             getDate(),
//         };
//     }

//     public Object[] getdetails()
//     {
//         //return those details here that would be visible when an item is clicked
//         //return image, owner name and his contact info and maybe email?
//         return new Object[] {
//             getImagePath(), //for showing image of the lost item.
//             owner.getName(),
//             owner.getContact(),
//             owner.getEmail()
//         };
//     }
// */
// }
