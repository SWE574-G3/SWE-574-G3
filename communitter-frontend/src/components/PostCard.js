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
    const [voteCount, setVoteCount] = useState(0); // State for vote count

    useEffect(() => {
        fetchWithOpts(`${url}/posts/voteCount/${post.id}`, {
            method: "GET",
            headers: {},
        })
            .then((data) => setVoteCount(data))
            .catch((e) => setErrorMessage(e.message));
    }, [post.id]);

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
            <p className="text-muted mt-2">Vote Count: {voteCount}</p>
        </CardBody>
    </Card>
  );
};

export default PostCard;
