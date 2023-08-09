package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.AccessLog;

@Repository
public interface AccessLogRepos extends JpaRepository<AccessLog, Integer> {

	@Query("from AccessLog order by date desc")
	List<AccessLog> findAllOrderByDateDesc();

	@Query(nativeQuery = true, value = "select * from access_log where message like ?1 order by date desc limit 0, 50;")
	List<AccessLog> findLastByLogin(String likeLogin);

}
