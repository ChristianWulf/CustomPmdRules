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
