module com.emberkeep.emberkeep {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.emberkeep.emberkeep to javafx.fxml;
    exports com.emberkeep.emberkeep;
}