#


# Set up maven binary, also an alias for skipTests.

notest=false
MAVEN_PARALLELISM=4

ifneq ($(notest), false)
	MVN=mvn3 -T$(MAVEN_PARALLELISM) -DskipTests
else
	MVN=mvn3 -T$(MAVEN_PARALLELISM)
endif

all: install

#
# Test targets
#

dmicroblogger: microblogger
	/opt/tomcat/bin/kill.sh
	rm -rf /opt/tomcat/webapps/microblogger*
	cp samples/sample-hibernate-thymeleaf/target/*.war /opt/tomcat/webapps/microblogger.war
	/opt/tomcat/bin/start.sh

microblogger:
	$(MVN) clean package --projects samples/sample-hibernate-thymeleaf


#
#
# Standard Maven targets
#
#

compile:
	$(MVN) clean compile

dependencies:
	$(MVN) clean dependency:tree

package:
	$(MVN) clean package

install:
	$(MVN) clean install

eclipse:
	$(MVN) eclipse:clean eclipse:eclipse -DdownloadSources=true -DdownloadJavadocs=true

clean:
	$(MVN) clean

release:
	$(MVN) clean
	$(MVN) release:prepare -DautoVersionSubmodules=true
	$(MVN) release:perform
	rm -rf target/checkout