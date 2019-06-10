package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.ChatMessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ChatMessage and its DTO ChatMessageDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ChatMessageMapper extends EntityMapper<ChatMessageDTO, ChatMessage> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "user.lastName", target = "userLastName")
    ChatMessageDTO toDto(ChatMessage chatMessage);

    @Mapping(source = "userId", target = "user")
    ChatMessage toEntity(ChatMessageDTO chatMessageDTO);

    default ChatMessage fromId(Long id) {
        if (id == null) {
            return null;
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(id);
        return chatMessage;
    }
}
