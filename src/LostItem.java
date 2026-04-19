import java.time.LocalDate;
public class LostItem extends Item {
    // fixed spelling: "Electonics" → "Electronics"
    public static final String Category_1 = "Physical Items";
    public static final String Category_2 = "Electronics";
    public static final String Category_3 = "Documents";
    public static final String Category_4 = "Valuables";

    private String ownerName;
    private String ownerContact;
    private String description;
    private String status;
    private String imagePath;

    public static final String Status_Lost    = "Lost";
    public static final String Status_Expired = "Expired";

    public LostItem() {
        super();
        this.status = Status_Lost;
    }

    public LostItem(String name, String location, String category, String ownerName,
                    String ownerContact, String description, String imagePath) {
        super(name, location, category);
        this.ownerName    = ownerName;
        this.ownerContact = ownerContact;
        this.description  = description;
        this.status       = Status_Lost;
        this.imagePath    = imagePath;
    }

    public String getOwnerName()    { return ownerName;    }
    public String getOwnerContact() { return ownerContact; }
    public String getDescription()  { return description;  }
    public String getStatus()       { return status;       }
    public String getImagePath()    { return imagePath;    }

    public void setOwnerName(String ownerName)       { this.ownerName    = ownerName;    }
    public void setOwnerContact(String ownerContact) { this.ownerContact = ownerContact; }
    public void setDescription(String description)   { this.description  = description;  }
    public void setImagePath(String imagePath)       { this.imagePath    = imagePath;    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(getDate().plusDays(30));
    }

    public boolean isActive() {
        return !isExpired();
    }

    @Override
    public String getsummarydata() {
        String displayStatus = isExpired() ? Status_Expired : Status_Lost;
        return "ID: "          + getItemID()
             + " | Name: "     + getName()
             + " | Category: " + getCategory()
             + " | Lost at: "  + getLocation()
             + " | Date: "     + getDate()
             + " | Status: "   + displayStatus;
    }

    public String getDetails() {
        String displayStatus = isExpired() ? Status_Expired : Status_Lost;
        return "  ID          : " + getItemID()    + "\n"
             + "  Name        : " + getName()      + "\n"
             + "  Category    : " + getCategory()  + "\n"
             + "  Lost at     : " + getLocation()  + "\n"
             + "  Date        : " + getDate()      + "\n"
             + "  Status      : " + displayStatus  + "\n"
             + "  Description : " + description    + "\n"
             + "  Owner       : " + ownerName      + "\n"
             + "  Contact     : " + ownerContact   + "\n"
             + "  Image       : " + (imagePath != null ? imagePath : "None");
    }
}
