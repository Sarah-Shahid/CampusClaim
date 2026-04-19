import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.*;
import java.util.stream.Collectors;

public class GUI extends Application {

    // ── static refs passed from Main ────────────────────────────────
    private static ItemService    itemService;
    private static FoundItemStorage foundStorage;
    private static LostItemStorage  lostStorage;

    public static void setItemService(ItemService s)        { itemService  = s; }
    public static void setFoundStorage(FoundItemStorage fs) { foundStorage = fs; }
    public static void setLostStorage(LostItemStorage ls)   { lostStorage  = ls; }

    // ── colour palette from prototype ───────────────────────────────
    private static final String BG          = "#d7e4d9";
    private static final String DARK_GREEN  = "#235347";
    private static final String MID_GREEN   = "#6e9e8c";   // semi-transparent panel
    private static final String LIGHT_PANEL = "#b5cfc0";   // lighter panel bg
    private static final String TEXT_DARK   = "#151f28";
    private static final String TEXT_LIGHT  = "#f4f6fc";

    private Stage primaryStage;

    // ── track which "lost view" type user picked (active/expired) for back nav
    private boolean viewingActiveLost = true;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("CampusClaim");
        stage.setWidth(900);
        stage.setHeight(600);
        stage.setResizable(false);

        showDashboard();
        stage.show();

        // save on window close
        stage.setOnCloseRequest(e -> saveAll());
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 1 — DASHBOARD
    // ════════════════════════════════════════════════════════════════
    private void showDashboard() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG + ";");

        // header
        root.setTop(buildHeader("Dashboard"));

        // left — system report
        VBox report = new VBox(12);
        report.setPadding(new Insets(20, 15, 20, 20));
        report.setPrefWidth(220);

        Label reportTitle = new Label("System Report");
        reportTitle.setStyle("-fx-font-size:18px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");

        int found   = itemService.getTotalFoundRegistered();
        int lost    = itemService.getTotalLostRegistered();
        int claimed = itemService.getTotalClaimed();
        double eff  = (found == 0) ? 0.0 : (claimed * 100.0 / found);

        VBox reportBox = new VBox(10);
        reportBox.setPadding(new Insets(12));
        reportBox.setStyle("-fx-background-color:" + LIGHT_PANEL + ";-fx-background-radius:10;");

        reportBox.getChildren().addAll(
            reportStatLabel("Found Items", String.valueOf(found)),
            reportStatLabel("Lost Items",  String.valueOf(lost)),
            reportStatLabel("Claimed",     String.valueOf(claimed)),
            buildEfficiencyBar(eff)
        );

        report.getChildren().addAll(reportTitle, reportBox);

        // centre — main menu buttons
        VBox centre = new VBox(20);
        centre.setAlignment(Pos.CENTER);
        centre.setPadding(new Insets(20));

        // welcome box
        Label welcome = new Label(
            "Welcome to the smart Lost & Found hub, a sleek,\n" +
            "all-in-one dashboard where you can instantly report\n" +
            "lost items, browse found treasures, and track\n" +
            "successful claims.");
        welcome.setStyle("-fx-font-size:13px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_LIGHT + ";-fx-text-alignment:center;");
        StackPane welcomeBox = new StackPane(welcome);
        welcomeBox.setPadding(new Insets(14));
        welcomeBox.setStyle("-fx-background-color:" + MID_GREEN + ";-fx-background-radius:12;");

        Label menuTitle = new Label("Main Menu");
        menuTitle.setStyle("-fx-font-size:20px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");

        HBox btnRow = new HBox(20);
        btnRow.setAlignment(Pos.CENTER);
        btnRow.setPadding(new Insets(15));
        btnRow.setStyle("-fx-background-color:" + MID_GREEN + ";-fx-background-radius:14;");

        Button btnFound   = bigMenuButton("Found Items",   "📦");
        Button btnLost    = bigMenuButton("Lost Items",    "🏷");
        Button btnClaimed = bigMenuButton("Claimed Items", "✅");

        btnFound.setOnAction(e   -> showFoundItemsMenu());
        btnLost.setOnAction(e    -> showLostItemsMenu());
        btnClaimed.setOnAction(e -> showClaimedItemsMenu());

        btnRow.getChildren().addAll(btnFound, btnLost, btnClaimed);

        Label clickHint = new Label("Click to open!");
        clickHint.setStyle("-fx-font-size:12px;-fx-font-weight:bold;-fx-text-fill:" + TEXT_DARK + ";");

        centre.getChildren().addAll(welcomeBox, menuTitle, btnRow, clickHint);

        // thank you bottom right
        Label thanks = new Label("Thank you");
        thanks.setStyle("-fx-background-color:" + MID_GREEN + ";-fx-background-radius:20;" +
                        "-fx-padding:8 18;-fx-font-family:'Georgia';-fx-font-size:14px;-fx-text-fill:" + TEXT_DARK + ";");
        BorderPane.setAlignment(thanks, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(thanks, new Insets(0, 15, 12, 0));

        root.setLeft(report);
        root.setCenter(centre);
        root.setBottom(thanks);

        primaryStage.setScene(new Scene(root));
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 2 — FOUND ITEMS MENU
    // ════════════════════════════════════════════════════════════════
    private void showFoundItemsMenu() {
        primaryStage.setScene(buildTwoButtonPage(
            "Found Items",
            "View Found Items",  () -> showViewFoundItems(),
            "Report Found Item", () -> showReportFoundItem(),
            () -> showDashboard()
        ));
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 3 — VIEW FOUND ITEMS
    // ════════════════════════════════════════════════════════════════
    private void showViewFoundItems() {
        BorderPane root = styledRoot();
        root.setTop(buildHeader("View Found Items"));

        ArrayList<FoundItem> items = itemService.getAvailableFoundItems();

        TableView<FoundItem> table = buildFoundItemTable(items);

        VBox content = new VBox(table);
        content.setPadding(new Insets(15, 20, 15, 20));
        VBox.setVgrow(table, Priority.ALWAYS);

        root.setCenter(content);
        root.setBottom(backBar(() -> showFoundItemsMenu()));
        primaryStage.setScene(new Scene(root));
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 4 & 5 — REPORT FOUND ITEM (step 1: basic info + category)
    // ════════════════════════════════════════════════════════════════
    private void showReportFoundItem() {
        BorderPane root = styledRoot();
        root.setTop(buildHeader("Found Item Registration"));

        VBox form = new VBox(14);
        form.setPadding(new Insets(20, 40, 20, 40));
        form.setStyle("-fx-background-color:" + LIGHT_PANEL + ";-fx-background-radius:14;");

        TextField tfName     = styledField();
        ComboBox<String> cbCat = styledCombo(
            "Electronics","Accessories","Clothing","Documents","Keys","Bags","Jewelry","Books");
        TextField tfLocation = styledField();
        TextField tfFinderName = styledField();
        TextField tfContact  = styledField();

        // dim other fields until category selected — as per prototype
        tfLocation.setDisable(true);
        tfFinderName.setDisable(true);
        tfContact.setDisable(true);

        cbCat.setOnAction(e -> {
            boolean selected = cbCat.getValue() != null;
            tfLocation.setDisable(!selected);
            tfFinderName.setDisable(!selected);
            tfContact.setDisable(!selected);
        });

        form.getChildren().addAll(
            formRow("Item Name",     tfName),
            formRow("Category:",     cbCat),
            formRow("Location:",     tfLocation),
            formRow("Your Name:",    tfFinderName),
            formRow("Your Contact:", tfContact)
        );

        // submit button goes to page 6 (validation questions)
        Button btnSubmit = submitButton("Next");
        btnSubmit.setOnAction(e -> {
            String name     = tfName.getText().trim();
            String catStr   = cbCat.getValue();
            String location = tfLocation.getText().trim();
            String finder   = tfFinderName.getText().trim();
            String contact  = tfContact.getText().trim();

            if (name.isEmpty() || catStr == null || location.isEmpty()
                    || finder.isEmpty() || contact.isEmpty()) {
                showError("Please fill in all fields.");
                return;
            }
            if (!contact.matches("03\\d{9}")) {
                showError("Enter a valid 11-digit Pakistani number starting with 03.");
                return;
            }

            int catChoice = getCategoryChoice(catStr);
            FoundItem item = itemService.createFoundItem(catChoice);
            itemService.setBasicFields(item, name, location, catStr, contact, finder);

            showReportFoundValidation(item);
        });

        VBox outer = new VBox(20, form, btnSubmit);
        outer.setPadding(new Insets(20));
        outer.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(form, Priority.ALWAYS);

        root.setCenter(outer);
        root.setBottom(backBar(() -> showFoundItemsMenu()));
        primaryStage.setScene(new Scene(root));
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 6 — REPORT FOUND ITEM (step 2: validation questions)
    // ════════════════════════════════════════════════════════════════
    private void showReportFoundValidation(FoundItem item) {
        BorderPane root = styledRoot();
        root.setTop(buildHeader("Found Item Registration"));

        Map<String, String> questions = item.getValidationQuestions();
        Map<String, TextField> fieldMap = new LinkedHashMap<>();

        VBox form = new VBox(14);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color:" + LIGHT_PANEL + ";-fx-background-radius:14;");

        Label hint = new Label("These answers will be used for validation when someone tries to claim this item.");
        hint.setStyle("-fx-font-size:13px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
        hint.setWrapText(true);
        form.getChildren().add(hint);

        for (Map.Entry<String, String> entry : questions.entrySet()) {
            TextField tf = styledField();
            fieldMap.put(entry.getKey(), tf);
            form.getChildren().add(formRow(entry.getValue(), tf));
        }

        Button btnSubmit = submitButton("Submit");
        btnSubmit.setOnAction(e -> {
            Map<String, String> answers = new LinkedHashMap<>();
            for (Map.Entry<String, TextField> entry : fieldMap.entrySet()) {
                String val = entry.getValue().getText().trim();
                if (val.isEmpty()) {
                    showError("Please answer all questions.");
                    return;
                }
                answers.put(entry.getKey(), val);
            }
            try {
                itemService.registerFoundItem(answers, item);
                showSuccessDialog("Registration Successful!", () -> showDashboard());
            } catch (Exception ex) {
                showSuccessDialog("Registration Failed\nPlease try again later.", () -> showDashboard());
            }
        });

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");

        VBox outer = new VBox(15, scroll, btnSubmit);
        outer.setPadding(new Insets(15, 20, 10, 20));
        outer.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        root.setCenter(outer);
        root.setBottom(backBar(() -> showReportFoundItem()));
        primaryStage.setScene(new Scene(root));
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 7 — CLAIMED ITEMS MENU
    // ════════════════════════════════════════════════════════════════
    private void showClaimedItemsMenu() {
        primaryStage.setScene(buildTwoButtonPage(
            "Claim Items",
            "View Claimed Items", () -> showViewClaimedItems(),
            "Claim an Item",      () -> showClaimAnItem(),
            () -> showDashboard()
        ));
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 8 — VIEW CLAIMED ITEMS
    // ════════════════════════════════════════════════════════════════
    private void showViewClaimedItems() {
        BorderPane root = styledRoot();
        root.setTop(buildHeader("View Claimed Items"));

        ArrayList<FoundItem> items = itemService.getClaimedItems();

        if (items.isEmpty()) {
            Label empty = new Label("No items have been claimed yet.");
            empty.setStyle("-fx-font-size:16px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
            StackPane sp = new StackPane(empty);
            sp.setPadding(new Insets(40));
            root.setCenter(sp);
        } else {
            TableView<FoundItem> table = buildFoundItemTable(items);
            VBox content = new VBox(table);
            content.setPadding(new Insets(15, 20, 15, 20));
            VBox.setVgrow(table, Priority.ALWAYS);
            root.setCenter(content);
        }

        root.setBottom(backBar(() -> showClaimedItemsMenu()));
        primaryStage.setScene(new Scene(root));
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 9 — CLAIM AN ITEM (show list + "click to claim" button)
    // ════════════════════════════════════════════════════════════════
    private void showClaimAnItem() {
        BorderPane root = styledRoot();
        root.setTop(buildHeader("Claim an Item"));

        ArrayList<FoundItem> items = itemService.getAvailableFoundItems();
        TableView<FoundItem> table = buildFoundItemTable(items);

        Button btnClaim = new Button("Click to claim an Item");
        btnClaim.setStyle("-fx-background-color:" + DARK_GREEN + ";-fx-background-radius:25;" +
                          "-fx-text-fill:" + TEXT_LIGHT + ";-fx-font-size:16px;" +
                          "-fx-font-family:'Georgia';-fx-padding:12 30;");
        btnClaim.setOnAction(e -> showEnterClaimIdDialog(items));

        VBox content = new VBox(12, table, btnClaim);
        content.setPadding(new Insets(15, 20, 15, 20));
        content.setAlignment(Pos.CENTER);
        VBox.setVgrow(table, Priority.ALWAYS);

        root.setCenter(content);
        root.setBottom(backBar(() -> showClaimedItemsMenu()));
        primaryStage.setScene(new Scene(root));
    }

    // dialog: enter item ID to claim
    private void showEnterClaimIdDialog(ArrayList<FoundItem> items) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Claim Item");
        dialog.setResizable(false);

        VBox box = new VBox(12);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color:" + BG + ";");
        box.setAlignment(Pos.CENTER);

        Label lbl = new Label("Enter the ID of the item you want to claim.");
        lbl.setStyle("-fx-font-size:14px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
        lbl.setWrapText(true);

        TextField tfId = styledField();
        tfId.setPromptText("Item ID");
        tfId.setMaxWidth(200);

        Button btnNext = new Button("Next");
        btnNext.setStyle("-fx-background-color:" + DARK_GREEN + ";-fx-background-radius:20;" +
                         "-fx-text-fill:" + TEXT_LIGHT + ";-fx-font-family:'Georgia';-fx-padding:8 24;");

        btnNext.setOnAction(e -> {
            String input = tfId.getText().trim();
            if (input.isEmpty() || !input.matches("\\d+")) {
                showError("Please enter a valid numeric ID.");
                return;
            }
            int id = Integer.parseInt(input);
            FoundItem item = itemService.findFoundItemByID(id);
            if (item == null || item.getIsClaimed()) {
                showError("No available item found with that ID.");
                return;
            }
            dialog.close();
            showClaimValidationQuestions(item);
        });

        box.getChildren().addAll(lbl, tfId, btnNext);
        dialog.setScene(new Scene(box, 360, 200));
        dialog.showAndWait();
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 10 — CLAIM VALIDATION QUESTIONS
    // ════════════════════════════════════════════════════════════════
    private void showClaimValidationQuestions(FoundItem item) {
        BorderPane root = styledRoot();
        root.setTop(buildHeader("Claim an Item"));

        Map<String, String> questions = item.getValidationQuestions();
        Map<String, TextField> fieldMap = new LinkedHashMap<>();

        VBox form = new VBox(14);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color:" + LIGHT_PANEL + ";-fx-background-radius:14;");

        Label hint = new Label("Answer these for validation.");
        hint.setStyle("-fx-font-size:13px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
        form.getChildren().add(hint);

        for (Map.Entry<String, String> entry : questions.entrySet()) {
            TextField tf = styledField();
            fieldMap.put(entry.getKey(), tf);
            form.getChildren().add(formRow(entry.getValue(), tf));
        }

        Button btnNext = submitButton("Next");
        btnNext.setOnAction(e -> {
            // check all fields filled
            for (Map.Entry<String, TextField> entry : fieldMap.entrySet()) {
                if (entry.getValue().getText().trim().isEmpty()) {
                    showError("Please answer all questions.");
                    return;
                }
            }
            Map<String, String> claimantAnswers = new LinkedHashMap<>();
            for (Map.Entry<String, TextField> entry : fieldMap.entrySet()) {
                claimantAnswers.put(entry.getKey(), entry.getValue().getText().trim());
            }
            String result = itemService.processClaim(item.getItemID(), claimantAnswers);

            if (result.equals("THE CLAIM IS APPROVED")) {
                showClaimResultDialog(true, item);
            } else {
                showClaimResultDialog(false, null);
            }
        });

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");

        VBox outer = new VBox(15, scroll, btnNext);
        outer.setPadding(new Insets(15, 20, 10, 20));
        outer.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        root.setCenter(outer);
        root.setBottom(backBar(() -> showClaimAnItem()));
        primaryStage.setScene(new Scene(root));
    }

    // claim result dialog
    private void showClaimResultDialog(boolean approved, FoundItem item) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(approved ? "Claim Approved" : "Claim Rejected");
        dialog.setResizable(false);

        VBox box = new VBox(14);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color:" + BG + ";");
        box.setAlignment(Pos.CENTER);

        Label msg;
        if (approved) {
            msg = new Label("Your claim has been validated. Congratulations!\n\n" +
                            "Finder: " + item.getFinderName() + "\n" +
                            "Contact: " + item.getFinderContact() + "\n\n" +
                            "Please recover your item.");
        } else {
            msg = new Label("The item is not yours.\nClaim not validated.");
        }
        msg.setStyle("-fx-font-size:14px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";-fx-text-alignment:center;");
        msg.setWrapText(true);
        msg.setTextAlignment(TextAlignment.CENTER);

        Button btnClose = new Button("Close");
        btnClose.setStyle("-fx-background-color:" + DARK_GREEN + ";-fx-background-radius:20;" +
                          "-fx-text-fill:" + TEXT_LIGHT + ";-fx-font-family:'Georgia';-fx-padding:8 24;");
        btnClose.setOnAction(e -> {
            dialog.close();
            showDashboard();
        });

        box.getChildren().addAll(msg, btnClose);
        dialog.setScene(new Scene(box, 380, 240));
        dialog.showAndWait();
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 11 — LOST ITEMS MENU
    // ════════════════════════════════════════════════════════════════
    private void showLostItemsMenu() {
        primaryStage.setScene(buildTwoButtonPage(
            "Lost Items",
            "View Lost Items",    () -> showLostItemsCategories(),
            "Report a lost Item", () -> showReportLostItem(),
            () -> showDashboard()
        ));
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 12 — LOST ITEMS CATEGORIES (active vs expired)
    // ════════════════════════════════════════════════════════════════
    private void showLostItemsCategories() {
        BorderPane root = styledRoot();
        root.setTop(buildHeader("Lost Items Categories"));

        VBox centre = new VBox(20);
        centre.setAlignment(Pos.CENTER);
        centre.setPadding(new Insets(30));

        HBox btnRow = new HBox(30);
        btnRow.setAlignment(Pos.CENTER);
        btnRow.setPadding(new Insets(20));
        btnRow.setStyle("-fx-background-color:" + MID_GREEN + ";-fx-background-radius:14;");

        Button btnActive  = bigMenuButton("View Active\nlost Items", "");
        Button btnExpired = bigMenuButton("View Expired\nlost Items", "");

        btnActive.setOnAction(e  -> { viewingActiveLost = true;  showLostItemsList(true);  });
        btnExpired.setOnAction(e -> { viewingActiveLost = false; showLostItemsList(false); });

        btnRow.getChildren().addAll(btnActive, btnExpired);

        Label activeDesc  = new Label("Active Items: Items registered within the last 30 days.");
        Label expiredDesc = new Label("Expired Items: Items registered before the last 30 days.");
        activeDesc.setStyle("-fx-font-size:13px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
        expiredDesc.setStyle("-fx-font-size:13px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");

        centre.getChildren().addAll(btnRow, activeDesc, expiredDesc);

        root.setCenter(centre);
        root.setBottom(backBar(() -> showLostItemsMenu()));
        primaryStage.setScene(new Scene(root));
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 13 / 14 — ACTIVE or EXPIRED LOST ITEMS LIST with filters
    // ════════════════════════════════════════════════════════════════
    private void showLostItemsList(boolean active) {
        BorderPane root = styledRoot();
        root.setTop(buildHeader(active ? "Active Lost Items" : "Expired Lost Items"));

        // category filter buttons
        String[] cats = {LostItem.Category_2, LostItem.Category_1, LostItem.Category_3, LostItem.Category_4};
        HBox filterRow = new HBox(10);
        filterRow.setPadding(new Insets(10, 20, 5, 20));
        filterRow.setAlignment(Pos.CENTER_LEFT);

        // state: selected category
        final String[] selectedCat = {null};

        // table placeholder
        VBox tableHolder = new VBox();
        tableHolder.setPadding(new Insets(0, 20, 10, 20));
        VBox.setVgrow(tableHolder, Priority.ALWAYS);

        Runnable refreshTable = () -> {
            ArrayList<LostItem> items;
            if (selectedCat[0] == null) {
                items = active ? itemService.getActiveLostItems() : itemService.getExpiredLostItems();
            } else {
                items = active
                    ? itemService.getActiveLostByCategory(selectedCat[0])
                    : itemService.getExpiredLostByCategory(selectedCat[0]);
            }
            tableHolder.getChildren().setAll(buildLostItemTable(items, active));
        };

        List<Button> filterBtns = new ArrayList<>();
        for (String cat : cats) {
            Button btn = new Button(cat);
            btn.setStyle("-fx-background-color:" + LIGHT_PANEL + ";-fx-background-radius:20;" +
                         "-fx-font-family:'Georgia';-fx-padding:6 14;-fx-text-fill:" + TEXT_DARK + ";");
            btn.setOnAction(e -> {
                if (cat.equals(selectedCat[0])) {
                    // deselect
                    selectedCat[0] = null;
                    filterBtns.forEach(b -> b.setOpacity(1.0));
                } else {
                    selectedCat[0] = cat;
                    filterBtns.forEach(b -> b.setOpacity(0.45));
                    btn.setOpacity(1.0);
                }
                refreshTable.run();
            });
            filterBtns.add(btn);
            filterRow.getChildren().add(btn);
        }

        Label filterHint = new Label("To filter by categories, click on a category above.");
        filterHint.setStyle("-fx-font-size:12px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
        filterHint.setPadding(new Insets(0, 20, 4, 20));

        refreshTable.run();

        VBox content = new VBox(filterRow, filterHint, tableHolder);
        VBox.setVgrow(tableHolder, Priority.ALWAYS);

        root.setCenter(content);
        root.setBottom(backBar(() -> showLostItemsCategories()));
        primaryStage.setScene(new Scene(root));
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 15 — VIEW LOST ITEM PICTURE
    // ════════════════════════════════════════════════════════════════
    private void showLostItemPicture(String imagePath, boolean fromActive) {
        BorderPane root = styledRoot();
        root.setTop(buildHeader("View Lost Item Picture"));

        VBox centre = new VBox();
        centre.setAlignment(Pos.CENTER);
        centre.setPadding(new Insets(20));

        Rectangle imgBox = new Rectangle(420, 310);
        imgBox.setArcWidth(20);
        imgBox.setArcHeight(20);
        imgBox.setFill(Color.web(DARK_GREEN));

        Label placeholder = new Label(imagePath != null ? imagePath : "It will display the picture here");
        placeholder.setStyle("-fx-font-size:14px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_LIGHT + ";");

        StackPane imgPane = new StackPane(imgBox, placeholder);

        centre.getChildren().add(imgPane);
        root.setCenter(centre);
        root.setBottom(backBar(() -> showLostItemsList(fromActive)));
        primaryStage.setScene(new Scene(root));
    }

    // ════════════════════════════════════════════════════════════════
    // PAGE 16 / 17 / 18 — REPORT LOST ITEM
    // ════════════════════════════════════════════════════════════════
    private void showReportLostItem() {
        BorderPane root = styledRoot();
        root.setTop(buildHeader("Lost Item Registration"));

        TextField tfName     = styledField();
        ComboBox<String> cbCat = styledCombo(
            LostItem.Category_1, LostItem.Category_2, LostItem.Category_3, LostItem.Category_4);
        TextField tfLocation = styledField();
        TextField tfDesc     = styledField();
        TextField tfOwner    = styledField();
        TextField tfContact  = styledField();

        // dim fields until category chosen
        tfLocation.setDisable(true);
        tfDesc.setDisable(true);
        tfOwner.setDisable(true);
        tfContact.setDisable(true);

        cbCat.setOnAction(e -> {
            boolean sel = cbCat.getValue() != null;
            tfLocation.setDisable(!sel);
            tfDesc.setDisable(!sel);
            tfOwner.setDisable(!sel);
            tfContact.setDisable(!sel);
        });

        // image path — add button
        final String[] imagePathHolder = {null};
        Label lblImageStatus = new Label("No image selected");
        lblImageStatus.setStyle("-fx-font-size:12px;-fx-text-fill:" + TEXT_DARK + ";");

        Button btnAdd = new Button("Add");
        btnAdd.setStyle("-fx-background-color:" + LIGHT_PANEL + ";-fx-background-radius:20;" +
                        "-fx-padding:6 20;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
        btnAdd.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Select Image");
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));
            java.io.File file = fc.showOpenDialog(primaryStage);
            if (file != null) {
                imagePathHolder[0] = file.getAbsolutePath();
                lblImageStatus.setText(file.getName());
            }
        });

        HBox imageRow = new HBox(10, btnAdd, lblImageStatus);
        imageRow.setAlignment(Pos.CENTER_LEFT);

        VBox form = new VBox(14);
        form.setPadding(new Insets(20, 40, 20, 40));
        form.setStyle("-fx-background-color:" + LIGHT_PANEL + ";-fx-background-radius:14;");
        form.getChildren().addAll(
            formRow("Item Name",        tfName),
            formRow("Category:",        cbCat),
            formRow("Last Location:",   tfLocation),
            formRow("Description:",     tfDesc),
            formRow("Owner's Name:",    tfOwner),
            formRow("Owner's Contact:", tfContact),
            formRow("Picture (optional):\nJPG/PNG only", imageRow)
        );

        Button btnSubmit = submitButton("Submit");
        btnSubmit.setOnAction(e -> {
            String name    = tfName.getText().trim();
            String catStr  = cbCat.getValue();
            String loc     = tfLocation.getText().trim();
            String desc    = tfDesc.getText().trim();
            String owner   = tfOwner.getText().trim();
            String contact = tfContact.getText().trim();

            if (name.isEmpty() || catStr == null || loc.isEmpty()
                    || desc.isEmpty() || owner.isEmpty() || contact.isEmpty()) {
                showError("Please fill in all required fields.");
                return;
            }
            if (!contact.matches("03\\d{9}")) {
                showError("Enter a valid 11-digit Pakistani number starting with 03.");
                return;
            }
            try {
                itemService.registerLostItem(name, loc, catStr, owner, contact, desc, imagePathHolder[0]);
                showSuccessDialog("Registration Successful!", () -> showDashboard());
            } catch (Exception ex) {
                showSuccessDialog("Registration Failed\nPlease try again later.", () -> showDashboard());
            }
        });

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:transparent;-fx-background-color:transparent;");

        VBox outer = new VBox(15, scroll, btnSubmit);
        outer.setPadding(new Insets(15, 20, 10, 20));
        outer.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        root.setCenter(outer);
        root.setBottom(backBar(() -> showLostItemsMenu()));
        primaryStage.setScene(new Scene(root));
    }

    // ════════════════════════════════════════════════════════════════
    // HELPER — build lost item table with Picture column
    // ════════════════════════════════════════════════════════════════
    @SuppressWarnings("unchecked")
    private TableView<LostItem> buildLostItemTable(ArrayList<LostItem> items, boolean fromActive) {
        TableView<LostItem> table = new TableView<>();
        table.setStyle("-fx-font-family:'Georgia';-fx-font-size:13px;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<LostItem, Integer> colId = new TableColumn<>("Item ID");
        colId.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getItemID()).asObject());

        TableColumn<LostItem, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));

        TableColumn<LostItem, String> colCat = new TableColumn<>("Category");
        colCat.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCategory()));

        TableColumn<LostItem, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDate().toString()));

        TableColumn<LostItem, String> colLoc = new TableColumn<>("Location");
        colLoc.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getLocation()));

        // Picture column — "Available" is clickable if imagePath not null
        TableColumn<LostItem, Void> colPic = new TableColumn<>("Picture");
        colPic.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                LostItem li = (LostItem) getTableRow().getItem();
                if (li.getImagePath() != null) {
                    Hyperlink link = new Hyperlink("Available");
                    link.setStyle("-fx-font-family:'Georgia';-fx-text-fill:" + DARK_GREEN + ";");
                    link.setOnAction(e -> showLostItemPicture(li.getImagePath(), fromActive));
                    setGraphic(link);
                } else {
                    Label lbl = new Label("Not Available");
                    lbl.setStyle("-fx-font-family:'Georgia';-fx-text-fill:#777;");
                    setGraphic(lbl);
                }
            }
        });

        table.getColumns().addAll(colId, colName, colCat, colDate, colLoc, colPic);
        table.setItems(FXCollections.observableArrayList(items));
        styleTable(table);
        return table;
    }

    // ════════════════════════════════════════════════════════════════
    // HELPER — build found item table
    // ════════════════════════════════════════════════════════════════
    @SuppressWarnings("unchecked")
    private TableView<FoundItem> buildFoundItemTable(ArrayList<FoundItem> items) {
        TableView<FoundItem> table = new TableView<>();
        table.setStyle("-fx-font-family:'Georgia';-fx-font-size:13px;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<FoundItem, Integer> colId = new TableColumn<>("Item ID");
        colId.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getItemID()).asObject());

        TableColumn<FoundItem, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));

        TableColumn<FoundItem, String> colCat = new TableColumn<>("Category");
        colCat.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCategory()));

        TableColumn<FoundItem, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDate().toString()));

        TableColumn<FoundItem, String> colLoc = new TableColumn<>("Location");
        colLoc.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getLocation()));

        TableColumn<FoundItem, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
            d.getValue().getIsClaimed() ? "Claimed" : "Not Claimed"));

        table.getColumns().addAll(colId, colName, colCat, colDate, colLoc, colStatus);
        table.setItems(FXCollections.observableArrayList(items));
        styleTable(table);
        return table;
    }


    private <T> void styleTable(TableView<T> table) {
        table.setStyle(
            "-fx-background-color:transparent;" +
            "-fx-table-cell-border-color:" + MID_GREEN + ";" +
            "-fx-font-family:'Georgia';"
        );

        table.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();

            row.styleProperty().bind(
                javafx.beans.binding.Bindings.when(row.indexProperty().greaterThanOrEqualTo(0))
                .then("-fx-background-color:" + (tv.getItems().indexOf(row.getItem()) % 2 == 0
                    ? LIGHT_PANEL : MID_GREEN) + ";")
                .otherwise("")
            );

            return row;
        });
    }


    // ════════════════════════════════════════════════════════════════
    // REUSABLE — two-big-button page (pages 2, 7, 11)
    // ════════════════════════════════════════════════════════════════
    private Scene buildTwoButtonPage(String title,
                                     String leftLabel,  Runnable leftAction,
                                     String rightLabel, Runnable rightAction,
                                     Runnable backAction) {
        BorderPane root = styledRoot();
        root.setTop(buildHeader(title));

        HBox btnRow = new HBox(40);
        btnRow.setAlignment(Pos.CENTER);
        btnRow.setPadding(new Insets(30));
        btnRow.setStyle("-fx-background-color:" + MID_GREEN + ";-fx-background-radius:14;");

        Button btnL = bigMenuButton(leftLabel,  "");
        Button btnR = bigMenuButton(rightLabel, "");
        btnL.setOnAction(e -> leftAction.run());
        btnR.setOnAction(e -> rightAction.run());

        btnRow.getChildren().addAll(btnL, btnR);

        StackPane centre = new StackPane(btnRow);
        centre.setPadding(new Insets(60, 80, 40, 80));

        root.setCenter(centre);
        root.setBottom(backBar(backAction));
        return new Scene(root);
    }

    // ════════════════════════════════════════════════════════════════
    // REUSABLE UI COMPONENTS
    // ════════════════════════════════════════════════════════════════

    private BorderPane styledRoot() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG + ";");
        return root;
    }

    private VBox buildHeader(String title) {
        Label lbl = new Label(title);
        lbl.setStyle("-fx-font-size:22px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
        lbl.setPadding(new Insets(16, 20, 4, 20));

        Rectangle line1 = new Rectangle();
        line1.setHeight(3);
        line1.setFill(Color.web(TEXT_DARK));
        line1.widthProperty().bind(primaryStage.widthProperty());

        Rectangle line2 = new Rectangle();
        line2.setHeight(6);
        line2.setFill(Color.web(DARK_GREEN));
        line2.widthProperty().bind(primaryStage.widthProperty());

        VBox header = new VBox(lbl, line1, line2);
        header.setStyle("-fx-background-color:" + BG + ";");
        return header;
    }

    private HBox backBar(Runnable action) {
        Button btn = new Button("← Back");
        btn.setStyle("-fx-background-color:" + MID_GREEN + ";-fx-background-radius:20;" +
                     "-fx-text-fill:" + TEXT_DARK + ";-fx-font-family:'Georgia';" +
                     "-fx-font-size:14px;-fx-padding:8 20;");
        btn.setOnAction(e -> action.run());

        HBox bar = new HBox(btn);
        bar.setAlignment(Pos.BOTTOM_RIGHT);
        bar.setPadding(new Insets(8, 15, 12, 0));
        return bar;
    }

    private Button bigMenuButton(String label, String icon) {
        Button btn = new Button((icon.isEmpty() ? "" : icon + " ") + label);
        btn.setStyle("-fx-background-color:" + DARK_GREEN + ";-fx-background-radius:16;" +
                     "-fx-text-fill:" + TEXT_LIGHT + ";-fx-font-size:17px;" +
                     "-fx-font-family:'Georgia';-fx-padding:40 30;-fx-wrap-text:true;" +
                     "-fx-text-alignment:center;");
        btn.setPrefWidth(200);
        btn.setPrefHeight(150);
        return btn;
    }

    private TextField styledField() {
        TextField tf = new TextField();
        tf.setStyle("-fx-background-color:" + DARK_GREEN + ";-fx-background-radius:25;" +
                    "-fx-text-fill:" + TEXT_LIGHT + ";-fx-font-family:'Georgia';" +
                    "-fx-font-size:14px;-fx-padding:8 16;");
        tf.setPrefWidth(340);
        return tf;
    }

    private ComboBox<String> styledCombo(String... options) {
        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll(options);
        cb.setPromptText("Select a category  ▼");
        cb.setStyle("-fx-background-color:" + DARK_GREEN + ";-fx-background-radius:25;" +
                    "-fx-font-family:'Georgia';-fx-font-size:14px;-fx-text-fill:" + TEXT_LIGHT + ";");
        cb.setPrefWidth(340);
        return cb;
    }

    private HBox formRow(String labelText, javafx.scene.Node field) {
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-font-size:14px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
        lbl.setPrefWidth(160);
        lbl.setWrapText(true);
        HBox row = new HBox(20, lbl, field);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(field, Priority.ALWAYS);
        return row;
    }

    private Button submitButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color:" + LIGHT_PANEL + ";-fx-background-radius:20;" +
                     "-fx-text-fill:" + TEXT_DARK + ";-fx-font-family:'Georgia';" +
                     "-fx-font-size:15px;-fx-padding:10 40;");
        return btn;
    }

    private VBox reportStatLabel(String title, String value) {
        Label t = new Label(title);
        t.setStyle("-fx-font-size:14px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
        Label v = new Label("(" + value + ")");
        v.setStyle("-fx-font-size:12px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
        VBox box = new VBox(2, t, v);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private VBox buildEfficiencyBar(double pct) {
        Label title = new Label("Efficiency:");
        title.setStyle("-fx-font-size:14px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");

        StackPane bar = new StackPane();
        bar.setPrefHeight(18);
        bar.setMaxWidth(160);

        Rectangle bg = new Rectangle(160, 18);
        bg.setArcWidth(18); bg.setArcHeight(18);
        bg.setFill(Color.web("#b0c4ba"));

        double fillW = Math.max(0, Math.min(160, 160 * pct / 100.0));
        Rectangle fill = new Rectangle(fillW, 18);
        fill.setArcWidth(18); fill.setArcHeight(18);
        fill.setFill(Color.web(DARK_GREEN));
        StackPane.setAlignment(fill, Pos.CENTER_LEFT);

        Label pctLabel = new Label(String.format("%.0f%%", pct));
        pctLabel.setStyle("-fx-font-size:11px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
        pctLabel.setPadding(new Insets(0, 0, 0, 170));

        bar.getChildren().addAll(bg, fill);

        HBox row = new HBox(8, bar, pctLabel);
        row.setAlignment(Pos.CENTER_LEFT);

        return new VBox(4, title, row);
    }

    // success/fail dialog matching colour theme
    private void showSuccessDialog(String message, Runnable onClose) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Status");
        dialog.setResizable(false);

        VBox box = new VBox(16);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color:" + BG + ";-fx-border-color:" + DARK_GREEN +
                     ";-fx-border-width:2;-fx-border-radius:12;-fx-background-radius:12;");
        box.setAlignment(Pos.CENTER);

        Label msg = new Label(message);
        msg.setStyle("-fx-font-size:15px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";-fx-text-alignment:center;");
        msg.setWrapText(true);
        msg.setTextAlignment(TextAlignment.CENTER);

        Button btnClose = new Button("Close");
        btnClose.setStyle("-fx-background-color:" + DARK_GREEN + ";-fx-background-radius:20;" +
                          "-fx-text-fill:" + TEXT_LIGHT + ";-fx-font-family:'Georgia';-fx-padding:8 24;");
        btnClose.setOnAction(e -> {
            dialog.close();
            onClose.run();
        });

        box.getChildren().addAll(msg, btnClose);
        dialog.setScene(new Scene(box, 340, 180));
        dialog.showAndWait();
    }

    private void showError(String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Error");
        dialog.setResizable(false);

        VBox box = new VBox(14);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color:" + BG + ";");
        box.setAlignment(Pos.CENTER);

        Label msg = new Label(message);
        msg.setStyle("-fx-font-size:14px;-fx-font-family:'Georgia';-fx-text-fill:" + TEXT_DARK + ";");
        msg.setWrapText(true);
        msg.setTextAlignment(TextAlignment.CENTER);

        Button btn = new Button("OK");
        btn.setStyle("-fx-background-color:" + DARK_GREEN + ";-fx-background-radius:20;" +
                     "-fx-text-fill:" + TEXT_LIGHT + ";-fx-font-family:'Georgia';-fx-padding:7 22;");
        btn.setOnAction(e -> dialog.close());

        box.getChildren().addAll(msg, btn);
        dialog.setScene(new Scene(box, 320, 160));
        dialog.showAndWait();
    }

    // map category string to int choice for createFoundItem()
    private int getCategoryChoice(String cat) {
        return switch (cat) {
            case "Electronics" -> 1;
            case "Accessories" -> 2;
            case "Clothing"    -> 3;
            case "Documents"   -> 4;
            case "Keys"        -> 5;
            case "Bags"        -> 6;
            case "Jewelry"     -> 7;
            case "Books"       -> 8;
            default            -> 1;
        };
    }

    // save all data on exit
    private void saveAll() {
        FileHandler.saveFoundItems(foundStorage.getAllItemsForSave());
        FileHandler.saveLostItems(lostStorage.getAllItemsForSave());
        FileHandler.saveCounter(Item.getCounter());
        FileHandler.saveCounters(
            itemService.getTotalFoundRegistered(),
            itemService.getTotalLostRegistered(),
            itemService.getTotalClaimed()
        );
    }
}
