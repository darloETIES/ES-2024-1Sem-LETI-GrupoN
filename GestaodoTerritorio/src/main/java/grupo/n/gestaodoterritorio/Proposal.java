package grupo.n.gestaodoterritorio;

import java.util.List;

public class Proposal {
    private Owner source;
    private Owner target;
    private List<Property> AUXsourceList;
    private List<Property> AUXtargetList;
    private String sourceExch1;
    private String sourceExch2;
    private String targetExch1;
    private String targetExch2;


    public Proposal(Owner source, Owner target, List<Property> AUXsourceList, List<Property> AUXtargetList) {
        this.source = source;
        this.target = target;
        this.AUXsourceList = AUXsourceList;
        this.AUXtargetList = AUXtargetList;

        System.out.println("Cenário 1: ");
        this.sourceExch1 = "O proprietário " + source + "fica com as propriedades: " + AUXsourceList.get(0)+ "e"+ AUXtargetList.get(0);
        this.targetExch1 = "O proprietário" + target + "fica com as propriedades: " + AUXsourceList.get(1)+ "e"+ AUXtargetList.get(1);
        System.out.println("Cenário 2: ");
        this.sourceExch2 = "O proprietário" + source + "fica com as propriedades: " + AUXsourceList.get(1)+ "e"+ AUXtargetList.get(1);
        this.targetExch2 = "O proprietário " + target + "fica com as propriedades: " + AUXsourceList.get(0)+ "e"+ AUXtargetList.get(0);
    }
}
