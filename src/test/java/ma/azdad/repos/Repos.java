package ma.azdad.repos;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import ma.azdad.GenericTest;
import ma.azdad.service.DelegationService;;

@Rollback(false)
public class Repos extends GenericTest {

	@Autowired
	DelegationService delegationService;
	
	@Autowired
	AppraisalsRepos sr;
	
	@Test
	@Transactional
	public void test1() throws Exception {

		//delegationService.splitDelegationPerTypeScript();
		System.out.println(sr.findAll().get(0));
	}

}
