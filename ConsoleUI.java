import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;


//ONLY handles output/input.
//collects input, passes it to the itemService class which performs methods and returns value
//it then prints outputs
//MUST NOT contain ANY logic or any thing related to data or storage.

//TEMPORARY: Later replaced by GUI.

public class ConsoleUI {
    ItemService itemServiceObj;
    Scanner scanner;

    public ConsoleUI (ItemService itemServiceObj) {
        this.itemServiceObj = itemServiceObj; //to access data and actual storage of the program.
        scanner = new Scanner (System.in);  //for input.
    }

    public void start () {      //begins the menu loop.
        System.out.println ("========================");
        System.out.println ("        WELCOME");
        System.out.println ("========================");

        while (true) {      //becomes false when 0 is entered. and exits loop.

            System.out.println("1. Report Found Item");
            System.out.println("2. View Found Items");
            System.out.println("3. Claim Item");
            System.out.println("4. Report Lost Item");
            System.out.println("5. View Lost Items");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> menuReportFoundItem();      //done
                case "2" -> menuViewFoundItems();       //done
                //case "3" -> menuClaimItem();          //summiya
                //case "4" -> menuReportLostItem();     //zoha
                //case "5" -> menuViewLostItems();      //zoha
                case "0" -> {
                    System.out.println("THANKYOU. You are exiting the program.");
                    return;      //exits the start() method.
                }
                default -> System.out.println("INVALID choice. Try again.");
            }
        }
    }

    //make all 5 methods being used in menu.
    //only write input/output
    //implement logic through itemService class

    //ConsoleUI methods are private because only start() calls them,
    //from inside the same class.

    private void menuReportFoundItem() {
        System.out.println("Enter item name.");
        String name = scanner.nextLine().trim();
        System.out.print("Enter item location? ");
        String location = scanner.nextLine().trim();

        System.out.println("Select item category: ");
        System.out.println("1. Electronics");
        System.out.println("2. Accessories");       //caps, glasses, umbrella etc
        System.out.println("3. Clothing");
        System.out.println("4. Documents");
        System.out.println("5. Keys");
        System.out.println("6. Bags");   
        System.out.println("7. Jewelry");
        System.out.println("8. Books");  
    
        //ensure this input is a number.
        int choice;
        while (true) {
            System.out.print("Enter your choice (1-8): ");

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());

                if (choice >= 1 && choice <= 8) {
                    break; // valid input, exit loop
                } else {
                    System.out.println("Please enter a number between 1 and 8.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer between 1 and 8.");
            }
        }

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

        //using ItemService for logic
        FoundItem item = itemServiceObj.createFoundItem(choice);

        //get user info
        System.out.println("\n======= Getting Finder Information. =======");
        System.out.print("Please enter your name? ");
        String finderName = scanner.nextLine().trim();

        String finderContact;

        //validation for reporters contact number.
        while (true) {
        System.out.println("Please enter your contact number? (11 digits beginning from '03')");
        finderContact = scanner.nextLine().trim();

            if (finderContact.matches("03\\d{9}")) {
                break;
            } else {
                System.out.println("Invalid number. Enter a valid Pakistani mobile number.");
            }
        }

        //using ItemService method to set this items details.
        itemServiceObj.setBasicFields(item, name, location, category, finderContact, finderName);

        //now get questions specific to the category.
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

        //store these hidden details and store the item in arraylist
        itemServiceObj.registerFoundItem(answers, item);

        System.out.println("====== Item Successfully Registered. ======");
        System.out.println("  Item ID   : " + item.getItemID());    //LOGIC PENDING
        System.out.println("  Name      : " + item.getName());
        System.out.println("  Category  : " + item.getCategory());
        System.out.println("  Location  : " + item.getLocation());
        System.out.println("  Date      : " + item.getDate());

    }

    //displays all found items.
    private void menuViewFoundItems()  {
        System.out.println("\n=======================");
        System.out.println("    FOUND ITEMS LIST   ");
        System.out.println("=======================");

        //getting all unclaimed items using items service
        ArrayList <FoundItem> items = itemServiceObj.getAvailableFoundItems();

        //check if list is empty
        if (items.isEmpty()) { 
            System.out.println("No found items have been reported yet.");
            return;
        }

        //when list is non-empty
        for (FoundItem fitem : items) {
            System.out.println(fitem.getsummarydata());
            System.out.println("-----------------------");
        }

        System.out.println("TOTAL ITEMS: " + items.size());
    }



    private void menuClaimItem()       { 

    System.out.println("\n     CLAIM ITEM       ");
    

    // 1. show all available items so user knows what IDs exist
    ArrayList<FoundItem> items = itemServiceObj.getAvailableFoundItems();
    
    if (items.isEmpty()) {
        System.out.println("No items available to claim.");
        return;
    }
    
    for (FoundItem item : items) {
        System.out.println(item.getsummarydata());
        System.out.println("-----------------------");
    }

        FoundItem item = null;
        int id= 0;

        //loop thru till it gets a valid input
        while (item == null) {
        System.out.print("\nEnter item ID to claim: ");
        String input = scanner.nextLine().trim();

    // check manually if every character is a digit
    if (!input.matches("\\d+")) { // \\d means "a digit" (0-9). +  means "one or more"
        System.out.println("Invalid. Please enter a number.");
        continue; // go back to top of loop
    }

    id = Integer.parseInt(input);
    item = itemServiceObj.findFoundItemByID(id);

    if (item == null) {
        System.out.println("No item found with that ID. Try again.");
    }
        }

    // 4. ask validation questions ; category specific, using polymorphism
    Map<String, String> questions = item.getValidationQuestions();
    Map<String, String> claimantAnswers = new LinkedHashMap<>();

    System.out.println("\nAnswer the following questions to verify ownership:\n");
    for (Map.Entry<String, String> entry : questions.entrySet()) {
        System.out.print(entry.getValue() + " ");
        String answer = scanner.nextLine().trim();
        claimantAnswers.put(entry.getKey(), answer);
    }

    // 5. process; all logic handled in ItemService
    String result = itemServiceObj.processClaim(id, claimantAnswers);

    // 6. show result
    System.out.println("\n" + result);
    
    // if approved, show finder contact so they can collect item
    if (result.equals("THE CLAIM IS APPROVED")) {
        System.out.println(item.getFullDetails());
    }
    }      








    private void menuReportLostItem()  { }      //zoha
    private void menuViewLostItems()   { }      //zoha

}
