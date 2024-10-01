package uk.ac.cf.spring.c4checker.test.driving;

import org.springframework.stereotype.Controller;
import uk.ac.cf.spring.c4checker.test.core.TestService;

/**
 * Controller for the test service.
 */
@Controller
public class TestController {

    /**
     * The test service.
     */
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    public String go(){
        testService.go();
        return "";
    }
}
