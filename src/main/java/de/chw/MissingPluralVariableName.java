package de.chw;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaAccessNode;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.properties.StringMultiProperty;

public class MissingPluralVariableName extends AbstractJavaRule {

	private static final StringMultiProperty COLLECTION_TYPE_NAMES_DESCRIPTOR = new StringMultiProperty(
			"collectionTypeNames", "Collection type names",
			new String[] { Collection.class.getName(), Set.class.getName(), List.class.getName() }, 1.0f, ',');
	private static final StringMultiProperty EXCLUDED_COLLECTION_TYPE_NAMES_DESCRIPTOR = new StringMultiProperty(
			"excludedCollectionTypeNames", "Excluded collection type names",
			new String[] { Queue.class.getName(), Stack.class.getName() }, 2.0f, ',');

	private final Set<Class<?>> collectionTypes = new HashSet<Class<?>>();
	private final Set<Class<?>> excludedCollectionTypes = new HashSet<Class<?>>();

	public MissingPluralVariableName() {
		definePropertyDescriptor(COLLECTION_TYPE_NAMES_DESCRIPTOR);
		definePropertyDescriptor(EXCLUDED_COLLECTION_TYPE_NAMES_DESCRIPTOR);

		setIncludesAndExcludes();

		addRuleChainVisit(ASTCompilationUnit.class);
		addRuleChainVisit(ASTFieldDeclaration.class);
		addRuleChainVisit(ASTLocalVariableDeclaration.class);
	}

	@Override
	public void apply(final List<? extends Node> nodes, final RuleContext ctx) {
		setIncludesAndExcludes();

		super.apply(nodes, ctx);
	}

	private void setIncludesAndExcludes() {
		String[] propertyValue;

		propertyValue = super.getProperty(COLLECTION_TYPE_NAMES_DESCRIPTOR);
		List<String> collectionTypeNames = Arrays.asList(propertyValue);
		for (String collectionTypeName : collectionTypeNames) {
			Class<?> collectionType = resolveClassByName(collectionTypeName);
			collectionTypes.add(collectionType);
		}

		propertyValue = super.getProperty(EXCLUDED_COLLECTION_TYPE_NAMES_DESCRIPTOR);
		List<String> excludedCollectionTypeNames = Arrays.asList(propertyValue);
		for (String excludedCollectionTypeName : excludedCollectionTypeNames) {
			Class<?> excludedCollectionType = resolveClassByName(excludedCollectionTypeName);
			excludedCollectionTypes.add(excludedCollectionType);
		}
	}

	private Class<?> resolveClassByName(final String className) {
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

		addViolationUponMissingPlural(node, data, variableName, type, node.isArray(), node.getArrayDepth());

		return data;
	}

	@Override
	public Object visit(final ASTLocalVariableDeclaration node, final Object data) {
		String variableName = node.getVariableName();
		Class<?> type = node.getTypeNode().getType();

		addViolationUponMissingPlural(node, data, variableName, type, node.isArray(), node.getArrayDepth());

		return data;
	}

	private void addViolationUponMissingPlural(final AbstractJavaAccessNode node, final Object data,
			final String variableName, final Class<?> type, boolean isArray, int arrayDepth) {
		if (null == type) {
			return;
		}

		if (!isArray && type.isPrimitive()) {
			return;
		}

		final boolean variableNameIsNotInPluralForm = !isPlural(variableName);

		if (isArray) {
			if (variableNameIsNotInPluralForm) {
				String typeName = type.getName() + StringUtils.repeat("[]", arrayDepth);
				addViolation(data, node, new Object[] { variableName, typeName });
				return;
			}
		}

		for (Class<?> excludedCollectionType : excludedCollectionTypes) {
			if (excludedCollectionType.isAssignableFrom(type)) {
				return;
			}
		}

		for (Class<?> collectionType : collectionTypes) {
			if (collectionType.isAssignableFrom(type)) {
				if (variableNameIsNotInPluralForm) {
					String typeName = getTypeName(type);
					addViolation(data, node, new Object[] { variableName, typeName });
					return;
				}
			}
		}
	}

	private String getTypeName(final Class<?> type) {
		// generic
		TypeVariable<?>[] typeParameters = type.getTypeParameters();
		String typeName = type.getName();

		if (typeParameters.length > 0) {
			StringBuilder typeNameBuilder = new StringBuilder();
			typeNameBuilder.append("<");
			for (TypeVariable<?> typeVariable : typeParameters) {
				String genericTypeName = typeVariable.getTypeName();
				typeNameBuilder.append(genericTypeName);
				typeNameBuilder.append(",");
			}
			typeNameBuilder.append(">");
			typeName += typeNameBuilder;
		}

		// array
		System.out.println("array? " + type.getComponentType() + ", " + type.isArray());
		if (type.isArray()) {
			typeName += "[]";
			Class<?> ct = type.getComponentType();
			while (ct != null) {
				typeName += "[]";
				ct = ct.getComponentType();
			}
		}
		return typeName;

	}

	private boolean isPlural(final String variableName) {
		if (variableName.endsWith("s")) {
			return true;
		}
		return false;
	}
}
