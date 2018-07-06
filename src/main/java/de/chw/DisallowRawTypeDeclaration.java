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

import java.lang.reflect.GenericDeclaration;

import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTReferenceType;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTTypeArguments;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class DisallowRawTypeDeclaration extends AbstractJavaRule {

	public DisallowRawTypeDeclaration() {
		setMessage("Illegal raw type usage in the {0} declaration of '{1}' from Line {2} to {3}.");
	}

	@Override
	public Object visit(ASTLocalVariableDeclaration node, Object data) {
		ASTType typeNode = node.getTypeNode();

		boolean isGenericType = isGenericType(typeNode);
		ASTTypeArguments typeArguments = typeNode.getFirstDescendantOfType(ASTTypeArguments.class);

		if (isGenericType && typeArguments == null) {
			addViolation(data, node,
					new Object[] { "local variable", node.getVariableName(), node.getBeginLine(), node.getEndLine() });
		}

		return null;
	}

	@Override
	public Object visit(ASTFieldDeclaration node, Object data) {
		ASTType typeNode = node.getFirstChildOfType(ASTType.class);

		boolean isGenericType = isGenericType(typeNode);
		ASTTypeArguments typeArguments = typeNode.getFirstDescendantOfType(ASTTypeArguments.class);

		if (isGenericType && typeArguments == null) {
			addViolation(data, node,
					new Object[] { "field", node.getVariableName(), node.getBeginLine(), node.getEndLine() });
		}

		return null;
	}

	@Override
	public Object visit(ASTFormalParameter node, Object data) {
		ASTType typeNode = node.getTypeNode();

		boolean isGenericType = isGenericType(typeNode);
		ASTTypeArguments typeArguments = typeNode.getFirstDescendantOfType(ASTTypeArguments.class);

		if (isGenericType && typeArguments == null) {
			String formalParameterName = getFormalParameterName(node);

			addViolation(data, node,
					new Object[] { "formal parameter", formalParameterName, node.getBeginLine(), node.getEndLine() });
		}

		return null;
	}

	private boolean isGenericType(ASTType typeNode) {
		ASTReferenceType referenceType = typeNode.getFirstChildOfType(ASTReferenceType.class);
		Class<?> type = referenceType.getType();
		return (type instanceof GenericDeclaration);
	}

	private String getFormalParameterName(ASTFormalParameter node) {
		ASTVariableDeclaratorId formalParameterVariable = node.getFirstChildOfType(ASTVariableDeclaratorId.class);
		String formalParameterName;
		if (formalParameterVariable == null) {
			formalParameterName = "<unknown parameter name>";
		} else {
			formalParameterName = formalParameterVariable.getImage();
		}
		return formalParameterName;
	}

}
