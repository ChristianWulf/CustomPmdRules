package de.chw;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaAccessNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.properties.StringMultiProperty;

public class MissingUnitSuffix extends AbstractJavaRule {

	private static final String[] DEFAULT_PHYSICAL_VARIABLE_NAMES = { "duration", "distance", "mean" };

	private static final StringMultiProperty PHYSICAL_VARIABLE_NAMES = new StringMultiProperty("physicalVariableNames",
			"Physical variable names", DEFAULT_PHYSICAL_VARIABLE_NAMES, 1.0f, ',');

	private final Set<String> physicalVariableNames = new HashSet<String>();

	public MissingUnitSuffix() {
		definePropertyDescriptor(PHYSICAL_VARIABLE_NAMES);

		addRuleChainVisit(ASTCompilationUnit.class);
		addRuleChainVisit(ASTFieldDeclaration.class);
		addRuleChainVisit(ASTLocalVariableDeclaration.class);
	}

	@Override
	public void apply(final List<? extends Node> nodes, final RuleContext ctx) {
		List<String> configuredPhysicalVariableNames = Arrays.asList(super.getProperty(PHYSICAL_VARIABLE_NAMES));
		physicalVariableNames.addAll(configuredPhysicalVariableNames);
		super.apply(nodes, ctx);
	}

	@Override
	public Object visit(final ASTFieldDeclaration node, final Object data) {
		String variableName = node.getVariableName();
		Class<?> type = node.getType();

		addViolationUponMissingUnit(node, data, variableName, type);

		return data;
	}

	@Override
	public Object visit(final ASTLocalVariableDeclaration node, final Object data) {
		String variableName = node.getVariableName();
		Class<?> type = node.getTypeNode().getType();

		addViolationUponMissingUnit(node, data, variableName, type);

		return data;
	}

	private void addViolationUponMissingUnit(final AbstractJavaAccessNode node, final Object data, final String variableName, final Class<?> type) {
		if (null != type && type.isPrimitive()) {
			if (physicalVariableNames.contains(variableName)) {
				addViolation(data, node, new Object[] { variableName });
			}
		}
	}
}
