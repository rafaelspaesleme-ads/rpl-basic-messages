package br.com.rplbasicmessages.gateways.repositories;

import br.com.rplbasicmessages.gateways.entities.CredentialsDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository extends MongoRepository<CredentialsDocument, String> {
    Optional<CredentialsDocument> findByNicknameAndActive(String nickname, Boolean active);

    Optional<CredentialsDocument> findByNickname(String nickname);
}
