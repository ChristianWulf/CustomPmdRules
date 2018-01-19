package de.chw;

import org.junit.Test;

import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.RuleViolation;

public class UnitTestMayNotExtendAnythingTest {

	@Test
	public void testUnitTestAnnotation() throws Exception {
		Report report = PmdTester.test(new UnitTestMayNotExtendAnything())
				.on("ExampleTest.txt", "ExampleTestWithExtends.txt").start();

		for (RuleViolation ruleViolation : report) {
			String message = String.format("%s, %s", ruleViolation.getRule(), ruleViolation.getFilename());
			System.out.println("UnitTestMayNotExtendAnythingTest.testUnitTestAnnotation(): " + message);
		}
	}

}
