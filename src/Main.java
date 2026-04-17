// Main.java — wires everything together
public class Main {
    public static void main(String[] args) {

        // 1. create storage
        FoundItemStorage foundStorage = new FoundItemStorage();
        LostItemStorage  lostStorage  = new LostItemStorage();

        // 2. pass storage into ItemService
        ItemService itemService = new ItemService(foundStorage, lostStorage);

        // 3. pass ItemService into ConsoleUI
        ConsoleUI ui = new ConsoleUI(itemService);

        // 4. start the program
        ui.start();
    }
}
