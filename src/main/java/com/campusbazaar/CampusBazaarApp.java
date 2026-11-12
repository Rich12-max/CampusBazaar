package com.campusbazaar;

import com.campusbazaar.database.DataStore;
import com.campusbazaar.model.*;
import com.campusbazaar.model.enums.AccountStatus;
import com.campusbazaar.service.*;
import com.campusbazaar.ui.javafx.dialogs.*;
import com.campusbazaar.ui.javafx.theme.ThemeManager;
import com.campusbazaar.ui.javafx.views.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Campus Peer-to-Peer Marketplace (CampusBazaar) - JavaFX Application Entrypoint.
 * 100% Native Java architecture preserving the existing modern visual experience.
 */
public class CampusBazaarApp extends Application {

    private DataStore dataStore;
    private ListingService listingService;
    private RentalService rentalService;

    private Student currentStudent;
    private User currentActiveUser;

    private Stage primaryStage;
    private StackPane rootOverlay;
    private BorderPane mainLayout;
    private VBox marketplaceContentBox;
    private ScrollPane centerScrollPane;

    private TopNavBarView topNavBar;
    private SubNavBarView subNavBar;
    private HeroBannerView heroBanner;
    private ControlsSectionView controlsSection;
    private CatalogueGridView catalogueGrid;

    private RentalsView rentalsView;
    private BarterSwapsView barterSwapsView;
    private SafeHandoversView safeHandoversView;
    private PriceBidsView priceBidsView;
    private ClearanceReceiptsView clearanceReceiptsView;
    private AdminModerationView adminModerationView;

    private List<Listing> masterListings = new ArrayList<>();
    private ControlsSectionView.FilterCriteria currentFilter = new ControlsSectionView.FilterCriteria();

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.dataStore = DataStore.getInstance();
        this.listingService = new ListingService();
        this.rentalService = new RentalService();

        // Default Persona: Aarav Patel (Student Buyer)
        this.currentStudent = new Student(1, "Aarav Patel", "aarav@campus.edu", "pass", "9876543210",
                AccountStatus.ACTIVE, "2023-CS-042", "Computer Science & Engg", "Hostel 4, Room 204", true, 4.9, 12);
        this.currentActiveUser = currentStudent;

        this.masterListings = new ArrayList<>(dataStore.getListings());

        buildUI();

        Scene scene = new Scene(rootOverlay, 1180, 780);
        scene.getStylesheets().add(getClass().getResource("/styles/marketplace.css").toExternalForm());
        ThemeManager.applyTheme(rootOverlay, ThemeManager.Theme.CAMPUS);

        // Global hotkey: '/' or 'Ctrl+K' opens Quick Search Modal
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if ((event.getCode() == KeyCode.SLASH || (event.isControlDown() && event.getCode() == KeyCode.K))) {
                if (!(scene.getFocusOwner() instanceof TextInputControl)) {
                    event.consume();
                    openQuickSearch();
                }
            }
        });

        stage.setTitle("CampusBazaar 🎓 Student Peer-to-Peer Marketplace");
        stage.setScene(scene);
        stage.setMinWidth(960);
        stage.setMinHeight(640);
        stage.show();
    }

    private void buildUI() {
        rootOverlay = new StackPane();
        mainLayout = new BorderPane();

        // 1. Top Section: Top Brand Nav + Sub Category Nav
        VBox topNavContainer = new VBox(0);
        topNavBar = new TopNavBarView(
            rootOverlay,
            this::openSellItemModal,
            this::openQuickSearch,
            this::openSmartPricingModal,
            this::openPersonaModal
        );

        subNavBar = new SubNavBarView(this::handleTabSelected);
        topNavContainer.getChildren().addAll(topNavBar, subNavBar);
        mainLayout.setTop(topNavContainer);

        // 2. Marketplace Main Views
        marketplaceContentBox = new VBox(22);
        marketplaceContentBox.setPadding(new Insets(24, 32, 40, 32));

        heroBanner = new HeroBannerView(this::openSellItemModal, this::openQuickSearch);

        controlsSection = new ControlsSectionView(criteria -> {
            this.currentFilter = criteria;
            applyFilter();
        });

        catalogueGrid = new CatalogueGridView(
            this::handleListingAction,
            this::handleListingDetails,
            this::handleWishlistToggle
        );

        marketplaceContentBox.getChildren().addAll(heroBanner, controlsSection, catalogueGrid);

        centerScrollPane = new ScrollPane(marketplaceContentBox);
        centerScrollPane.setFitToWidth(true);
        centerScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        mainLayout.setCenter(centerScrollPane);

        // 3. Floating Corner Action Button (Bottom-Right)
        Button floatingSearchBtn = new Button("🔍 Find Item  /");
        floatingSearchBtn.getStyleClass().add("floating-corner-search");
        floatingSearchBtn.setOnAction(e -> openQuickSearch());
        StackPane.setAlignment(floatingSearchBtn, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(floatingSearchBtn, new Insets(0, 26, 26, 0));

        rootOverlay.getChildren().addAll(mainLayout, floatingSearchBtn);

        // Initial Data Load
        applyFilter();
    }

    private void handleTabSelected(SubNavBarView.TabId tab) {
        switch (tab) {
            case MARKETPLACE:
                centerScrollPane.setContent(marketplaceContentBox);
                break;
            case RENTALS:
                if (rentalsView == null) {
                    List<RentalListing> rentals = masterListings.stream()
                        .filter(l -> l instanceof RentalListing)
                        .map(l -> (RentalListing) l)
                        .collect(Collectors.toList());
                    rentalsView = new RentalsView(rentalService, rentals);
                }
                centerScrollPane.setContent(rentalsView);
                break;
            case BARTER:
                if (barterSwapsView == null) {
                    List<BarterListing> barters = masterListings.stream()
                        .filter(l -> l instanceof BarterListing)
                        .map(l -> (BarterListing) l)
                        .collect(Collectors.toList());
                    barterSwapsView = new BarterSwapsView(barters);
                }
                centerScrollPane.setContent(barterSwapsView);
                break;
            case HANDOVERS:
                if (safeHandoversView == null) safeHandoversView = new SafeHandoversView();
                centerScrollPane.setContent(safeHandoversView);
                break;
            case NEGOTIATIONS:
                if (priceBidsView == null) priceBidsView = new PriceBidsView();
                centerScrollPane.setContent(priceBidsView);
                break;
            case RECEIPTS:
                if (clearanceReceiptsView == null) clearanceReceiptsView = new ClearanceReceiptsView();
                centerScrollPane.setContent(clearanceReceiptsView);
                break;
            case WISHLIST:
                centerScrollPane.setContent(marketplaceContentBox);
                Alert wAlert = new Alert(Alert.AlertType.INFORMATION, "Showing your 2 wishlisted campus items!");
                wAlert.showAndWait();
                break;
            case ADMIN:
                if (adminModerationView == null) adminModerationView = new AdminModerationView();
                centerScrollPane.setContent(adminModerationView);
                break;
        }
    }

    private void applyFilter() {
        String q = (currentFilter.query != null) ? currentFilter.query.toLowerCase() : "";
        String cat = currentFilter.category;
        String type = currentFilter.listingType;
        String sort = currentFilter.sortBy;

        List<Listing> filtered = masterListings.stream()
            .filter(item -> {
                Product p = item.getProduct();
                if (p == null) return false;

                if (!q.isEmpty()) {
                    boolean match = p.getTitle().toLowerCase().contains(q) ||
                        (p.getDescription() != null && p.getDescription().toLowerCase().contains(q));
                    if (!match) return false;
                }
                if (!"All Types".equalsIgnoreCase(type)) {
                    if ("Outright Sale".equalsIgnoreCase(type) && !(item instanceof SaleListing)) return false;
                    if ("Campus Rental".equalsIgnoreCase(type) && !(item instanceof RentalListing)) return false;
                    if ("Peer Item Swap".equalsIgnoreCase(type) && !(item instanceof BarterListing)) return false;
                }
                return true;
            })
            .collect(Collectors.toList());

        if ("Price: Low to High".equalsIgnoreCase(sort)) {
            filtered.sort(Comparator.comparingDouble(Listing::getEffectivePrice));
        } else if ("Price: High to Low".equalsIgnoreCase(sort)) {
            filtered.sort(Comparator.comparingDouble(Listing::getEffectivePrice).reversed());
        }

        catalogueGrid.renderListings(filtered);
    }

    private void openQuickSearch() {
        QuickSearchModal modal = new QuickSearchModal(
            masterListings,
            this::handleListingDetails,
            this::openSellItemModal
        );
        modal.initOwner(primaryStage);
        modal.showAndWait();
    }

    private void openSellItemModal() {
        SellItemModal modal = new SellItemModal(currentStudent, newListing -> {
            masterListings.add(0, newListing);
            dataStore.addListing(newListing);
            applyFilter();
            String title = (newListing.getProduct() != null) ? newListing.getProduct().getTitle() : "Item";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item \"" + title + "\" published successfully to the campus marketplace!");
            alert.showAndWait();
        });
        modal.initOwner(primaryStage);
        modal.showAndWait();
    }

    private void openSmartPricingModal() {
        SmartPricingModal modal = new SmartPricingModal();
        modal.initOwner(primaryStage);
        modal.showAndWait();
    }

    private void openPersonaModal() {
        PersonaSwitcherModal modal = new PersonaSwitcherModal(currentActiveUser, selectedUser -> {
            this.currentActiveUser = selectedUser;
            if (selectedUser instanceof Student) {
                this.currentStudent = (Student) selectedUser;
            }
            String initial = selectedUser.getFullName().substring(0, 1);
            String roleStr = (selectedUser instanceof Admin) ? "🛡️ Campus Administrator" : "🎓 " + selectedUser.getRole();
            topNavBar.updatePersona(selectedUser.getFullName(), roleStr, initial);
        });
        modal.initOwner(primaryStage);
        modal.showAndWait();
    }

    private void handleListingAction(Listing listing) {
        if (listing instanceof RentalListing) {
            subNavBar.setActiveTab(SubNavBarView.TabId.RENTALS);
        } else if (listing instanceof BarterListing) {
            subNavBar.setActiveTab(SubNavBarView.TabId.BARTER);
        } else {
            String title = (listing.getProduct() != null) ? listing.getProduct().getTitle() : "Item";
            String sellerName = (listing.getSeller() != null) ? listing.getSeller().getFullName() : "Seller";
            TextInputDialog dialog = new TextInputDialog(String.valueOf(Math.round(listing.getEffectivePrice())));
            dialog.setTitle("Submit Purchase Request");
            dialog.setHeaderText("Request to Buy: " + title);
            dialog.setContentText("Offered Price (₹):");
            dialog.showAndWait().ifPresent(price -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Purchase request of ₹" + price + " sent to seller " + sellerName + "!");
                alert.showAndWait();
            });
        }
    }

    private void handleListingDetails(Listing listing) {
        ProductDetailsModal modal = new ProductDetailsModal(listing, this::handleListingAction);
        modal.initOwner(primaryStage);
        modal.showAndWait();
    }

    private void handleWishlistToggle(Listing listing) {
        // Toggle in wishlist
    }

    public static void main(String[] args) {
        launch(args);
    }
}
