package grupo.n.gestaodoterritorio;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

public class Property {
    private String objectId;
    private String parId;
    private String parNum;
    private double shapeLength;
    private double shapeArea;
    private Geometry geometry;
    private String owner;
    private String parish;
    private String county;
    private String district;

    public Property(String objectId, String parId, String parNum, double shapeLength, double shapeArea, Geometry geometry, String owner, String parish, String county, String district) {
        this.objectId = objectId;
        this.parId = parId;
        this.parNum = parNum;
        this.shapeLength = shapeLength;
        this.shapeArea = shapeArea;
        this.geometry = geometry;
        this.owner = owner;
        this.parish = parish;
        this.county = county;
        this.district = district;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setParId(String parId) {
        this.parId = parId;
    }

    public String getParId() {
        return parId;
    }

    public void setParNum(String parNum) {
        this.parNum = parNum;
    }

    public String getParNum() {
        return parNum;
    }

    public void setShapeLength(double shapeLength) {
        this.shapeLength = shapeLength;
    }

    public double getShapeLength() {
        return shapeLength;
    }

    public void setShapeArea(double shapeArea) {
        this.shapeArea = shapeArea;
    }

    public double getShapeArea() {
        return shapeArea;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }
    public String getParish() {
        return parish;
    }
    public void setCounty(String county) {
        this.county = county;
    }
    public String getCounty() {
        return county;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getDistrict() {
        return district;
    }


    @Override
    public String toString() {
        return "ObjectID: " + objectId + " | ParID: " + parId + " | ParNum: " + parNum + " Owner: " + owner + " Parish: " + parish + "County: " + county + "District: " + district;
    }
}
