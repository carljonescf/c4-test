package uk.ac.cf.spring.c4checker.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
