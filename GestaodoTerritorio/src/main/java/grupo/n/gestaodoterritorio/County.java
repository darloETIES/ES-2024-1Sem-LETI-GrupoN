package grupo.n.gestaodoterritorio;

import java.util.ArrayList;
import java.util.List;

public class County extends District {
    //count(y/ies) - concelho(s)
    //parish(es) - freguesia(s)
    protected String countyName;
    private List<Parish> parishes;

    public County(String districtName, String countyName) {
        super(districtName);
        this.countyName = countyName;
        this.parishes = new ArrayList<Parish>();
    }
    public String getDistrict() {
        return districtName;
    }

    public String getCountyName() {
        return countyName;
    }
    public void addParish(Parish parish){
        parishes.add(parish);
    }

    public List<Parish> getParishes() {
        return parishes;
    }
}
