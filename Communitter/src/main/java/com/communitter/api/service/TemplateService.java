package com.communitter.api.service;

import com.communitter.api.repository.DataFieldRepository;
import com.communitter.api.repository.DataFieldTypeRepository;
import com.communitter.api.repository.TemplateRepository;
import com.communitter.api.model.Community;
import com.communitter.api.repository.CommunityRepository;
import com.communitter.api.model.DataField;
import com.communitter.api.model.DataFieldType;
import com.communitter.api.model.Template;
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

    @Transactional
    public Template createTemplate(Long id, Template template){

        if (template.getDataFields() != null && !template.getDataFields().isEmpty()) {
            Community community = communityRepository.findById(id).orElseThrow();
            template.setCommunity(community);
            Template createdTemplate = templateRepository.save(template);
            for (DataField dataField : template.getDataFields()) {
                dataField.setTemplate(createdTemplate);
            }
            dataFieldRepository.saveAll(template.getDataFields());
            return createdTemplate;
        }else{
            throw new RuntimeException("Template has to have data fields");
        }

    }

    public List<DataFieldType> getTypes(){
       return dataFieldTypeRepository.findAll();
    }
}
