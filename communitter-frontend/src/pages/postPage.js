import React, { useState, useEffect } from "react";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import {defaultFetchOpts, url} from "../utilities/config";
import PostCard from "../components/PostCard";
import { PostComments } from '../components/PostComments';
import { useParams, useNavigate } from "react-router-dom";
import { CommentService } from "../utilities/CommentService";
import {useDispatch, useSelector} from "react-redux";
import {deletePost, setVisitedCommunity} from "../features/communitySlice";
import {setErrorMessage} from "../features/errorSlice";

export const PostPage = () => {
    //Post and Comment statements
    const { communityId, id } = useParams();
    const [post, setPost] = useState(null);
    const [error, setError] = useState(null);
    const [comments, setComments] = useState([]);
    const community = useSelector((state) => state.community.visitedCommunity);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    // Commenting Statements
    const [commentingState, setCommentingState] = useState({
        id: null,
        author: null,
        content: ""
    });
    const handleEditPost = (updatedPost) => {
        console.log("CCCC");


        console.log(`updated post = ${JSON.stringify(updatedPost)}`);
        console.log(`URL = ${url}/community/${communityId}/edit-post/${updatedPost.id}`);
        console.log(`GONDERILEN POST = ${JSON.stringify({postFields: updatedPost.postFields})}`);

        // Debug log before fetch call
        console.log("Sending request...");

        fetchWithOpts(`${url}/community/${communityId}/edit-post/${updatedPost.id}`, {
            ...defaultFetchOpts,
            method: "PUT",
            headers: {
                "Content-Type": "application/json", // Explicitly set the Content-Type
            },
            body: JSON.stringify({
                postFields: updatedPost.postFields,
            }),
        })
            .then((response) => {
                console.log("YYYYYY");

                dispatch(
                    setVisitedCommunity({
                        ...community,
                        posts: community.posts.map((post) =>
                            post.id === updatedPost.id
                                ? { ...post, postFields: updatedPost.postFields } // Creating a new object for the updated post
                                : post
                        ),
                    })
                );
                navigate(`/community/${communityId}`)
            })
            .catch((err) => {
                console.error("Request error:", err.message);
                dispatch(setErrorMessage(err.message));
            });
    };
    const handleChange = (e) => {
        setCommentingState({ ...commentingState, [e.target.name]: e.target.value });
    };

    const handleDeleteComment = async (commentId) => {
        if (commentId == null) {
            throw new Error("Comment ID is required");
        }        
        try {
            await CommentService.deleteComment(commentId);
    
            setComments((prevComments) => prevComments.filter((comment) => comment.id !== commentId));
        } catch (error) {
            console.error("Failed to delete comment:", error.message);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const newComment = await CommentService.createComment(commentingState, id);
            console.log("New comment:", newComment);

            if (newComment && newComment.id) {
                setComments([newComment, ...comments]); 
            } else {
                console.error("New comment is missing an 'id'.", newComment);
            }
            setCommentingState({ content: "" });
        } catch (error) {
            console.error(error.message);
        }
    }
    const handleDeletePost = async (postId) => {
        try {
            const communityId = community.id;

            const response = await fetchWithOpts(`${url}/community/${communityId}/delete-post/${postId}`, {
                ...defaultFetchOpts,
                method: "DELETE",
            });
            dispatch(deletePost(postId));

            navigate(`/community/${communityId}`);
        } catch (err) {
            dispatch(setErrorMessage(err.message));
        }
    };

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const postResponse = await fetchWithOpts(`${url}/posts/${id}`, {
                    method: "GET",
                    headers: {}
                });
                if (postResponse) {
                    setPost(postResponse); // Set the post data
                    setComments(postResponse.comments || []);
                }
            } catch (error) {
                setError("Error loading post: " + error.message); // Handle errors
            }
        };

        fetchPost();
    }, [id]); // Dependency on postId, so it fetches new data if postId changes

    if (error) {
        return <div>{error}</div>;
    }

    if (!post) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2>Post Details</h2>
            <PostCard post={post} onDelete={handleDeletePost} handleEditPost={handleEditPost}/>
            <div className="mt-4">
                <h3>Add a Comment</h3>
                <form onSubmit={handleSubmit}>
                    <textarea
                        name="content"
                        value={commentingState.content}
                        onChange={handleChange}
                        rows="4"
                        placeholder="Write your comment here..."
                        className="form-control"
                    />
                    <button type="submit" className="btn btn-primary mt-2">Submit Comment</button>
                </form>
            </div>
            <PostComments comments={comments || []} onDeleteComment={handleDeleteComment} />
        </div>
    );
};

export default PostPage;
