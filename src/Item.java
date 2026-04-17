import java.io.Serializable; //import required so it can implement this.
import java.time.LocalDate;

public abstract class Item implements Serializable {

    private static final long serialVersionUID = 1L; // version tag
    private static int counter = 100; // will be managed by FileHandler

    private int itemID;
    private String name;
    private String location;
    private String category;
    private LocalDate date;

    //private String owner;  //do we need this attribute? what shud be the type.

    //make constructors.
    Item () { itemID = (++counter);} //no arg constructor

    Item (String name, String location, String category)
    {
        itemID = (++counter); 
        this.name = name;
        this.location = location;
        this.category = category;
        date = LocalDate.now(); //this would make an instant of the current date 

        //owner? shud it be from class User?
    }

    //make getters setters for all attributes.

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int value) {
        counter = value;
    }

    public int getItemID() { return itemID; }
    public void setItemID(int itemID) {
    this.itemID = itemID;
    }

    public String getName () {return name;}
    public void setName (String name)
    {
        this.name = name;
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
    // public String getImagePath() {
    // return imagePath;
    // }
    // public void setImagePath(String imagePath) {
    // this.imagePath = imagePath;
    // }


    public abstract String getsummarydata (); //abstract method means you dont need to define that method in the parent class, implement in subclasses.
    //public abstract Object[] getdetails () ;                 
}
