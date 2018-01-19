package de.chw;

import org.junit.Test;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTExtendsList;
import net.sourceforge.pmd.lang.java.ast.ASTMarkerAnnotation;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class UnitTestMayNotExtendAnything extends AbstractJavaRule {

	private String typeName;
	private boolean hasTestAnnotation;

	@Override
	public Object visit(ASTExtendsList node, Object data) {
		ASTClassOrInterfaceType classType = node.getFirstChildOfType(ASTClassOrInterfaceType.class);
		typeName = classType.getImage();
		// classType.getType() // can be null
		// TODO Auto-generated method stub
		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTMarkerAnnotation node, Object data) {
		System.out.println("UnitTestMayNotExtendAnything.visit(): " + node.getType());

		if (node.getType().equals(Test.class)) {
			System.out.println("UnitTestMayNotExtendAnything.visit(): found test annotation");
			hasTestAnnotation = true;
			return null;
		}

		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTCompilationUnit node, Object data) {
		typeName = "";
		hasTestAnnotation = false;

		Object result = super.visit(node, data);

		if (!typeName.isEmpty() && hasTestAnnotation) {
			addViolation(data, node);
		}

		return result;
	}
}
