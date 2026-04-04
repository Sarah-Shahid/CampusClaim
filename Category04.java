import java.util.LinkedHashMap;
import java.util.Map;

public class Category04 extends FoundItem {

    //private String itemType;        // ring, watch, bracelet, wallets
    private String colour;           //gold, silver, redgold, colourful
    private String material;        // metal, gold, silver leather
    private String description;     // any engraving, gemstone, unique feature

    // Getters
    // public String getItemType() {
    //    return itemType;
    // }

    public String getColour() {
       return colour;
    }

    public String getMaterial() {
       return material;
    }   

    public String getDescription() {
      return description;
    }

    // Setters
    // public void setItemType(String itemType) {
    //   this.itemType = itemType;
    // }

    public void setColour(String colour) {
      this.colour = colour;
    }

    public void setMaterial(String material) {
      this.material = material;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    //make methods.
    @Override
    public void registerDetails(Map <String , String> details)  
    {
        //this stores the real answers.
        this.colour = details.getOrDefault("colour", "").toLowerCase().trim();
        this.material = details.getOrDefault("material", "").toLowerCase().trim();
        this.description = details.getOrDefault("description", "").toLowerCase().trim();
    }

    @Override   //returns the questions to be asked.
    public Map <String, String> getValidationQuestions ()   
    {
        Map <String , String> questions = new LinkedHashMap<>();
        questions.put("colour", "What is the colour of this item?");
        questions.put("material", "What is the material of this item?");
        questions.put("description", "Enter description (any engraving, gemstone, unique feature).");
        return questions;
    }


    @Override
    public boolean verifyClaims (Map <String , String> claimantAnswers)
    {
        Map <String , String> realAnswers = new LinkedHashMap <> ();
        realAnswers.put("colour",   this.colour);
        realAnswers.put("material",    this.material);
        realAnswers.put("description",     this.description);

        return validateWithScore(claimantAnswers, realAnswers, 2);
    }
}
