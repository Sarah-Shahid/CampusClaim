import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private static final String FOUND_FILE    = "foundItems.ser";
    private static final String LOST_FILE     = "lostItems.ser";
    private static final String COUNTER_FILE  = "counter.txt";
    private static final String COUNTERS_FILE = "statCounters.txt"; // found/lost/claimed counts

    // ── SAVE ────────────────────────────────────────────────────────

    public static void saveFoundItems(ArrayList<FoundItem> items) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(FOUND_FILE))) {
            out.writeObject(items);
        } catch (IOException e) {
            System.out.println("Error saving found items: " + e.getMessage());
        }
    }

    public static void saveLostItems(ArrayList<LostItem> items) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(LOST_FILE))) {
            out.writeObject(items);
        } catch (IOException e) {
            System.out.println("Error saving lost items: " + e.getMessage());
        }
    }

    public static void saveCounter(int counter) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COUNTER_FILE))) {
            writer.println(counter);
        } catch (IOException e) {
            System.out.println("Error saving counter: " + e.getMessage());
        }
    }

    // saves found/lost/claimed stat counters
    public static void saveCounters(int totalFound, int totalLost, int totalClaimed) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COUNTERS_FILE))) {
            writer.println(totalFound);
            writer.println(totalLost);
            writer.println(totalClaimed);
        } catch (IOException e) {
            System.out.println("Error saving stat counters: " + e.getMessage());
        }
    }

    // ── LOAD ────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    public static ArrayList<FoundItem> loadFoundItems() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(FOUND_FILE))) {
            return (ArrayList<FoundItem>) in.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading found items: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<LostItem> loadLostItems() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(LOST_FILE))) {
            return (ArrayList<LostItem>) in.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading lost items: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static int loadCounter() {
        try (BufferedReader reader = new BufferedReader(new FileReader(COUNTER_FILE))) {
            return Integer.parseInt(reader.readLine().trim());
        } catch (FileNotFoundException e) {
            return 100;
        } catch (IOException e) {
            System.out.println("Error loading counter: " + e.getMessage());
            return 100;
        }
    }

    // returns [totalFound, totalLost, totalClaimed]
    public static int[] loadCounters() {
        try (BufferedReader reader = new BufferedReader(new FileReader(COUNTERS_FILE))) {
            int found   = Integer.parseInt(reader.readLine().trim());
            int lost    = Integer.parseInt(reader.readLine().trim());
            int claimed = Integer.parseInt(reader.readLine().trim());
            return new int[]{found, lost, claimed};
        } catch (FileNotFoundException e) {
            return new int[]{0, 0, 0};
        } catch (IOException e) {
            System.out.println("Error loading stat counters: " + e.getMessage());
            return new int[]{0, 0, 0};
        }
    }
}
