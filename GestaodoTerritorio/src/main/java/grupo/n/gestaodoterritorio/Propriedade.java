package grupo.n.gestaodoterritorio;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.vividsolutions.jts.geom.MultiPolygon;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Propriedade {
    private String objectId;
    private String parId;
    private String parNum;
    private double shapeLength;
    private double shapeArea;
    private MultiPolygon geometry;
    private int owner;

    public Propriedade(String objectId, String parId, String parNum, double shapeLength, double shapeArea, MultiPolygon geometry, int owner) {
        this.objectId = objectId;
        this.parId = parId;
        this.parNum = parNum;
        this.shapeLength = shapeLength;
        this.shapeArea = shapeArea;
        this.geometry = geometry;
        this.owner = owner;

    }

    public MultiPolygon getGeometry() {
        return geometry;
    }
    public boolean isAdjacentTo(Propriedade other) {
        // Check if two geometries actually touch
        System.out.println(this.geometry.touches(other.getGeometry()));  // This should print true or false

        return this.geometry.touches(other.getGeometry());  // Verifica se são adjacentes
    }


}
