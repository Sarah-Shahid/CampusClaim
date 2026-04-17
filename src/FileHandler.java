//this class saves and loads all data
//called by FoundItemStorage and LostItemStorage

//java .io library contains all interfaces and classes that can be used for
//input output operations and serialization
import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private static final String FOUND_FILE   = "foundItems.ser";
    private static final String LOST_FILE    = "lostItems.ser";
    private static final String COUNTER_FILE = "counter.txt";
    //for counter, just need to store one number so that counter wont restart
    //using .txt file cuz no objects involved.

    //saving methods. these are called when the program is exiting.
    public static void saveFoundItems(ArrayList<FoundItem> items) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(FOUND_FILE))) {
            out.writeObject(items);
            System.out.println("Found items saved.");
        } catch (IOException e) {
            System.out.println("Error saving found items: " + e.getMessage());
        }
    }

    public static void saveLostItems(ArrayList<LostItem> items) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(LOST_FILE))) {
            out.writeObject(items);
            System.out.println("Lost items saved.");
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

    //Loading methods, called when the program starts.
    //loads everthing from the files into the memory (ArrayLists) again.
    
    @SuppressWarnings("unchecked")
    public static ArrayList<FoundItem> loadFoundItems() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(FOUND_FILE))) {
            return (ArrayList<FoundItem>) in.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>(); // first run, no file yet
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
            return new ArrayList<>(); // first run, no file yet
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading lost items: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static int loadCounter() {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(COUNTER_FILE))) {
            return Integer.parseInt(reader.readLine().trim());
        } catch (FileNotFoundException e) {
            return 100; // first run, start from 100
        } catch (IOException e) {
            System.out.println("Error loading counter: " + e.getMessage());
            return 100;
        }
    }
}
