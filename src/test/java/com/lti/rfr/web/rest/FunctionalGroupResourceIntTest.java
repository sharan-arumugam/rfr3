package com.lti.rfr.web.rest;

import com.lti.rfr.Rfr3App;

import com.lti.rfr.domain.FunctionalGroup;
import com.lti.rfr.repository.FunctionalGroupRepository;
import com.lti.rfr.service.FunctionalGroupService;
import com.lti.rfr.service.dto.FunctionalGroupDTO;
import com.lti.rfr.service.mapper.FunctionalGroupMapper;
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
 * Test class for the FunctionalGroupResource REST controller.
 *
 * @see FunctionalGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Rfr3App.class)
public class FunctionalGroupResourceIntTest {

    private static final String DEFAULT_IMT = "AAAAAAAAAA";
    private static final String UPDATED_IMT = "BBBBBBBBBB";

    private static final String DEFAULT_IMT_1 = "AAAAAAAAAA";
    private static final String UPDATED_IMT_1 = "BBBBBBBBBB";

    private static final String DEFAULT_IMT_2 = "AAAAAAAAAA";
    private static final String UPDATED_IMT_2 = "BBBBBBBBBB";

    @Autowired
    private FunctionalGroupRepository functionalGroupRepository;

    @Autowired
    private FunctionalGroupMapper functionalGroupMapper;

    @Autowired
    private FunctionalGroupService functionalGroupService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFunctionalGroupMockMvc;

    private FunctionalGroup functionalGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FunctionalGroupResource functionalGroupResource = new FunctionalGroupResource(functionalGroupService);
        this.restFunctionalGroupMockMvc = MockMvcBuilders.standaloneSetup(functionalGroupResource)
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
    public static FunctionalGroup createEntity(EntityManager em) {
        FunctionalGroup functionalGroup = new FunctionalGroup()
            .imt(DEFAULT_IMT)
            .imt1(DEFAULT_IMT_1)
            .imt2(DEFAULT_IMT_2);
        return functionalGroup;
    }

    @Before
    public void initTest() {
        functionalGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createFunctionalGroup() throws Exception {
        int databaseSizeBeforeCreate = functionalGroupRepository.findAll().size();

        // Create the FunctionalGroup
        FunctionalGroupDTO functionalGroupDTO = functionalGroupMapper.toDto(functionalGroup);
        restFunctionalGroupMockMvc.perform(post("/api/functional-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(functionalGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the FunctionalGroup in the database
        List<FunctionalGroup> functionalGroupList = functionalGroupRepository.findAll();
        assertThat(functionalGroupList).hasSize(databaseSizeBeforeCreate + 1);
        FunctionalGroup testFunctionalGroup = functionalGroupList.get(functionalGroupList.size() - 1);
        assertThat(testFunctionalGroup.getImt()).isEqualTo(DEFAULT_IMT);
        assertThat(testFunctionalGroup.getImt1()).isEqualTo(DEFAULT_IMT_1);
        assertThat(testFunctionalGroup.getImt2()).isEqualTo(DEFAULT_IMT_2);
    }

    @Test
    @Transactional
    public void createFunctionalGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = functionalGroupRepository.findAll().size();

        // Create the FunctionalGroup with an existing ID
        functionalGroup.setId(1L);
        FunctionalGroupDTO functionalGroupDTO = functionalGroupMapper.toDto(functionalGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFunctionalGroupMockMvc.perform(post("/api/functional-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(functionalGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FunctionalGroup in the database
        List<FunctionalGroup> functionalGroupList = functionalGroupRepository.findAll();
        assertThat(functionalGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFunctionalGroups() throws Exception {
        // Initialize the database
        functionalGroupRepository.saveAndFlush(functionalGroup);

        // Get all the functionalGroupList
        restFunctionalGroupMockMvc.perform(get("/api/functional-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(functionalGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].imt").value(hasItem(DEFAULT_IMT.toString())))
            .andExpect(jsonPath("$.[*].imt1").value(hasItem(DEFAULT_IMT_1.toString())))
            .andExpect(jsonPath("$.[*].imt2").value(hasItem(DEFAULT_IMT_2.toString())));
    }

    @Test
    @Transactional
    public void getFunctionalGroup() throws Exception {
        // Initialize the database
        functionalGroupRepository.saveAndFlush(functionalGroup);

        // Get the functionalGroup
        restFunctionalGroupMockMvc.perform(get("/api/functional-groups/{id}", functionalGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(functionalGroup.getId().intValue()))
            .andExpect(jsonPath("$.imt").value(DEFAULT_IMT.toString()))
            .andExpect(jsonPath("$.imt1").value(DEFAULT_IMT_1.toString()))
            .andExpect(jsonPath("$.imt2").value(DEFAULT_IMT_2.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFunctionalGroup() throws Exception {
        // Get the functionalGroup
        restFunctionalGroupMockMvc.perform(get("/api/functional-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFunctionalGroup() throws Exception {
        // Initialize the database
        functionalGroupRepository.saveAndFlush(functionalGroup);
        int databaseSizeBeforeUpdate = functionalGroupRepository.findAll().size();

        // Update the functionalGroup
        FunctionalGroup updatedFunctionalGroup = functionalGroupRepository.findOne(functionalGroup.getId());
        // Disconnect from session so that the updates on updatedFunctionalGroup are not directly saved in db
        em.detach(updatedFunctionalGroup);
        updatedFunctionalGroup
            .imt(UPDATED_IMT)
            .imt1(UPDATED_IMT_1)
            .imt2(UPDATED_IMT_2);
        FunctionalGroupDTO functionalGroupDTO = functionalGroupMapper.toDto(updatedFunctionalGroup);

        restFunctionalGroupMockMvc.perform(put("/api/functional-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(functionalGroupDTO)))
            .andExpect(status().isOk());

        // Validate the FunctionalGroup in the database
        List<FunctionalGroup> functionalGroupList = functionalGroupRepository.findAll();
        assertThat(functionalGroupList).hasSize(databaseSizeBeforeUpdate);
        FunctionalGroup testFunctionalGroup = functionalGroupList.get(functionalGroupList.size() - 1);
        assertThat(testFunctionalGroup.getImt()).isEqualTo(UPDATED_IMT);
        assertThat(testFunctionalGroup.getImt1()).isEqualTo(UPDATED_IMT_1);
        assertThat(testFunctionalGroup.getImt2()).isEqualTo(UPDATED_IMT_2);
    }

    @Test
    @Transactional
    public void updateNonExistingFunctionalGroup() throws Exception {
        int databaseSizeBeforeUpdate = functionalGroupRepository.findAll().size();

        // Create the FunctionalGroup
        FunctionalGroupDTO functionalGroupDTO = functionalGroupMapper.toDto(functionalGroup);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFunctionalGroupMockMvc.perform(put("/api/functional-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(functionalGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the FunctionalGroup in the database
        List<FunctionalGroup> functionalGroupList = functionalGroupRepository.findAll();
        assertThat(functionalGroupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFunctionalGroup() throws Exception {
        // Initialize the database
        functionalGroupRepository.saveAndFlush(functionalGroup);
        int databaseSizeBeforeDelete = functionalGroupRepository.findAll().size();

        // Get the functionalGroup
        restFunctionalGroupMockMvc.perform(delete("/api/functional-groups/{id}", functionalGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FunctionalGroup> functionalGroupList = functionalGroupRepository.findAll();
        assertThat(functionalGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FunctionalGroup.class);
        FunctionalGroup functionalGroup1 = new FunctionalGroup();
        functionalGroup1.setId(1L);
        FunctionalGroup functionalGroup2 = new FunctionalGroup();
        functionalGroup2.setId(functionalGroup1.getId());
        assertThat(functionalGroup1).isEqualTo(functionalGroup2);
        functionalGroup2.setId(2L);
        assertThat(functionalGroup1).isNotEqualTo(functionalGroup2);
        functionalGroup1.setId(null);
        assertThat(functionalGroup1).isNotEqualTo(functionalGroup2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FunctionalGroupDTO.class);
        FunctionalGroupDTO functionalGroupDTO1 = new FunctionalGroupDTO();
        functionalGroupDTO1.setId(1L);
        FunctionalGroupDTO functionalGroupDTO2 = new FunctionalGroupDTO();
        assertThat(functionalGroupDTO1).isNotEqualTo(functionalGroupDTO2);
        functionalGroupDTO2.setId(functionalGroupDTO1.getId());
        assertThat(functionalGroupDTO1).isEqualTo(functionalGroupDTO2);
        functionalGroupDTO2.setId(2L);
        assertThat(functionalGroupDTO1).isNotEqualTo(functionalGroupDTO2);
        functionalGroupDTO1.setId(null);
        assertThat(functionalGroupDTO1).isNotEqualTo(functionalGroupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(functionalGroupMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(functionalGroupMapper.fromId(null)).isNull();
    }
}
