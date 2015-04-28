package de.chw;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaAccessNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.properties.StringMultiProperty;

public class MissingPluralVariableName extends AbstractJavaRule {

	public static final StringMultiProperty COLLECTION_TYPE_NAMES_DESCRIPTOR = new StringMultiProperty("collectionTypeNames",
			"Collection type names", new String[] { Collection.class.getName(), Set.class.getName(), List.class.getName() }, 1.0f, ',');

	public static final StringMultiProperty EXCLUDED_COLLECTION_TYPE_NAMES_DESCRIPTOR = new StringMultiProperty("excludedCollectionTypeNames",
			"Excluded collection type names", new String[] { Queue.class.getName(), Stack.class.getName() }, 2.0f, ',');

	private final Set<Class<?>> collectionTypes = new HashSet<Class<?>>();
	private final Set<Class<?>> excludedCollectionTypes = new HashSet<Class<?>>();

	public MissingPluralVariableName() {
		definePropertyDescriptor(COLLECTION_TYPE_NAMES_DESCRIPTOR);
		definePropertyDescriptor(EXCLUDED_COLLECTION_TYPE_NAMES_DESCRIPTOR);

		addRuleChainVisit(ASTCompilationUnit.class);
		addRuleChainVisit(ASTFieldDeclaration.class);
		addRuleChainVisit(ASTLocalVariableDeclaration.class);
	}

	@Override
	public Object visit(ASTCompilationUnit node, Object data) {
		List<String> collectionTypeNames = Arrays.asList(super.getProperty(COLLECTION_TYPE_NAMES_DESCRIPTOR));
		for (String collectionTypeName : collectionTypeNames) {
			Class<?> collectionType = resolveClassByName(collectionTypeName);
			collectionTypes.add(collectionType);
		}

		List<String> excludedCollectionTypeNames = Arrays.asList(super.getProperty(EXCLUDED_COLLECTION_TYPE_NAMES_DESCRIPTOR));
		for (String excludedCollectionTypeName : excludedCollectionTypeNames) {
			Class<?> excludedCollectionType = resolveClassByName(excludedCollectionTypeName);
			excludedCollectionTypes.add(excludedCollectionType);
		}

		return data;
	}

	private Class<?> resolveClassByName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Object visit(final ASTFieldDeclaration node, final Object data) {
		String variableName = node.getVariableName();
		Class<?> type = node.getType();
		addViolationUponMissingPlural(node, data, variableName, type);

		return data;
	}

	@Override
	public Object visit(final ASTLocalVariableDeclaration node, final Object data) {
		String variableName = node.getVariableName();
		Class<?> type = node.getTypeNode().getType();
		addViolationUponMissingPlural(node, data, variableName, type);

		return data;
	}

	private void addViolationUponMissingPlural(final AbstractJavaAccessNode node, final Object data, final String variableName, final Class<?> type) {
		if (null != type) {
			for (Class<?> excludedCollectionType : excludedCollectionTypes) {
				if (excludedCollectionType.isAssignableFrom(type)) {
					return;
				}
			}

			for (Class<?> collectionType : collectionTypes) {
				if (collectionType.isAssignableFrom(type)) {
					if (!isPlural(variableName)) {
						addViolation(data, node, new Object[] { variableName, type.getName() });
						break;
					}
				}
			}
		}
	}

	private boolean isPlural(final String variableName) {
		if (variableName.endsWith("s")) {
			return true;
		}
		return false;
	}
}
