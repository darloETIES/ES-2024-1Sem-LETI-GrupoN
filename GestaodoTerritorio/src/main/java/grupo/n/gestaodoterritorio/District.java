package grupo.n.gestaodoterritorio;

import java.util.*;

public class District{
    protected String districtName; //protected para poder ser usado por classes derivadas
    private List<County> counties;

    public District(String districtName) {
        this.districtName = districtName;
        this.counties = new ArrayList<County>();
    }
    public String getDistrictName () {
        return districtName;
    }
    public void addCounty (County county) {
        counties.add(county);
    }
    public List<County> getCounties() {
        return counties;
    }
}
