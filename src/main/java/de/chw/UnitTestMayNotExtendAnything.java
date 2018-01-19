package de.chw;

import org.junit.Test;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTExtendsList;
import net.sourceforge.pmd.lang.java.ast.ASTMarkerAnnotation;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class UnitTestMayNotExtendAnything extends AbstractJavaRule {

	private static final String KEY = "";

	static class AstContext {
		String typeNameOfSuperClass = "";
		boolean hasTestAnnotation = false;
	}

	@Override
	public Object visit(ASTExtendsList node, Object data) {
		ASTClassOrInterfaceType classType = node.getFirstChildOfType(ASTClassOrInterfaceType.class);

		RuleContext ruleContext = (RuleContext) data;
		AstContext astContext = (AstContext) ruleContext.getAttribute(KEY);
		astContext.typeNameOfSuperClass = classType.getImage();

		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTMarkerAnnotation node, Object data) {
		if (node.getType().equals(Test.class)) {
			RuleContext ruleContext = (RuleContext) data;
			AstContext astContext = (AstContext) ruleContext.getAttribute(KEY);
			astContext.hasTestAnnotation = true;
			return null;
		}

		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTCompilationUnit node, Object data) {
		AstContext astContext = new AstContext();

		RuleContext ruleContext = (RuleContext) data;
		ruleContext.setAttribute(KEY, astContext);

		Object result = super.visit(node, data);

		if (!astContext.typeNameOfSuperClass.isEmpty() && astContext.hasTestAnnotation) {
			addViolation(data, node);
		}

		return result;
	}
}
