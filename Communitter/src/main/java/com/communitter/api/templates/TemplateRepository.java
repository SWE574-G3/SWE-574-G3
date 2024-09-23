package com.communitter.api.templates;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template,Long> {

    Template findByName(String name);
}
