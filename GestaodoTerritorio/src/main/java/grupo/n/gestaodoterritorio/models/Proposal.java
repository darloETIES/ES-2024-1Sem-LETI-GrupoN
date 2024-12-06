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

    /**
     * Construtor de uma sugestão de troca de propriedades
     * @param source Proprietário envolvido na sugestão de troca
     * @param target Proprietário envolvido na sugestão de troca
     * @param sp1 Propriedade envolvida na sugestão de troca
     * @param sp2 Propriedade envolvida na sugestão de troca
     * @param tp1 Propriedade envolvida na sugestão de troca
     * @param tp2 Propriedade envolvida na sugestão de troca
     */
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

    /**
     * Modifica o proprietário
     * @param source Novo proprietário
     */
    public void setSource(Owner source) {
        this.source = source;
    }

    /**
     * Obtém o proprietário
     * @return Proprietário
     */
    public Owner getSource() {
        return source;
    }

    /**
     * Modifica o proprietário
     * @param target Novo proprietário
     */
    public void setTarget(Owner target) {
        this.target = target;
    }

    /**
     * Obtém o proprietário
     * @return Proprietário
     */
    public Owner getTarget() {
        return target;
    }

    /**
     * Modifica propriedade
     * @param sp1 Nova propriedade
     */
    public void setSp1(Property sp1) {
        this.sp1 = sp1;
    }

    /**
     * Obtém propriedade
     * @return Propriedade
     */
    public Property getSp1() {
        return sp1;
    }
    /**
     * Modifica propriedade
     * @param sp2 Nova propriedade
     */
    public void setSp2(Property sp2) {
        this.sp2 = sp2;
    }
    /**
     * Obtém propriedade
     * @return Propriedade
     */
    public Property getSp2() {
        return sp2;
    }
    /**
     * Modifica propriedade
     * @param tp1 Nova propriedade
     */
    public void setTp1(Property tp1) {
        this.tp1 = tp1;
    }
    /**
     * Obtém propriedade
     * @return Propriedade
     */
    public Property getTp1() {
        return tp1;
    }
    /**
     * Modifica propriedade
     * @param tp2 Nova propriedade
     */
    public void setTp2(Property tp2) {
        this.tp2 = tp2;
    }
    /**
     * Obtém propriedade
     * @return Propriedade
     */
    public Property getTp2() {
        return tp2;
    }

    /**
     * Modifica o cenário 1 de troca do proprietário source
     * @param sourceExch1
     */
    public void setSourceExch1(String sourceExch1) {
        this.sourceExch1 = sourceExch1;
    }

    /**
     * Obtém o cenário 1 de troca para o proprietário source
     * @return Cenário 1 para o proprietário source
     */
    public String getSourceExch1() {
        return sourceExch1;
    }
    /**
     * Modifica o cenário 2 de troca do proprietário source
     * @param sourceExch2
     */
    public void setSourceExch2(String sourceExch2) {
        this.sourceExch2 = sourceExch2;
    }
    /**
     * Obtém o cenário 2 de troca para o proprietário source
     * @return Cenário 2 para o proprietário source
     */
    public String getSourceExch2() {
        return sourceExch2;
    }
    /**
     * Modifica o cenário 1 de troca do proprietário target
     * @param targetExch1
     */
    public void setTargetExch1(String targetExch1) {
        this.targetExch1 = targetExch1;
    }
    /**
     * Obtém o cenário 1 de troca para o proprietário target
     * @return Cenário 1 para o proprietário target
     */
    public String getTargetExch1() {
        return targetExch1;
    }
    /**
     * Modifica o cenário 2 de troca do proprietário target
     * @param targetExch2
     */
    public void setTargetExch2(String targetExch2) {
        this.targetExch2 = targetExch2;
    }
    /**
     * Obtém o cenário 2 de troca para o proprietário target
     * @return Cenário 2 para o proprietário target
     */
    public String getTargetExch2() {
        return targetExch2;
    }

}
