// generated with ast extension for cup
// version 0.8
// 14/8/2023 18:15:58


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatAssign extends DesignatorStatement {

    private DesignatorStatAssignopExpr DesignatorStatAssignopExpr;

    public DesignatorStatAssign (DesignatorStatAssignopExpr DesignatorStatAssignopExpr) {
        this.DesignatorStatAssignopExpr=DesignatorStatAssignopExpr;
        if(DesignatorStatAssignopExpr!=null) DesignatorStatAssignopExpr.setParent(this);
    }

    public DesignatorStatAssignopExpr getDesignatorStatAssignopExpr() {
        return DesignatorStatAssignopExpr;
    }

    public void setDesignatorStatAssignopExpr(DesignatorStatAssignopExpr DesignatorStatAssignopExpr) {
        this.DesignatorStatAssignopExpr=DesignatorStatAssignopExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorStatAssignopExpr!=null) DesignatorStatAssignopExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorStatAssignopExpr!=null) DesignatorStatAssignopExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorStatAssignopExpr!=null) DesignatorStatAssignopExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatAssign(\n");

        if(DesignatorStatAssignopExpr!=null)
            buffer.append(DesignatorStatAssignopExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatAssign]");
        return buffer.toString();
    }
}
