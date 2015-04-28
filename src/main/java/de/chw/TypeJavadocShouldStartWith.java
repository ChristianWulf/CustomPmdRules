package de.chw;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.Comment;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class TypeJavadocShouldStartWith extends AbstractJavaRule {

	@Override
	public Object visit(final ASTClassOrInterfaceDeclaration node, final Object data) {
		Comment comment = node.comment();


		return super.visit(node, data);
	}
}
