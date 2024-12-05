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
    private Owner owner;
    private String parish;
    private String county;
    private String district;

    /**
     * Construtor de propriedades
     * @param objectId ID da propriedade
     * @param parId ID da parcela
     * @param parNum Numero da parcela
     * @param shapeLength Largura
     * @param shapeArea Área da propriedade
     * @param geometry Geometria da propriedade
     * @param owner Dono da propriedade
     * @param parish Freguesia da propriedade
     * @param county Concelho da propriedade
     * @param district Distrito da propriedade
     */
    public Property(String objectId, String parId, String parNum, double shapeLength, double shapeArea, Geometry geometry, Owner owner, String parish, String county, String district) {
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

    /**
     * Modifica o ID de uma propriedade
     * @param objectId Novo ID da propriedade
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * Obtém o ID de uma propriedade
     * @return ID da propriedade
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * Modifica o ID da parcela
     * @param parId Novo ID da parcela
     */
    public void setParId(String parId) {
        this.parId = parId;
    }

    /**
     * Obtém o ID da parcela
     * @return ID da parcela
     */
    public String getParId() {
        return parId;
    }

    /**
     * Modifica o número da parcela
     * @param parNum Novo número da parcela
     */
    public void setParNum(String parNum) {
        this.parNum = parNum;
    }

    /**
     * Obtém o número da parcela
     * @return Número da parcela
     */
    public String getParNum() {
        return parNum;
    }

    /**
     * Modifica a largura
     * @param shapeLength Nova largura
     */
    public void setShapeLength(double shapeLength) {
        this.shapeLength = shapeLength;
    }

    /**
     * Obtém a largura
     * @return Largura
     */
    public double getShapeLength() {
        return shapeLength;
    }

    /**
     * Modifica a área da propriedade
     * @param shapeArea Nova área da propriedade
     */
    public void setShapeArea(double shapeArea) {
        this.shapeArea = shapeArea;
    }

    /**
     * Obtém a área de uma propriedade
     * @return Área da propriedade
     */
    public double getShapeArea() {
        return shapeArea;
    }

    /**
     * Modifica a forma da propriedade
     * @param geometry Nova forma da propriedade
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     * Obtém forma/geometria da propriedade
     * @return Forma da propriedade
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * Modifica o proprietário da propriedade
     * @param owner Novo proprietário da propriedade
     */
    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    /**
     * Obtém o proprietário da propriedade
     * @return Proprietário da propriedade
     */
    public Owner getOwner() {
        return owner;
    }

    /**
     * Modifica a freguesia da propriedade
     * @param parish Nova freguesia da propriedade
     */
    public void setParish(String parish) {
        this.parish = parish;
    }

    /**
     * Obtém a freguesia da propriedade
     * @return Freguesia da propriedade
     */
    public String getParish() {
        return parish;
    }

    /**
     * Modifica o Concelho da propriedade
     * @param county Novo Concelho da propriedade
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * Obtém o Concelho da propriedade
     * @return Concelho da propriedade
     */
    public String getCounty() {
        return county;
    }

    /**
     * Modifica o Distrito de uma propriedade
     * @param district Novo Distrito da propriedade
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * Obtém o Distrito da propriedade
     * @return Distrito da propriedade
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Método para descrever a Propriedade
     * @return ID da propriedade e respetivo proprietário
     */
    @Override
    public String toString() {
        return objectId +  " | " + owner ;
    }
}
