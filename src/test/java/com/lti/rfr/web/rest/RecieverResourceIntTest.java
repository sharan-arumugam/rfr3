package com.lti.rfr.web.rest;

import com.lti.rfr.Rfr3App;

import com.lti.rfr.domain.Reciever;
import com.lti.rfr.repository.RecieverRepository;
import com.lti.rfr.service.RecieverService;
import com.lti.rfr.service.dto.RecieverDTO;
import com.lti.rfr.service.mapper.RecieverMapper;
import com.lti.rfr.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.util.List;

import static com.lti.rfr.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RecieverResource REST controller.
 *
 * @see RecieverResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Rfr3App.class)
public class RecieverResourceIntTest {

    private static final String DEFAULT_PS_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PS_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_APPLE_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_APPLE_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private RecieverRepository recieverRepository;

    @Autowired
    private RecieverMapper recieverMapper;

    @Autowired
    private RecieverService recieverService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRecieverMockMvc;

    private Reciever reciever;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecieverResource recieverResource = new RecieverResource(recieverService);
        this.restRecieverMockMvc = MockMvcBuilders.standaloneSetup(recieverResource)
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
    public static Reciever createEntity(EntityManager em) {
        Reciever reciever = new Reciever()
            .psNumber(DEFAULT_PS_NUMBER)
            .appleMail(DEFAULT_APPLE_MAIL)
            .name(DEFAULT_NAME);
        return reciever;
    }

    @Before
    public void initTest() {
        reciever = createEntity(em);
    }

    @Test
    @Transactional
    public void createReciever() throws Exception {
        int databaseSizeBeforeCreate = recieverRepository.findAll().size();

        // Create the Reciever
        RecieverDTO recieverDTO = recieverMapper.toDto(reciever);
        restRecieverMockMvc.perform(post("/api/recievers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recieverDTO)))
            .andExpect(status().isCreated());

        // Validate the Reciever in the database
        List<Reciever> recieverList = recieverRepository.findAll();
        assertThat(recieverList).hasSize(databaseSizeBeforeCreate + 1);
        Reciever testReciever = recieverList.get(recieverList.size() - 1);
        assertThat(testReciever.getPsNumber()).isEqualTo(DEFAULT_PS_NUMBER);
        assertThat(testReciever.getAppleMail()).isEqualTo(DEFAULT_APPLE_MAIL);
        assertThat(testReciever.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createRecieverWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recieverRepository.findAll().size();

        // Create the Reciever with an existing ID
        reciever.setId(1L);
        RecieverDTO recieverDTO = recieverMapper.toDto(reciever);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecieverMockMvc.perform(post("/api/recievers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recieverDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reciever in the database
        List<Reciever> recieverList = recieverRepository.findAll();
        assertThat(recieverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRecievers() throws Exception {
        // Initialize the database
        recieverRepository.saveAndFlush(reciever);

        // Get all the recieverList
        restRecieverMockMvc.perform(get("/api/recievers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reciever.getId().intValue())))
            .andExpect(jsonPath("$.[*].psNumber").value(hasItem(DEFAULT_PS_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].appleMail").value(hasItem(DEFAULT_APPLE_MAIL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getReciever() throws Exception {
        // Initialize the database
        recieverRepository.saveAndFlush(reciever);

        // Get the reciever
        restRecieverMockMvc.perform(get("/api/recievers/{id}", reciever.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reciever.getId().intValue()))
            .andExpect(jsonPath("$.psNumber").value(DEFAULT_PS_NUMBER.toString()))
            .andExpect(jsonPath("$.appleMail").value(DEFAULT_APPLE_MAIL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReciever() throws Exception {
        // Get the reciever
        restRecieverMockMvc.perform(get("/api/recievers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReciever() throws Exception {
        // Initialize the database
        recieverRepository.saveAndFlush(reciever);
        int databaseSizeBeforeUpdate = recieverRepository.findAll().size();

        // Update the reciever
        Reciever updatedReciever = recieverRepository.findOne(reciever.getId());
        // Disconnect from session so that the updates on updatedReciever are not directly saved in db
        em.detach(updatedReciever);
        updatedReciever
            .psNumber(UPDATED_PS_NUMBER)
            .appleMail(UPDATED_APPLE_MAIL)
            .name(UPDATED_NAME);
        RecieverDTO recieverDTO = recieverMapper.toDto(updatedReciever);

        restRecieverMockMvc.perform(put("/api/recievers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recieverDTO)))
            .andExpect(status().isOk());

        // Validate the Reciever in the database
        List<Reciever> recieverList = recieverRepository.findAll();
        assertThat(recieverList).hasSize(databaseSizeBeforeUpdate);
        Reciever testReciever = recieverList.get(recieverList.size() - 1);
        assertThat(testReciever.getPsNumber()).isEqualTo(UPDATED_PS_NUMBER);
        assertThat(testReciever.getAppleMail()).isEqualTo(UPDATED_APPLE_MAIL);
        assertThat(testReciever.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingReciever() throws Exception {
        int databaseSizeBeforeUpdate = recieverRepository.findAll().size();

        // Create the Reciever
        RecieverDTO recieverDTO = recieverMapper.toDto(reciever);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRecieverMockMvc.perform(put("/api/recievers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recieverDTO)))
            .andExpect(status().isCreated());

        // Validate the Reciever in the database
        List<Reciever> recieverList = recieverRepository.findAll();
        assertThat(recieverList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteReciever() throws Exception {
        // Initialize the database
        recieverRepository.saveAndFlush(reciever);
        int databaseSizeBeforeDelete = recieverRepository.findAll().size();

        // Get the reciever
        restRecieverMockMvc.perform(delete("/api/recievers/{id}", reciever.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Reciever> recieverList = recieverRepository.findAll();
        assertThat(recieverList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reciever.class);
        Reciever reciever1 = new Reciever();
        reciever1.setId(1L);
        Reciever reciever2 = new Reciever();
        reciever2.setId(reciever1.getId());
        assertThat(reciever1).isEqualTo(reciever2);
        reciever2.setId(2L);
        assertThat(reciever1).isNotEqualTo(reciever2);
        reciever1.setId(null);
        assertThat(reciever1).isNotEqualTo(reciever2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecieverDTO.class);
        RecieverDTO recieverDTO1 = new RecieverDTO();
        recieverDTO1.setId(1L);
        RecieverDTO recieverDTO2 = new RecieverDTO();
        assertThat(recieverDTO1).isNotEqualTo(recieverDTO2);
        recieverDTO2.setId(recieverDTO1.getId());
        assertThat(recieverDTO1).isEqualTo(recieverDTO2);
        recieverDTO2.setId(2L);
        assertThat(recieverDTO1).isNotEqualTo(recieverDTO2);
        recieverDTO1.setId(null);
        assertThat(recieverDTO1).isNotEqualTo(recieverDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(recieverMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(recieverMapper.fromId(null)).isNull();
    }
}
