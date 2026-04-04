//import java.util.LinkedHashMap;
//import java.util.Map;
import java.util.Scanner;

//this class is just to see everything is working after each step.
public class TestRunner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner (System.in);
        System.out.println("select item category:");
        System.out.println("1. Electronics");
        System.out.println("2. Accessories");       //caps, glasses, umbrella etc
        System.out.println("3. Clothing");
        System.out.println("4. Documents");
        System.out.println("5. Keys");
        System.out.println("6. Bags");   
        System.out.println("7. Jewelry");
        System.out.println("8. Books");  

        int choice = Integer.parseInt(scanner.nextLine().trim());

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

    }

    
}
