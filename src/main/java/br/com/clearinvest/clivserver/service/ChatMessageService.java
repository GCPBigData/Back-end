package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.ChatMessage;
import br.com.clearinvest.clivserver.repository.ChatMessageRepository;
import br.com.clearinvest.clivserver.service.dto.ChatMessageDTO;
import br.com.clearinvest.clivserver.service.mapper.ChatMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing ChatMessage.
 */
@Service
@Transactional
public class ChatMessageService {

    private final Logger log = LoggerFactory.getLogger(ChatMessageService.class);

    private final ChatMessageRepository chatMessageRepository;

    private final ChatMessageMapper chatMessageMapper;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatMessageMapper chatMessageMapper) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageMapper = chatMessageMapper;
    }

    /**
     * Save a chatMessage.
     *
     * @param chatMessageDTO the entity to save
     * @return the persisted entity
     */
    public ChatMessageDTO save(ChatMessageDTO chatMessageDTO) {
        log.debug("Request to save ChatMessage : {}", chatMessageDTO);

        ChatMessage chatMessage = chatMessageMapper.toEntity(chatMessageDTO);
        chatMessage.setCreatedAt(ZonedDateTime.now());
        chatMessage = chatMessageRepository.save(chatMessage);
        return chatMessageMapper.toDto(chatMessage);
    }

    /**
     * Get all the chatMessages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ChatMessageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatMessages");
        return chatMessageRepository.findAll(pageable)
            .map(chatMessageMapper::toDto);
    }


    /**
     * Get one chatMessage by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ChatMessageDTO> findOne(Long id) {
        log.debug("Request to get ChatMessage : {}", id);
        return chatMessageRepository.findById(id)
            .map(chatMessageMapper::toDto);
    }

    /**
     * Delete the chatMessage by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ChatMessage : {}", id);
        chatMessageRepository.deleteById(id);
    }
}
