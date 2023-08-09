package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Currency;

@Repository
public interface CurrencyRepos extends JpaRepository<Currency, Integer> {

	@Query("select count(*) from Currency where name = ?1 and (?2 is null or id != ?2)")
	Long countByName(String name, Integer id);

	Currency findByName(String name);

}
