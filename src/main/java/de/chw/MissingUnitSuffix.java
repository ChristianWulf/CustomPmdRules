/**
 * Copyright © 2016 Christian Wulf (${email})
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.chw;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RulePriority;
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

		setPriority(RulePriority.HIGH);

		updateConfiguration();

		addRuleChainVisit(ASTCompilationUnit.class);
		addRuleChainVisit(ASTFieldDeclaration.class);
		addRuleChainVisit(ASTLocalVariableDeclaration.class);
	}

	@Override
	public void apply(final List<? extends Node> nodes, final RuleContext ctx) {
		updateConfiguration();

		super.apply(nodes, ctx);
	}

	private void updateConfiguration() {
		List<String> configuredPhysicalVariableNames = Arrays.asList(super.getProperty(PHYSICAL_VARIABLE_NAMES));
		physicalVariableNames.addAll(configuredPhysicalVariableNames);
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

	private void addViolationUponMissingUnit(final AbstractJavaAccessNode node, final Object data,
			final String variableName, final Class<?> type) {
		if (null != type && type.isPrimitive()) {
			if (physicalVariableNames.contains(variableName)) {
				addViolation(data, node, new Object[] { variableName });
			}
		}
	}
}
