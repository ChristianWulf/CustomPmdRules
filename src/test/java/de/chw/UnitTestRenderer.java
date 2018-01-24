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

import java.io.IOException;

import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.renderers.AbstractRenderer;
import net.sourceforge.pmd.renderers.Renderer;
import net.sourceforge.pmd.util.datasource.DataSource;

class UnitTestRenderer extends AbstractRenderer implements Renderer {

	private final Report mergedReport = new Report();

	public UnitTestRenderer() {
		super("UnitTestRenderer", "PMD renderer for unit tests");
	}

	public String defaultFileExtension() {
		return null;
	}

	public void start() throws IOException {
		// do nothing
	}

	public void startFileAnalysis(DataSource dataSource) {
		System.out.println("UnitTestRenderer.startFileAnalysis(): " + dataSource);
	}

	public void renderFileReport(Report report) throws IOException {
		mergedReport.merge(report);
	}

	public void end() throws IOException {
		// do nothing
	}

	public Report getMergedReport() {
		return mergedReport;
	}

}
