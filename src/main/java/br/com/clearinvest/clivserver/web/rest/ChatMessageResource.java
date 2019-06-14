package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.ChatMessageService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.web.rest.util.PaginationUtil;
import br.com.clearinvest.clivserver.service.dto.ChatMessageDTO;
import br.com.clearinvest.clivserver.service.dto.ChatMessageCriteria;
import br.com.clearinvest.clivserver.service.ChatMessageQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ChatMessage.
 */
@RestController
@RequestMapping("/api")
public class ChatMessageResource {

    private final Logger log = LoggerFactory.getLogger(ChatMessageResource.class);

    private static final String ENTITY_NAME = "chatMessage";

    private final ChatMessageService chatMessageService;

    private final ChatMessageQueryService chatMessageQueryService;

    public ChatMessageResource(ChatMessageService chatMessageService, ChatMessageQueryService chatMessageQueryService) {
        this.chatMessageService = chatMessageService;
        this.chatMessageQueryService = chatMessageQueryService;
    }

    /**
     * POST  /chat-messages : Create a new chatMessage.
     *
     * @param chatMessageDTO the chatMessageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chatMessageDTO, or with status 400 (Bad Request) if the chatMessage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chat-messages")
    @Timed
    public ResponseEntity<ChatMessageDTO> createChatMessage(@Valid @RequestBody ChatMessageDTO chatMessageDTO) throws URISyntaxException {
        log.debug("REST request to save ChatMessage : {}", chatMessageDTO);
        if (chatMessageDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatMessageDTO result = chatMessageService.save(chatMessageDTO);
        return ResponseEntity.created(new URI("/api/chat-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chat-messages : Updates an existing chatMessage.
     *
     * @param chatMessageDTO the chatMessageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chatMessageDTO,
     * or with status 400 (Bad Request) if the chatMessageDTO is not valid,
     * or with status 500 (Internal Server Error) if the chatMessageDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chat-messages")
    @Timed
    public ResponseEntity<ChatMessageDTO> updateChatMessage(@Valid @RequestBody ChatMessageDTO chatMessageDTO) throws URISyntaxException {
        log.debug("REST request to update ChatMessage : {}", chatMessageDTO);
        if (chatMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChatMessageDTO result = chatMessageService.save(chatMessageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chatMessageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chat-messages : get all the chatMessages.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of chatMessages in body
     */
    @GetMapping("/chat-messages")
    @Timed
    public ResponseEntity<List<ChatMessageDTO>> getAllChatMessages(ChatMessageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ChatMessages by criteria: {}", criteria);
        Page<ChatMessageDTO> page = chatMessageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chat-messages");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /chat-messages/count : count all the chatMessages.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/chat-messages/count")
    @Timed
    public ResponseEntity<Long> countChatMessages(ChatMessageCriteria criteria) {
        log.debug("REST request to count ChatMessages by criteria: {}", criteria);
        return ResponseEntity.ok().body(chatMessageQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /chat-messages/:id : get the "id" chatMessage.
     *
     * @param id the id of the chatMessageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chatMessageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/chat-messages/{id}")
    @Timed
    public ResponseEntity<ChatMessageDTO> getChatMessage(@PathVariable Long id) {
        log.debug("REST request to get ChatMessage : {}", id);
        Optional<ChatMessageDTO> chatMessageDTO = chatMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatMessageDTO);
    }

    /**
     * DELETE  /chat-messages/:id : delete the "id" chatMessage.
     *
     * @param id the id of the chatMessageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chat-messages/{id}")
    @Timed
    public ResponseEntity<Void> deleteChatMessage(@PathVariable Long id) {
        log.debug("REST request to delete ChatMessage : {}", id);
        chatMessageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
