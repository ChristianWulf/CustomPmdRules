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

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTExtendsList;
import net.sourceforge.pmd.lang.java.ast.ASTMarkerAnnotation;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class UnitTestMayNotExtendAnything extends AbstractJavaRule {

	private static class AstContext {
		String typeName = "";
		String typeNameOfSuperClass = "";
		boolean hasTestAnnotation = false;
	}

	private static final String JUNIT_TEST_ANNOTATION_FQN = "org.junit.Test";

	public UnitTestMayNotExtendAnything() {
		setMessage("Illegal test class declaration: the test class {0} inherits from another class, namely {1}.");
	}

	@Override
	public Object visit(ASTExtendsList node, Object data) {
		ASTClassOrInterfaceType classType = node.getFirstChildOfType(ASTClassOrInterfaceType.class);

		AstContext astContext = (AstContext) data;
		astContext.typeNameOfSuperClass = classType.getImage();

		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTMarkerAnnotation node, Object data) {
		Class<?> type = node.getType();
		if (type != null) {
			// We check by comparing the type name and not by using instanceof
			// to avoid importing org.junit.Test.
			final String fullyQualifiedTypeName = type.getName();
			if (JUNIT_TEST_ANNOTATION_FQN.equals(fullyQualifiedTypeName)) {
				AstContext astContext = (AstContext) data;
				astContext.hasTestAnnotation = true;
				return null;
			}
		}

		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTCompilationUnit node, Object data) {
		AstContext astContext = new AstContext();

		Object result = super.visit(node, astContext);

		if (!astContext.typeNameOfSuperClass.isEmpty() && astContext.hasTestAnnotation) {
			addViolation(data, node, new Object[] { astContext.typeName, astContext.typeNameOfSuperClass });
		}

		return result;
	}

	@Override
	public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
		AstContext astContext = (AstContext) data;

		// only check the outer class, not inner classes or sibling classes
		if (astContext.typeName.isEmpty()) {
			astContext.typeName = node.getImage();
		}

		return super.visit(node, data);
	}

	String getKey(Node node) {
		ASTCompilationUnit compilationUnit = node.getFirstParentOfType(ASTCompilationUnit.class);
		return compilationUnit.toString();
	}
}
