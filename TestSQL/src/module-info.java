module TestSQL {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;

    opens sample;
    opens sample.Login;
    opens sample.EDIT;
}