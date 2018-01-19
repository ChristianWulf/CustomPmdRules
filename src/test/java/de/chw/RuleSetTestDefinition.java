package de.chw;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.util.datasource.DataSource;
import net.sourceforge.pmd.util.datasource.FileDataSource;

public class RuleSetTestDefinition {

	private RuleSetFactory ruleSetFactory;

	RuleSetTestDefinition(RuleSetFactory ruleSetFactory) {
		this.ruleSetFactory = ruleSetFactory;
	}

	public RuleSetTestExecution on(String... resourceFilePaths) {
		List<DataSource> datasources = new ArrayList<DataSource>();

		for (String resourcePath : resourceFilePaths) {
			datasources.add(new FileDataSource(resolveFile(resourcePath)));
		}

		return new RuleSetTestExecution(ruleSetFactory, datasources);
	}

	private File resolveFile(String resourcePath) {
		URL resource = getClass().getClassLoader().getResource(resourcePath);

		if (resource == null) {
			String message = String.format(
					"Resource could not be found or the invoker doesn't have adequate privileges to get the resource: '%s'",
					resourcePath);
			throw new IllegalArgumentException(message);
		}

		try {
			return new File(resource.toURI());
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}
}
