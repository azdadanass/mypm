package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Supplier;
import ma.azdad.model.SupplierStatus;

@Repository
public interface SupplierRepos extends JpaRepository<Supplier, Integer> {

	String constructor0 = "select new Supplier(a.id,a.name) ";
	String constructor1 = "select new Supplier(a.id,a.name,a.photo,a.active) ";
	String constructor2 = "select new Supplier(a.id,name,description,status,active,a.category) ";

	@Query(constructor0 + "from Supplier a")
	public List<Supplier> findLight();

	@Query(constructor1 + "from Supplier a")
	public List<Supplier> find();

	@Query(constructor1 + "from Supplier a where category.id = ?1")
	public List<Supplier> findByCategory(Integer supplierCategoryId);

	@Query("select distinct new Supplier(a.supplier.id,a.supplier.name,a.supplier.photo,a.supplier.active) from SupplierCompany a where a.company.id = ?1 and a.active is true and a.supplier.active = ?2 and a.supplier.status = ?3")
	public List<Supplier> findByCompany(Integer companyId, Boolean active, SupplierStatus status);

	@Query(constructor2 + "from Supplier a where a.status = ?1")
	List<Supplier> findByStatus(SupplierStatus status);

	@Query("select count(*) from Supplier a where a.status = ?1")
	Long countByStatus(SupplierStatus status);

	@Query(constructor2 + "from Supplier a where a.active is true and 0 = (select count(*) from BankAccount b where b.supplier.id = a.id and b.active is true)")
	List<Supplier> findWithouActiveBankAccount();

	@Query("select count(*) from Supplier a where a.active is true and 0 = (select count(*) from BankAccount b where b.supplier.id = a.id and b.active is true)")
	Long countWithouActiveBankAccount();

	@Query("select count(*) from Supplier where name = ?1 and (?2 is null or id != ?2)")
	public Long countByName(String name, Integer id);

	@Query("select count(*) from Supplier where email = ?1 and (?2 is null or id != ?2)")
	public Long countByEmail(String email, Integer id);

	@Query("select count(*) from Supplier where accountingCode = ?1 and (?2 is null or id != ?2)")
	public Long countByAccountingCode(String accountingCode, Integer id);

	@Query("select lastPaymentIndex from Supplier where id = ?1")
	public Integer getLastPaymentIndex(Integer id);

	@Modifying
	@Query("update Supplier set lastPaymentIndex = ?2 where id = ?1")
	public void updateLastPaymentIndex(Integer id, Integer lastPaymentIndex);

	@Query("select count(*) from SupplierAccountingCode where company.id = ?1 and accountingCode = ?2")
	Long countByCompanyAndAccountingCode(Integer companyId, Integer accountingCode);

	@Query("select count(*) from SupplierAccountingCode where company.id = ?1 and accountingCode = ?2 and id != ?3")
	Long countByCompanyAndAccountingCode(Integer companyId, Integer accountingCode, Integer id);

	// inplace
	@Modifying
	@Query("update Supplier set ibuyAccess = ?2 where id = ?1")
	void updateIbuyAccess(Integer id, Boolean ibuyAccess);

	@Modifying
	@Query("update Supplier set invoiceManagement = ?2 where id = ?1")
	void updateInvoiceManagement(Integer id, Boolean invoiceManagement);

	@Modifying
	@Query("update Supplier set receiptNotifications = ?2 where id = ?1")
	void updateReceiptNotifications(Integer id, Boolean receiptNotifications);

	@Modifying
	@Query("update Supplier set canViewComments = ?2 where id = ?1")
	void updateCanViewComments(Integer id, Boolean canViewComments);

	@Query("select max(accountingCode) from SupplierAccountingCode where company.id = ?1")
	Integer findLastAccountingCodeByCompany(Integer companyId);

}
