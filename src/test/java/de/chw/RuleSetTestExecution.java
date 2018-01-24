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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.renderers.Renderer;
import net.sourceforge.pmd.util.datasource.DataSource;

public class RuleSetTestExecution {

	private RuleSetFactory ruleSetFactory;
	private List<DataSource> datasources;

	RuleSetTestExecution(RuleSetFactory ruleSetFactory, List<DataSource> datasources) {
		this.ruleSetFactory = ruleSetFactory;
		this.datasources = datasources;
	}

	/**
	 * @return a report containing the violations of all passed rules.
	 */
	public Report start() {
		PMDConfiguration configuration = new PMDConfiguration();

		RuleContext ctx = new RuleContext();

		UnitTestRenderer renderer = new UnitTestRenderer();
		List<Renderer> renderers = new ArrayList<Renderer>();
		renderers.add(renderer);

		PMD.processFiles(configuration, ruleSetFactory, datasources, ctx, renderers);

		return renderer.getMergedReport();
	}
}
