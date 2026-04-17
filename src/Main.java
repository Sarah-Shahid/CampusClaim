public class Main {
    public static void main(String[] args) {
        //load counter so IDs starts from where it 
        //was left off when program was last terminated.
        int savedCounter = FileHandler.loadCounter();
        Item.setCounter(savedCounter);

        // 1. create storage for both lost and found items
        FoundItemStorage foundStorage = new FoundItemStorage();
        LostItemStorage  lostStorage  = new LostItemStorage();

        foundStorage.loadData(FileHandler.loadFoundItems());
        lostStorage.loadData(FileHandler.loadLostItems());
        ItemService itemService = new ItemService(foundStorage, lostStorage);

        ConsoleUI ui = new ConsoleUI(itemService);

        //start the program
        //loop will continue until user exits
        ui.start();

        //save all data from memory when user exits.
        //call savefiles
        FileHandler.saveFoundItems(foundStorage.getAllItemsForSave());
        FileHandler.saveLostItems(lostStorage.getAllItemsForSave());
        FileHandler.saveCounter(Item.getCounter());
    }
}
