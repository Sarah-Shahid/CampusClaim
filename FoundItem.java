/*fields are split into two groups:
 *   PUBLIC  – shown on the listing board (id, category, location, date, title )
 *   SECRET  – hidden from claimants- used only during validation
 *             (colour, brand, identityMark), different for diff groups.
 * */
import java.util.Map;

public abstract class FoundItem extends Item {
    private Boolean isClaimed = false;
    //name, date, ID, location, category from item class
      
    //finder information
    private String finderName;
    private String finderContact;
    //private String finderEmail;
    
    //constructors //check again
    public FoundItem () {};
    public FoundItem(String name, String location, String category, String finderName, String finderContact){
    super(name, location, category);
    this.finderName   = finderName;
    this.finderContact   = finderContact;
    }

    //getters
    public String getFinderName()         { return finderName; }
    public String getFinderContact()      { return finderContact; }
    public Boolean getIsClaimed()      { return isClaimed; }

    //setters
    public void setIsClaimed(Boolean claimed) {  this.isClaimed = claimed; }
    
    public void setFinderName(String finderName) {
        this.finderName = finderName;
    }

    public void setFinderContact(String finderContact) {
        this.finderContact = finderContact;
    }
    
//..........................methods..................
   
//Reporter fills in the ANSWER sheet   →  registerDetails()
    //Claimant fills in the QUESTION sheet →  getVerificationQuestions()
    //Then system compares the two         →  verifyClaim()

    public abstract void registerDetails(Map <String , String> name);     //store right answers
    public abstract Map <String, String> getValidationQuestions ();       //decides what questions to ask.
    public abstract boolean verifyClaims (Map <String , String> claimantAnswers);   //returns true if claimant answers right.

    //all categories need to use this: so that
    //"sky blue"  should match  "blue"      
    //"NIKE"      should match  "nike"      
    //" black "   should match  "black" 
    public boolean softMatch(String userAnswer, String realAnswer)
    {
        if (userAnswer == null || realAnswer == null) return false; //answer not matching
        String real = realAnswer.toLowerCase().trim();
        String user = userAnswer.toLowerCase().trim();

        if (user.equals(real)) return true; //this answer matches
      
        for (String word : real.split(" "))
        {
            if (word.length() > 2 && user.contains(word)) return true;
        }
        return false;
    }

    //make a mathod that can validate claim only when you answer some right questions
    //according to different threshold for each class.
    public boolean validateWithScore (Map <String, String> userAnswers, Map <String, String> realAnswers, int threshold)
    {
        int correct = 0; //keeps track of how many questions claimant got right.
        for (String key : realAnswers.keySet())
        {
            String real = realAnswers.get(key);
            String user = userAnswers.get(key); //using key variable here to match
            //match done by key name, not position, more safe.
            if (softMatch(user, real))
            {
                correct ++;
            }
        }
        return correct >= threshold; //return true if user fulfilled the requirement.
    }

    @Override //overrides the abstarct method in item class
    public String getsummarydata() {
        return "FOUND | ID: "     + getItemID()
             + " | Name: "        + getName()
             + " | Category: "    + getCategory()
             + " | Found at: "    + getLocation()
             + " | Date: "        + getDate()
             + " | Status: "      + (getIsClaimed()? "CLAIMED" : "FOUND");
    }

    /* Full details shown ONLY after passing validation. */
    //claim validated ---> show this
    public String getFullDetails() {
        return "++++++Complete Details Of The Item+++++"
             + getsummarydata()
             + "\n  Finder         : " + getFinderName()
             + "\n  Finder Contact : " + getFinderContact();
            //maybe show its image too??

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
*/
}
