package grupo.n.gestaodoterritorio.models;

public class Proposal {
    private Owner source;
    private Owner target;
    private Property sp1;
    private Property sp2;
    private Property tp1;
    private Property tp2;
    private String sourceExch1;
    private String sourceExch2;
    private String targetExch1;





    private String targetExch2;


    public Proposal(Owner source, Owner target, Property sp1, Property sp2, Property tp1, Property tp2) {
        this.source = source;
        this.target = target;
        this.sp1 = sp1;
        this.sp2 = sp2;
        this.tp1 = tp1;
        this.tp2 = tp2;

        System.out.println("Cenário 1: ");
        this.sourceExch1 = "O proprietário " + source + " fica com as propriedades: " + sp1 + " e "+ tp1;
        this.targetExch1 = "O proprietário " + target + " fica com as propriedades: " + sp2 + " e "+ tp2;
        System.out.println("Cenário 2: ");
        this.sourceExch2 = "O proprietário " + source + " fica com as propriedades: " + sp2 + " e "+ tp2;
        this.targetExch2 = "O proprietário " + target + " fica com as propriedades: " + sp1 + " e "+ tp1;
    }
    public void setSource(Owner source) {
        this.source = source;
    }
    public Owner getSource() {
        return source;
    }
    public void setTarget(Owner target) {
        this.target = target;
    }
    public Owner getTarget() {
        return target;
    }
    public void setSp1(Property sp1) {
        this.sp1 = sp1;
    }
    public Property getSp1() {
        return sp1;
    }
    public void setSp2(Property sp2) {
        this.sp2 = sp2;
    }
    public Property getSp2() {
        return sp2;
    }
    public void setTp1(Property tp1) {
        this.tp1 = tp1;
    }
    public Property getTp1() {
        return tp1;
    }
    public void setTp2(Property tp2) {
        this.tp2 = tp2;
    }
    public Property getTp2() {
        return tp2;
    }

    public void setSourceExch1(String sourceExch1) {
        this.sourceExch1 = sourceExch1;
    }
    public String getSourceExch1() {
        return sourceExch1;
    }
    public void setSourceExch2(String sourceExch2) {
        this.sourceExch2 = sourceExch2;
    }
    public String getSourceExch2() {
        return sourceExch2;
    }
    public void setTargetExch1(String targetExch1) {
        this.targetExch1 = targetExch1;
    }
    public String getTargetExch1() {
        return targetExch1;
    }
    public void setTargetExch2(String targetExch2) {
        this.targetExch2 = targetExch2;
    }
    public String getTargetExch2() {
        return targetExch2;
    }

}
