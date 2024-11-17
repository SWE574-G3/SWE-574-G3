import React, { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const EditPostModal = ({ post, show, onClose, onSave }) => {
    const [fields, setFields] = useState(post?.postFields || []);


    const handleFieldChange = (index, value) => {
        setFields((prevFields) => {
            const updatedFields = [...prevFields];
            updatedFields[index] = { ...updatedFields[index], value };
            return updatedFields;
        });
    };

    const handleSave = () => {
        onSave({ ...post, postFields: fields });
        onClose();
    };

    return (
        <Modal show={show} onHide={onClose}>
            <Modal.Header closeButton>
                <Modal.Title>Edit Post</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {fields.map((field, index) => (
                    <Form.Group key={field.id} className="mb-3">
                        <Form.Label>{field.dataField.name}</Form.Label>
                        <Form.Control
                            type="text"
                            value={field.value}
                            onChange={(e) => handleFieldChange(index, e.target.value)}
                        />
                    </Form.Group>
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
