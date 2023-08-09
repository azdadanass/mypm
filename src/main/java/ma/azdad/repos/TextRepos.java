package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Text;

@Repository
public interface TextRepos extends JpaRepository<Text, Integer> {

	@Query("from Text where app = ?1")
	List<Text> find(String app);

	@Query("select distinct value from Text where app = ?1 and beanName = ?2")
	List<String> findValueList(String app, String beanName);

	@Query("select distinct value from Text where app = ?1 and beanName = ?2 and type = ?3")
	List<String> findValueList(String app, String beanName, String type);

}
