package br.com.rplbasicmessages.gateways.repositories;

import br.com.rplbasicmessages.gateways.entities.ProfileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends MongoRepository<ProfileDocument, String> {
    Optional<ProfileDocument> findByNicknameAndActive(String nickname, Boolean active);
}
