package de.chw;

import org.junit.Test;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTExtendsList;
import net.sourceforge.pmd.lang.java.ast.ASTMarkerAnnotation;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class UnitTestMayNotExtendAnything extends AbstractJavaRule {

	private static class AstContext {
		String typeName = "";
		String typeNameOfSuperClass = "";
		boolean hasTestAnnotation = false;
	}

	public UnitTestMayNotExtendAnything() {
		setMessage("Illegal test class declaration: the test class {0} inherits from another class, namely {1}.");
	}

	@Override
	public Object visit(ASTExtendsList node, Object data) {
		ASTClassOrInterfaceType classType = node.getFirstChildOfType(ASTClassOrInterfaceType.class);

		AstContext astContext = (AstContext) data;
		astContext.typeNameOfSuperClass = classType.getImage();

		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTMarkerAnnotation node, Object data) {
		if (node.getType().equals(Test.class)) {
			AstContext astContext = (AstContext) data;
			astContext.hasTestAnnotation = true;
			return null;
		}

		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTCompilationUnit node, Object data) {
		AstContext astContext = new AstContext();

		Object result = super.visit(node, astContext);

		if (!astContext.typeNameOfSuperClass.isEmpty() && astContext.hasTestAnnotation) {
			addViolation(data, node, new Object[] { astContext.typeName, astContext.typeNameOfSuperClass });
		}

		return result;
	}

	@Override
	public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
		AstContext astContext = (AstContext) data;

		// only check the outer class, not inner classes or sibling classes
		if (astContext.typeName.isEmpty()) {
			astContext.typeName = node.getImage();
		}

		return super.visit(node, data);
	}

	String getKey(Node node) {
		ASTCompilationUnit compilationUnit = node.getFirstParentOfType(ASTCompilationUnit.class);
		return compilationUnit.toString();
	}
}
