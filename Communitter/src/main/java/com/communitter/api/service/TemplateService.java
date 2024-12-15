package com.communitter.api.service;

import com.communitter.api.model.*;
import com.communitter.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateService {
    private final CommunityRepository communityRepository;
    private final TemplateRepository templateRepository;
    private  final DataFieldTypeRepository dataFieldTypeRepository;
    private final DataFieldRepository dataFieldRepository;
    private final DataFieldEnumValueRepository dataFieldEnumValueRepository;

    @Transactional
    public Template createTemplate(Long id, Template template){

        if (template.getDataFields() != null && !template.getDataFields().isEmpty()) {
            Community community = communityRepository.findById(id).orElseThrow();
            template.setCommunity(community);
            Template createdTemplate = templateRepository.save(template);
            for (DataField dataField : template.getDataFields()) {
                dataField.setTemplate(createdTemplate);
                DataField createdDataField = dataFieldRepository.save(dataField);
                if(dataField.getDataFieldType().getType().equals("enumeration")){
                    for(DataFieldEnumValue dataFieldEnumValue : dataField.getEnumValues()){
                        dataFieldEnumValue.setDataField(createdDataField);
                        dataFieldEnumValueRepository.save(dataFieldEnumValue);
                    }
                }
            }
            return createdTemplate;
        }else{
            throw new RuntimeException("Template has to have data fields");
        }

    }

    public List<DataFieldType> getTypes(){
       return dataFieldTypeRepository.findAll();
    }
}
