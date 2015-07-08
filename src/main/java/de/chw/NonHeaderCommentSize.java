package de.chw;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.Comment;
import net.sourceforge.pmd.lang.java.rule.comments.AbstractCommentRule;
import net.sourceforge.pmd.lang.rule.properties.BooleanProperty;
import net.sourceforge.pmd.lang.rule.properties.IntegerProperty;
import net.sourceforge.pmd.util.StringUtil;

public class NonHeaderCommentSize extends AbstractCommentRule {

	public static final IntegerProperty MAX_LINES = new IntegerProperty("maxLines", "Maximum lines", 2, 200, 6, 2.0f);
	public static final IntegerProperty MAX_LINE_LENGTH = new IntegerProperty("maxLineLength", "Maximum line length",
			1, 200, 80, 2.0f);
	public static final BooleanProperty IGNORE_HEADER_COMMENT = new BooleanProperty("ignoreHeaderComment",
			"Ignore header comment", true, 2.0f);

	private static final String CR = "\n";

	private boolean ignoreHeaderComment;

	public NonHeaderCommentSize() {
		definePropertyDescriptor(MAX_LINES);
		definePropertyDescriptor(MAX_LINE_LENGTH);
		definePropertyDescriptor(IGNORE_HEADER_COMMENT);
	}

	private static boolean hasRealText(final String line) {
		if (StringUtil.isEmpty(line)) {
			return false;
		}

		return !StringUtil.isAnyOf(line.trim(), "//", "/*", "/**", "*", "*/");
	}

	private boolean hasTooManyLines(final Comment comment) {
		String[] lines = comment.getImage().split(CR);

		int start = 0; // start from top
		for (; start < lines.length; start++) {
			if (hasRealText(lines[start])) {
				break;
			}
		}

		int end = lines.length - 1; // go up from bottom
		for (; end > 0; end--) {
			if (hasRealText(lines[end])) {
				break;
			}
		}

		int lineCount = end - start + 1;

		return lineCount > getProperty(MAX_LINES);
	}

	private String withoutCommentMarkup(final String text) {
		return StringUtil.withoutPrefixes(text.trim(), "//", "*", "/**");
	}

	private List<Integer> overLengthLineIndicesIn(final Comment comment) {
		int maxLength = getProperty(MAX_LINE_LENGTH);

		List<Integer> indicies = new ArrayList<Integer>();
		String[] lines = comment.getImage().split(CR);

		int offset = comment.getBeginLine();

		for (int i = 0; i < lines.length; i++) {
			String cleaned = withoutCommentMarkup(lines[i]);
			if (cleaned.length() > maxLength) {
				indicies.add(i + offset);
			}
		}

		return indicies;
	}

	@Override
	public void apply(final List<? extends Node> nodes, final RuleContext ctx) {
		ignoreHeaderComment = getProperty(IGNORE_HEADER_COMMENT);
		super.apply(nodes, ctx);
	}

	@Override
	public Object visit(final ASTCompilationUnit cUnit, final Object data) {
		for (Comment comment : cUnit.getComments()) {
			if (ignoreHeaderComment && comment.getBeginLine() == 1) {
				continue;
			}

			if (hasTooManyLines(comment)) {
				addViolationWithMessage(data, cUnit, this.getMessage() + ": Too many lines "+ignoreHeaderComment+" "+comment.getBeginLine(), comment.getBeginLine(),
						comment.getEndLine());
			}

			List<Integer> lineNumbers = overLengthLineIndicesIn(comment);
			if (lineNumbers.isEmpty()) {
				continue;
			}

			for (Integer lineNum : lineNumbers) {
				addViolationWithMessage(data, cUnit, this.getMessage() + ": Line too long", lineNum, lineNum);
			}
		}

		return super.visit(cUnit, data);
	}

}
