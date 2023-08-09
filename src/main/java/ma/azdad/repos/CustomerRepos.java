package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Customer;

@Repository
public interface CustomerRepos extends JpaRepository<Customer, Integer> {

	String constructor0 = "select new Customer(a.id,a.name) ";
	String constructor1 = "select new Customer(a.id,a.name,a.photo) ";
	String constructor2 = "select new Customer(a.id,name,description,a.category) ";

	@Query(constructor0 + "from Customer a where a.isCustomer is true")
	public List<Customer> findLight();

	@Query(constructor1 + "from Customer a")
	public List<Customer> find();

	@Query("select distinct id from Customer where manager.username = ?1")
	public List<Integer> findIdList(String managerUsername);

	@Query(constructor1 + "from Customer a where category.id = ?1")
	public List<Customer> findByCategory(Integer customerCategoryId);

	@Query("select distinct new Customer(a.customer.id,a.customer.name,a.customer.photo) from CustomerCompany a where a.company.id = ?1 and a.active is true")
	public List<Customer> findByCompany(Integer companyId);

	@Query(constructor2 + "from Customer a where 0 = (select count(*) from BankAccount b where b.customer.id = a.id and b.active is true)")
	List<Customer> findWithouActiveBankAccount();

	@Query("select count(*) from Customer a where 0 = (select count(*) from BankAccount b where b.customer.id = a.id and b.active is true)")
	Long countWithouActiveBankAccount();

	@Query("select count(*) from Customer where name = ?1 and (?2 is null or id != ?2)")
	public Long countByName(String name, Integer id);

	@Query("select count(*) from Customer where email = ?1 and (?2 is null or id != ?2)")
	public Long countByEmail(String email, Integer id);

	@Query("select count(*) from Customer where accountingCode = ?1 and (?2 is null or id != ?2)")
	public Long countByAccountingCode(String accountingCode, Integer id);

	@Query("select count(*) from CustomerAccountingCode where company.id = ?1 and accountingCode = ?2")
	Long countByCompanyAndAccountingCode(Integer companyId, Integer accountingCode);

	@Query("select count(*) from CustomerAccountingCode where company.id = ?1 and accountingCode = ?2 and id != ?3")
	Long countByCompanyAndAccountingCode(Integer companyId, Integer accountingCode, Integer id);

	// inplace
	@Modifying
	@Query("update Customer set invoiceAccess = ?2 where id = ?1")
	void updateInvoiceAccess(Integer id, Boolean invoiceAccess);

	@Modifying
	@Query("update Customer set invoiceManagement = ?2 where id = ?1")
	void updateInvoiceManagement(Integer id, Boolean invoiceManagement);

	@Modifying
	@Query("update Customer set receiptNotifications = ?2 where id = ?1")
	void updateReceiptNotifications(Integer id, Boolean receiptNotifications);

	@Modifying
	@Query("update Customer set canViewComments = ?2 where id = ?1")
	void updateCanViewComments(Integer id, Boolean canViewComments);

	@Query("select max(accountingCode) from CustomerAccountingCode where company.id = ?1")
	Integer findLastAccountingCodeByCompany(Integer companyId);

	@Query("select a.company.id from CustomerCompany a where a.customer.id = ?1")
	List<Integer> findCompanyIdList(Integer customerId);

	@Query("select max(code) from Customer where code is not null")
	Integer findMaxCode();

}
