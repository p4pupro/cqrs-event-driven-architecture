package es.dperez.query.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"},
    features = "src/test/resources/features",
    glue = "es.dperez.query.cucumber",
    stepNotifications = true,
    publish = true
)
public class CucumberIT {

}
