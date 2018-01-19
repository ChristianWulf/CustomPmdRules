package de.chw;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleSetNotFoundException;
import net.sourceforge.pmd.RuleSets;

public class PmdTester {

	public static RuleSetTestDefinition test(final Rule rule) {
		RuleSet ruleSet = new RuleSet();
		ruleSet.addRule(rule);

		return test(ruleSet);
	}

	public static RuleSetTestDefinition test(final RuleSet ruleSet) {
		RuleSetFactory ruleSetFactory = new RuleSetFactory() {
			@Override
			public synchronized RuleSets createRuleSets(String referenceString) throws RuleSetNotFoundException {
				return new RuleSets(ruleSet);
			}
		};

		return new RuleSetTestDefinition(ruleSetFactory);
	}

}
