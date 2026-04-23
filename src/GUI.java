import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GUI extends Application {

    // ------- Colour palette (from prototype) -------
    private static final String BG       = "#d7e4d9";
    private static final String ACCENT   = "#235347";
    private static final String ACCENT_L = "#235347CC"; // slightly transparent
    private static final String ACCENT_X = "#23534780"; // lighter transparent
    private static final String DARK_TXT = "#151f28";
    private static final String LIGHT_TXT= "#f4f6fc";

    // ------- service references injected from Main -------
    private static ItemService itemService;
    private static FoundItemStorage foundStorage;
    private static LostItemStorage lostStorage;

    public static void setItemService(ItemService s)    { itemService   = s; }
    public static void setFoundStorage(FoundItemStorage s){ foundStorage = s; }
    public static void setLostStorage(LostItemStorage s){ lostStorage   = s; }

    // ------- main stage / root container -------
    private Stage stage;
    private BorderPane root;
    private static final String FONT = "Playfair Display";

    // 8 found-item categories (mapped to ItemService.createFoundItem choice)
    private static final String[] FOUND_CATEGORIES = {
        "Electronics", "Accessories", "Clothing", "Documents",
        "Keys", "Bags", "Jewelry", "Books"
    };

    // 4 lost-item categories
    private static final String[] LOST_CATEGORIES = {
        LostItem.Category_1, LostItem.Category_2,
        LostItem.Category_3, LostItem.Category_4
    };

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG + ";");

        Scene scene = new Scene(root, 1000, 650);
        stage.setTitle("CampusClaim — Lost & Found");
        stage.setScene(scene);
        stage.show();

        showDashboard();

        // save data on close
        stage.setOnCloseRequest(e -> persistAll());
    }

    private void persistAll() {
        FileHandler.saveFoundItems(foundStorage.getAllItemsForSave());
        FileHandler.saveLostItems(lostStorage.getAllItemsForSave());
        FileHandler.saveCounter(Item.getCounter());
        FileHandler.saveCounters(
            itemService.getTotalFoundRegistered(),
            itemService.getTotalLostRegistered(),
            itemService.getTotalClaimed());
    }

    // =========================================================
    //  Reusable styling helpers
    // =========================================================
    private Label title(String s) {
        Label l = new Label(s);
        l.setFont(Font.font(FONT, FontWeight.BOLD, 28));
        l.setStyle("-fx-text-fill:" + DARK_TXT + ";");
        return l;
    }
    private Label subtitle(String s) {
        Label l = new Label(s);
        l.setFont(Font.font(FONT, FontWeight.NORMAL, 14));
        l.setStyle("-fx-text-fill:" + DARK_TXT + ";");
        l.setWrapText(true);
        return l;
    }
    private Label fieldLabel(String s) {
        Label l = new Label(s);
        l.setFont(Font.font(FONT, FontWeight.SEMI_BOLD, 14));
        l.setStyle("-fx-text-fill:" + DARK_TXT + ";");
        return l;
    }
    private Button primaryBtn(String s) {
        Button b = new Button(s);
        b.setStyle("-fx-background-color:" + ACCENT + "; -fx-text-fill:" + LIGHT_TXT
                + "; -fx-font-weight:bold; -fx-padding:8 22 8 22; -fx-background-radius:6;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:" + ACCENT_L + "; -fx-text-fill:" + LIGHT_TXT
                + "; -fx-font-weight:bold; -fx-padding:8 22 8 22; -fx-background-radius:6;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color:" + ACCENT + "; -fx-text-fill:" + LIGHT_TXT
                + "; -fx-font-weight:bold; -fx-padding:8 22 8 22; -fx-background-radius:6;"));
        return b;
    }
    private Button bigMenuBtn(String s) {
        Button b = primaryBtn(s);
        b.setMinSize(260, 150);
        b.setPrefSize(260, 150);
        b.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        return b;
    }

    /** Wraps a row of large menu buttons in a light-coloured rounded container,
     *  centred horizontally so it aligns nicely with the page. */
    private VBox wrapMenuRow(Button... buttons) {
        HBox row = new HBox(35, buttons);
        row.setAlignment(Pos.CENTER);

        VBox box = new VBox(row);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(35, 50, 35, 50));
        box.setStyle(
            "-fx-background-color:" + ACCENT_X + ";" +
            "-fx-background-radius:14;");
        box.setMaxWidth(Region.USE_PREF_SIZE);

        VBox outer = new VBox(box);
        outer.setAlignment(Pos.CENTER);
        outer.setPadding(new Insets(60, 40, 40, 40));
        return outer;
    }
    private TextField inputField() {
        TextField t = new TextField();
        t.setStyle("-fx-background-color:" + LIGHT_TXT + "; -fx-text-fill:" + DARK_TXT
                + "; -fx-border-color:" + ACCENT + "; -fx-border-radius:4; -fx-background-radius:4; -fx-padding:6;");
        return t;
    }
    private TextArea textArea() {
        TextArea t = new TextArea();
        t.setStyle("-fx-control-inner-background:" + LIGHT_TXT + "; -fx-text-fill:" + DARK_TXT
                + "; -fx-border-color:" + ACCENT + "; -fx-border-radius:4; -fx-background-radius:4;");
        t.setPrefRowCount(3);
        return t;
    }

    /** Page-name strip used on every page (Dashboard-style with thick rule). */
    private VBox headerBar(String pageTitle) {
        Label pg = new Label(pageTitle);
        pg.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        pg.setStyle("-fx-text-fill:" + DARK_TXT + ";");

        // thick rule under the page name (same on every page)
        Region rule = new Region();
        rule.setMinHeight(5);
        rule.setPrefHeight(5);
        rule.setMaxHeight(5);
        rule.setStyle("-fx-background-color:" + ACCENT + "; -fx-background-radius:3;");

        VBox v = new VBox(6, pg, rule);
        v.setPadding(new Insets(15, 30, 8, 30));
        v.setStyle("-fx-background-color:" + BG + ";");
        return v;
    }

    private HBox footerBar(Runnable backAction) {
        Button back = primaryBtn("Back");
        back.setOnAction(e -> backAction.run());
        HBox h = new HBox(back);
        h.setAlignment(Pos.CENTER_RIGHT);
        h.setPadding(new Insets(15, 24, 20, 24));
        return h;
    }

    private void setPage(javafx.scene.Node header, javafx.scene.Node center, javafx.scene.Node footer) {
        root.setTop(header);
        root.setCenter(center);
        root.setBottom(footer);
    }

    /** Themed dialog (cream box, dark green pill OK button, Playfair Display). */
    private void showThemedDialog(String windowTitle, String headerText, String bodyText, List<String> bulletLines) {
        Stage dlg = new Stage();
        dlg.initModality(Modality.APPLICATION_MODAL);
        if (stage != null) dlg.initOwner(stage);
        dlg.setTitle(windowTitle);
        dlg.setResizable(false);

        VBox content = new VBox(14);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(35, 50, 30, 50));
        content.setStyle(
            "-fx-background-color: #eaf1ec;" +
            "-fx-border-color:" + ACCENT + ";" +
            "-fx-border-width:2;" +
            "-fx-border-radius:10;" +
            "-fx-background-radius:10;");

        if (headerText != null && !headerText.isEmpty()) {
            Label h = new Label(headerText);
            h.setFont(Font.font(FONT, FontWeight.BOLD, 22));
            h.setStyle("-fx-text-fill:" + DARK_TXT + ";");
            h.setWrapText(true);
            h.setAlignment(Pos.CENTER);
            h.setMaxWidth(560);
            content.getChildren().add(h);
        }

        if (bodyText != null && !bodyText.isEmpty()) {
            Label b = new Label(bodyText);
            b.setFont(Font.font(FONT, FontWeight.NORMAL, 16));
            b.setStyle("-fx-text-fill:" + DARK_TXT + ";");
            b.setWrapText(true);
            b.setAlignment(Pos.CENTER);
            b.setMaxWidth(520);
            content.getChildren().add(b);
        }

        if (bulletLines != null && !bulletLines.isEmpty()) {
            VBox lines = new VBox(8);
            lines.setAlignment(Pos.CENTER_LEFT);
            lines.setPadding(new Insets(6, 10, 6, 10));
            for (String line : bulletLines) {
                Label l = new Label(line);
                l.setFont(Font.font(FONT, FontWeight.NORMAL, 16));
                l.setStyle("-fx-text-fill:" + DARK_TXT + ";");
                lines.getChildren().add(l);
            }
            content.getChildren().add(lines);
        }

        Button ok = new Button("OK");
        ok.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        String okBase = "-fx-background-color:" + ACCENT + "; -fx-text-fill:" + LIGHT_TXT
            + "; -fx-padding:8 32 8 32; -fx-background-radius:30;";
        String okHover= "-fx-background-color:" + ACCENT_L + "; -fx-text-fill:" + LIGHT_TXT
            + "; -fx-padding:8 32 8 32; -fx-background-radius:30;";
        ok.setStyle(okBase);
        ok.setOnMouseEntered(e -> ok.setStyle(okHover));
        ok.setOnMouseExited(e -> ok.setStyle(okBase));
        ok.setOnAction(e -> dlg.close());

        HBox btnRow = new HBox(ok);
        btnRow.setAlignment(Pos.CENTER);
        btnRow.setPadding(new Insets(8, 0, 0, 0));
        content.getChildren().add(btnRow);

        // Outer wrapper so the cream card sits on a neutral backdrop
        StackPane outer = new StackPane(content);
        outer.setPadding(new Insets(10));
        outer.setStyle("-fx-background-color:#eef3ef;");

        Scene s = new Scene(outer);
        dlg.setScene(s);
        dlg.sizeToScene();
        dlg.showAndWait();
    }

    private void showError(String msg) {
        showThemedDialog("Error", null, msg, null);
    }
    private void showInfo(String msg) {
        showThemedDialog("Message", null, msg, null);
    }

    /** Pakistani contact validation: must be exactly 11 digits and start with 03. */
    private boolean validateContact(String c) {
        if (!c.matches("^03\\d{9}$")) {
            showError("Enter a valid 11-digit Pakistani number starting with 03.");
            return false;
        }
        return true;
    }

    /** Copy a picked image into a local "pictures" folder beside the program,
     *  giving it a unique, safe filename. Returns the relative saved path
     *  (e.g. "pictures/1714060000000_bag.jpg") or null on failure. */
    private String saveImageLocally(File source) {
        try {
            File dir = new File("pictures");
            if (!dir.exists() && !dir.mkdirs()) return null;

            // Sanitize original filename: keep letters/digits/dot/dash/underscore only.
            String safeName = source.getName().replaceAll("[^A-Za-z0-9._-]", "_");
            String unique = System.currentTimeMillis() + "_" + safeName;

            File dest = new File(dir, unique);
            // Avoid extremely unlikely collision
            int n = 1;
            while (dest.exists()) {
                dest = new File(dir, System.currentTimeMillis() + "_" + (n++) + "_" + safeName);
            }
            java.nio.file.Files.copy(
                source.toPath(),
                dest.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            // Return a relative path so it works no matter where the project lives.
            return "pictures/" + dest.getName();
        } catch (Exception ex) {
            return null;
        }
    }

    /** Clean the raw getFullDetails() string into one "Key: Value" per line. */
    private List<String> prettifyDetails(String raw) {
        List<String> out = new ArrayList<>();
        if (raw == null) return out;
        // strip leading/trailing "+++ Complete Details Of The Item +++" markers
        String s = raw.replaceAll("\\++\\s*Complete Details Of The Item\\s*\\++", " ");
        String[] parts = s.split("[|\\n]");
        for (String p : parts) {
            String t = p.trim().replaceAll("\\s+:\\s*", ": ").replaceAll("\\s{2,}", " ");
            if (t.isEmpty()) continue;
            // friendlier key names
            if (t.startsWith("ID:"))     t = "Item " + t;
            else if (t.startsWith("Name:")) t = "Item " + t;
            out.add(t);
        }
        return out;
    }

    // =========================================================
    //  PAGE 1 — DASHBOARD
    // =========================================================
    private void showDashboard() {
        VBox topStrip = headerBar("Dashboard");

        // ---- Brand pill (left) + welcome card (right) ----
        Label brandPill = new Label("CampusClaim");
        brandPill.setFont(Font.font(FONT, FontWeight.BOLD, 30));
        brandPill.setStyle(
            "-fx-text-fill:" + LIGHT_TXT + ";" +
            "-fx-background-color:" + ACCENT + ";" +
            "-fx-background-radius:40;" +
            "-fx-padding:14 40 14 40;");

        Label welcome = new Label(
            "Welcome to the smart Lost & Found hub , a sleek, all-in-one " +
            "dashboard where you can instantly report lost items, browse " +
            "found treasures, and track successful claims.");
        welcome.setFont(Font.font(FONT, FontWeight.NORMAL, 14));
        welcome.setStyle(
            "-fx-text-fill:" + DARK_TXT + ";" +
            "-fx-background-color:" + ACCENT_X + ";" +
            "-fx-background-radius:14;" +
            "-fx-padding:18 22 18 22;");
        welcome.setWrapText(true);
        welcome.setMaxWidth(440);

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        HBox topRow = new HBox(20, brandPill, topSpacer, welcome);
        // align tops so the welcome box sits flush with the top of the CampusClaim pill
        topRow.setAlignment(Pos.TOP_LEFT);
        topRow.setPadding(new Insets(20, 50, 25, 50));

        int totalFound   = itemService.getTotalFoundRegistered();
        int totalLost    = itemService.getTotalLostRegistered();
        int totalClaimed = itemService.getTotalClaimed();
        // claim efficiency = how many of the reported FOUND items got claimed
        double efficiency = totalFound == 0 ? 0 : ((double) totalClaimed / totalFound) * 100.0;

        // ---- LEFT column: "System Report" label outside (left-aligned), banner below ----
        Label rTitle = new Label("System Report");
        rTitle.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        rTitle.setStyle("-fx-text-fill:" + DARK_TXT + ";");

        // light-coloured ribbon-style box that holds the 3 stats + efficiency
        VBox banner = new VBox(14);
        banner.setAlignment(Pos.TOP_CENTER);
        banner.setPadding(new Insets(18, 22, 22, 22));
        banner.setStyle(
            "-fx-background-color:" + ACCENT_X + ";" +
            "-fx-background-radius:6;");
        // narrower so there's a clearer gap between the report and the main menu
        banner.setMinWidth(170);
        banner.setMaxWidth(170);

        banner.getChildren().addAll(
                makeReportLine("Found Items:",  totalFound),
                makeReportLine("Lost Items:",   totalLost),
                makeReportLine("Claimed:",      totalClaimed)
        );

        // efficiency block inside the banner
        Label effTitle = new Label("Efficiency:");
        effTitle.setFont(Font.font(FONT, FontWeight.BOLD, 14));
        effTitle.setStyle("-fx-text-fill:" + LIGHT_TXT + ";");

        ProgressBar pb = new ProgressBar(efficiency / 100.0);
        pb.setPrefWidth(110);
        pb.setPrefHeight(12);
        pb.setStyle(
            "-fx-accent:" + ACCENT + ";" +
            "-fx-control-inner-background:" + LIGHT_TXT + ";" +
            "-fx-border-color:" + ACCENT + ";" +
            "-fx-border-radius:8;" +
            "-fx-background-radius:8;");

        Label effPct = new Label(String.format("%.0f%%", efficiency));
        effPct.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        effPct.setStyle("-fx-text-fill:" + LIGHT_TXT + ";");

        HBox effRow = new HBox(8, pb, effPct);
        effRow.setAlignment(Pos.CENTER_LEFT);

        VBox effBox = new VBox(4, effTitle, effRow);
        effBox.setAlignment(Pos.CENTER_LEFT);
        banner.getChildren().add(effBox);

        VBox leftCol = new VBox(10, rTitle, banner);
        // left-align the heading so it sits exactly above the banner's left edge
        leftCol.setAlignment(Pos.TOP_LEFT);

        // ---- RIGHT column: "Main Menu" label (left-aligned) + 3 icon cards in a light wrapper ----
        Label mTitle = new Label("Main Menu");
        mTitle.setFont(Font.font(FONT, FontWeight.BOLD, 22));
        mTitle.setStyle("-fx-text-fill:" + DARK_TXT + ";");
        HBox mTitleRow = new HBox(mTitle);
        mTitleRow.setAlignment(Pos.CENTER_LEFT);

        HBox cardRow = new HBox(20,
                buildMenuCard("Found Items",   "found.png",   this::showFoundItemsMenu),
                buildMenuCard("Lost Items",    "lost.png",    this::showLostItemsMenu),
                buildMenuCard("Claimed Items", "claimed.png", this::showClaimMenu));
        cardRow.setAlignment(Pos.CENTER);

        VBox cardWrap = new VBox(cardRow);
        cardWrap.setAlignment(Pos.CENTER);
        cardWrap.setPadding(new Insets(20, 25, 20, 25));
        cardWrap.setStyle(
            "-fx-background-color:" + ACCENT_X + ";" +
            "-fx-background-radius:8;");

        Label hint = new Label("Click to open!");
        hint.setFont(Font.font(FONT, FontWeight.BOLD, 13));
        hint.setStyle("-fx-text-fill:" + DARK_TXT + ";");
        HBox hintRow = new HBox(hint);
        hintRow.setAlignment(Pos.CENTER_LEFT);

        VBox rightCol = new VBox(12, mTitleRow, cardWrap, hintRow);
        rightCol.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(rightCol, Priority.ALWAYS);

        // pushed a little further down + bigger gap between Report and Main Menu
        HBox center = new HBox(70, leftCol, rightCol);
        center.setAlignment(Pos.TOP_LEFT);
        center.setPadding(new Insets(40, 50, 30, 50));

        VBox body = new VBox(topStrip, topRow, center);
        body.setStyle("-fx-background-color:" + BG + ";");

        root.setTop(null);
        root.setCenter(body);
        root.setBottom(null);
    }

    /** "Heading: N" line — single row, no caption text. */
    private VBox makeReportLine(String heading, int count) {
        Label h = new Label(heading);
        h.setFont(Font.font(FONT, FontWeight.BOLD, 15));
        h.setStyle("-fx-text-fill:" + LIGHT_TXT + ";");

        // numbers use the same dark colour as the "Dashboard" page heading
        Label n = new Label(String.valueOf(count));
        n.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        n.setStyle("-fx-text-fill:" + DARK_TXT + ";");

        VBox v = new VBox(2, h, n);
        v.setAlignment(Pos.CENTER);
        return v;
    }

    /** A big square menu card with an icon on top of a label. */
    private VBox buildMenuCard(String label, String iconFile, Runnable onClick) {
        Label l = new Label(label);
        l.setFont(Font.font(FONT, FontWeight.BOLD, 18));
        l.setStyle("-fx-text-fill:" + LIGHT_TXT + ";");

        ImageView iv = new ImageView();
        try {
            File f = new File(iconFile);
            if (f.exists()) {
                Image img = new Image(f.toURI().toString(), 80, 80, true, true);
                iv.setImage(img);
            }
        } catch (Exception ignore) {}

        // tint icon area white-ish background block to match prototype
        VBox iconBox = new VBox(iv);
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setPrefSize(110, 100);
        iconBox.setStyle(
            "-fx-background-color:" + LIGHT_TXT + ";" +
            "-fx-background-radius:6;");

        VBox card = new VBox(12, l, iconBox);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(170, 180);
        card.setMinSize(170, 180);
        card.setPadding(new Insets(15));
        String base = "-fx-background-color:" + ACCENT + "; -fx-background-radius:14;";
        String hover= "-fx-background-color:" + ACCENT_L + "; -fx-background-radius:14;";
        card.setStyle(base);
        card.setOnMouseEntered(e -> card.setStyle(hover));
        card.setOnMouseExited(e -> card.setStyle(base));
        card.setOnMouseClicked(e -> onClick.run());
        card.setCursor(javafx.scene.Cursor.HAND);
        return card;
    }

    // =========================================================
    //  PAGE 2 — Found Items menu
    // =========================================================
    private void showFoundItemsMenu() {
        Button v = bigMenuBtn("View Found Items");
        Button r = bigMenuBtn("Report Found Item");
        v.setOnAction(e -> showViewFoundItems());
        r.setOnAction(e -> showFoundRegistrationStep1());

        setPage(headerBar("Found Items"), wrapMenuRow(v, r), footerBar(this::showDashboard));
    }

    // =========================================================
    //  PAGE 3 — View Found Items table
    // =========================================================
    private void showViewFoundItems() {
        TableView<FoundItem> table = new TableView<>();
        themeTable(table);

        TableColumn<FoundItem,String> cId = col("Item ID", f -> "A-" + f.getItemID());
        TableColumn<FoundItem,String> cN  = col("Name",     FoundItem::getName);
        TableColumn<FoundItem,String> cC  = col("Category", FoundItem::getCategory);
        TableColumn<FoundItem,String> cD  = col("Date",     f -> String.valueOf(f.getDate()));
        TableColumn<FoundItem,String> cL  = col("Location", FoundItem::getLocation);
        TableColumn<FoundItem,String> cS  = col("Status",   f -> f.getIsClaimed() ? "Claimed" : "Not Claimed");
        table.getColumns().addAll(cId, cN, cC, cD, cL, cS);

        ObservableList<FoundItem> data = FXCollections.observableArrayList(itemService.getAvailableFoundItems());
        table.setItems(data);

        VBox center = new VBox(15, subtitle("All currently available found items:"), table);
        center.setPadding(new Insets(20, 30, 20, 30));
        VBox.setVgrow(table, Priority.ALWAYS);

        setPage(headerBar("View Found Items"), center, footerBar(this::showFoundItemsMenu));
    }

    /** Apply the project's green theme to a TableView (no blue selection,
     *  light green alternating rows, dark green tint when a row is selected,
     *  and stretch columns so there is no empty trailing column). */
    @SuppressWarnings("unchecked")
    private <T> void themeTable(TableView<T> t) {
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // -fx-base re-tints all the default JavaFX colours; selection-bar
        // controls the highlighted-row colour (replaces the default blue).
        t.setStyle(
            "-fx-base:" + BG + ";" +
            "-fx-control-inner-background:#e2ece3;" +
            "-fx-control-inner-background-alt:#d2dfd4;" +
            "-fx-selection-bar:" + ACCENT_X + ";" +
            "-fx-selection-bar-text:" + DARK_TXT + ";" +
            "-fx-selection-bar-non-focused:" + ACCENT_X + ";" +
            "-fx-table-cell-border-color:" + ACCENT_L + ";" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );
    }

    @SuppressWarnings("unchecked")
    private <T> TableColumn<T,String> col(String name, java.util.function.Function<T,String> getter) {
        TableColumn<T,String> c = new TableColumn<>(name);
        c.setCellValueFactory(cd -> new javafx.beans.property.SimpleStringProperty(getter.apply(cd.getValue())));
        c.setPrefWidth(140);
        return c;
    }

    // =========================================================
    //  PAGE 4–6 — Found Item Registration (3 steps)
    // =========================================================
    private void showFoundRegistrationStep1() {
        GridPane g = buildFormGrid();

        TextField itemName  = inputField();  itemName.setPrefWidth(420);
        ComboBox<String> cat = new ComboBox<>(FXCollections.observableArrayList(FOUND_CATEGORIES));
        cat.setPromptText("Select a category");
        cat.setStyle("-fx-background-color:" + LIGHT_TXT + ";");
        TextField loc       = inputField();  loc.setPrefWidth(420);
        TextField yourName  = inputField();  yourName.setPrefWidth(420);
        TextField contact   = inputField();  contact.setPrefWidth(420);

        g.add(fieldLabel("Item Name"),    0, 0); g.add(itemName, 1, 0);
        g.add(fieldLabel("Category:"),    0, 1); g.add(cat,      1, 1);
        g.add(fieldLabel("Location:"),    0, 2); g.add(loc,      1, 2);
        g.add(fieldLabel("Your Name:"),   0, 3); g.add(yourName, 1, 3);
        g.add(fieldLabel("Your Contact:"),0, 4); g.add(contact,  1, 4);

        Button submit = primaryBtn("Submit");
        submit.setOnAction(e -> {
            // ---- Validation ----
            if (itemName.getText().trim().isEmpty()
                || cat.getValue() == null
                || loc.getText().trim().isEmpty()
                || yourName.getText().trim().isEmpty()
                || contact.getText().trim().isEmpty()) {
                showError("Please fill in ALL the fields before submitting.");
                return;
            }
            String c = contact.getText().trim();
            if (!validateContact(c)) return;
            // create object
            int choice = cat.getSelectionModel().getSelectedIndex() + 1;
            FoundItem fi = itemService.createFoundItem(choice);
            itemService.setBasicFields(fi,
                itemName.getText().trim(),
                loc.getText().trim(),
                cat.getValue(),
                c,
                yourName.getText().trim());
            showFoundRegistrationStep2(fi);
        });

        HBox btns = new HBox(20, submit);
        btns.setAlignment(Pos.CENTER_RIGHT);
        g.add(btns, 1, 5);

        setPage(headerBar("Found Item Registration"), g, footerBar(this::showFoundItemsMenu));
    }

    /** Step 2 — answer validation questions. */
    private void showFoundRegistrationStep2(FoundItem fi) {
        Map<String,String> qs = fi.getValidationQuestions();
        Map<String, TextField> answerFields = new LinkedHashMap<>();

        GridPane g = buildFormGrid();

        Label note = subtitle("These answers will be used for validation when someone tries to claim this item.");
        note.setWrapText(true);
        g.add(note, 0, 0, 2, 1);

        int row = 1;
        for (Map.Entry<String,String> q : qs.entrySet()) {
            Label ql = fieldLabel(q.getValue());
            TextField tf = inputField();
            tf.setPrefWidth(420);
            answerFields.put(q.getKey(), tf);
            g.add(ql, 0, row);
            g.add(tf, 1, row);
            row++;
        }

        Button submit = primaryBtn("Submit");
        submit.setOnAction(e -> {
            Map<String,String> answers = new LinkedHashMap<>();
            for (Map.Entry<String, TextField> en : answerFields.entrySet()) {
                String v = en.getValue().getText().trim();
                if (v.isEmpty()) {
                    showError("Please answer ALL validation questions.");
                    return;
                }
                answers.put(en.getKey(), v);
            }
            itemService.registerFoundItem(answers, fi);
            persistAll();
            showInfo("Found item registered successfully!\nID: A-" + fi.getItemID());
            showFoundItemsMenu();
        });

        HBox btnRow = new HBox(submit);
        btnRow.setAlignment(Pos.CENTER_RIGHT);
        g.add(btnRow, 1, row);

        setPage(headerBar("Found Item Registration"), g, footerBar(this::showFoundItemsMenu));
    }

    /** Shared GridPane styling for question/registration forms (matches Lost Item Registration). */
    private GridPane buildFormGrid() {
        GridPane g = new GridPane();
        g.setHgap(20);
        g.setVgap(20);
        g.setPadding(new Insets(40, 90, 40, 90));
        g.setAlignment(Pos.TOP_LEFT);
        return g;
    }

    // =========================================================
    //  PAGE 7 — Claim menu
    // =========================================================
    private void showClaimMenu() {
        Button v = bigMenuBtn("View Claimed Items");
        Button c = bigMenuBtn("Claim an Item");
        v.setOnAction(e -> showViewClaimedItems());
        c.setOnAction(e -> showClaimItemList());

        setPage(headerBar("Claim Items"), wrapMenuRow(v, c), footerBar(this::showDashboard));
    }

    // ---- View Claimed Items ----
    private void showViewClaimedItems() {
        TableView<FoundItem> table = new TableView<>();
        themeTable(table);
        TableColumn<FoundItem,String> cId = col("Item ID", f -> "A-" + f.getItemID());
        TableColumn<FoundItem,String> cN  = col("Name",     FoundItem::getName);
        TableColumn<FoundItem,String> cC  = col("Category", FoundItem::getCategory);
        TableColumn<FoundItem,String> cD  = col("Date",     f -> String.valueOf(f.getDate()));
        TableColumn<FoundItem,String> cL  = col("Location", FoundItem::getLocation);
        TableColumn<FoundItem,String> cS  = col("Status",   f -> "Claimed");
        table.getColumns().addAll(cId, cN, cC, cD, cL, cS);

        table.setItems(FXCollections.observableArrayList(itemService.getClaimedItems()));

        VBox center = new VBox(15, subtitle("All items that have been successfully claimed:"), table);
        center.setPadding(new Insets(20, 30, 20, 30));
        VBox.setVgrow(table, Priority.ALWAYS);

        setPage(headerBar("View Claimed Items"), center, footerBar(this::showClaimMenu));
    }

    // ---- Claim an Item: show available list, click row → claim ----
    private void showClaimItemList() {
        TableView<FoundItem> table = new TableView<>();
        themeTable(table);
        TableColumn<FoundItem,String> cId = col("Item ID", f -> "A-" + f.getItemID());
        TableColumn<FoundItem,String> cN  = col("Name",     FoundItem::getName);
        TableColumn<FoundItem,String> cC  = col("Category", FoundItem::getCategory);
        TableColumn<FoundItem,String> cD  = col("Date",     f -> String.valueOf(f.getDate()));
        TableColumn<FoundItem,String> cL  = col("Location", FoundItem::getLocation);
        TableColumn<FoundItem,String> cS  = col("Status",   f -> "Not Claimed");
        table.getColumns().addAll(cId, cN, cC, cD, cL, cS);
        table.setItems(FXCollections.observableArrayList(itemService.getAvailableFoundItems()));

        Button claim = primaryBtn("Claim Selected Item");
        claim.setOnAction(e -> {
            FoundItem sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                showError("Please select an item from the list to claim.");
                return;
            }
            showClaimAnswerForm(sel);
        });

        HBox bottom = new HBox(claim);
        bottom.setAlignment(Pos.CENTER);

        VBox center = new VBox(15, table, bottom);
        center.setPadding(new Insets(20, 30, 20, 30));
        VBox.setVgrow(table, Priority.ALWAYS);

        setPage(headerBar("Claim an Item"), center, footerBar(this::showClaimMenu));
    }

    private void showClaimAnswerForm(FoundItem sel) {
        Map<String,String> qs = sel.getValidationQuestions();
        Map<String, TextField> fields = new LinkedHashMap<>();

        GridPane g = buildFormGrid();

        Label note = subtitle("Answer these for validation.");
        g.add(note, 0, 0, 2, 1);

        int row = 1;
        for (Map.Entry<String,String> q : qs.entrySet()) {
            g.add(fieldLabel(q.getValue()), 0, row);
            TextField tf = inputField();
            tf.setPrefWidth(420);
            fields.put(q.getKey(), tf);
            g.add(tf, 1, row);
            row++;
        }

        Button next = primaryBtn("Next");
        next.setOnAction(e -> {
            Map<String,String> answers = new LinkedHashMap<>();
            for (Map.Entry<String, TextField> en : fields.entrySet()) {
                String v = en.getValue().getText().trim();
                if (v.isEmpty()) {
                    showError("Please answer ALL validation questions.");
                    return;
                }
                answers.put(en.getKey(), v);
            }
            String result = itemService.processClaim(sel.getItemID(), answers);
            persistAll();
            if (result.contains("APPROVED")) {
                List<String> lines = prettifyDetails(sel.getFullDetails());
                showThemedDialog("Message", "THE CLAIM IS APPROVED",
                        "Complete Details Of The Item", lines);
            } else {
                showError(result);
            }
            showClaimMenu();
        });

        HBox btnRow = new HBox(next);
        btnRow.setAlignment(Pos.CENTER_RIGHT);
        g.add(btnRow, 1, row);

        setPage(headerBar("Claim an Item"), g, footerBar(this::showClaimMenu));
    }

    // =========================================================
    //  Lost Items menu (page 11)
    // =========================================================
    private void showLostItemsMenu() {
        Button v = bigMenuBtn("View Lost Items");
        Button r = bigMenuBtn("Report a lost Item");
        v.setOnAction(e -> showLostCategoriesMenu());
        r.setOnAction(this::showLostRegistrationDummyArg);

        setPage(headerBar("Lost Items"), wrapMenuRow(v, r), footerBar(this::showDashboard));
    }
    private void showLostRegistrationDummyArg(javafx.event.ActionEvent e){ showLostRegistration(); }

    // page 12
    private void showLostCategoriesMenu() {
        Button a = bigMenuBtn("View Active\nlost Items");
        Button x = bigMenuBtn("View Expired\nlost Items");
        a.setOnAction(e -> showLostList(true,  null));
        x.setOnAction(e -> showLostList(false, null));

        VBox wrapped = wrapMenuRow(a, x);

        Label note = new Label(
            "Active Items: Items registered within the last 30 days.\n" +
            "Expired Items: Items registered before the last 30 days.");
        note.setStyle("-fx-text-fill:" + DARK_TXT + ";");
        note.setWrapText(true);

        VBox noteBox = new VBox(note);
        noteBox.setAlignment(Pos.CENTER);
        noteBox.setPadding(new Insets(0, 40, 20, 40));

        VBox center = new VBox(10, wrapped, noteBox);
        center.setAlignment(Pos.TOP_CENTER);

        setPage(headerBar("Lost Items Categories"), center, footerBar(this::showLostItemsMenu));
    }

    // pages 13/14 — active/expired lost items list (filterable)
    private void showLostList(boolean active, String filterCat) {
        // Filter row
        HBox filterRow = new HBox(10);
        filterRow.setPadding(new Insets(0,0,5,0));
        for (String c : LOST_CATEGORIES) {
            Button b = primaryBtn(c);
            b.setOnAction(e -> showLostList(active, c));
            filterRow.getChildren().add(b);
        }
        Button all = primaryBtn("All");
        all.setOnAction(e -> showLostList(active, null));
        filterRow.getChildren().add(all);

        Label hint = subtitle("To filter by categories, click on a category above.");

        TableView<LostItem> table = new TableView<>();
        themeTable(table);
        table.getColumns().add(col("Item ID",  i -> "L-" + i.getItemID()));
        table.getColumns().add(col("Name",     LostItem::getName));
        table.getColumns().add(col("Category", LostItem::getCategory));
        table.getColumns().add(col("Date",     i -> String.valueOf(i.getDate())));
        table.getColumns().add(col("Location", LostItem::getLocation));
        table.getColumns().add(col("Picture",
                i -> (i.getImagePath() != null && !i.getImagePath().isEmpty()) ? "Available" : "not Available"));

        java.util.ArrayList<LostItem> items;
        if (filterCat == null) {
            items = active ? itemService.getActiveLostItems() : itemService.getExpiredLostItems();
        } else {
            items = active
                ? itemService.getActiveLostByCategory(filterCat)
                : itemService.getExpiredLostByCategory(filterCat);
        }
        table.setItems(FXCollections.observableArrayList(items));

        Button viewPic = primaryBtn("View Picture");
        viewPic.setOnAction(e -> {
            LostItem sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { showError("Please select an item first."); return; }
            showLostPicture(sel, active);
        });

        HBox bottom = new HBox(viewPic);
        bottom.setAlignment(Pos.CENTER_RIGHT);

        VBox center = new VBox(10, filterRow, hint, table, bottom);
        center.setPadding(new Insets(15, 25, 15, 25));
        VBox.setVgrow(table, Priority.ALWAYS);

        String pageTitle = active ? "Active Lost Items" : "Expired Lost Items";
        setPage(headerBar(pageTitle), center, footerBar(this::showLostCategoriesMenu));
    }

    // page 15 — show picture
    private void showLostPicture(LostItem item, boolean fromActive) {
        VBox center = new VBox(15);
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(20));

        if (item.getImagePath() == null || item.getImagePath().isEmpty()) {
            Label l = new Label("No picture available for this item.");
            l.setStyle("-fx-text-fill:" + DARK_TXT + "; -fx-font-size:16;");
            center.getChildren().add(l);
        } else {
            try {
                Image img = new Image(new File(item.getImagePath()).toURI().toString(),
                                      600, 400, true, true);
                ImageView iv = new ImageView(img);
                center.getChildren().add(iv);
            } catch (Exception ex) {
                Label l = new Label("Could not load image: " + ex.getMessage());
                l.setStyle("-fx-text-fill:" + DARK_TXT + ";");
                center.getChildren().add(l);
            }
        }
        Label info = subtitle("Item: " + item.getName() + "  |  Owner: " + item.getOwnerName()
                + "  |  Contact: " + item.getOwnerContact());
        center.getChildren().add(info);

        setPage(headerBar("View Lost Item Picture"), center,
                footerBar(() -> showLostList(fromActive, null)));
    }

    // pages 16/17/18 — Lost Item Registration
    private String chosenImagePath = null;
    private void showLostRegistration() {
        chosenImagePath = null;

        GridPane g = buildFormGrid();

        TextField name  = inputField();  name.setPrefWidth(500);
        ComboBox<String> cat = new ComboBox<>(FXCollections.observableArrayList(LOST_CATEGORIES));
        cat.setPromptText("Select a category");
        cat.setStyle("-fx-background-color:" + LIGHT_TXT + ";");
        TextField loc   = inputField();  loc.setPrefWidth(500);
        TextArea  desc  = textArea();    desc.setPrefWidth(500);
        TextField owner = inputField();  owner.setPrefWidth(500);
        TextField contact = inputField(); contact.setPrefWidth(500);

        Label imgStatus = new Label("(no image chosen)");
        imgStatus.setStyle("-fx-text-fill:" + DARK_TXT + "; -fx-font-style:italic;");
        Button addImg = primaryBtn("Add");
        addImg.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select Picture");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg","*.jpeg","*.png"));
            File f = fc.showOpenDialog(stage);
            if (f != null) {
                String n = f.getName().toLowerCase();
                if (!(n.endsWith(".jpg") || n.endsWith(".jpeg") || n.endsWith(".png"))) {
                    showError("Only JPG / PNG images are allowed.");
                    return;
                }
                String savedPath = saveImageLocally(f);
                if (savedPath == null) {
                    showError("Could not save the picture. Please try again.");
                    return;
                }
                chosenImagePath = savedPath;
                imgStatus.setText(new File(savedPath).getName());
            }
        });
        Label imgHint = new Label("JPG / PNG only");
        imgHint.setStyle("-fx-text-fill:" + DARK_TXT + "; -fx-font-size:11; -fx-font-style:italic;");
        VBox imgBox = new VBox(4, new HBox(10, addImg, imgStatus), imgHint);

        g.add(fieldLabel("Item Name"),         0, 0); g.add(name,    1, 0);
        g.add(fieldLabel("Category:"),         0, 1); g.add(cat,     1, 1);
        g.add(fieldLabel("Last Location:"),    0, 2); g.add(loc,     1, 2);
        g.add(fieldLabel("Description:"),      0, 3); g.add(desc,    1, 3);
        g.add(fieldLabel("Owner's Name:"),     0, 4); g.add(owner,   1, 4);
        g.add(fieldLabel("Owner's Contact:"),  0, 5); g.add(contact, 1, 5);
        g.add(fieldLabel("Picture (optional):"),0,6); g.add(imgBox,  1, 6);

        Button submit = primaryBtn("Submit");
        submit.setOnAction(e -> {
            if (name.getText().trim().isEmpty()
                || cat.getValue() == null
                || loc.getText().trim().isEmpty()
                || desc.getText().trim().isEmpty()
                || owner.getText().trim().isEmpty()
                || contact.getText().trim().isEmpty()) {
                showError("Please fill in ALL the required fields.");
                return;
            }
            String c = contact.getText().trim();
            if (!validateContact(c)) return;
            LostItem li = itemService.registerLostItem(
                name.getText().trim(),
                loc.getText().trim(),
                cat.getValue(),
                owner.getText().trim(),
                c,
                desc.getText().trim(),
                chosenImagePath);
            persistAll();
            showInfo("Lost item registered successfully!\nID: L-" + li.getItemID());
            showLostItemsMenu();
        });

        HBox btns = new HBox(submit);
        btns.setAlignment(Pos.CENTER_RIGHT);
        g.add(btns, 1, 7);

        setPage(headerBar("Lost Item Registration"), g, footerBar(this::showLostItemsMenu));
    }
}
