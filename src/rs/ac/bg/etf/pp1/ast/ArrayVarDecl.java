// generated with ast extension for cup
// version 0.8
// 14/8/2023 18:15:58


package rs.ac.bg.etf.pp1.ast;

public class ArrayVarDecl extends SingleVarDecl {

    private String arrayVarName;

    public ArrayVarDecl (String arrayVarName) {
        this.arrayVarName=arrayVarName;
    }

    public String getArrayVarName() {
        return arrayVarName;
    }

    public void setArrayVarName(String arrayVarName) {
        this.arrayVarName=arrayVarName;
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
        buffer.append("ArrayVarDecl(\n");

        buffer.append(" "+tab+arrayVarName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ArrayVarDecl]");
        return buffer.toString();
    }
}
