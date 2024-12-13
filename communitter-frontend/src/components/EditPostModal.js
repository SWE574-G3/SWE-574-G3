import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import GeolocationCardField from "./GeolocationCardField";
import ImageCardField from "./ImageCardField";
import PostCardField from "./PostCardField";

const EditPostModal = ({ post, show, onClose, onSave }) => {
    const [fields, setFields] = useState(post?.postFields || []);
    const {postFields} = post


    const handleFieldChange = (index, value) => {
        setFields((prevFields) => {
            const updatedFields = [...prevFields];
            updatedFields[index] = { ...updatedFields[index], value };
            return updatedFields;
        });
    };

    const handleSave = async () => {
        await onSave({ ...post, postFields: fields });
        onClose();
    };

    return (
        <Modal show={show} onHide={onClose}>
            <Modal.Header closeButton>
                <Modal.Title>Edit Post</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {fields.map((field, index) => (
                    <PostCardField
                        key={field.id}
                        postField={field}
                        isEditable={true}
                        onFieldChange={(newValue) => handleFieldChange(index, newValue)}
                    />
                ))}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onClose}>
                    Cancel
                </Button>
                <Button variant="primary" onClick={handleSave}>
                    Save Changes
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default EditPostModal;
