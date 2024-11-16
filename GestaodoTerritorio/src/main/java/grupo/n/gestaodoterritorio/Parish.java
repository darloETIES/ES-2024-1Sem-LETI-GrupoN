package grupo.n.gestaodoterritorio;

public class Parish extends County {
    private String parishName;

    public Parish(String districtName, String countyName,String parishName) {
       super(districtName, countyName);
       this.parishName = parishName;
    }
    public String getDistrict() {
        return super.getDistrict();
    }

    public String getCountyName() {
        return super.getCountyName();
    }
    public String getParishName() {
        return parishName;
    }

}
