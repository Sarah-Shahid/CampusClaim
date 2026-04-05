import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;

///guys, run this to see how its working on the console so far. its not formatted, just see the working of program.
/// keep note of any errors so we can fix later. (current problem = store item ids)

//this class is just to see everything is working after each step.
public class TestRunner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner (System.in);

        System.out.println("\n\nyoure posting a new found item.");
        System.out.print("Item name? ");
        String name = scanner.nextLine().trim();
        System.out.print("location? ");
        String location = scanner.nextLine().trim();

        System.out.println("select item category: ");
        System.out.println("1. Electronics");
        System.out.println("2. Accessories");       //caps, glasses, umbrella etc
        System.out.println("3. Clothing");
        System.out.println("4. Documents");
        System.out.println("5. Keys");
        System.out.println("6. Bags");   
        System.out.println("7. Jewelry");
        System.out.println("8. Books");  
        System.out.print("enter choice. ");
    

        int choice = Integer.parseInt(scanner.nextLine().trim());

        String category = switch (choice) {
            case 1 -> "Electronics";
            case 2 -> "Accessories";
            case 3 -> "Clothing";
            case 4 -> "Documents";
            case 5 -> "Keys";
            case 6 -> "Bags";
            case 7 -> "Jewelry";
            case 8 -> "Books";
            default -> "Unknown";
             
        };
        //this is how it decides what category of object it gets instantiated as.
        //the attribute category common to all items is also decided like this
        FoundItem item = switch (choice) {
            case 1 -> new Category02(); // Electronics
            case 2 -> new Category04(); // accessories
            case 3 -> new Category01(); // clothing
            case 4 -> new Category03(); // documents
            case 5 -> new Category01(); // keys
            case 6 -> new Category01(); // bags
            case 7 -> new Category04(); // jewelry
            case 8 -> new Category03(); // books
            default -> null;
        };

        if (item == null) {
            System.out.println("Invalid choice.");
            scanner.close();
            return; //being terminated
        }

        //get user info
        System.out.println("Getting finder info");
        System.out.println("your name?");
        String finderName = scanner.nextLine().trim();
        System.out.println("your contact?");
        String finderContact = scanner.nextLine().trim();

        item.setName(name);
        item.setLocation(location);
        item.setCategory(category);
        item.setDate(LocalDate.now());
        item.setFinderName(finderName);
        item.setFinderContact(finderContact);

        //now get questions specific to fields.
        Map <String, String> questions = item.getValidationQuestions();
        Map <String, String> answers = new LinkedHashMap<>(); //a place where real answers are stored.

        for (Map.Entry <String, String> entry : questions.entrySet())
        {
            String key = entry.getKey();
            String question = entry.getValue();
            
            System.out.println(question + " ");
            String answer = scanner.nextLine().trim();
            answers.put(key, answer);

        }
        //store these answers
        item.registerDetails(answers);
        System.out.println("\n✔ Item registered successfully!");
        System.out.println("  Item ID   : " + item.getItemID());
        System.out.println("  Name      : " + item.getName());
        System.out.println("  Category  : " + item.getCategory());
        System.out.println("  Location  : " + item.getLocation());
        System.out.println("  Date      : " + item.getDate());

        System.out.println("checking for right answers: (test run)");
        Map<String, String> claimantAnswers = new LinkedHashMap<>(); // ← separate map

        for (Map.Entry <String, String> entry : questions.entrySet())
        {
            String key = entry.getKey();
            String question = entry.getValue();
            
            System.out.println(question + " ");
            String answer = scanner.nextLine().trim();
            claimantAnswers.put(key, answer);
        }

        if (item.verifyClaims(claimantAnswers)) {
            System.out.println("Please come and get your item.");
            System.out.println("Finder Name: " + item.getFinderName());
            System.out.println("Finder Contact: " + item.getFinderContact());

        } else { System.out.println("ITs not your ITEM."); }

        scanner.close();
    }

}
