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

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.AbstractNode;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTExtendsList;
import net.sourceforge.pmd.lang.java.ast.ASTMarkerAnnotation;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class UnitTestMayNotExtendAnything extends AbstractJavaRule {

	private static class AstContext {
		String typeNameOfSuperClass = "";
		boolean hasTestAnnotation = false;
	}

	private static final String JUNIT_TEST_ANNOTATION_FQN = "org.junit.Test";

	public UnitTestMayNotExtendAnything() {
		setMessage("Illegal test class declaration: the test class {0} inherits from another class, namely {1}.");
	}

	@Override
	public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
		AstContext astContext = new AstContext();

		String uniqueKey = buildUniqueKey(node);

		RuleContext ruleContext = (RuleContext) data;
		ruleContext.setAttribute(uniqueKey, astContext);

		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTExtendsList node, Object data) {
		AbstractNode keyNode = node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
		String uniqueKey = buildUniqueKey(keyNode);
		RuleContext ruleContext = (RuleContext) data;
		AstContext astContext = (AstContext) ruleContext.getAttribute(uniqueKey);

		ASTClassOrInterfaceType classType = node.getFirstChildOfType(ASTClassOrInterfaceType.class);
		astContext.typeNameOfSuperClass = classType.getImage();

		return null;
	}

	@Override
	public Object visit(ASTClassOrInterfaceBody node, Object data) {
		AbstractNode keyNode = node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
		String uniqueKey = buildUniqueKey(keyNode);
		RuleContext ruleContext = (RuleContext) data;
		AstContext astContext = (AstContext) ruleContext.getAttribute(uniqueKey);

		ASTClassOrInterfaceBody parentClassNode = node.getFirstParentOfType(ASTClassOrInterfaceBody.class);

		// only check the outer class;
		// don't check inner classes or sibling classes
		if (parentClassNode != null) {
			return null;
		}

		Object result = super.visit(node, data);

		if (!astContext.typeNameOfSuperClass.isEmpty() && astContext.hasTestAnnotation) {
			addViolation(data, node, new Object[] { keyNode.getImage(), astContext.typeNameOfSuperClass });
		}

		return result;
	}

	@Override
	public Object visit(ASTMarkerAnnotation node, Object data) {
		Class<?> type = node.getType();
		if (type != null) {
			// We check by comparing the type name and not by using instanceof
			// to avoid importing org.junit.Test.
			final String fullyQualifiedTypeName = type.getName();
			if (JUNIT_TEST_ANNOTATION_FQN.equals(fullyQualifiedTypeName)) {
				AbstractNode keyNode = node.getFirstParentOfType(ASTClassOrInterfaceDeclaration.class);
				String uniqueKey = buildUniqueKey(keyNode);
				RuleContext ruleContext = (RuleContext) data;
				AstContext astContext = (AstContext) ruleContext.getAttribute(uniqueKey);

				astContext.hasTestAnnotation = true;
				return null;
			}
		}

		return null;
	}

	private String buildUniqueKey(AbstractNode node) {
		return node.getImage() + "-" + node.getBeginLine() + "-" + node.getEndLine();
	}
}
