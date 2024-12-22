package com.communitter.api.service;

import com.communitter.api.model.*;
import com.communitter.api.repository.CommunityRepository;
import com.communitter.api.repository.DataFieldRepository;
import com.communitter.api.repository.DataFieldTypeRepository;
import com.communitter.api.repository.TemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TemplateServiceTest {

    @InjectMocks
    private TemplateService templateService;

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private DataFieldTypeRepository dataFieldTypeRepository;

    @Mock
    private DataFieldRepository dataFieldRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTemplate_shouldCreateTemplate_whenDataFieldsExist() {
        Long communityId = 1L;

        // Mock community
        Community mockCommunity = new Community();
        mockCommunity.setId(communityId);

        // Mock data field type
        DataFieldType mockFieldType = new DataFieldType(1L, "String");

        // Mock data field
        DataField mockField = DataField.builder()
                .name("Field1")
                .isRequired(true)
                .dataFieldType(mockFieldType)
                .build();

        // Mock template with a Set of data fields
        Template mockTemplate = Template.builder()
                .name("Template1")
                .dataFields(Set.of(mockField)) // Use Set.of for a single element set
                .build();

        // Mock community repository behavior
        when(communityRepository.findById(communityId)).thenReturn(Optional.of(mockCommunity));

        // Mock template repository behavior to simulate database ID generation
        when(templateRepository.save(any(Template.class))).thenAnswer(invocation -> {
            Template template = invocation.getArgument(0);
            template.setId(1L); // Simulate generated ID
            return template;
        });

        // Mock data field repository saveAll behavior
        when(dataFieldRepository.saveAll(any())).thenReturn(List.of(mockField));

        // Call the service method
        Template result = templateService.createTemplate(communityId, mockTemplate);

        // Assertions
        assertNotNull(result); // Ensure the result is not null
        assertEquals("Template1", result.getName()); // Verify template name
        assertEquals(mockCommunity, result.getCommunity()); // Verify associated community
        assertEquals(1, result.getDataFields().size()); // Verify the number of data fields

        // Verify repository interactions
        verify(communityRepository, times(1)).findById(communityId);
        verify(templateRepository, times(1)).save(any(Template.class));
        verify(dataFieldRepository, times(1)).saveAll(mockTemplate.getDataFields());
    }


    @Test
    void createTemplate_shouldThrowException_whenDataFieldsNotExist() {
        Long communityId = 1L;

        // Mock template with no data fields
        Template mockTemplate = Template.builder()
                .name("TemplateWithoutFields")
                .dataFields(null) // No data fields
                .build();

        // Verify that the method throws an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                templateService.createTemplate(communityId, mockTemplate));
        assertEquals("Template has to have data fields", exception.getMessage());

        // Verify no interactions with repositories
        verifyNoInteractions(communityRepository);
        verifyNoInteractions(templateRepository);
        verifyNoInteractions(dataFieldRepository);
    }

    @Test
    void getTypes_shouldReturnAllDataFieldTypes() {
        // Mock data field types
        DataFieldType mockType1 = new DataFieldType(1L, "String");
        DataFieldType mockType2 = new DataFieldType(2L, "Number");

        // Mock repository behavior
        when(dataFieldTypeRepository.findAll()).thenReturn(List.of(mockType1, mockType2));

        // Call the service method
        List<DataFieldType> result = templateService.getTypes();

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("String", result.get(0).getType());
        assertEquals("Number", result.get(1).getType());

        // Verify repository interaction
        verify(dataFieldTypeRepository, times(1)).findAll();
    }
}
