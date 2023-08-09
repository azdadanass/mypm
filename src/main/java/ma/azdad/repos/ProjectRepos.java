package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Project;

@Repository
public interface ProjectRepos extends JpaRepository<Project, Integer> {

	String c1 = "select new Project(a.id,a.name,a.type,a.manager.fullName) ";
	String c2 = "select new Project(a.id,a.name,a.startDate,a.endDate,a.status,a.type,a.category,a.subType,a.countFiles,a.costcenter.lob.name,a.costcenter.year,a.costcenter.lob.bu.company.name,a.currency.name,a.manager.fullName) ";

	@Query(c1 + "FROM Project a ")
	List<Project> findLight();

	@Query(c2 + "from Project a order by id desc")
	List<Project> find();

	@Query(c2 + "from Project a where status = ?1 order by id desc")
	List<Project> find(String status);

	@Query(c2 + "from Project a where manager.username = ?1 or a.id in (select b.project.id from ProjectManager b where b.user.username = ?1) order by id desc")
	List<Project> findByUser(String username);

	@Query(c2 + "from Project a where manager.username = ?1 order by id desc")
	List<Project> findByManager(String managerUsername);

	@Query(c2 + "from Project a where manager.username = ?1 and status = ?2 order by id desc")
	List<Project> findByManager(String managerUsername, String status);

	@Query(c2 + "from Project a where costcenter.id = ?1 order by id desc")
	List<Project> findByCostcenter(Integer costcenterId);

	@Query("select new Project(id,name) from Project where manager.username = ?1 and status = ?2")
	List<Project> findLightByManagerAndStatus(String managerUsername, String status);

	@Query(c2 + "from Project a where 0 = (select count(*) from Task b where b.project.id = a.id) order by id desc")
	List<Project> findWithoutTasks();

	@Query("select count(*) from Project a where 0 = (select count(*) from Task b where b.project.id = a.id)")
	Long countWithoutTasks();

	@Query("select count(*) from Project where name = ?1 and (?2 is null or id != ?2)")
	Long countByName(String name, Integer id);

	@Modifying
	@Query("update Project set sdm = ?2 where id = ?1")
	void updateSdm(Integer projectId, Boolean sdm);

	@Modifying
	@Query("update Project set customerWarehousing = ?2 where id = ?1")
	void updateCustomerWarehousing(Integer projectId, Boolean customerWarehousing);

	@Modifying
	@Query("update Project set customerStockManagement = ?2 where id = ?1")
	void updateCustomerStockManagement(Integer projectId, Boolean customerStockManagement);

	@Modifying
	@Query("update Project set companyWarehousing = ?2 where id = ?1")
	void updateCompanyWarehousing(Integer projectId, Boolean companyWarehousing);

	@Modifying
	@Query("update Project set companyStockManagement = ?2 where id = ?1")
	void updateCompanyStockManagement(Integer projectId, Boolean companyStockManagement);

	@Modifying
	@Query("update Project set supplierWarehousing = ?2 where id = ?1")
	void updateSupplierWarehousing(Integer projectId, Boolean supplierWarehousing);

	@Modifying
	@Query("update Project set supplierStockManagement = ?2 where id = ?1")
	void updateSupplierStockManagement(Integer projectId, Boolean supplierStockManagement);
	
	@Query("from Project where companyWarehousing is true or companyStockManagement is true or supplierWarehousing is true or supplierStockManagement is true or customerWarehousing is true or customerStockManagement is true")
	List<Project> findIlogisticsProjectList();

}
