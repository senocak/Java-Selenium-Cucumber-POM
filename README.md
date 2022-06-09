# Cucumber Selenium
Automation Framework for Web Application tests using Cucumber and Selenium

## Project Description
This project is to implement Page Object Model pattern with Cucumber and Selenium Webdriver.

### Page Object Model Pattern:
- Page Object Model is a design pattern that creates Object Repository for web UI elements and it reduces code duplication and improves test maintenance.

## Execution Instructions
- `mvn -DHEADLESS=true -DBROWSER=edge verify -Dcucumber.filter.tags=@login`
- `mvn -DHEADLESS=false -DBROWSER=edge verify -Dcucumber.filter.tags=@login`
- `java -Dmaven.multiModuleProjectDirectory=C:\Users\asenocak\Desktop\calisma\Java-Selenium-Cucumber-POM "-Dmaven.home=C:\Program Files\JetBrains\IntelliJ IDEA 2022.1.1\plugins\maven\lib\maven3" "-Dclassworlds.conf=C:\Program Files\JetBrains\IntelliJ IDEA 2022.1.1\plugins\maven\lib\maven3\bin\m2.conf" "-Dmaven.ext.class.path=C:\Program Files\JetBrains\IntelliJ IDEA 2022.1.1\plugins\maven\lib\maven-event-listener.jar" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\JetBrains\IntelliJ IDEA 2022.1.1\plugins\maven\lib\maven3\boot\plexus-classworlds-2.6.0.jar;" org.codehaus.classworlds.Launcher -Didea.version=2022.1.2 -DHEADLESS=false -DBROWSER=edge verify -Dcucumber.filter.tags=@login`


### Reports Location:
After execution, reports can be accessible via the link displayed in the maven logs (provided by Cucumber latest version).

When this project is integrated in a Jenkins pipeline, _cucumber.json_ file in _target/cucumber-reports_ can be integrated in the Build using Jenkins Cucumber plugin



