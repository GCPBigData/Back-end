package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.ChatMessage;
import br.com.clearinvest.clivserver.domain.User;
import br.com.clearinvest.clivserver.repository.ChatMessageRepository;
import br.com.clearinvest.clivserver.service.ChatMessageService;
import br.com.clearinvest.clivserver.service.dto.ChatMessageDTO;
import br.com.clearinvest.clivserver.service.mapper.ChatMessageMapper;
import br.com.clearinvest.clivserver.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static br.com.clearinvest.clivserver.web.rest.TestUtil.sameInstant;
import static br.com.clearinvest.clivserver.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ChatMessageResource REST controller.
 *
 * @see ChatMessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class ChatMessageResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChatMessageMockMvc;

    private ChatMessage chatMessage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatMessageResource chatMessageResource = new ChatMessageResource(chatMessageService);
        this.restChatMessageMockMvc = MockMvcBuilders.standaloneSetup(chatMessageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatMessage createEntity(EntityManager em) {
        ChatMessage chatMessage = new ChatMessage()
            .createdAt(DEFAULT_CREATED_AT)
            .message(DEFAULT_MESSAGE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        chatMessage.setUser(user);
        return chatMessage;
    }

    @Before
    public void initTest() {
        chatMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatMessage() throws Exception {
        int databaseSizeBeforeCreate = chatMessageRepository.findAll().size();

        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);
        restChatMessageMockMvc.perform(post("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isCreated());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeCreate + 1);
        ChatMessage testChatMessage = chatMessageList.get(chatMessageList.size() - 1);
        assertThat(testChatMessage.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testChatMessage.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    public void createChatMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatMessageRepository.findAll().size();

        // Create the ChatMessage with an existing ID
        chatMessage.setId(1L);
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatMessageMockMvc.perform(post("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllChatMessages() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get all the chatMessageList
        restChatMessageMockMvc.perform(get("/api/chat-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }
    
    @Test
    @Transactional
    public void getChatMessage() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        // Get the chatMessage
        restChatMessageMockMvc.perform(get("/api/chat-messages/{id}", chatMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatMessage.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChatMessage() throws Exception {
        // Get the chatMessage
        restChatMessageMockMvc.perform(get("/api/chat-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatMessage() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        int databaseSizeBeforeUpdate = chatMessageRepository.findAll().size();

        // Update the chatMessage
        ChatMessage updatedChatMessage = chatMessageRepository.findById(chatMessage.getId()).get();
        // Disconnect from session so that the updates on updatedChatMessage are not directly saved in db
        em.detach(updatedChatMessage);
        updatedChatMessage
            .createdAt(UPDATED_CREATED_AT)
            .message(UPDATED_MESSAGE);
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(updatedChatMessage);

        restChatMessageMockMvc.perform(put("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isOk());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeUpdate);
        ChatMessage testChatMessage = chatMessageList.get(chatMessageList.size() - 1);
        assertThat(testChatMessage.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testChatMessage.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingChatMessage() throws Exception {
        int databaseSizeBeforeUpdate = chatMessageRepository.findAll().size();

        // Create the ChatMessage
        ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatMessageMockMvc.perform(put("/api/chat-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChatMessage in the database
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChatMessage() throws Exception {
        // Initialize the database
        chatMessageRepository.saveAndFlush(chatMessage);

        int databaseSizeBeforeDelete = chatMessageRepository.findAll().size();

        // Get the chatMessage
        restChatMessageMockMvc.perform(delete("/api/chat-messages/{id}", chatMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        assertThat(chatMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatMessage.class);
        ChatMessage chatMessage1 = new ChatMessage();
        chatMessage1.setId(1L);
        ChatMessage chatMessage2 = new ChatMessage();
        chatMessage2.setId(chatMessage1.getId());
        assertThat(chatMessage1).isEqualTo(chatMessage2);
        chatMessage2.setId(2L);
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);
        chatMessage1.setId(null);
        assertThat(chatMessage1).isNotEqualTo(chatMessage2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatMessageDTO.class);
        ChatMessageDTO chatMessageDTO1 = new ChatMessageDTO();
        chatMessageDTO1.setId(1L);
        ChatMessageDTO chatMessageDTO2 = new ChatMessageDTO();
        assertThat(chatMessageDTO1).isNotEqualTo(chatMessageDTO2);
        chatMessageDTO2.setId(chatMessageDTO1.getId());
        assertThat(chatMessageDTO1).isEqualTo(chatMessageDTO2);
        chatMessageDTO2.setId(2L);
        assertThat(chatMessageDTO1).isNotEqualTo(chatMessageDTO2);
        chatMessageDTO1.setId(null);
        assertThat(chatMessageDTO1).isNotEqualTo(chatMessageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chatMessageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chatMessageMapper.fromId(null)).isNull();
    }
}
