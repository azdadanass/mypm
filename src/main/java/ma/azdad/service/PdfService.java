package ma.azdad.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class PdfService {
	protected final Logger log = LoggerFactory.getLogger(PdfService.class);

}
