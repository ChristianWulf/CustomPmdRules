package de.chw;

import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTTypeArguments;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class DisallowRawTypeDeclaration extends AbstractJavaRule {

	public DisallowRawTypeDeclaration() {
		setMessage("Illegal raw type usage in the {0} declaration from Line {1} to {2}.");
	}

	@Override
	public Object visit(ASTLocalVariableDeclaration node, Object data) {
		ASTType typeNode = node.getTypeNode();
		ASTTypeArguments typeArguments = typeNode.getFirstDescendantOfType(ASTTypeArguments.class);

		if (typeArguments == null) {
			addViolation(data, node, new Object[] { "local variable", node.getBeginLine(), node.getEndLine() });
		}

		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTFieldDeclaration node, Object data) {
		ASTType typeNode = node.getFirstChildOfType(ASTType.class);
		ASTTypeArguments typeArguments = typeNode.getFirstDescendantOfType(ASTTypeArguments.class);

		if (typeArguments == null) {
			addViolation(data, node, new Object[] { "field", node.getBeginLine(), node.getEndLine() });
		}

		return super.visit(node, data);
	}
}
