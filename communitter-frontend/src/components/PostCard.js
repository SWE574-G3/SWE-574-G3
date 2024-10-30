import Card from "react-bootstrap/Card";
import CardBody from "react-bootstrap/CardBody";
import CardTitle from "react-bootstrap/CardTitle";
import PostField from "./PostCardField";
import React, { useState, useEffect } from "react";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { url } from "../utilities/config";
import {setErrorMessage} from "../features/errorSlice";

const PostCard = ({ post }) => {
    const { author, postFields, date: timestamp } = post; // Destructure post object
    const [voteCount, setVoteCount] = useState(0);

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

    return (
        <Card className="mb-3">
            <CardTitle>
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
                <div className="vote-buttons mt-2">
                    <button onClick={handleUpvote} className="btn btn-success me-2">
                        Upvote
                    </button>
                    <span>{voteCount}</span>
                    <button onClick={handleDownvote} className="btn btn-danger ms-2">
                        Downvote
                    </button>
                </div>
            </CardBody>
        </Card>
    );
};

export default PostCard;
