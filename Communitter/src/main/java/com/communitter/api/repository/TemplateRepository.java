package com.communitter.api.repository;

import com.communitter.api.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template,Long> {

    Template findByName(String name);
}
