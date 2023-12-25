package rs.ac.bg.etf.pp1;
import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.Add;
import rs.ac.bg.etf.pp1.ast.AddExpr;
import rs.ac.bg.etf.pp1.ast.Addop;
import rs.ac.bg.etf.pp1.ast.DesStatAssignopExpr;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.DesignatorArrName;
import rs.ac.bg.etf.pp1.ast.DesignatorArray;
import rs.ac.bg.etf.pp1.ast.DesignatorFindAny;
import rs.ac.bg.etf.pp1.ast.DesignatorNoArr;
import rs.ac.bg.etf.pp1.ast.DesignatorStatAssign;
import rs.ac.bg.etf.pp1.ast.DesignatorStatDec;
import rs.ac.bg.etf.pp1.ast.DesignatorStatInc;
import rs.ac.bg.etf.pp1.ast.Divide;
import rs.ac.bg.etf.pp1.ast.FactorBool;
import rs.ac.bg.etf.pp1.ast.FactorChar;
import rs.ac.bg.etf.pp1.ast.FactorDes;
import rs.ac.bg.etf.pp1.ast.FactorNewExpr;
import rs.ac.bg.etf.pp1.ast.FactorNum;
import rs.ac.bg.etf.pp1.ast.FindAnyStatement;
import rs.ac.bg.etf.pp1.ast.FuncCall;
import rs.ac.bg.etf.pp1.ast.MethTypeName;
import rs.ac.bg.etf.pp1.ast.MethodDecl;
import rs.ac.bg.etf.pp1.ast.MethodName;
import rs.ac.bg.etf.pp1.ast.MethodTypeName;
import rs.ac.bg.etf.pp1.ast.Mulop;
import rs.ac.bg.etf.pp1.ast.Multiply;
import rs.ac.bg.etf.pp1.ast.NegativeExpr;
import rs.ac.bg.etf.pp1.ast.PrintStatement;
import rs.ac.bg.etf.pp1.ast.PrintStatementNum;
import rs.ac.bg.etf.pp1.ast.ReadStatement;
import rs.ac.bg.etf.pp1.ast.ReturnExpr;
import rs.ac.bg.etf.pp1.ast.ReturnNoExpr;
import rs.ac.bg.etf.pp1.ast.Sub;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.TermMulop;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPC;
	private Obj findAnyExpr;
	
	public CodeGenerator() {
		
		Obj len = Tab.find("len");
		len.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.arraylength);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Obj chr = Tab.find("chr");
		chr.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		Obj ord = Tab.find("ord");
		ord.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(1);
		Code.put(1);
		Code.put(Code.load_n);
		Code.put(Code.exit);
		Code.put(Code.return_);	
	}
	
	public int getMainPc() {
		return mainPC;
	}
	
	
	public void visit(PrintStatement ps) {
		Struct boolType=Tab.find("bool").getType();
		if(ps.getExpr().struct==Tab.intType || ps.getExpr().struct==boolType) {
			Code.loadConst(5);
			Code.put(Code.print);
		}else {
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	public void visit(PrintStatementNum psNum) {
		Struct boolType=Tab.find("bool").getType();
		if(psNum.getExpr().struct==Tab.intType || psNum.getExpr().struct==boolType) {
			Code.loadConst(psNum.getN2());
			Code.put(Code.print);
		}else {
			Code.loadConst(psNum.getN2());
			Code.put(Code.bprint);
		}
	}
	
	public void visit(ReadStatement readStatement) {
		if(readStatement.getDesignator().obj.getType()== Tab.charType) {
			Code.put(Code.bread);
		}else {
			Code.put(Code.read);
		}
		Code.store(readStatement.getDesignator().obj);
	}
	
	public void visit(FactorNum factorNum) {  //konstanta
		Obj con = Tab.insert(Obj.Con, "$", factorNum.struct);
		con.setLevel(0);
		con.setAdr(factorNum.getN1());
		
		Code.load(con);
	}
	
	public void visit(FactorChar factorChar) {
		Obj con = Tab.insert(Obj.Con, "$", factorChar.struct);
		con.setLevel(0);
		con.setAdr(factorChar.getC1());
		Code.load(con);
	}
	
	public void visit(FactorBool factorBool) {
		Obj con = Tab.insert(Obj.Con, "$", factorBool.struct);
		con.setLevel(0);
		con.setAdr(factorBool.getB1());
		Code.load(con);
	}
	
	public void visit(FactorNewExpr factorNewExpr) {
	/*	Obj nObj = factorNewExpr.getExpr().obj;
		Code.load(nObj);  */
		Struct type=factorNewExpr.struct;
		Code.put(Code.newarray);  
		if(type.equals(Tab.charType))
			Code.put(0);
		else 
			Code.put(1);
	}
	
	public void visit(DesStatAssignopExpr desStatAssignopExpr) {
		Code.store(desStatAssignopExpr.getDesignator().obj);  
		//promenljiva koja se dodeljuje je vec stavljena na ExprStack, sada se skida sa steka i upisuje u promenljivu
	} 
	
	
	public void visit(DesignatorNoArr designator) {
		SyntaxNode parent=designator.getParent();
		SyntaxNode node=designator;
		//if(DesignatorStatAssign.class!=parent.getClass() && FuncCall.class !=parent.getClass()) {
			if(FactorDes.class == parent.getClass()) {
			Code.load(designator.obj);  //stavlja se na ExprStack
		}
			if(DesignatorFindAny.class==parent.getClass()) {
				//findAnyExpr=designator.obj;
				Code.load(designator.obj);
			}
	}
	
	public void visit(DesignatorArray designatorArray) {
		SyntaxNode parent=designatorArray.getParent();
		
		if(parent.getClass()==FactorDes.class || DesignatorFindAny.class==parent.getClass()) {
			Code.load(designatorArray.obj);  //stavlja se na ExprStack
		}
	}
	
	public void visit(DesignatorArrName desArrName) {
		Code.load(desArrName.obj);
	}
	
	public void visit(DesignatorStatInc desStatInc) {
		if(desStatInc.getDesignator().obj.getKind()==Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(desStatInc.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(desStatInc.getDesignator().obj);
		
	}
	
	public void visit(DesignatorStatDec desStatDec) {
		if(desStatDec.getDesignator().obj.getKind()==Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(desStatDec.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(desStatDec.getDesignator().obj);
	}
	
	public void visit(NegativeExpr negativeExpr) {
		Code.put(Code.neg);
	}
	
	public void visit(AddExpr addExpr) {
		Addop kind=addExpr.getAddop();
		if(kind instanceof Add)
			Code.put(Code.add);
		else if(kind instanceof Sub)
			Code.put(Code.sub);  //elementi koji ucestvuju se vec nalaze na ExprStack-u
	}
	
	public void visit(TermMulop termMulop) {
		Mulop kind=termMulop.getMulop();
		if(kind instanceof Multiply)
			Code.put(Code.mul);
		else if(kind instanceof Divide)
			Code.put(Code.div);
		else
			Code.put(Code.rem);
	}
	
	public void visit(FindAnyStatement findAny) {
		
	/*	DesignatorNoArr d=(DesignatorNoArr) findAny.getDesignatorFindAny().getDesignator();
		Obj niz=Tab.find(d.getDes()); //dohvatili smo niz preko naziva 
									*/
		//Obj niz=findAny.getDesignatorFindAny().getDesignator().obj;
		Obj k=new Obj(Obj.Var,"Objk",Tab.intType);
		
		
		
		Code.store(k);
		Code.put(Code.pop); //niz
		
		Code.loadConst(0);
		Code.put(Code.dup);
		Code.load(findAny.getDesignatorFindAny().getDesignator().obj);
		Code.put(Code.arraylength);
		
		Code.putFalseJump(Code.lt, 0); 
		int pc1=Code.pc-2;
		
		
		Code.load(findAny.getDesignatorFindAny().getDesignator().obj); //load niz
	
		Code.put(Code.dup2);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		
		Code.load(k);
		
		Code.putFalseJump(Code.eq, Code.pc-22);
		
		Code.put(Code.pop); //izbacujemo ono sto nam ne treba
		
		Code.loadConst(1);
		
		Code.putJump(Code.pc+5);
		
		Code.fixup(pc1);
		Code.put(Code.pop);
		
		Code.loadConst(0); //ako nisu jednaki
		Code.store(findAny.getDesignator().obj);  

	
		
		//resenje sa cuvanjem adrese niza na steku
/*		Obj k=new Obj(Obj.Var,"Objk",Tab.intType);
		
		Code.store(k);
		Code.loadConst(0);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.dup);
		Code.put(Code.arraylength);
		Code.put(Code.dup_x2); //hocemo na vrhu da nobijemo n i i
		Code.put(Code.pop);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.put(Code.dup_x1);
		
		Code.putFalseJump(Code.gt, 0); //ako je n>i ne skaci
		int pc1=Code.pc-2;
		
		Code.put(Code.dup2); //niz i niz i
		Code.put(Code.aload); //ucitavamo element niza
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.loadConst(1);  
		Code.put(Code.add);  //uvecavanje i za 1
		Code.put(Code.dup_x2); //treba da niz[i] bude na vrhu
		Code.put(Code.pop);
		Code.put(Code.dup_x2);
		Code.put(Code.pop);
		Code.load(k); 
		
		Code.putFalseJump(Code.eq, Code.pc-24); //poredimo niz[i] i k, ako nisu jednaki ponovo while petlja
		
		Code.put(Code.pop); //izbacujemo ono sto nam ne treba
		Code.put(Code.pop);
		Code.loadConst(1); //ako su jednaki upisujemo 1 na stek i preskacemo na kraj programa
		
		Code.putJump(Code.pc+6);
		
		Code.fixup(pc1);
		Code.put(Code.pop);
		Code.put(Code.pop);
		Code.loadConst(0); //ako nisu jednaki
		Code.store(findAny.getDesignator().obj);
		*/
	}
	
	
	
	//metodi, funkcije u nastavku
	
	public void visit(MethodName methodName) {
		
		if("main".equalsIgnoreCase(methodName.getMethName())) { //ako se radi o main metodi
			mainPC=Code.pc;
		} 
		methodName.obj.setAdr(Code.pc);
		
		//dohvatanje agrumenata i lokalnih varijabli
		SyntaxNode methodNode=methodName.getParent();
		
		VarCounter varCounter=new VarCounter();
		methodNode.traverseTopDown(varCounter);
		
		FormParamCounter formParamCounter=new FormParamCounter();
		methodNode.traverseTopDown(formParamCounter);
		
		//Generate the entry
		
		Code.put(Code.enter);
		Code.put(formParamCounter.getCount());
		Code.put(formParamCounter.getCount()+varCounter.getCount()); 
		
	}
	
	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(FuncCall funcCall) {
		Obj functionObj = funcCall.getDesignator().obj;
		int offset=functionObj.getAdr()-Code.pc; //pc relativna adresa
		Code.put(Code.call);
		Code.put2(offset);
	}
	
	public void visit(ReturnExpr retExp) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(ReturnNoExpr retNoExp) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	
	
	
}
