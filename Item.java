import java.time.LocalDate;

public abstract class Item {
    private static int counter = 100;
    private int itemID;
    private String name;
    private String location;
    private String category;
    private LocalDate date;


    private String description;
    private String imagePath;
    //private String owner;  //do we need this attribute? what shud be the type.

    //make constructors.
    Item () {} //no arg constructor
    Item (String name, String description, String location, String category, String imagePath)
    {
        itemID = (++counter); //later fix: save it to file handling so the counter doesnt start
        //                       from 100 again when u restart the program.
        this.name = name;
        this.description = description;
        this.location = location;
        this.category = category;
        this.imagePath = imagePath;
        date = LocalDate.now(); //this would make an instant of the current date 
        //owner? shud it be from class User?
    }

    //make getters setters for all attributes.

    public int getItemID() { return itemID; }
    public void setItemID(int itemID) {
    this.itemID = itemID;
    }

    public String getName () {return name;}
    public void setName (String name)
    {
        this.name = name;
    }

    public String getDescription() {
    return description;
    }
    public void setDescription(String description) {
    this.description = description;
    }

    public String getLocation () {return location;}
    public void setLocation (String location)
    {
        this.location = location;
    }

    public String getCategory() {
    return category;
    }
    public void setCategory(String category) {
    this.category = category;
    }

    public LocalDate getDate () 
    {
        return date;
    }
    public void setDate (LocalDate date)
    {
        this.date = date;
    }

    //maybe make imagepath only specific to lost items.
    public String getImagePath() {
    return imagePath;
    }
    public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
    }

    //pending getter setter for owner.

    public abstract Object[] getsummarydata (); //abstract method means you dont need to define that method in the parent class, implement in subclasses.
    public abstract Object[] getdetails () ;                 
}
