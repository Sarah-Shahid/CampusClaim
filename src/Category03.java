import java.util.LinkedHashMap;
import java.util.Map;

public class Category03 extends FoundItem {
    //ids, cards, books, passport
    //private String documentType;  //same as the name of object
    private String nameOnDocument;
    private String issuingAuthority;
    private String mark;

    // Getters
    // public String getDocumentType() {
    //     return documentType;
    // }

    public String getNameOnDocument() {
        return nameOnDocument;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public String getMark() {
        return mark;
    }

    // Setters
    // public void setDocumentType(String documentType) {
    //     this.documentType = documentType;
    // }

    public void setNameOnDocument(String nameOnDocument) {
        this.nameOnDocument = nameOnDocument;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
    
    //make methods.
    @Override
    public void registerDetails(Map <String , String> details)  
    {
        //this stores the real answers.
        this.nameOnDocument = details.getOrDefault("nameOnDocument", "").toLowerCase().trim();
        this.issuingAuthority = details.getOrDefault("issuingAuthority", "").toLowerCase().trim();
        this.mark = details.getOrDefault("mark", "").toLowerCase().trim();
    }

    @Override   //returns the questions to be asked.
    public Map <String, String> getValidationQuestions ()   
    {
        Map <String , String> questions = new LinkedHashMap<>();
        questions.put("nameOnDocument", "What is the name on this document?");
        questions.put("issuingAuthority", "What is the issuing authority of this document?");
        questions.put("mark", "Any identifying mark on the document? (sticker, fold, scratch, bent corner)");
        return questions;
    }

    @Override
    public boolean verifyClaims (Map <String , String> claimantAnswers)
    {
        Map <String , String> realAnswers = new LinkedHashMap <> ();
        realAnswers.put("nameOnDocument",   this.nameOnDocument);
        realAnswers.put("issuingAuthority",    this.issuingAuthority);
        realAnswers.put("mark",     this.mark);

        return validateWithScore(claimantAnswers, realAnswers, 2);
    }
}
