package br.com.rplbasicmessages.gateways;

import br.com.rplbasicmessages.commons.enums.ETypeManipulationMessage;
import br.com.rplbasicmessages.commons.exceptions.RplMessageBusinessException;
import br.com.rplbasicmessages.commons.exceptions.RplMessageNotFoundException;
import br.com.rplbasicmessages.gateways.entities.ChatDocument;
import br.com.rplbasicmessages.gateways.repositories.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessagesGateway {

    private final ChatRepository chatRepository;

    @Transactional
    public List<ChatDocument> send(ChatDocument document) {
        try {
            document.setView(false);
            document.setFixed(false);
            document.setEdited(false);
            document.setActive(true);
            document.setDatetimeSendMessage(LocalDateTime.now());
            chatRepository.save(document);

            return getMessagesActive(document.getNickname(), document.getNicknameDestination());

        } catch (Exception e) {
            throw new RplMessageBusinessException("Não foi possivel enviar mensagem.", e.getCause(), this.getClass());
        }
    }

    @Transactional
    public List<ChatDocument> editMessage(ChatDocument document) {
        try {
            document.setView(false);
            document.setEdited(true);
            document.setDatetimeEditedMessage(LocalDateTime.now());
            chatRepository.save(document);

            return getMessagesActive(document.getNickname(), document.getNicknameDestination());

        } catch (Exception e) {
            throw new RplMessageBusinessException("Não foi possivel enviar mensagem.", e.getCause(), this.getClass());
        }
    }

    @Transactional
    public List<ChatDocument> manipulateMessage(String id, ETypeManipulationMessage action) {

        try {
            AtomicReference<ChatDocument> chatDocument = new AtomicReference<>(new ChatDocument());

            chatRepository.findById(id)
                    .ifPresentOrElse(validationActionManipulationMessage(action, chatDocument), () -> {
                        throw new RplMessageNotFoundException("Mensagem não existe.", this.getClass());
                    });

            return getMessagesActive(chatDocument.get().getNickname(), chatDocument.get().getNicknameDestination());
        } catch (Exception e) {
            throw new RplMessageBusinessException(Optional.ofNullable(e.getMessage()).orElse("Não foi possivel arquivar mensagem"), e.getCause(), this.getClass());
        }
    }

    private Consumer<ChatDocument> validationActionManipulationMessage(ETypeManipulationMessage action, AtomicReference<ChatDocument> chatDocument) {
        return document -> {

            if (ETypeManipulationMessage.ARCHIVE.name().equals(action.name())) {
                document.setActive(false);
            }

            if (ETypeManipulationMessage.UNARCHIVE.name().equals(action.name())) {
                document.setActive(true);
            }

            if (ETypeManipulationMessage.VIEW.name().equals(action.name())) {
                document.setView(true);
            }

            if (ETypeManipulationMessage.FIXED.name().equals(action.name())) {
                document.setFixed(true);
            }

            chatRepository.save(document);
            chatDocument.set(document);
        };
    }

    @Transactional
    public List<ChatDocument> deleteMessage(String id) {
        try {

            ChatDocument document = chatRepository.findById(id)
                    .orElseThrow(() -> new RplMessageNotFoundException("Mensagem não encontrada.", this.getClass()));

            chatRepository.delete(document);

            return getMessagesActive(document.getNickname(), document.getNicknameDestination());

        } catch (Exception e) {
            throw new RplMessageBusinessException(Optional.ofNullable(e.getMessage()).orElse("Não foi possivel deletar mensagem."), this.getClass());
        }
    }

    public List<ChatDocument> getMessagesActive(String nickname, String nicknameDestination) {
        return getMessages(nickname, nicknameDestination, true);

    }

    public List<ChatDocument> getArchiveMessages(String nickname, String nicknameDestination) {
        return getMessages(nickname, nicknameDestination, false);
    }

    public List<ChatDocument> getMessages(String nickname, String nicknameDestination, Boolean active) {
        return findMessagesOptional(nickname, nicknameDestination, active)
                .stream()
                .sorted(Comparator.comparing(ChatDocument::getFixed, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<ChatDocument> findMessagesOptional(String nickname, String nicknameDestination, Boolean active) {

        try {
            if (Objects.isNull(active)) {
                List<ChatDocument> messagesByNickname = chatRepository.findByNicknameAndNicknameDestination(nickname, nicknameDestination);
                List<ChatDocument> messagesByNicknameDestination = chatRepository.findByNicknameAndNicknameDestination(nicknameDestination, nickname);


                return validationHistoricMessage(messagesByNickname, messagesByNicknameDestination);
            }

            List<ChatDocument> messagesByNickname = chatRepository.findByNicknameAndNicknameDestinationAndActive(nickname, nicknameDestination, active);
            List<ChatDocument> messagesByNicknameDestination = chatRepository.findByNicknameAndNicknameDestinationAndActive(nicknameDestination, nickname, active);

            return validationHistoricMessage(messagesByNickname, messagesByNicknameDestination);
        } catch (Exception e) {
            throw new RplMessageBusinessException("Não foi possivel retornar mensagens.", e.getCause(), this.getClass());
        }
    }

    private static List<ChatDocument> validationHistoricMessage(List<ChatDocument> messagesByNickname, List<ChatDocument> messagesByNicknameDestination) {
        List<ChatDocument> historicMessages = new ArrayList<>();

        messagesByNickname.forEach(document -> {
            document.setInput(true);
            document.setOutput(false);
        });

        messagesByNicknameDestination.forEach(document -> {
            document.setInput(false);
            document.setOutput(true);
            document.setFixed(false);
        });

        historicMessages.addAll(messagesByNickname);
        historicMessages.addAll(messagesByNicknameDestination);

        return historicMessages;
    }

}
