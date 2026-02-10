module com.danieldpereira.usersystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.dotenv;

    opens com.danieldpereira.usersystem to javafx.fxml;
    exports com.danieldpereira.usersystem;
    exports com.danieldpereira.usersystem.dao;
}