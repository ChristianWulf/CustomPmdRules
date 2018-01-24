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
import net.sourceforge.pmd.lang.java.ast.ASTBlock;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

public class NoEmptyLineAfterMethodHeader extends AbstractJavaRule {

	public NoEmptyLineAfterMethodHeader() {
		addRuleChainVisit(ASTMethodDeclaration.class);
	}

	@Override
	public Object visit(final ASTMethodDeclaration node, final Object data) {
		ASTBlock block = node.getBlock();
		int numChildren = block.jjtGetNumChildren();
		if (numChildren > 0) {
			Node firstBlockStatement = block.jjtGetChild(0);
			int lineDiff = firstBlockStatement.getBeginLine() - block.getBeginLine();
			boolean commentInLine = MyCommentUtil.isCommentInLine(node, block.getBeginLine() + 1);
			if (lineDiff > 1 && !commentInLine) {
				String fullQualifiedMethodName = node.getMethodName();
				addViolation(data, node, fullQualifiedMethodName);
			}
		}

		return super.visit(node, data);
	}

}
