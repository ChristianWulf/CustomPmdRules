package de.chw;

import java.util.Collection;

import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaAccessNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class MissingPluralVariableName extends AbstractJavaRule {

	@SuppressWarnings("rawtypes")
	private static final Class<Collection> COLLECTION_TYPE = Collection.class;

	@Override
	public Object visit(final ASTFieldDeclaration node, final Object data) {
		String variableName = node.getVariableName();
		Class<?> type = node.getType();
		addViolationUponMissingPlural(node, data, variableName, type);

		return super.visit(node, data);
	}

	@Override
	public Object visit(final ASTLocalVariableDeclaration node, final Object data) {
		String variableName = node.getVariableName();
		Class<?> type = node.getTypeNode().getType();
		addViolationUponMissingPlural(node, data, variableName, type);

		return super.visit(node, data);
	}

	private void addViolationUponMissingPlural(final AbstractJavaAccessNode node, final Object data, final String variableName, final Class<?> type) {
		if (null != type) {
			if (COLLECTION_TYPE.isAssignableFrom(type) && !isPlural(variableName)) {
				addViolation(data, node, new Object[] { variableName, type.getName() });
			}
		}
	}

	private boolean isPlural(final String variableName) {
		if (variableName.endsWith("s")) {
			return true;
		}
		return false;
	}
}
