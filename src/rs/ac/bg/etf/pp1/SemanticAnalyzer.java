package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {
	
	int printCallCount=0;
	int varDeclCount=0;
	Obj currentMethod = null;
	boolean returnFound=false;
	boolean errorDetected=false;
	int nVars;
	Struct currentType;
	int constant;
	Struct constantType;
	Struct boolType=Tab.find("bool").getType();
	
	private boolean mainDetected = false;
	
	
	Logger log = Logger.getLogger(getClass());
	
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected=true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public void visit(ProgName progName) {  //pocetak programa
		progName.obj =Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.openScope();
	}
	
	public void visit(Program program) {  //kraj programa
		nVars=Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
		if(!mainDetected)
			report_error("Programu nedostaje main metoda", program);
	}

	
	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if(typeNode==Tab.noObj) {  //ne postoji u tabeli simbola
			report_error("Nije pronadjen tip "+type.getTypeName() +" u tabeli simbola!", null);
			currentType = Tab.noType;
		}else {
			if(Obj.Type == typeNode.getKind()) {
				currentType = typeNode.getType();
			}else {
				report_error("Greska: Ime "+type.getTypeName() +" ne predstavlja tip!", type);
				currentType = Tab.noType;
			}
		}
	}
	
	
	@Override
	public void visit(ConstDeclaration constDecl) {
		Obj constObj = Tab.find(constDecl.getConstName());
		if(constObj != Tab.noObj) {
			report_error("Greska: Konstanta je definisana vise puta "+ constDecl.getConstName(), constDecl);
		}
		else {
			if(constantType.assignableTo(currentType)) {
				constObj = Tab.insert(Obj.Con, constDecl.getConstName(), currentType);
				constObj.setAdr(constant);
			}
			else {
				report_error("Greska: Neadekvatna dodela konstanti: " + constDecl.getConstName(), constDecl);
			}
		}
		report_info("Detektovana je konstanta " + constDecl.getConstName(), constDecl);
	} 
	
	@Override
	public void visit(SingleConstDeclaration singleConstDecl) {
		
	}
	
	@Override
	public void visit(ConstantDeclList constDeclList) {
		
	}
	
	@Override
	public void visit(ConstDecl constDecl) {
		
	}
	
	@Override
	public void visit(ConstNum constNum) {
		constant=constNum.getVal();
		constantType = Tab.intType;
	}
	
	@Override
	public void visit(ConstChar constChar) {
		constant=constChar.getVal();
		constantType = Tab.charType;
	}
	
	@Override
	public void visit(ConstBool constBool) {
		constant=constBool.getVal();
		constantType = boolType;
	}
	
	@Override
	public void visit(SingleVarDeclar singleVarDeclar) {
		Obj varObj=null;
		if(currentMethod == null) {
			varObj = Tab.find(singleVarDeclar.getVarName()); //globalna promenljiva, pretrazuje se cela tabela
			//find ako ne pronadje, vraca novi objekat
			report_info("Detektovana je globalna promenljiva " + singleVarDeclar.getVarName(), singleVarDeclar);
		}
		else {
			varObj=Tab.currentScope().findSymbol(singleVarDeclar.getVarName()); //lokalna promenljiva, pretrazuje se samo taj scope
			//findSymbol ako ne pronadje, vraca null
			report_info("Detektovana je lokalna promenljiva " + singleVarDeclar.getVarName(), singleVarDeclar);
		}
		if(varObj==null || varObj == Tab.noObj) {
			varObj = Tab.insert(Obj.Var, singleVarDeclar.getVarName(), currentType);
		}
		else {
			report_error("Greska: Dvostruka definicija promenljive "+ singleVarDeclar.getVarName(), singleVarDeclar);
		}
	}
	
	@Override
	public void visit(ArrayVarDecl arrVarDecl) {
		Obj varObj=null;
		if(currentMethod == null)
			varObj = Tab.find(arrVarDecl.getArrayVarName());
		else
			varObj=Tab.currentScope().findSymbol(arrVarDecl.getArrayVarName());
		
		if(varObj==null || varObj == Tab.noObj) {
			varObj = Tab.insert(Obj.Var, arrVarDecl.getArrayVarName(), new Struct(Struct.Array, currentType));
		}
		else {
			report_error("Greska: Dvostruka definicija promenljive "+ arrVarDecl.getArrayVarName(), arrVarDecl);	
		}
	}
	
	@Override
	public void visit(MethodName methodName) {
		if(methodName.getMethName().equalsIgnoreCase("main")) {
			mainDetected = true;
		}
		currentMethod = Tab.insert(Obj.Meth, methodName.getMethName(), Tab.noType); //void
		methodName.obj=currentMethod;
		Tab.openScope();		
	}
	
	@Override
	public void visit(MethodDecl methodDecl) { //zavrsetak metode
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		currentMethod = null;
	}
	
	@Override
	public void visit(FactorDes factorDes) {  //factor Designator
		factorDes.struct = factorDes.getDesignator().obj.getType();
	}
	
	@Override
	public void visit(FactorNum factorNum) {
		factorNum.struct = Tab.intType;
	}
	
	@Override
	public void visit(FactorChar factorChar) {
		factorChar.struct = Tab.charType;
	}
	
	@Override
	public void visit(FactorBool factorBool) {
		factorBool.struct = boolType;
	}
		
	@Override
	public void visit(FactorNewExpr factorNewExpr) {
		if(!factorNewExpr.getExpr().struct.equals(Tab.intType)) {
			report_error("Velicina niza treba da bude tipa int", factorNewExpr);
			factorNewExpr.struct = Tab.noType;
		}
		else {
			factorNewExpr.struct = new Struct(Struct.Array, currentType);
		}
	}
	
	public void visit(FuncCall factorFuncCall) {
		Obj func=factorFuncCall.getDesignator().obj;
		if(Obj.Meth == func.getKind()) {
			factorFuncCall.struct=func.getType();
		}
		else {
			factorFuncCall.struct=Tab.noType;
		}
	}
	
	@Override
	public void visit(FactorExpr factorExpr) {
		factorExpr.struct = factorExpr.getExpr().struct;
	}
	
	@Override
	public void visit(DesignatorNoArr designatorNoArr) {
		Obj varObj= Tab.find(designatorNoArr.getDes());
		if(varObj == Tab.noObj) {
			report_error("Greska: Pristupa se nedefinisanoj promenljivoj "+designatorNoArr.getDes(), designatorNoArr);
			designatorNoArr.obj = Tab.noObj;
		}
		else if(varObj.getKind() != Obj.Var && varObj.getKind() != Obj.Con) {  //Designator moze biti samo Var ili Const
			report_error("Greska: Promenljiva nije adekvatna: " +designatorNoArr.getDes(), designatorNoArr);
			designatorNoArr.obj = Tab.noObj;
		}
		else {
			designatorNoArr.obj = varObj;
		}
	}
	
	@Override
	public void visit(DesignatorArrName designatorArr) {
		Obj arrObj= Tab.find(designatorArr.getD());
		if(arrObj == Tab.noObj) {
			report_error("Greska: Pristupa se nedefinisanoj promenljivoj niza "+designatorArr.getD(), designatorArr);
			designatorArr.obj = Tab.noObj;
		}
		else if(arrObj.getKind() != Obj.Var || arrObj.getType().getKind() != Struct.Array) {
			report_error("Greska: Promenljiva niza nije adekvatna: " +designatorArr.getD(), designatorArr);
			designatorArr.obj = Tab.noObj;
		}
		else {
			designatorArr.obj = arrObj;
		}
	}
	
	@Override
	public void visit(DesignatorArray designatorArray) {
		Obj arrObj = designatorArray.getDesignatorArrName().obj;
		if(arrObj == Tab.noObj)
			designatorArray.obj = Tab.noObj;
		else if(!designatorArray.getExpr().struct.equals(Tab.intType)) {
			report_error("Indeksiranje vrednoscu koja nije int! ", designatorArray);
			designatorArray.obj = Tab.noObj;
		}
		else {
			designatorArray.obj = new Obj(Obj.Elem, arrObj.getName() + "[$]", arrObj.getType().getElemType());
		}
	}
	
	@Override
	public void visit(AddExpr addExpr) {
		Struct left = addExpr.getExpr().struct;
		Struct right = addExpr.getTerm().struct;
		if(left.equals(Tab.intType) && right.equals(Tab.intType))
			addExpr.struct = Tab.intType;
		else {
			report_error("Greska: Tip mora da bude int u addop operaciji", addExpr);
			addExpr.struct=Tab.noType;
		}
	}
	
	@Override
	public void visit(TermExpr termExpr) {
		termExpr.struct=termExpr.getTerm().struct;
	}
	
	@Override
	public void visit(NegativeExpr negativeExpr) {
		if(negativeExpr.getTerm().struct.equals(Tab.intType)) {
			negativeExpr.struct = Tab.intType;
		}
		else {
			report_error("Greska: Negacija vrednosti koja nije int", negativeExpr);
			negativeExpr.struct=Tab.noType;
		}
	}
	
	@Override
	public void visit(JustTerm justTerm) {
		justTerm.struct = justTerm.getFactor().struct;
	}
	
	@Override
	public void visit(TermMulop termMulop) {
		Struct left = termMulop.getTerm().struct;
		Struct right = termMulop.getFactor().struct;
		if(left.equals(Tab.intType) && right.equals(Tab.intType))
			termMulop.struct = Tab.intType;
		else {
			report_error("Greska: Tip mora da bude int u mulop operaciji", termMulop);
			termMulop.struct=Tab.noType;
		}
	}
	
	@Override
	public void visit(Expr expr) {
		expr.struct=expr.getTermList().struct;
	}
	
	@Override
	public void visit(ReadStatement rs) {
		rs.struct=rs.getDesignator().obj.getType();
	}
	
		
	@Override
	public void visit(PrintStatement ps) {
		if(ps.getExpr().struct!= Tab.intType && ps.getExpr().struct!=Tab.charType && ps.getExpr().struct!=boolType)
			report_error("Greska na liniji "+ ps.getLine() + ": Operand instrukcije PRINT mora biti char, int ili bool tipa", ps);
		// printCallCount++;
	}
	
	@Override
	public void visit(PrintStatementNum printStatNum) {
		if(printStatNum.getExpr().struct!= Tab.intType && printStatNum.getExpr().struct!=Tab.charType)
			report_error("Greska na liniji "+ printStatNum.getLine() + ": Operand instrukcije PRINT mora biti char ili int tipa", printStatNum);
	}
	
	@Override
	public void visit(DesStatAssignopExpr desStatAssignopExpr) {
		int kind=desStatAssignopExpr.getDesignator().obj.getKind();
			if(kind!=Obj.Var && kind!=Obj.Elem) {
				report_error("Greska: Dodela u neodgovarajucu promenljivu: " + desStatAssignopExpr.getDesignator().obj.getName(), desStatAssignopExpr);
			}
			else
				if(!desStatAssignopExpr.getExpr().struct.assignableTo(desStatAssignopExpr.getDesignator().obj.getType()))
					report_error("Greska: dodela u neodgovarajucu promenljivu:" + desStatAssignopExpr.getDesignator().obj.getName(), desStatAssignopExpr);
		
	}  
	
	@Override
	public void visit(DesignatorStatAssign designatorStatAssign) {
		designatorStatAssign.struct=designatorStatAssign.getDesignatorStatAssignopExpr().struct;
	}  
	
	@Override
	public void visit(DesignatorStatInc designatorStatInc) {
		int kind =designatorStatInc.getDesignator().obj.getKind();
		if(kind!= Obj.Var && kind!= Obj.Elem)
			report_error("Greska: Inkrement neodgovarajuce promenljive: "+designatorStatInc.getDesignator().obj.getName(), designatorStatInc);
		else
			if(!designatorStatInc.getDesignator().obj.getType().equals(Tab.intType))
				report_error("Greska: Inkrement promenljive koja nije int: "+designatorStatInc.getDesignator().obj.getName(), designatorStatInc);
	}
	
	@Override
	public void visit(DesignatorStatDec designatorStatDec) {
		int kind =designatorStatDec.getDesignator().obj.getKind();
		if(kind!= Obj.Var && kind!= Obj.Elem)
			report_error("Greska: Dekrement neodgovarajuce promenljive: "+designatorStatDec.getDesignator().obj.getName(), designatorStatDec);
		else
			if(!designatorStatDec.getDesignator().obj.getType().equals(Tab.intType))
				report_error("Greska: Dekrement promenljive koja nije int: "+designatorStatDec.getDesignator().obj.getName(), designatorStatDec);
	}
	
	@Override
	public void visit(DesignatorStat designatorStat) {
		designatorStat.struct=designatorStat.getDesignatorStatement().struct;
	}
	
	@Override
	public void visit(FindAnyStatement findAny) {
		Struct typeLeft=findAny.getDesignator().obj.getType();
		if(typeLeft!=boolType)
			report_error("Greska: Sa leve strane", findAny);
	}
	
	
	
	public boolean passed() {
		return !errorDetected;
	}
}
