package ma.azdad.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Appraisals;
import ma.azdad.model.User;

@Repository
public interface UserRepos extends JpaRepository<User, String> {

	String lobName = "(select b.name from Lob b where b.id = a.affectation.lob.id)";
	String companyName = "(select b.name from Company b where b.id = a.company.id)";
	String customerName = "(select b.name from Customer b where b.id = a.customer.id)";
	String supplierName = "(select b.name from Supplier b where b.id = a.supplier.id)";
	String c1 = "select new User(a.username,a.fullName) ";
	String c2 = "select new User(a.username,a.login,a.internal,a.fullName,a.photo,a.email,a.job,a.phone,a.cin,a.active,a.contractActive,a.countFiles," //
			+ lobName + ",a.companyType," + companyName + "," + customerName + "," + supplierName + ",a.other,(select count(*) from User b,Affectation c where b.username = c.user.username and c.lineManager.username = a.username),(select count(*) from Project b where b.manager.username = a.username)) ";

	@Query(c2 + "from User a")
	List<User> find();

	@Query(c2 + "from User a where a.active = ?1")
	List<User> find(Boolean active);

	@Query(c2 + "from User a,Affectation b where a.username = b.user.username and b.lineManager.username = ?1 ")
	List<User> findByLineManager(String lineManagerUsername);

	@Query(c2 + "from User a,Affectation b where a.username = b.user.username and b.lineManager.username = ?1 and active = ?2 ")
	List<User> findByLineManager(String lineManagerUsername, Boolean active);

	@Query(c2 + "from User a where (?1 is null or login like ?1) and (?2 is null or firstName like ?2) and (?3 is null or lastName like ?3) and (?4 is null or active = ?4) and  (?5 is null or contractActive = ?5) and  (?6 is null or internal = ?6)")
	List<User> findLight(String login, String firstName, String lastName, Boolean active, Boolean contractActive, Boolean internal);

	@Query(c1 + "from User a")
	List<User> findLight();

	@Query(c1 + "from User a")
	List<User> findLight(Boolean internal);

	@Query(c1 + "from User a where a.internal = ?1 and a.active = ?2")
	List<User> findLight(Boolean internal, Boolean active);

	@Query("select new User(user.username,user.fullName) from UserRole where role = ?1")
	List<User> findLightByRole(String role);

	@Query(c1 + "from User a where username in (?1)")
	List<User> findLightByUsernameList(List<String> list);

	@Query(c1 + "from User a,Affectation b where a.username = b.user.username and b.lineManager.username = ?1 and a.active = ?2")
	List<User> findLightByLineManagerAndStatus(String lineManagerUsername, Boolean active);

	@Query(c1 + "from User a where a.active = ?1")
	List<User> findLightByStatus(Boolean active);

	@Query(c1 + "from User a where (a.customer is not null and a.customer.id = ?1) or (a.supplier is not null and a.supplier.id = ?2) ")
	List<User> findLightByCustomerOrSupplier(Integer customerId, Integer supplierId);

	@Query(c1 + "from User a where (a.customer is not null and a.customer.id = ?1) or (a.supplier is not null and a.supplier.id = ?2) and a.active = ?3")
	List<User> findLightByCustomerOrSupplier(Integer customerId, Integer supplierId, Boolean active);

	@Query("select a.username from User a where a.customer.id = ?1")
	List<String> findUsernameListByCustomer(Integer customerId);

	@Query("select a.username from User a where a.supplier.id = ?1")
	List<String> findUsernameListBySupplier(Integer supplierd);

	User findByEmail(String email);

	User findByPhone(String phone);

	Long countByEmail(String email);

	Long countByUsername(String username);

	Long countByLogin(String login);

	User findByErId(Integer erId);

	@Query("select count(*) from User where firstName = ?1 and lastName = ?2 and birthday = ?3 and internal is true and (?4 is null or username != ?4)")
	Long countByFirstNameAndLastNameAndBirthdayAndInternal(String firstName, String lastName, Date birthday, String username);

	@Query("select count(*) from User where firstName = ?1 and lastName = ?2 and birthday = ?3 and internal is false and customer.id = ?4 and (?5 is null or username != ?5)")
	Long countByFirstNameAndLastNameAndBirthdayAndExternalAndCustomer(String firstName, String lastName, Date birthday, Integer customerId, String username);

	@Query("select count(*) from User where firstName = ?1 and lastName = ?2 and birthday = ?3 and internal is false and supplier.id = ?4 and (?5 is null or username != ?5)")
	Long countByFirstNameAndLastNameAndBirthdayAndExternalAndSupplier(String firstName, String lastName, Date birthday, Integer supplierId, String username);

	@Query("select count(*) from User where firstName = ?1 and lastName = ?2 and birthday = ?3 and internal is false and other = ?4 and (?5 is null or username != ?5)")
	Long countByFirstNameAndLastNameAndBirthdayAndExternalAndOther(String firstName, String lastName, Date birthday, String other, String username);

	@Query("select count(*) from User where active is true and firstName = ?1 and lastName = ?2 and birthday != ?3 and (?4 is null or username != ?4)")
	Long countByActiveAndFirstNameAndLastNameAndNotBirthday(String firstName, String lastName, Date birthday, String username);

	@Query("select count(*) from User where login = ?1 and (?2 is null or username != ?2)")
	Long countByLogin(String name, String username);

	@Query("select count(*) from User where email = ?1 and active is true and (?2 is null or username != ?2)")
	Long countByEmailAndActive(String email, String username);

	@Query("select count(*) from User where phone = ?1 and active is true and (?2 is null or username != ?2)")
	Long countByPhoneAndActive(String phone, String username);

	@Modifying
	@Query("update User set password = ?2 where username = ?1")
	void updatePassword(String username, String password);

	@Modifying
	@Query("update User set active = ?1 where cin = ?2")
	void updateActiveByCin(Boolean active, String cin);

	@Modifying
	@Query("update User a set a.contractActive = (case when (select b.status from UserData b where b.user.username = a.username) = 'ACTIVE' then true else false end)")
	void updateContractActive();

	// security
	User findByLogin(String login);

	@Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.login = ?2")
	@Modifying
	void updateFailedAttempts(int failAttempts, String login);

	@Query(c2 + "from User a where a.customer.id = ?1")
	List<User> findByCustomer(Integer customerId);

	@Query(c2 + "from User a where a.supplier.id = ?1")
	List<User> findBySupplier(Integer supplierId);
	


}
