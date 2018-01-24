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

import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.CommentUtil;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.properties.StringProperty;

public class TypeJavadocShouldStartWith extends AbstractJavaRule {

	private static final String DEFAULT_FIRST_CHARACTERS = "Represents ";

	private static final StringProperty FIRST_CHARACTERS = new StringProperty("firstCharacters", "First characters",
			DEFAULT_FIRST_CHARACTERS, 1.0f);

	private String firstCharacters;

	public TypeJavadocShouldStartWith() {
		definePropertyDescriptor(FIRST_CHARACTERS);

		addRuleChainVisit(ASTCompilationUnit.class);
		addRuleChainVisit(ASTClassOrInterfaceDeclaration.class);
	}

	@Override
	public void apply(final List<? extends Node> nodes, final RuleContext ctx) {
		firstCharacters = getProperty(FIRST_CHARACTERS);
		super.apply(nodes, ctx);
	}

	@Override
	public Object visit(final ASTClassOrInterfaceDeclaration node, final Object data) {
		if (null != node.comment()) {
			String comment = node.comment().toString();
			List<String> commentLines = CommentUtil.multiLinesIn(comment);
			if (!commentLines.isEmpty()) {
				String firstCommentLine = commentLines.get(0);
				if (!firstCommentLine.startsWith(firstCharacters)) {
					addViolation(data, node,
							new Object[] { node.getType().getName(), firstCharacters, firstCommentLine });
				}
			}
		}

		return super.visit(node, data);
	}

}
