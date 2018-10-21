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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.Test;

import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.RuleViolation;

public class NoEmptyLineAfterMethodHeaderTest {

	@Test
	public void testUnitTestAnnotation() throws Exception {
		Report report = PmdTester.test(new NoEmptyLineAfterMethodHeader())
				.on("EmptyLineAfterMethodHeader.txt").start();

		// for (RuleViolation ruleViolation : report) {
		// String message = String.format("%s, %s, %s", ruleViolation.getRule(),
		// ruleViolation.getFilename(),
		// ruleViolation.getDescription());
		// System.out.println(DisallowRawTypeDeclarationTest.class.getName() + ": " +
		// message);
		// }

		Iterator<RuleViolation> iter = report.iterator();

		RuleViolation ruleViolation = iter.next();
		assertThat(ruleViolation.getRule(), is(instanceOf(NoEmptyLineAfterMethodHeader.class)));
		assertThat(ruleViolation.getClassName(), is(equalTo("EmptyLineAfterMethodHeader")));
		assertThat(ruleViolation.getMethodName(), is(equalTo("emptyLine")));
		assertThat(ruleViolation.getBeginLine(), is(8));

		ruleViolation = iter.next();
		assertThat(ruleViolation.getRule(), is(instanceOf(NoEmptyLineAfterMethodHeader.class)));
		assertThat(ruleViolation.getClassName(), is(equalTo("EmptyLineAfterMethodHeader")));
		assertThat(ruleViolation.getMethodName(), is(equalTo("twoEmptyLines")));
		assertThat(ruleViolation.getBeginLine(), is(13));
		
		ruleViolation = iter.next();
		assertThat(ruleViolation.getRule(), is(instanceOf(NoEmptyLineAfterMethodHeader.class)));
		assertThat(ruleViolation.getClassName(), is(equalTo("EmptyLineAfterMethodHeader")));
		assertThat(ruleViolation.getMethodName(), is(equalTo("EmptyLineAfterMethodHeader")));
		assertThat(ruleViolation.getBeginLine(), is(23));
		
		ruleViolation = iter.next();
		assertThat(ruleViolation.getRule(), is(instanceOf(NoEmptyLineAfterMethodHeader.class)));
		assertThat(ruleViolation.getClassName(), is(equalTo("EmptyLineAfterMethodHeader")));
		assertThat(ruleViolation.getMethodName(), is(equalTo("EmptyLineAfterMethodHeader")));
		assertThat(ruleViolation.getBeginLine(), is(27));

		assertThat(iter.hasNext(), is(false));
	}

}
