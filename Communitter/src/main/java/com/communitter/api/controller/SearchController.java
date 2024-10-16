package com.communitter.api.controller;


import com.communitter.api.search.BasicSearchResponse;
import com.communitter.api.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;
    @GetMapping
    public ResponseEntity<BasicSearchResponse> searchBasic(@RequestParam String input){
        return ResponseEntity.ok(searchService.basicSearch(input));
    }
}
