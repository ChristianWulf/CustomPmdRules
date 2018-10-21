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

import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaAccessNode;
import net.sourceforge.pmd.lang.java.ast.Comment;

final class MyCommentUtil {

	private MyCommentUtil() {
		// utility class
	}

	public static Comment getJavadocOfNode(final ASTMethodDeclaration node) {
		ASTCompilationUnit compilationUnit = node.getParentsOfType(ASTCompilationUnit.class).get(0);
		for (Comment comment : compilationUnit.getComments()) {
			int lineDiff = node.getBeginLine() - comment.getEndLine();
			int columnDiff = node.getBeginColumn() - comment.getEndColumn();
			if (lineDiff <= 1 && columnDiff <= 1) {
				return comment;
			}
		}

		return null;
	}

	public static boolean isCommentInLine(final AbstractJavaAccessNode node, final int lineNumber) {
		ASTCompilationUnit compilationUnit = node.getParentsOfType(ASTCompilationUnit.class).get(0);
		for (Comment comment : compilationUnit.getComments()) {
			if (comment.getBeginLine() <= lineNumber && lineNumber <= comment.getEndLine()) {
				return true;
			}
		}
		return false;
	}

}
