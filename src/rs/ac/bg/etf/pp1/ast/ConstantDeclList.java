// generated with ast extension for cup
// version 0.8
// 14/8/2023 18:15:58


package rs.ac.bg.etf.pp1.ast;

public class ConstantDeclList extends ConstDeclList {

    private ConstDeclList ConstDeclList;
    private SingleConstDecl SingleConstDecl;

    public ConstantDeclList (ConstDeclList ConstDeclList, SingleConstDecl SingleConstDecl) {
        this.ConstDeclList=ConstDeclList;
        if(ConstDeclList!=null) ConstDeclList.setParent(this);
        this.SingleConstDecl=SingleConstDecl;
        if(SingleConstDecl!=null) SingleConstDecl.setParent(this);
    }

    public ConstDeclList getConstDeclList() {
        return ConstDeclList;
    }

    public void setConstDeclList(ConstDeclList ConstDeclList) {
        this.ConstDeclList=ConstDeclList;
    }

    public SingleConstDecl getSingleConstDecl() {
        return SingleConstDecl;
    }

    public void setSingleConstDecl(SingleConstDecl SingleConstDecl) {
        this.SingleConstDecl=SingleConstDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclList!=null) ConstDeclList.accept(visitor);
        if(SingleConstDecl!=null) SingleConstDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclList!=null) ConstDeclList.traverseTopDown(visitor);
        if(SingleConstDecl!=null) SingleConstDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclList!=null) ConstDeclList.traverseBottomUp(visitor);
        if(SingleConstDecl!=null) SingleConstDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstantDeclList(\n");

        if(ConstDeclList!=null)
            buffer.append(ConstDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleConstDecl!=null)
            buffer.append(SingleConstDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstantDeclList]");
        return buffer.toString();
    }
}
