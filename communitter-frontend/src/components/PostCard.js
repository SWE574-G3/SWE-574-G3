import Card from "react-bootstrap/Card";
import CardBody from "react-bootstrap/CardBody";
import CardTitle from "react-bootstrap/CardTitle";
import PostField from "./PostCardField";
import React from "react";

const PostCard = ({ post }) => {
    const { author, postFields, date: timestamp } = post; // Destructure post object

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
        </CardBody>
    </Card>
  );
};

export default PostCard;
