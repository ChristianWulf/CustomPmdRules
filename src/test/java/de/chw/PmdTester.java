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
