package com.communitter.api.controller;

import com.communitter.api.dto.CommunityDto;
import com.communitter.api.model.CommunityLabel;
import com.communitter.api.model.UserInterest;
import com.communitter.api.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/recommendation")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping("/user/interests")
    public ResponseEntity<List<UserInterest>> saveUserInterests(@RequestBody List<UserInterest> interests){
        return ResponseEntity.ok(recommendationService.saveUserInterest(interests));
    }

    @PreAuthorize("@authorizer.checkCreator(#root,#id)")
    @PostMapping("/community/{id}/labels")
    public ResponseEntity<List<CommunityLabel>> saveCommunityLabels(@RequestBody List<CommunityLabel> labels,@P("id") @PathVariable Long id){
        return ResponseEntity.ok(recommendationService.saveCommunityLabel(labels,id));
    }

    @GetMapping("/communities")
    public ResponseEntity <Set<CommunityDto>> getCommunityRecommendations(){
        return ResponseEntity.ok(recommendationService.getCommunityRecommendations());
    }

    @GetMapping("/user/interests")
    public ResponseEntity<List<UserInterest>> getUserInterests(){
        return ResponseEntity.ok(recommendationService.getUserInterest());
    }

    @PreAuthorize("@authorizer.checkCreator(#root,#id)")
    @GetMapping("/community/{id}/labels")
    public ResponseEntity<List<CommunityLabel>> saveCommunityLabels(@P("id") @PathVariable Long id){
        return ResponseEntity.ok(recommendationService.getCommunityLabel(id));
    }

    @DeleteMapping("/user/interests/{code}")
    public ResponseEntity<String> deleteUserInterest(@PathVariable String code){
        recommendationService.deleteUserInterest(code);
        return ResponseEntity.ok("User Interest deleted");
    }

    @PreAuthorize("@authorizer.checkCreator(#root,#id)")
    @GetMapping("/community/{id}/labels/{code}")
    public ResponseEntity<String> deleteCommunityLabel(@P("id") @PathVariable Long id,@PathVariable String code){
        recommendationService.deleteCommunityLabel(code,id);
        return ResponseEntity.ok("Community label deleted");
    }

}
