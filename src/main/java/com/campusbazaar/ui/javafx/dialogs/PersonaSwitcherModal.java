package com.campusbazaar.ui.javafx.dialogs;

import com.campusbazaar.model.Admin;
import com.campusbazaar.model.Student;
import com.campusbazaar.model.User;
import com.campusbazaar.model.enums.AccountStatus;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.function.Consumer;

/**
 * Persona Switcher Dialog in JavaFX.
 * Seamlessly toggles user roles between Student Buyer, Student Seller, and Campus Administrator.
 */
public class PersonaSwitcherModal extends Dialog<User> {

    public PersonaSwitcherModal(User currentActiveUser, Consumer<User> onPersonaSelected) {
        setTitle("Switch Student Persona");
        setHeaderText(null);

        DialogPane pane = getDialogPane();
        pane.getStylesheets().add(getClass().getResource("/styles/marketplace.css").toExternalForm());
        pane.setStyle("-fx-background-color: -cb-card-bg; -fx-padding: 16px;");
        pane.getButtonTypes().add(ButtonType.CLOSE);

        initializeUI(pane, onPersonaSelected);
    }

    private void initializeUI(DialogPane pane, Consumer<User> onPersonaSelected) {
        VBox container = new VBox(12);
        container.setPrefWidth(420);

        Label header = new Label("🎓 Select Active Campus Persona");
        header.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: -cb-text;");

        // Option 1: Student Buyer
        Student buyer = new Student(1, "Aarav Patel", "aarav@campus.edu", "pass", "9876543210",
                AccountStatus.ACTIVE, "2023-CS-042", "Computer Science & Engg", "Hostel 4, Room 204", true, 4.9, 12);
        container.getChildren().add(createPersonaOption("A", "Aarav Patel", "🎓 Student Buyer • 2nd Year CSE", buyer, onPersonaSelected));

        // Option 2: Student Seller
        Student seller = new Student(2, "Rohan Verma", "rohan@campus.edu", "pass", "9876543211",
                AccountStatus.ACTIVE, "2024-ME-088", "Mechanical Engineering", "Hostel 2, Room 108", true, 4.8, 8);
        container.getChildren().add(createPersonaOption("R", "Rohan Verma", "🏷️ Student Seller • 1st Year ME", seller, onPersonaSelected));

        // Option 3: Admin
        Admin admin = new Admin(3, "Prof. R.K. Mehta", "admin@campus.edu", "pass", "9876543212",
                AccountStatus.ACTIVE, "STF-992", "Head of Student Affairs");
        container.getChildren().add(createPersonaOption("M", "Prof. R.K. Mehta", "🛡️ Campus Administrator • Student Affairs", admin, onPersonaSelected));

        pane.setContent(container);
    }

    private HBox createPersonaOption(String initial, String name, String subtitle, User user, Consumer<User> onSelected) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: -cb-card-bg; -fx-border-color: -cb-border; -fx-border-radius: 12px; -fx-background-radius: 12px; -fx-padding: 10px 14px; -fx-cursor: hand;");

        Label avatar = new Label(initial);
        avatar.getStyleClass().add("user-persona-avatar");

        VBox textBox = new VBox(2);
        Label nameLbl = new Label(name);
        nameLbl.setStyle("-fx-font-weight: 800; -fx-font-size: 13px; -fx-text-fill: -cb-text;");
        Label subLbl = new Label(subtitle);
        subLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: -cb-text-muted;");
        textBox.getChildren().addAll(nameLbl, subLbl);

        row.getChildren().addAll(avatar, textBox);

        row.setOnMouseClicked(e -> {
            close();
            if (onSelected != null) {
                onSelected.accept(user);
            }
        });

        return row;
    }
}
