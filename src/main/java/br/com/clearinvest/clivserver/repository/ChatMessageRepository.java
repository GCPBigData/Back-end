package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.ChatMessage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ChatMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, JpaSpecificationExecutor<ChatMessage> {

    @Query("select chat_message from ChatMessage chat_message where chat_message.user.login = ?#{principal.username}")
    List<ChatMessage> findByUserIsCurrentUser();

}
