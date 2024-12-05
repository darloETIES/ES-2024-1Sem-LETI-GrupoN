package grupo.n.gestaodoterritorio;

import java.util.Objects;


public class PairProperty {
    private Property property1;
    private Property property2;
    private double jointArea;

    public PairProperty(Property property1, Property property2) {
        this.property1 = property1;
        this.property2 = property2;
        this.jointArea = property1.getShapeArea() + property2.getShapeArea();
    }
    public Property getProperty1() {
        return property1;
    }
    public void setProperty1(Property property1) {
        this.property1 = property1;
    }
    public Property getProperty2() {
        return property2;
    }
    public void setProperty2(Property property2) {
        this.property2 = property2;
    }
    public double getJointArea(){
        return jointArea;
    }
    public void setJointArea(double jointArea){
        this.jointArea = jointArea;
    }

    public boolean equals(PairProperty pp) {
        if (this == pp) return true; // Se forem o mesmo objeto, são iguais
        if (pp == null || getClass() != pp.getClass()) return false; // Verifica o tipo

        PairProperty that = (PairProperty) pp;

        // Verifica se os atributos são iguais
        return Double.compare(that.jointArea, jointArea) == 0 &&
                Objects.equals(property1, that.property1) &&
                Objects.equals(property2, that.property2);
    }
}
