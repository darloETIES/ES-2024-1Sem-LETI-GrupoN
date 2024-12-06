package grupo.n.gestaodoterritorio.comparators;

import grupo.n.gestaodoterritorio.models.PairProperty;

import java.util.Comparator;

public class ComparatorPairProperty implements Comparator<PairProperty> {
    /**
     * Comparador de áreas totais entre duas adjacências
     * @param p1 the first object to be compared.
     * @param p2 the second object to be compared.
     * @return Comparação
     */
    public int compare(PairProperty p1, PairProperty p2){
        return (int)Double.compare(p2.getJointArea(), p1.getJointArea()); //ordenacao decrescente
    }
}
