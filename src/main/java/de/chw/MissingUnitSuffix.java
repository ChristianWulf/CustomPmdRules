package de.chw;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaAccessNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class MissingUnitSuffix extends AbstractJavaRule {

	private final Set<String> physicalVariableNames = new HashSet<String>();

	public MissingUnitSuffix() {
		physicalVariableNames.add("duration");
		physicalVariableNames.add("distance");
		physicalVariableNames.add("mean");
	}

	@Override
	public Object visit(final ASTFieldDeclaration node, final Object data) {
		String variableName = node.getVariableName();

		addViolationUponMissingUnit(node, data, variableName);

		return super.visit(node, data);
	}

	@Override
	public Object visit(final ASTLocalVariableDeclaration node, final Object data) {
		String variableName = node.getVariableName();

		addViolationUponMissingUnit(node, data, variableName);

		return super.visit(node, data);
	}

	private void addViolationUponMissingUnit(final AbstractJavaAccessNode node, final Object data, final String variableName) {
		if (physicalVariableNames.contains(variableName)) {
			addViolation(data, node, new Object[] { variableName });
		}
	}
}
