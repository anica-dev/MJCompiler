// generated with ast extension for cup
// version 0.8
// 14/8/2023 18:15:58


package rs.ac.bg.etf.pp1.ast;

public class ReturnStatement extends Statement {

    private ReturnStat ReturnStat;

    public ReturnStatement (ReturnStat ReturnStat) {
        this.ReturnStat=ReturnStat;
        if(ReturnStat!=null) ReturnStat.setParent(this);
    }

    public ReturnStat getReturnStat() {
        return ReturnStat;
    }

    public void setReturnStat(ReturnStat ReturnStat) {
        this.ReturnStat=ReturnStat;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ReturnStat!=null) ReturnStat.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ReturnStat!=null) ReturnStat.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ReturnStat!=null) ReturnStat.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ReturnStatement(\n");

        if(ReturnStat!=null)
            buffer.append(ReturnStat.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ReturnStatement]");
        return buffer.toString();
    }
}
