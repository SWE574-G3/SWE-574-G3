package com.communitter.api.repository;

import com.communitter.api.model.DataFieldType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataFieldTypeRepository extends JpaRepository<DataFieldType,Long> {
    DataFieldType findByType(String type);
}
