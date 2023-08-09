package ma.azdad;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenericTest {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	protected long before, after;

	@Before
	public void before() {
		System.out.println("\n-------------------------------------------------------------------\n\n\n");
		start();
	}

	
	
	public void duration() {
		after = System.currentTimeMillis();
		System.out.println("\n\nTemps: " + Long.toString(after - before) + " ms");
	}

	@After
	public void after() {
		System.out.println("\n\n\n-------------------------------------------------------------------\n");
		duration();
	}

	public void start() {
		before = System.currentTimeMillis();
	}
}