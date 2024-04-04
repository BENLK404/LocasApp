module ben.locasapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens ben.locasapplication to javafx.fxml;
    exports ben.locasapplication;
    exports controllers;
    opens controllers to javafx.fxml;
    exports database;
    opens database to javafx.fxml;
    exports dao;
    opens dao to javafx.fxml;
}