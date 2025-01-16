package br.com.rplbasicmessages.gateways.repositories;

import br.com.rplbasicmessages.gateways.entities.ChatDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<ChatDocument, String> {
    List<ChatDocument> findByNicknameAndActive(String nickname, Boolean active);

    List<ChatDocument> findByNicknameAndNicknameDestinationAndActive(String nickname, String nicknameDestination, Boolean active);
    List<ChatDocument> findByNicknameAndNicknameDestination(String nickname, String nicknameDestination);

    boolean existsByNickname(String nickname);

    boolean existsByNicknameDestination(String nicknameDestination);
}
