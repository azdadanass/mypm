package ma.azdad.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTestController {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/rest/test")
	public String test() {
		log.info("rest working");
		return "rest working";
	}

}
