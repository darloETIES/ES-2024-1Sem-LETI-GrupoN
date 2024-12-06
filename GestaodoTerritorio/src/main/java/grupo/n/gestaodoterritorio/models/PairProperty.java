package grupo.n.gestaodoterritorio.models;

import java.util.Objects;


public class PairProperty {
    private Property property1;
    private Property property2;
    private double jointArea;

    /**
     * Método utilizado para guardar uma adjacência entre duas propriedades e respetiva área total
     * @param property1
     * @param property2
     */
    public PairProperty(Property property1, Property property2) {
        this.property1 = property1;
        this.property2 = property2;
        this.jointArea = property1.getShapeArea() + property2.getShapeArea();
    }

    /**
     *
     * @return Proriedade pertencente a uma adjacência
     */
    public Property getProperty1() {
        return property1;
    }

    /**
     * Altera um objeto propriedade de uma adjacência
     * @param property1 Novo objeto para uma propriedade de uma adjacência
     */
    public void setProperty1(Property property1) {
        this.property1 = property1;
    }

    /**
     *
     * @return Propriedade pertencente a uma adjacência
     */
    public Property getProperty2() {
        return property2;
    }

    /**
     * Altera um objeto propriedade de uma adjacência
     * @param property2 Novo objeto para uma propriedade de uma adjacência
     */
    public void setProperty2(Property property2) {
        this.property2 = property2;
    }

    /**
     *
     * @return Valor da área total de duas propriedades adjacentes
     */
    public double getJointArea(){
        return jointArea;
    }

    /**
     * Altera o valor da área total de duas propriedades adjacentes
     * @param jointArea Novo valor de área de duas propriedades adjacentes
     */
    public void setJointArea(double jointArea){
        this.jointArea = jointArea;
    }

    /**
     * Verifica se dois objetos do tipo propriedade são iguais
     * @param pp Propriedade a ser comparada com o objeto
     * @return Valor lógico da comparação
     */
    public boolean equals(PairProperty pp) {
        if (this == pp) return true; // Se forem o mesmo objeto, são iguais
        if (pp == null || getClass() != pp.getClass()) return false; // Verifica o tipo

        // Verifica se os atributos são iguais
        return Double.compare(pp.jointArea, jointArea) == 0 &&
                Objects.equals(property1, pp.property1) &&
                Objects.equals(property2, pp.property2);
    }
}
