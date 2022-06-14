# Cucumber Selenium
Automation Framework for Web Application tests using Cucumber and Selenium

### BDD	-	Behavior-driven development
- An agile software development process that encourages collaboration among developers, qa testers and customer representatives in a software project.
- It encourages teams to use conversation and concrete examples to formalize a shared understanding of how the application should behave.
- It emerged from test-driven development (TDD)
- Although BDD is principally an idea about how software development should be managed by both business interests and technical insight, the practice of BDD does assume the use of specialized software tools to support the development process.
- Focuses on:
- Where to start in the process
- What to test and what not to test
- How much to test in one go
- What to call the tests
- How to understand why a test fails
* The hardest single part of building a software system is deciding precisely what to build.(Fred Brooks, The mythical man-month)

### Cucumber
- Software tool that supports behavior-driven development (BDD).
- Central to the Cucumber BDD approach is its ordinary language parser called Gherkin.
- It allows expected software behaviors to be specified in a logical language that customers can understand. As such, Cucumber allows the execution of feature documentation written in business-facing text.
- It is often used for testing other software.
- It runs automated acceptance tests written in a behavior-driven development (BDD)

### Gherkin
- Language that Cucumber uses to define test cases.
- It is designed to be non-technical and human-readable and collectively describes use cases relating to a software system.
- The purpose behind Gherkin's syntax is to promote behavior-driven development practices across an entire development team, including business analysts and managers.
- It seeks to enforce firm, unambiguous requirements starting in the initial phases of requirements definition by business management and in other stages of the development lifecycle.

### Page Object Model Pattern:
- Page Object Model is a design pattern that creates Object Repository for web UI elements and it reduces code duplication and improves test maintenance.

## Execution Instructions
- `mvn -DHEADLESS=true -DBROWSER=edge verify -Dcucumber.filter.tags=@login-fail`
- `mvn -DHEADLESS=false -DBROWSER=edge verify -Dcucumber.filter.tags=@login`
- `mvn -DHEADLESS=false -DBROWSER=edge verify -Dcucumber.filter.tags=@login`
- `java -Dmaven.multiModuleProjectDirectory=C:\Users\asenocak\Desktop\calisma\Java-Selenium-Cucumber-POM "-Dmaven.home=C:\Program Files\JetBrains\IntelliJ IDEA 2022.1.1\plugins\maven\lib\maven3" "-Dclassworlds.conf=C:\Program Files\JetBrains\IntelliJ IDEA 2022.1.1\plugins\maven\lib\maven3\bin\m2.conf" "-Dmaven.ext.class.path=C:\Program Files\JetBrains\IntelliJ IDEA 2022.1.1\plugins\maven\lib\maven-event-listener.jar" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\JetBrains\IntelliJ IDEA 2022.1.1\plugins\maven\lib\maven3\boot\plexus-classworlds-2.6.0.jar;" org.codehaus.classworlds.Launcher -Didea.version=2022.1.2 -DHEADLESS=false -DBROWSER=edge verify -Dcucumber.filter.tags=@login`

### Reports Location:
After execution, reports can be accessible via the link displayed in the maven logs (provided by Cucumber latest version).