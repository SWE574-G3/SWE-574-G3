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
        Community targetCommunity = communityRepository.findById(communityId).orElseThrow(()->new NoSuchElementException("Community does not exist"));
        Post postToDelete =postRepository.findById(id).orElseThrow(()->new NoSuchElementException("Post does not exist"));
        User currentUser = authUtil.getCurrentUser();

        logger.info(String.valueOf(currentUser));

        if (currentUser.getId().equals(postToDelete.getAuthor().getId()) ||
                currentUser.getId().equals(targetCommunity.getCreator().getId())) {
            postRepository.deleteById(id);
        } else {
            throw new NotAuthorizedException("You are not authorized to delete this post");
        }
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
}
