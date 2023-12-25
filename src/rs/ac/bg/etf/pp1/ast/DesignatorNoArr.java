// generated with ast extension for cup
// version 0.8
// 14/8/2023 18:15:58


package rs.ac.bg.etf.pp1.ast;

public class DesignatorNoArr extends Designator {

    private String des;

    public DesignatorNoArr (String des) {
        this.des=des;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des=des;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorNoArr(\n");

        buffer.append(" "+tab+des);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorNoArr]");
        return buffer.toString();
    }
}
