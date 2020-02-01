package java.lang;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ThreadLocalTest {

    private ThreadLocal<String> local = new ThreadLocal<>();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void test(){
        local.set("NO1");

        String s = local.get();
        assertEquals("NO1", s);
    }

}