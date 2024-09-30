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
    requires javafx.media;

    opens org.example.geometryproject to javafx.fxml, javafx.graphics, javafx.controls, java.naming;
    exports org.example.geometryproject;
    exports org.example.geometryproject.controller;
    opens org.example.geometryproject.controller to java.naming, javafx.controls, javafx.fxml, javafx.graphics;
    exports org.example.geometryproject.core;
    opens org.example.geometryproject.core to java.naming, javafx.controls, javafx.fxml, javafx.graphics;
    exports org.example.geometryproject.utilities;
    opens org.example.geometryproject.utilities to java.naming, javafx.controls, javafx.fxml, javafx.graphics;
    exports org.example.geometryproject.main;
    opens org.example.geometryproject.main to java.naming, javafx.controls, javafx.fxml, javafx.graphics;
}