package ma.azdad.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.aop.LogExecutionTime;
import ma.azdad.model.BankAccount;
import ma.azdad.model.CompanyType;
import ma.azdad.model.User;
import ma.azdad.repos.UserDataRepos;
import ma.azdad.repos.UserRepos;
import ma.azdad.repos.UserRoleRepos;

@Component
@Transactional
public class UserService {

	@Autowired
	private UserRepos repos;

	@Autowired
	private UserRoleRepos userRoleRepos;

	@Autowired
	private UserDataRepos userDataRepos;

	@Autowired
	private CacheService cacheService;

	@LogExecutionTime
	public User findOne(String username) {
		User u = repos.findById(username).get();
		Hibernate.initialize(u.getUser());
		Hibernate.initialize(u.getCompany());
		Hibernate.initialize(u.getCustomer());
		Hibernate.initialize(u.getSupplier());
		Hibernate.initialize(u.getAffectation());
		Hibernate.initialize(u.getRoleList());
		Hibernate.initialize(u.getHistoryList());
		Hibernate.initialize(u.getFileList());
		return u;
	}

	public User findOneLight(String username) {
		return repos.findById(username).get();
	}

	public void updatePassword(String username, String password) {
		repos.updatePassword(username, password);
	}

//	@Cacheable("userService.find")
//	public List<User> find() {
//		return repos.find();
//	}

	@Cacheable("userService.find")
	public List<User> find(Boolean active) {
		if (active == null)
			return repos.find();
		return repos.find(active);
	}

	public User saveAndRefresh(User a) {
		save(a);
		return findOne(a.getUsername());
	}

	@Cacheable("userService.findLight")
	public List<User> findLight() {
		return repos.findLight();
	}

	@Cacheable("userService.findLight")
	public List<User> findLight(Boolean internal, Boolean active) {
		return repos.findLight(internal, active);
	}

	public List<User> findLightByInternalAndActive() {
		return findLight(true, true);
	}

	public List<User> findLightByExternalAndActive() {
		return findLight(true, true);
	}

	// don't cache !
	public List<User> findLight(User user) {
		return repos.findLight(user.getLogin(), user.getFirstName(), user.getLastName(), user.getActive(), user.getContractActive(), user.getInternal());
	}

	public User findByEmail(String email) {
		return repos.findByEmail(email);
	}

	public User findByPhone(String phone) {
		return repos.findByPhone(phone);
	}

	public Long countByEmail(String email) {
		return repos.countByEmail(email);
	}

	@Cacheable("userService.findAsMap")
	public Map<String, User> findAsMap() {
		return repos.find().stream().collect(Collectors.toMap(i -> i.getUsername(), i -> i));
	}

	@Cacheable("userService.findLightByStatus")
	public List<User> findLightByStatus(Boolean active) {
		return repos.findLightByStatus(active);
	}

	@Cacheable("userService.findByLineManager")
	public List<User> findByLineManager(String lineManagerUsername, Boolean active) {
		if (active == null)
			return repos.findByLineManager(lineManagerUsername);
		return repos.findByLineManager(lineManagerUsername, active);
	}

	public User save(User a) {
		evictCache();
		a.getUserData().setInternal(a.getInternal());
		a.getUserData().setFirstName(a.getFirstName());
		a.getUserData().setLastName(a.getLastName());
		a.getUserData().setFullName(a.getFullName());
		a.getUserData().setEmail(a.getEmail());
		a.getUserData().setEmail2(a.getEmail2());
		a.getUserData().setPhone(a.getPhone());
		a.getUserData().setCin(a.getCin());
		a.getUserData().setGender(a.getGender() ? "Male" : "Female");
		a.getUserData().setBirthday(a.getBirthday());
		a.setStatus(a.getActive() ? "Active" : "Inactive");
		// TODO to coorect
		// return repos.save(a);
		BankAccount ba = a.getBankAccount();
		a.setBankAccount(null);
		a = repos.save(a);
		a.setBankAccount(ba);
		return repos.save(a);

	}

	public void generateUserDataId() {
		int i = 1;
		for (User user : repos.findAll())
			userDataRepos.updateId(user.getUsername(), i++);
	}

	public void evictCache() {
		cacheService.evictCache("userService");
		cacheService.evictCacheOthers("userService");
	}

	public void initialize(Object proxy) {
		Hibernate.initialize(proxy);
	}

	public void delete(String username) throws DataIntegrityViolationException, Exception {
		evictCache();
		repos.deleteById(username);
	}

	public void delete(User user) throws DataIntegrityViolationException, Exception {
		evictCache();
		repos.delete(user);
	}

	public Long countByUsername(String username) {
		return repos.countByUsername(username);
	}
	
	public List<User> findLightByStatu(Boolean active) {
		return repos.findLightByStatu(active);
	}

	public Long countByLogin(String login) {
		return repos.countByLogin(login);
	}

	public Long countByLogin(String name, String username) {
		return repos.countByLogin(name, username);
	}

	public Long countByActiveAndFirstNameAndLastNameAndNotBirthday(User user) {
		return repos.countByActiveAndFirstNameAndLastNameAndNotBirthday(user.getFirstName(), user.getLastName(), user.getBirthday(), user.getUsername());
	}

	public Long countByFirstNameAndLastNameAndBirthdayAndInternal(User user) {
		return repos.countByFirstNameAndLastNameAndBirthdayAndInternal(user.getFirstName(), user.getLastName(), user.getBirthday(), user.getUsername());
	}

	public Long countByFirstNameAndLastNameAndBirthdayAndExternalAndCompany(User user) {
		switch (user.getCompanyType()) {
		case CUSTOMER:
			return repos.countByFirstNameAndLastNameAndBirthdayAndExternalAndCustomer(user.getFirstName(), user.getLastName(), user.getBirthday(), user.getCustomerId(), user.getUsername());
		case SUPPLIER:
			return repos.countByFirstNameAndLastNameAndBirthdayAndExternalAndSupplier(user.getFirstName(), user.getLastName(), user.getBirthday(), user.getSupplierId(), user.getUsername());
		case OTHER:
			return repos.countByFirstNameAndLastNameAndBirthdayAndExternalAndOther(user.getFirstName(), user.getLastName(), user.getBirthday(), user.getOther(), user.getUsername());
		default:
			return 0l;
		}
	}

	public Long countByEmailAndActive(User user) {
		return repos.countByEmailAndActive(user.getEmail(), user.getUsername());
	}

	public Long countByPhoneAndActive(User user) {
		return repos.countByPhoneAndActive(user.getPhone(), user.getUsername());
	}

	public void updateActiveByCin(Boolean active, String cin) {
		evictCache();
		repos.updateActiveByCin(active, cin);
	}

	public void updateContractActive() {
		evictCache();
		repos.updateContractActive();
	}

//	public void copyAuthorities() {
//		if (userRoleRepos.count() > 0)
//			return;
//		authoritiesRepos.findAll().stream().map(a -> new UserRole(a.getUser(), Role.valueOf(a.getId().getAuthority()))).forEach(ur -> userRoleRepos.save(ur));
//		repos.findAll().forEach(u -> {
//			u.addRole(Role.ROLE_APPS);
//			repos.save(u);
//		});
//	}

	public String generateNewUsername(String firstName, String lastName, Boolean internal) {
		if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName))
			return null;
		firstName = UtilsFunctions.formatName(firstName).toLowerCase();
		lastName = UtilsFunctions.formatName(lastName).toLowerCase();
		String username = (internal ? "" : "e.") + firstName.charAt(0) + "." + lastName.replace(" ", "");
		if (countByUsername(username) == 0 && countByLogin(username) == 0)
			return username;
		else {
			int i = 1;
			while (true) {
				String usernamePrime = username + i;
				if (countByUsername(usernamePrime) == 0 && countByLogin(usernamePrime) == 0)
					return usernamePrime;
				i++;
			}
		}
	}

	// security

	public static int MAX_FAILED_ATTEMPTS;

	public static long LOCK_TIME_DURATION;

	@Value("${MAX_FAILED_ATTEMPTS}")
	public void setMaxFailedAttempts(int maxFailedAttempts) {
		MAX_FAILED_ATTEMPTS = maxFailedAttempts;
	}

	@Value("${LOCK_TIME_DURATION}")
	public void setLockTimeDuration(long lockTimeDuration) {
		LOCK_TIME_DURATION = lockTimeDuration;
	}

	public User findByLogin(String login) {
		User u = repos.findByLogin(login);
		if (u != null)
			Hibernate.initialize(u.getRoleList());
		return u;
	}

	public void increaseFailedAttempts(User user) {
		int newFailAttempts = user.getFailedAttempt() + 1;
		repos.updateFailedAttempts(newFailAttempts, user.getLogin());
	}

	public void resetFailedAttempts(String login) {
		repos.updateFailedAttempts(0, login);
	}

	public void lock(User user) {
		user.setAccountNonLocked(false);
		user.setLockTime(new Date());
		repos.save(user);
	}

	public boolean unlockWhenTimeExpired(User user) {
		long lockTimeInMillis = user.getLockTime().getTime();
		long currentTimeInMillis = System.currentTimeMillis();
		if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
			user.unlock();
			repos.save(user);
			return true;
		}
		return false;
	}

	public List<String> findUsernameListByCustomerOrSupplier(CompanyType companyType, Integer customerId, Integer supplierId) {
		switch (companyType) {
		case CUSTOMER:
			return repos.findUsernameListByCustomer(customerId);
		case SUPPLIER:
			return repos.findUsernameListBySupplier(supplierId);
		default:
			return new ArrayList<String>();
		}

	}

	public List<String> findUsernameListByCustomerOrSupplier(User user) {
		return findUsernameListByCustomerOrSupplier(user.getCompanyType(), user.getCustomerId(), user.getSupplierId());
	}

	@Cacheable("userService.findLightByCustomerOrSupplier")
	public List<User> findLightByCustomerOrSupplier(Integer customerId, Integer supplierId, Boolean active) {
		return repos.findLightByCustomerOrSupplier(customerId, supplierId, active);
	}

	@Cacheable("userService.findLightByCustomerOrSupplier")
	public List<User> findLightByCustomerOrSupplier(Integer customerId, Integer supplierId) {
		return repos.findLightByCustomerOrSupplier(customerId, supplierId);
	}

	@Cacheable("userService.findByCustomer")
	public List<User> findByCustomer(Integer customerId) {
		return repos.findByCustomer(customerId);
	}

	@Cacheable("userService.findBySupplier")
	public List<User> findBySupplier(Integer supplierId) {
		return repos.findBySupplier(supplierId);
	}
}
