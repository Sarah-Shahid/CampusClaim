//import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class Category01 extends FoundItem {
    //includes bags, keys and clothing
    //attributes that are stored when item is registered.
    private String colour;
    private String brand;
    //private String size;
    private String mark;
    
    // Getters
    public String getColour() {
        return colour;
    }
    public String getBrand() {
        return brand;
    }
    public String getMark() {
        return mark;
    }  

    // Setters
    public void setColour(String colour) {
        this.colour = colour;
    }
    public void setBrand(String brand) {
    this.brand = brand;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }

    //make methods.
    @Override
    public void registerDetails(Map <String , String> details)  
    {
        //this stores the real answers.
        this.colour = details.getOrDefault("colour", "").toLowerCase().trim();
        this.brand = details.getOrDefault("brand", "").toLowerCase().trim();
        this.mark = details.getOrDefault("mark", "").toLowerCase().trim();
        //use lowercase and trim function here so that later onthe user answers can be matched easily
        //even if the case or spaces dont exactly match.
    }

    @Override   //returns the questions to be asked.
    public Map <String, String> getValidationQuestions ()   
    {
        Map <String , String> questions = new LinkedHashMap<>();
        questions.put("colour", "What is the primary colour?");
        questions.put("brand", "What is the brand?");
        questions.put("mark", "What is one distinguishing mark?");
        return questions;

    }

    // 3. Called after claimant answers
    //    Compares claimant's answers against stored reporter answers
    //    called in claimMenu
    @Override
    public boolean verifyClaims (Map <String , String> claimantAnswers)
    {
        Map <String , String> realAnswers = new LinkedHashMap <> ();
        realAnswers.put("colour",   this.colour);
        realAnswers.put("brand",    this.brand);
        realAnswers.put("mark",     this.mark);

        return validateWithScore(claimantAnswers, realAnswers, 2);
    }

}
