package br.com.rplbasicmessages.gateways.repositories;

import br.com.rplbasicmessages.gateways.entities.LoginDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends MongoRepository<LoginDocument, String> {
    Optional<LoginDocument> findByNickname(String nickname);

    Optional<LoginDocument> findByToken(String token);
}
