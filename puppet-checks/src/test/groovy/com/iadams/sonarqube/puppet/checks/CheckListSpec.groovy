/*
 * Sonar Puppet Plugin
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Iain Adams
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.iadams.sonarqube.puppet.checks

import org.sonar.api.batch.rule.ActiveRules
import org.sonar.api.batch.rule.CheckFactory
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder
import org.sonar.api.rules.Rule
import org.sonar.squidbridge.checks.SquidCheck
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by iwarapter
 */
class CheckListSpec extends Specification {

	def "each check is defined in list"(){
		given:
		def count = new File('src/main/java/com/iadams/sonarqube/puppet/checks/').listFiles().count{ it.name.endsWith('Check.java') }

		expect:
		count == CheckList.getChecks().size()
	}

	@Unroll
	def "Check #check.getSimpleName() has test"(){
		expect:
		String testName = '/' + check.getName().replace('.', '/') + "Spec.class";
		assert getClass().getResource(testName)

		where:
		check << CheckList.getChecks();
	}

	def "Check #check.getSimpleName() has name and description"(){
		given:
		ActiveRules activeRules = (new ActiveRulesBuilder())
				.build();
		CheckFactory checkFactory = new CheckFactory(activeRules);
		Collection<Rule> rules = checkFactory.<Rule>create("repositoryKey").addAnnotatedChecks(CheckList.getChecks()).all()

		expect:
		for(rule in rules) {
			assert getClass().getResource("/org/sonar/l10n/puppet/rules/puppet/" + rule.getKey() + ".html")
		}
	}
}