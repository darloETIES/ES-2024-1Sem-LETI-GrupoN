module grupo.n.gestaodoterritorio {
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires jts.core;
    requires org.apache.commons.csv;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.jgrapht.ext;
    requires java.desktop;

    opens grupo.n.gestaodoterritorio to javafx.fxml;
    exports grupo.n.gestaodoterritorio;
    exports grupo.n.gestaodoterritorio.controllers;
    opens grupo.n.gestaodoterritorio.controllers to javafx.fxml;
}