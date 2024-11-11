package grupo.n.gestaodoterritorio;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import mil.nga.sf.geojson.MultiPolygon;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Propriedade {
    private String objectId;
    private String parId;
    private String parNum;
    private double shapeArea;
    private MultiPolygon geometry;
    private int owner;

    public Propriedade(String objectId, String parId, String parNum, double shapeArea, MultiPolygon geometry, int owner) {
        this.objectId = objectId;
        this.parId = parId;
        this.parNum = parNum;
        this.shapeArea = shapeArea;
        this.geometry = geometry;
        this.owner = owner;

    }

}
