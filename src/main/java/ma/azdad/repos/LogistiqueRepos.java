package ma.azdad.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Logistique;

@Repository
public interface LogistiqueRepos extends JpaRepository<Logistique, Integer> {

}
