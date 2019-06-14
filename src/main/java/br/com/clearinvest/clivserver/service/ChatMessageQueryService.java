package br.com.clearinvest.clivserver.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import br.com.clearinvest.clivserver.domain.ChatMessage;
import br.com.clearinvest.clivserver.domain.*; // for static metamodels
import br.com.clearinvest.clivserver.repository.ChatMessageRepository;
import br.com.clearinvest.clivserver.service.dto.ChatMessageCriteria;
import br.com.clearinvest.clivserver.service.dto.ChatMessageDTO;
import br.com.clearinvest.clivserver.service.mapper.ChatMessageMapper;

/**
 * Service for executing complex queries for ChatMessage entities in the database.
 * The main input is a {@link ChatMessageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChatMessageDTO} or a {@link Page} of {@link ChatMessageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChatMessageQueryService extends QueryService<ChatMessage> {

    private final Logger log = LoggerFactory.getLogger(ChatMessageQueryService.class);

    private final ChatMessageRepository chatMessageRepository;

    private final ChatMessageMapper chatMessageMapper;

    public ChatMessageQueryService(ChatMessageRepository chatMessageRepository, ChatMessageMapper chatMessageMapper) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageMapper = chatMessageMapper;
    }

    /**
     * Return a {@link List} of {@link ChatMessageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChatMessageDTO> findByCriteria(ChatMessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChatMessage> specification = createSpecification(criteria);
        return chatMessageMapper.toDto(chatMessageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChatMessageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatMessageDTO> findByCriteria(ChatMessageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChatMessage> specification = createSpecification(criteria);
        return chatMessageRepository.findAll(specification, page)
            .map(chatMessageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChatMessageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChatMessage> specification = createSpecification(criteria);
        return chatMessageRepository.count(specification);
    }

    /**
     * Function to convert ChatMessageCriteria to a {@link Specification}
     */
    private Specification<ChatMessage> createSpecification(ChatMessageCriteria criteria) {
        Specification<ChatMessage> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ChatMessage_.id));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), ChatMessage_.createdAt));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(ChatMessage_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
