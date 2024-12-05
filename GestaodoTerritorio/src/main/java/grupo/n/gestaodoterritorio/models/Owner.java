package grupo.n.gestaodoterritorio.models;

import java.util.ArrayList;
import java.util.List;

public class Owner {
    private String ownerID;
    private List<Property> ownerPropertyList;

    public Owner(String ownerID) {
        this.ownerID = ownerID;
        this.ownerPropertyList = new ArrayList<Property>();
    }
    public String getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }
    public List<Property> getOwnerPropertyList() {
        return ownerPropertyList;
    }
    public void addToOwnerPropertyList(Property property) {
        ownerPropertyList.add(property);
    }

    public String toString() {
        return ownerID;
    }
}
