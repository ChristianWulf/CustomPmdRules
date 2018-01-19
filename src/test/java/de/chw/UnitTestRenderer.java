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
