/*
 * SonarQube Puppet Plugin
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Iain Adams and David RACODON
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
package com.iadams.sonarqube.puppet.parser.compound_statements

import com.iadams.sonarqube.puppet.parser.GrammarSpec

import static com.iadams.sonarqube.puppet.api.PuppetGrammar.CASE_STMT
import static org.sonar.sslr.tests.Assertions.assertThat

public class CaseStatement extends GrammarSpec {

  def setup() {
    setRootRule(CASE_STMT)
  }

  def "case statement parses correctly"() {
    expect:
    assertThat(p).matches('''case $operatingsystem {
		  'Solaris':          { include role::solaris } # apply the solaris class
		}''')
  }

  def "case with regex option"() {
    expect:
    assertThat(p).matches('''case $operatingsystem {
		  'Solaris':          { include role::solaris } # apply the solaris class
		  /^(Debian|Ubuntu)$/:{ include role::debian  } # apply the debian class
		  default:            { include role::generic } # apply the generic class
		}''')
  }

  def "case with list of options"() {
    expect:
    assertThat(p).matches('''case $operatingsystem {
		  'Solaris':          				{ include role::solaris } # apply the solaris class
		  true, false, 'RedHat', 'CentOS':  { include role::redhat  } # apply the redhat class
		  default:            				{ include role::generic } # apply the generic class
		}''')
  }

  def "case statement with default"() {
    expect:
    assertThat(p).matches('''case $::osfamily {
								  'debian': {
									$access_log_file      = 'access.log\'
								  } 'redhat': {
									$access_log_file      = 'access_log\'
								  }
								  default: {
									fail("Unsupported osfamily ${::osfamily}")
								  }
								}''')
  }

  def "case values accept appropriate values"(){
    expect:
    assertThat(p).matches('''case $osver[0] {
                               default: { }
                              }''')
    assertThat(p).matches('''case $::lsbmajdistrelease {
                               5, 6: {
                                  $os_rel = $::lsbmajdistrelease
                               }
                               default: {
                                 $os_rel = 6
                               }
                            }''')
  }
}
