import React, { useState, useEffect } from "react";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { url } from "../utilities/config"; 
import PostCard from "../components/PostCard";
import { PostComments } from '../components/PostComments';
import { useParams } from "react-router-dom"; 

export const PostPage = () => {
  const { id } = useParams(); // Get the post ID from the URL
  const [post, setPost] = useState(null); // State to store the post data
  const [error, setError] = useState(null); // State to handle any errors
  const [comments, setComments] = useState([]);

  useEffect(() => {
    const fetchPost = async () => {
      try {
        const postResponse = await fetchWithOpts(`${url}/posts/${id}`, {
          method: "GET",
          headers: {}
        });
        if (postResponse) {
          setPost(postResponse); // Set the post data
          setComments(postResponse.comments);
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
      <PostCard post={post}/>
      <PostComments comments={comments || []} />
    </div>
  );
};

export default PostPage;
