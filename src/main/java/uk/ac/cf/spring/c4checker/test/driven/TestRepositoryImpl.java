package uk.ac.cf.spring.c4checker.test.driven;

import org.springframework.stereotype.Repository;
import uk.ac.cf.spring.c4checker.test.core.TestRepository;

@Repository
public class TestRepositoryImpl implements TestRepository {
    @Override
    public void go() {
        // Do something
    }
}
