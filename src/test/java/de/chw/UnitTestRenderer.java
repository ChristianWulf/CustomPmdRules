package de.chw;

import java.io.IOException;

import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.renderers.AbstractRenderer;
import net.sourceforge.pmd.renderers.Renderer;
import net.sourceforge.pmd.util.datasource.DataSource;

public class UnitTestRenderer extends AbstractRenderer implements Renderer {

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
		for (RuleViolation ruleViolation : report) {
			String mesage = String.format("%s, %s", ruleViolation.getRule(), ruleViolation.getFilename());
			System.out.println("UnitTestRenderer.renderFileReport(): " + mesage);
		}
	}

	public void end() throws IOException {
		// do nothing
	}

}
