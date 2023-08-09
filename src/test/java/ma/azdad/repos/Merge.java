package ma.azdad.repos;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import ma.azdad.GenericTest;
import ma.azdad.service.UserService;;

@Rollback(false)
public class Merge extends GenericTest {

	@Autowired
	UserService us;

	@Autowired
	UserDataRepos udr;

	@Test
	@Transactional
	public void test1() throws Exception {
		// launch before.sql
//		us.copyAuthorities();
//		ers.exportToUserScript();
//		mers.exportToUserScript();
		// launch after.sql
	}

}
