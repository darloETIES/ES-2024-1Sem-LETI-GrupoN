package grupo.n.gestaodoterritorio.models;

import java.util.ArrayList;
import java.util.List;

public class Owner {
    private String ownerID;
    private List<Property> ownerPropertyList;

    /**
     * Construtor de um proprietário(owner)
     * @param ownerID identificador de um proprietário
     */
    public Owner(String ownerID) {
        this.ownerID = ownerID;
        this.ownerPropertyList = new ArrayList<Property>();
    }

    /**
     *
     * @return ID de um proprietário
     */
    public String getOwnerID() {
        return ownerID;
    }

    /**
     * Altera o ownerID para o valor fornecido
     * @param ownerID
     */
    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    /**
     *
     * @return Lista de propriedades de um proprietário
     */
    public List<Property> getOwnerPropertyList() {
        return ownerPropertyList;
    }

    /**
     * Adiociona uma propriedade à lista de propriedades do proprietário
     * @param property Propriedade a adicionar
     */
    public void addToOwnerPropertyList(Property property) {
        ownerPropertyList.add(property);
    }

    /**
     * Método para descrever o proprietário
     * @return ID do proprietário
     */
    public String toString() {
        return ownerID ;
    }
}
