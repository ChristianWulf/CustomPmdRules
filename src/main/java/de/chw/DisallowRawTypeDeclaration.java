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
