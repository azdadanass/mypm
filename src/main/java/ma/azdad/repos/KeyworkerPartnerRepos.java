package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.KeyworkerPartner;

@Repository
public interface KeyworkerPartnerRepos extends JpaRepository<KeyworkerPartner, Integer>{

}
