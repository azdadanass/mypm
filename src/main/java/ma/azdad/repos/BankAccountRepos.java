package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.BankAccount;

@Repository
public interface BankAccountRepos extends JpaRepository<BankAccount, Integer> {

	@Query("from BankAccount where company.id = ?1")
	List<BankAccount> findByCompany(Integer companyId);

	@Query("from BankAccount where supplier.id = ?1")
	List<BankAccount> findBySupplier(Integer supplierId);

	@Query("select new BankAccount(id,concat(a.company.name,',',a.name,',',a.currency.name)) from BankAccount a where a.company.id = ?1")
	List<BankAccount> findLightByCompany(Integer companyId);

	@Query("select new BankAccount(id,concat(a.supplier.name,',',a.name,',',a.currency.name)) from BankAccount a where a.supplier.id = ?1 and a.active = ?2 ")
	List<BankAccount> findLightBySupplier(Integer supplierId, Boolean active);

	@Query("SELECT a.id,concat(a.company.name,',',a.name,',',a.currency.name) FROM BankAccount a where a.company is not null")
	List<Object[]> getFullNameMap();

	@Query("from BankAccount where company is not null and fullName is null")
	List<BankAccount> findWithoutFullName();

}
