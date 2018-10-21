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

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * There must be a test file for each java file. A test file is identified by
 * its file name.
 * 
 * @author Christian Wulf
 */
public class DisallowUntestedType extends AbstractJavaRule {

	public DisallowUntestedType() {
		setMessage("Missing test file for the type {0}.");
	}

	@Override
	public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
		if (node.isNested()) {
			return null;
		}

		if (node.isInterface()) {
			return null;
		}

		String typeName = getFullyQualifiedTypeName(node);
		String testTypeName = String.format("%sTest", typeName);

		try {
			Class.forName(testTypeName);
		} catch (ClassNotFoundException e) {
			addViolation(data, node, new Object[] { node.getImage() });
		}

		return null;
	}

	private String getFullyQualifiedTypeName(ASTClassOrInterfaceDeclaration node) {
		// TODO Auto-generated method stub
		return null;
	}
}
