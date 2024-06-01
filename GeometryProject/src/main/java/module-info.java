module org.example.geometryproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens org.example.geometryproject to javafx.fxml, javafx.graphics, javafx.controls, java.naming;
    exports org.example.geometryproject;
    exports org.example.geometryproject.controller;
    opens org.example.geometryproject.controller to java.naming, javafx.controls, javafx.fxml, javafx.graphics;
}