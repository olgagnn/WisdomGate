module ece.wisdomgate {
    requires javafx.controls;
    requires javafx.fxml;


    opens ece.wisdomgate to javafx.fxml;
    exports ece.wisdomgate;
    exports ece.wisdomgate.MyControllers;
    opens ece.wisdomgate.MyControllers to javafx.fxml;
}