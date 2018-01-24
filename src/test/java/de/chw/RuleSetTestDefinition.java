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
