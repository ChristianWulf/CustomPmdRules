package de.chw;

import java.util.Iterator;

import org.junit.Test;

import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.RuleViolation;

public class UnitTestMayNotExtendAnythingTest {

	@Test
	public void testUnitTestAnnotation() throws Exception {
		Report report = PmdTester.test(new UnitTestMayNotExtendAnything())
				.on("ExampleTest.txt", "ExampleTestWithExtends.txt").start();

		for (RuleViolation ruleViolation : report) {
			String message = String.format("%s, %s, %s", ruleViolation.getRule(), ruleViolation.getFilename(),
					ruleViolation.getDescription());
			System.out.println("UnitTestMayNotExtendAnythingTest.testUnitTestAnnotation(): " + message);
		}

		Iterator<RuleViolation> iter = report.iterator();

		RuleViolation ruleViolation = iter.next();
//		assertThat(ruleViolation.getClassName(), is(equalTo("ExampleTestWithExtends")));

		// assertThat(iter.hasNext(), is(false));
	}

}
