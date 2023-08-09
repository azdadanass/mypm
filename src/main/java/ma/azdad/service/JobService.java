package ma.azdad.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobService {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	DelegationService delegationService;

	@Scheduled(cron = "00 25 04 * * *")
	public void updateDelegationStatus() {
		delegationService.updateStatus();
	}

}