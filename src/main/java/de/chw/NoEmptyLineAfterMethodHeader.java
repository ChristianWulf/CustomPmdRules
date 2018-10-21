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
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.Comment;
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

	/*
	 * We copied this method from MyCommentUtil since MyCommentUtil is not loaded by
	 * the class loader from our PMD Eclipse plugin. As long as our
	 * qa-eclipse-plugin is not able to load referenced (helper) classes from a rule
	 * class, we use this work-around.
	 */
	private static boolean isCommentInLine(final ASTMethodDeclaration node, final int lineNumber) {
		ASTCompilationUnit compilationUnit = node.getParentsOfType(ASTCompilationUnit.class).get(0);
		for (Comment comment : compilationUnit.getComments()) {
			if (comment.getBeginLine() <= lineNumber && lineNumber <= comment.getEndLine()) {
				return true;
			}
		}
		return false;
	}

}
