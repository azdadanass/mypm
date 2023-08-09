package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.DocType;

@Repository
public interface DocTypeRepos extends JpaRepository<DocType, Integer> {

	@Query("select name from DocType where app = ?1 and type = ?2")
	public List<String> findByAppAndType(String app, String type);

	@Query("select name from DocType where app = ?1 and type = ?2 and filter = ?3")
	public List<String> findByAppAndType(String app, String type, Integer filter);

}
