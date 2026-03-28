//every person who uses the application would first have to register
//store all user info using this class. also used for file handling.
//the item class need to use the user class aswell.

public class User {
    private String name;
    private String contact;
    private String email;

    //make constructors


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
