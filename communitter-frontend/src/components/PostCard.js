import Card from "react-bootstrap/Card";
import CardBody from "react-bootstrap/CardBody";
import CardTitle from "react-bootstrap/CardTitle";
import PostField from "./PostCardField";
import Button from "react-bootstrap/Button";
import React, { useState, useEffect } from "react";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { url } from "../utilities/config";
import {setErrorMessage} from "../features/errorSlice";
import { useNavigate } from "react-router-dom";

const PostCard = ({ post, onDelete }) => {
  const { author, postFields, date: timestamp, id } = post; // Destructure post object

    const [voteCount, setVoteCount] = useState(0);
    const navigate = useNavigate();

    // Function to fetch the latest vote count
    const fetchVoteCount = async () => {
        fetchWithOpts(`${url}/posts/${post.id}/voteCount`, {
            method: "GET",
            headers: {},
        })
            .then((data) => setVoteCount(data))
            .catch((e) => setErrorMessage(e.message));
    };

    // Initial fetch of the vote count when the component mounts
    useEffect(() => {
        fetchVoteCount();
    }, [post.id]);

    // Directing to postview
    const directToPostView = () => {
        navigate(`/posts/${post.id}`);
      };

    // Handle upvote
    const handleUpvote = async () => {
        try {
            const response = await fetchWithOpts(`${url}/posts/${post.id}/vote?isUpvote=true`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
            });
            if (response) {
                await fetchVoteCount();
            }
        } catch (error) {
            setErrorMessage("Failed to upvote: " + error.message);
        }
    };

    // Handle downvote
    const handleDownvote = async () => {
        try {
            const response = await fetchWithOpts(`${url}/posts/${post.id}/vote?isUpvote=false`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
            });
            if (response) {
                await fetchVoteCount();
            }
        } catch (error) {
            setErrorMessage("Failed to downvote: " + error.message);
        }
    };
    
    const handleDeleteClick = () => {
        if (window.confirm("Are you sure you want to delete this post?")) {
            onDelete(id);
        }
    };
  return (
    <Card className="mb-3">
      <CardTitle onClick={directToPostView} style={{cursor: "pointer"}}>
        {author.username} -{" "}
        {new Date(timestamp).toLocaleDateString("tr-TR", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit",
          hour12: false,
        })}{" "}
        - Template: {post.template.name}
      </CardTitle>
      <CardBody>
        {postFields.map((postField) => (
          <PostField key={postField.id} postField={postField} />
        ))}
          <div className="d-flex justify-content-between">
              <Button variant="danger" onClick={handleDeleteClick}>
                  Delete
              </Button>
          </div>
          <div className="vote-buttons mt-2  d-flex align-items-center" style={{ position: "absolute", bottom: "10px", right: "10px" }}>
              <i onClick={handleUpvote} className="bi bi-arrow-up me-2" style={{ cursor: "pointer", color: "green" }}></i>
              <span>{voteCount}</span>
              <i onClick={handleDownvote} className="bi bi-arrow-down ms-2" style={{ cursor: "pointer", color: "red" }}></i>
          </div>
      </CardBody>
    </Card>
  );
};

export default PostCard;
