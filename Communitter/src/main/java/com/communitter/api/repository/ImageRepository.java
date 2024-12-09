package com.communitter.api.repository;

import com.communitter.api.model.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageData,Long> {

    ImageData findByName(String fileName);
    ImageData findByUserId(long userId);
    ImageData findByCommunityId(long communityId);

}
