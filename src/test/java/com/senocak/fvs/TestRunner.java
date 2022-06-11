package com.senocak.fvs;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {
        "src/test/resources/features"
    },
    plugin = {
        "pretty",
        "json:target/cucumber-reports/cucumber.json",
        "html:target/cucumber-reports/cucumber.html"
    },
    publish = true,
    glue = {
        "com.senocak.fvs.stepdefinitions"
    },
    dryRun = true
)
public class TestRunner {}
