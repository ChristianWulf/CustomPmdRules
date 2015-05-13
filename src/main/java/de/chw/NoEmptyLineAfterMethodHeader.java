package de.chw;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTBlock;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class NoEmptyLineAfterMethodHeader extends AbstractJavaRule {

	public NoEmptyLineAfterMethodHeader() {
		addRuleChainVisit(ASTMethodDeclaration.class);
	}

	@Override
	public Object visit(final ASTMethodDeclaration node, final Object data) {
		ASTBlock block = node.getBlock();
		int numChildren = block.jjtGetNumChildren();
		if (numChildren > 0) {
			Node firstBlockStatement = block.jjtGetChild(0);
			int lineDiff = block.getBeginLine() - firstBlockStatement.getBeginLine();
			if (lineDiff < -1) {
				String fullQualifiedMethodName = node.getMethodName();
				addViolation(data, node, fullQualifiedMethodName);
			}
		}

		return super.visit(node, data);
	}
}
