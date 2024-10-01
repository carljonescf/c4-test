package uk.ac.cf.spring.c4checker.test.core;

import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;

    public TestServiceImpl(TestRepository testRepository) {

        this.testRepository = testRepository;
    }

    @Override
    public void go() {
        testRepository.go();
    }
}
