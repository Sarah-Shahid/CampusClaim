import java.util.LinkedHashMap;
import java.util.Map;

public class Category02 extends FoundItem {
    //for all gadgets
    //for electronics, phones, laptops
    //private String deviceType;
    private String colour;
    private String brand; //apple samsung
    private String model; //iPhone 13 vs 15 etc
    private String identifyingMark;

    // Getters
    // public String getDeviceType() {
    //     return deviceType;
    // }
    public String getColour() {
        return colour;
    }
    public String getBrand() {
        return brand;
    }
    public String getModel() {
        return model;
    }
    public String getIdentifyingMark() {
        return identifyingMark;
    }

    // Setters
    // public void setDeviceType(String deviceType) {
    //     this.deviceType = deviceType;
    // }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setIdentifyingMark(String identifyingMark) {
        this.identifyingMark = identifyingMark;
    }

    //make methods.
    @Override
    public void registerDetails(Map <String , String> details)  
    {
        //this stores the real answers.
        this.colour = details.getOrDefault("colour", "").toLowerCase().trim();
        this.brand = details.getOrDefault("brand", "").toLowerCase().trim();
        this.model = details.getOrDefault("model", "").toLowerCase().trim();
        this.identifyingMark = details.getOrDefault("identifyingMark", "").toLowerCase().trim();

    }

    @Override   //returns the questions to be asked.
    public Map <String, String> getValidationQuestions ()   
    {
        Map <String , String> questions = new LinkedHashMap<>();
        questions.put("colour", "What is the primary colour?");
        questions.put("brand", "What is the brand?");
        questions.put("model", "What is the model?");
        questions.put("identifyingMark", "What is one distinguishing mark (a sticker or logo)?");
        return questions;
    }

    @Override
    public boolean verifyClaims (Map <String , String> claimantAnswers)
    {
        Map <String , String> realAnswers = new LinkedHashMap <> ();
        realAnswers.put("colour",   this.colour);
        realAnswers.put("brand",    this.brand);
        realAnswers.put("model",     this.model);
        realAnswers.put("identifyingMark",     this.identifyingMark);

        return validateWithScore(claimantAnswers, realAnswers, 3);
    }

}
