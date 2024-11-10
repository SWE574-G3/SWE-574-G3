package com.communitter.api.service;

import com.communitter.api.key.PostVoteKey;
import com.communitter.api.model.*;
import com.communitter.api.repository.*;
import com.communitter.api.util.PostValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.communitter.api.util.BasicAuthorizationUtil;
import com.communitter.api.exception.NotAuthorizedException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;
    private final CommunityRepository communityRepository;
    private final TemplateRepository templateRepository;
    private final PostFieldRepository postFieldRepository;
    private final BasicAuthorizationUtil authUtil;
    public Logger logger = LoggerFactory.getLogger(PostService.class);

    @Transactional
    public Post createPost(Long id, Post post) {
        User author= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Community targetCommunity = communityRepository.findById(id).orElseThrow();
        Template postTemplate = templateRepository.findById(post.getTemplate().getId()).orElseThrow();
        if(!postTemplate.getCommunity().getId().equals(targetCommunity.getId())) throw new RuntimeException("Community does not support this template");
        if (post.getPostFields() != null && !post.getPostFields().isEmpty()) {
            if (!checkRequiredFields(post.getPostFields(), postTemplate)) throw new RuntimeException("Post does not have all required fields");
            if(!PostValidator.validatePost(post)) throw new RuntimeException("Post fields don't comply with the expected format");
            post.setCommunity(targetCommunity);
            post.setAuthor(author);
            Post createdPost=postRepository.save(post);
            for(PostField postField: post.getPostFields()){
                postField.setPost(createdPost);
            }
            postFieldRepository.saveAll(post.getPostFields());
            return  createdPost;
        }
        else{
            throw new RuntimeException("Post has to have fields");
        }
    }

    @Transactional
    public void deletePost(Long communityId, Long id){
        communityRepository.findById(communityId).orElseThrow(()->new NoSuchElementException("Community does not exist"));
        postRepository.findById(id).orElseThrow(()->new NoSuchElementException("Post does not exist"));
        postRepository.deleteById(id);
    }

    @Transactional
    public Post editPost(Long postId, Post updatedPost) {
        // Fetch the existing post
        Post existingPost = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        // Update the date or other top-level fields if needed
        existingPost.setDate(new Date());

        if (updatedPost.getPostFields() != null && !updatedPost.getPostFields().isEmpty()) {
            Template postTemplate = templateRepository.findById(existingPost.getTemplate().getId()).orElseThrow();

            if (!checkRequiredFields(updatedPost.getPostFields(), postTemplate)) {
                throw new RuntimeException("Updated post does not have all required fields");
            }

            // Loop through each updated field and apply changes to existing fields
            for (PostField updatedField : updatedPost.getPostFields()) {
                PostField existingField = postFieldRepository.findById(updatedField.getId())
                        .orElseThrow(() -> new RuntimeException("Post field not found"));

                // Update the existing field's value
                existingField.setValue(updatedField.getValue());
                postFieldRepository.save(existingField);
            }
        } else {
            throw new RuntimeException("Post must have fields");
        }

        // Save and return the modified post
        return postRepository.save(existingPost);
    }

    @Transactional
    public PostVote votePost(Long id, boolean isUpvote){
        User author= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post postToVote =postRepository.findById(id).orElseThrow(()->new NoSuchElementException("Post does not exist"));
        PostVoteKey postVoteKey = new PostVoteKey(author.getId(), postToVote.getId());
        PostVote postVote = PostVote.builder()
                .id(postVoteKey)
                .isUpvote(isUpvote)
                .post(postToVote)
                .user(author)
                .build();
        return postVoteRepository.save(postVote);
    }

    public Long getVoteCount(Long id){
        return postVoteRepository.countVotesForPost(id, true) - postVoteRepository.countVotesForPost(id, false);
    }

    private boolean checkRequiredFields(Set<PostField> postFields, Template postTemplate) {
        Set<DataField> templateFields = postTemplate.getDataFields();
        Set<Long> postRequiredFieldsSet = new HashSet<>();
        Set<Long> templateFieldSet = new HashSet<>();
        Set<Long> postFieldSet = new HashSet<>();

        //Get the set of data field ids and required data field ids
        for (DataField dataField : templateFields) {
           templateFieldSet.add(dataField.getId());
            if (dataField.isRequired())
                postRequiredFieldsSet.add(dataField.getId());
        }

        //Map the existing data fields in the current post and check if all fields belongs to correct template
        for (PostField postField : postFields) {
            postFieldSet.add(postField.getDataField().getId());
            if(!templateFieldSet.contains(postField.getDataField().getId())) return false;
        }

        //Check if all required fields are in the post
        for (Long dataFieldId : postRequiredFieldsSet) {
            if (!postFieldSet.contains(dataFieldId)) return false;
        }
        return true;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow();

        return post;
    }
}
