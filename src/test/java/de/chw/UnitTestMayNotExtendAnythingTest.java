package de.chw;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleSetNotFoundException;
import net.sourceforge.pmd.RuleSets;
import net.sourceforge.pmd.renderers.Renderer;
import net.sourceforge.pmd.util.datasource.DataSource;
import net.sourceforge.pmd.util.datasource.FileDataSource;

public class UnitTestMayNotExtendAnythingTest {

	@Test
	public void testUnitTestAnnotation() throws Exception {
		PMDConfiguration configuration = new PMDConfiguration();
		RuleSetFactory ruleSetFactory = new RuleSetFactory() {
			@Override
			public synchronized RuleSets createRuleSets(String referenceString) throws RuleSetNotFoundException {
				RuleSet ruleSet = new RuleSet();
				ruleSet.addRule(new UnitTestMayNotExtendAnything());
				return new RuleSets(ruleSet);
			}
		};
		List<DataSource> datasources = new ArrayList<DataSource>();
		datasources.add(new FileDataSource(resolveFile("ExampleTest.txt")));
		datasources.add(new FileDataSource(resolveFile("ExampleTestWithExtends.txt")));
		RuleContext ctx = new RuleContext();
		List<Renderer> renderers = new ArrayList<Renderer>();
		renderers.add(new UnitTestRenderer());
		PMD.processFiles(configuration, ruleSetFactory, datasources, ctx, renderers);
	}

	private File resolveFile(String fileName) throws URISyntaxException {
		URL resource = getClass().getClassLoader().getResource(fileName);
		return new File(resource.toURI());
	}
}
