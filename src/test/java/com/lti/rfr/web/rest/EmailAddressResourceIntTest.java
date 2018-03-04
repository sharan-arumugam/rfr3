package com.lti.rfr.web.rest;

import com.lti.rfr.Rfr3App;

import com.lti.rfr.domain.EmailAddress;
import com.lti.rfr.repository.EmailAddressRepository;
import com.lti.rfr.service.EmailAddressService;
import com.lti.rfr.service.dto.EmailAddressDTO;
import com.lti.rfr.service.mapper.EmailAddressMapper;
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
 * Test class for the EmailAddressResource REST controller.
 *
 * @see EmailAddressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Rfr3App.class)
public class EmailAddressResourceIntTest {

    private static final String DEFAULT_PS_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PS_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_APPLE_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_APPLE_MAIL = "BBBBBBBBBB";

    @Autowired
    private EmailAddressRepository emailAddressRepository;

    @Autowired
    private EmailAddressMapper emailAddressMapper;

    @Autowired
    private EmailAddressService emailAddressService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmailAddressMockMvc;

    private EmailAddress emailAddress;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmailAddressResource emailAddressResource = new EmailAddressResource(emailAddressService);
        this.restEmailAddressMockMvc = MockMvcBuilders.standaloneSetup(emailAddressResource)
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
    public static EmailAddress createEntity(EntityManager em) {
        EmailAddress emailAddress = new EmailAddress()
            .psNumber(DEFAULT_PS_NUMBER)
            .name(DEFAULT_NAME)
            .appleMail(DEFAULT_APPLE_MAIL);
        return emailAddress;
    }

    @Before
    public void initTest() {
        emailAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmailAddress() throws Exception {
        int databaseSizeBeforeCreate = emailAddressRepository.findAll().size();

        // Create the EmailAddress
        EmailAddressDTO emailAddressDTO = emailAddressMapper.toDto(emailAddress);
        restEmailAddressMockMvc.perform(post("/api/email-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the EmailAddress in the database
        List<EmailAddress> emailAddressList = emailAddressRepository.findAll();
        assertThat(emailAddressList).hasSize(databaseSizeBeforeCreate + 1);
        EmailAddress testEmailAddress = emailAddressList.get(emailAddressList.size() - 1);
        assertThat(testEmailAddress.getPsNumber()).isEqualTo(DEFAULT_PS_NUMBER);
        assertThat(testEmailAddress.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmailAddress.getAppleMail()).isEqualTo(DEFAULT_APPLE_MAIL);
    }

    @Test
    @Transactional
    public void createEmailAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emailAddressRepository.findAll().size();

        // Create the EmailAddress with an existing ID
        emailAddress.setId(1L);
        EmailAddressDTO emailAddressDTO = emailAddressMapper.toDto(emailAddress);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailAddressMockMvc.perform(post("/api/email-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmailAddress in the database
        List<EmailAddress> emailAddressList = emailAddressRepository.findAll();
        assertThat(emailAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEmailAddresses() throws Exception {
        // Initialize the database
        emailAddressRepository.saveAndFlush(emailAddress);

        // Get all the emailAddressList
        restEmailAddressMockMvc.perform(get("/api/email-addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].psNumber").value(hasItem(DEFAULT_PS_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].appleMail").value(hasItem(DEFAULT_APPLE_MAIL.toString())));
    }

    @Test
    @Transactional
    public void getEmailAddress() throws Exception {
        // Initialize the database
        emailAddressRepository.saveAndFlush(emailAddress);

        // Get the emailAddress
        restEmailAddressMockMvc.perform(get("/api/email-addresses/{id}", emailAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(emailAddress.getId().intValue()))
            .andExpect(jsonPath("$.psNumber").value(DEFAULT_PS_NUMBER.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.appleMail").value(DEFAULT_APPLE_MAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmailAddress() throws Exception {
        // Get the emailAddress
        restEmailAddressMockMvc.perform(get("/api/email-addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmailAddress() throws Exception {
        // Initialize the database
        emailAddressRepository.saveAndFlush(emailAddress);
        int databaseSizeBeforeUpdate = emailAddressRepository.findAll().size();

        // Update the emailAddress
        EmailAddress updatedEmailAddress = emailAddressRepository.findOne(emailAddress.getId());
        // Disconnect from session so that the updates on updatedEmailAddress are not directly saved in db
        em.detach(updatedEmailAddress);
        updatedEmailAddress
            .psNumber(UPDATED_PS_NUMBER)
            .name(UPDATED_NAME)
            .appleMail(UPDATED_APPLE_MAIL);
        EmailAddressDTO emailAddressDTO = emailAddressMapper.toDto(updatedEmailAddress);

        restEmailAddressMockMvc.perform(put("/api/email-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailAddressDTO)))
            .andExpect(status().isOk());

        // Validate the EmailAddress in the database
        List<EmailAddress> emailAddressList = emailAddressRepository.findAll();
        assertThat(emailAddressList).hasSize(databaseSizeBeforeUpdate);
        EmailAddress testEmailAddress = emailAddressList.get(emailAddressList.size() - 1);
        assertThat(testEmailAddress.getPsNumber()).isEqualTo(UPDATED_PS_NUMBER);
        assertThat(testEmailAddress.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmailAddress.getAppleMail()).isEqualTo(UPDATED_APPLE_MAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingEmailAddress() throws Exception {
        int databaseSizeBeforeUpdate = emailAddressRepository.findAll().size();

        // Create the EmailAddress
        EmailAddressDTO emailAddressDTO = emailAddressMapper.toDto(emailAddress);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmailAddressMockMvc.perform(put("/api/email-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the EmailAddress in the database
        List<EmailAddress> emailAddressList = emailAddressRepository.findAll();
        assertThat(emailAddressList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmailAddress() throws Exception {
        // Initialize the database
        emailAddressRepository.saveAndFlush(emailAddress);
        int databaseSizeBeforeDelete = emailAddressRepository.findAll().size();

        // Get the emailAddress
        restEmailAddressMockMvc.perform(delete("/api/email-addresses/{id}", emailAddress.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EmailAddress> emailAddressList = emailAddressRepository.findAll();
        assertThat(emailAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailAddress.class);
        EmailAddress emailAddress1 = new EmailAddress();
        emailAddress1.setId(1L);
        EmailAddress emailAddress2 = new EmailAddress();
        emailAddress2.setId(emailAddress1.getId());
        assertThat(emailAddress1).isEqualTo(emailAddress2);
        emailAddress2.setId(2L);
        assertThat(emailAddress1).isNotEqualTo(emailAddress2);
        emailAddress1.setId(null);
        assertThat(emailAddress1).isNotEqualTo(emailAddress2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailAddressDTO.class);
        EmailAddressDTO emailAddressDTO1 = new EmailAddressDTO();
        emailAddressDTO1.setId(1L);
        EmailAddressDTO emailAddressDTO2 = new EmailAddressDTO();
        assertThat(emailAddressDTO1).isNotEqualTo(emailAddressDTO2);
        emailAddressDTO2.setId(emailAddressDTO1.getId());
        assertThat(emailAddressDTO1).isEqualTo(emailAddressDTO2);
        emailAddressDTO2.setId(2L);
        assertThat(emailAddressDTO1).isNotEqualTo(emailAddressDTO2);
        emailAddressDTO1.setId(null);
        assertThat(emailAddressDTO1).isNotEqualTo(emailAddressDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(emailAddressMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(emailAddressMapper.fromId(null)).isNull();
    }
}
