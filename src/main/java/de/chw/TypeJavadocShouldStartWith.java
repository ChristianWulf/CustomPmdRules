package de.chw;

import java.util.List;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.CommentUtil;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.properties.StringProperty;

public class TypeJavadocShouldStartWith extends AbstractJavaRule {

	private static final String DEFAULT_FIRST_CHARACTERS = "Represents ";

	private static final StringProperty FIRST_CHARACTERS = new StringProperty("firstCharacters",
			"First characters", DEFAULT_FIRST_CHARACTERS, 1.0f);

	private String firstCharacters;

	public TypeJavadocShouldStartWith() {
		definePropertyDescriptor(FIRST_CHARACTERS);

		addRuleChainVisit(ASTCompilationUnit.class);
		addRuleChainVisit(ASTClassOrInterfaceDeclaration.class);
	}

	@Override
	public Object visit(ASTCompilationUnit node, Object data) {
		firstCharacters = getProperty(FIRST_CHARACTERS);
		// List<Comment> comments = node.getComments();
		// ASTClassOrInterfaceDeclaration firstDescendantOfType = comments.get(1).getFirstDescendantOfType(ASTClassOrInterfaceDeclaration.class);
		return super.visit(node, data);
	}

	@Override
	public Object visit(final ASTClassOrInterfaceDeclaration node, final Object data) {
		if (null != node.comment()) {
			String comment = node.comment().toString();
			List<String> commentLines = CommentUtil.multiLinesIn(comment);
			if (!commentLines.isEmpty()) {
				String firstCommentLine = commentLines.get(0);
				if (!firstCommentLine.startsWith(firstCharacters)) {
					addViolation(data, node, new Object[] { node.getType().getName(), firstCharacters, firstCommentLine });
				}
			}
		}

		return super.visit(node, data);
	}
}
