package com.communitter.api.controller;

import com.communitter.api.templates.DataFieldType;
import com.communitter.api.templates.Template;
import com.communitter.api.templates.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/templates")
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService templateService;

    @PreAuthorize("@authorizer.checkCreator(#root,#id)")
    @PostMapping("/community/{id}")
    public ResponseEntity<Template> createTemplate(@P("id") @PathVariable Long id, @RequestBody Template template){

            return ResponseEntity.ok(templateService.createTemplate(id,template));

    }
    @GetMapping("/types")
    public ResponseEntity<List<DataFieldType>> getFieldTypes(){
                return ResponseEntity.ok(templateService.getTypes());
    }
}
