package de.chw;

import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
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

	public static boolean isCommentInLine(final ASTMethodDeclaration node, final int lineNumber) {
		ASTCompilationUnit compilationUnit = node.getParentsOfType(ASTCompilationUnit.class).get(0);
		for (Comment comment : compilationUnit.getComments()) {
			if (comment.getBeginLine() <= lineNumber && lineNumber <= comment.getEndLine()) {
				return true;
			}
		}
		return false;
	}

}
