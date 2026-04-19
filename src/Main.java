import javafx.application.Application;

public class Main {
    public static void main(String[] args) {

        // 1. load counter so IDs start from where they left off
        int savedCounter = FileHandler.loadCounter();
        Item.setCounter(savedCounter);

        // 2. create storage
        FoundItemStorage foundStorage = new FoundItemStorage();
        LostItemStorage  lostStorage  = new LostItemStorage();

        foundStorage.loadData(FileHandler.loadFoundItems());
        lostStorage.loadData(FileHandler.loadLostItems());

        // 3. create service
        ItemService itemService = new ItemService(foundStorage, lostStorage);

        // 4. load saved counters
        int[] counters = FileHandler.loadCounters();
        itemService.setTotalFoundRegistered(counters[0]);
        itemService.setTotalLostRegistered(counters[1]);
        itemService.setTotalClaimed(counters[2]);

        // 5. pass service to GUI and launch
        GUI.setItemService(itemService);
        GUI.setFoundStorage(foundStorage);
        GUI.setLostStorage(lostStorage);

        Application.launch(GUI.class, args);
    }
}
