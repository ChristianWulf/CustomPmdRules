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
