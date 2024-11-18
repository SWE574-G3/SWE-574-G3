import React, { useState, useEffect } from "react";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import {defaultFetchOpts, url} from "../utilities/config";
import PostCard from "../components/PostCard";
import { PostComments } from '../components/PostComments';
import { useParams, useNavigate } from "react-router-dom";
import { CommentService } from "../utilities/CommentService";
import {useDispatch, useSelector} from "react-redux";
import {deletePost} from "../features/communitySlice";
import {setErrorMessage} from "../features/errorSlice";

export const PostPage = () => {
    //Post and Comment statements
    const { id } = useParams();
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

    const handleChange = (e) => {
        setCommentingState({ ...commentingState, [e.target.name]: e.target.value });
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
            <PostCard post={post} onDelete={handleDeletePost}/>
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
            <PostComments comments={comments || []} />
        </div>
    );
};

export default PostPage;
