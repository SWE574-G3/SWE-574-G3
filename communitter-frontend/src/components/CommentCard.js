import React, { useState } from "react";
import Card from "react-bootstrap/Card";
import CardTitle from "react-bootstrap/CardTitle";
import CardBody from "react-bootstrap/CardBody";
import Button from "react-bootstrap/esm/Button";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

export const CommentCard = ({ comment, onDeleteComment, onEditComment }) => {
    const { author, date: timestamp, content, id } = comment;
    const navigate = useNavigate();
    const loggedInUser = useSelector((state) => state.user.loggedInUser);

    // States for editing
    const [isEditing, setIsEditing] = useState(false);
    const [editedContent, setEditedContent] = useState(content);

    const directToUserView = () => {
        navigate(`/user/${author.id}`);
    };

    const handleDeleteComment = () => {
        onDeleteComment(id);
    };

    const handleEditChange = (e) => {
        setEditedContent(e.target.value);
    };

    const handleSaveEdit = () => {
        const updatedComment = {
            id: id,
            author: author, 
            date: timestamp,
            content: editedContent, 
        };

        onEditComment(updatedComment, id); 

        setIsEditing(false); 
    };

    const handleCancelEdit = () => {
        setIsEditing(false); 
        setEditedContent(content); 
    };

    return (
        <Card className="mb-3">
            <CardTitle onClick={directToUserView} style={{ cursor: "pointer" }}>
                {author.username} -{" "}
                {new Date(timestamp).toLocaleDateString("tr-TR", {
                    year: "numeric",
                    month: "2-digit",
                    day: "2-digit",
                    hour: "2-digit",
                    minute: "2-digit",
                    hour12: false,
                })}
            </CardTitle>
            <CardBody>
                {isEditing ? (
                    <div>
                        <textarea
                            value={editedContent}
                            onChange={handleEditChange}
                            rows="4"
                            className="form-control"
                        />
                        <div className="mt-2">
                            <Button variant="primary" onClick={handleSaveEdit}>
                                Save
                            </Button>
                            <Button variant="secondary" onClick={handleCancelEdit} className="ms-2">
                                Cancel
                            </Button>
                        </div>
                    </div>
                ) : (
                    <p>{content}</p>
                )}

                {author.id === loggedInUser.id && !isEditing && (
                    <div className="d-flex justify-content-between">
                        <Button variant="primary" onClick={() => setIsEditing(true)}>
                            Edit
                        </Button>
                        <Button variant="danger" onClick={handleDeleteComment}>
                            Delete
                        </Button>
                    </div>
                )}
            </CardBody>
        </Card>
    );
};
