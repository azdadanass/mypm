package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.AccountingSystem;

@Repository
public interface AccountingSystemRepos extends JpaRepository<AccountingSystem, Integer> {

	@Query("select count(*) from AccountingSystem a where a.company.id = ?1 and a.numero = ?2")
	Long countByCompanyAndNumero(Integer companyId, Integer numero);

}
