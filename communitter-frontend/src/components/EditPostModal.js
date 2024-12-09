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

    const renderField = (field, index) => {
        const { dataField } = field;
        const { name, dataFieldType, enumValues } = dataField;

        switch (dataFieldType.type) {
            case "enumeration":
                return (
                    <Form.Group key={field.id} className="mb-3">
                        <Form.Label>{name}</Form.Label>
                        <Form.Select
                            value={field.value || ""}
                            onChange={(e) => handleFieldChange(index, e.target.value)}
                        >
                            <option value="">Select an option</option>
                            {enumValues.map((enumObj, idx) => (
                                <option key={idx} value={enumObj.value}>
                                    {enumObj.value}
                                </option>
                            ))}
                        </Form.Select>
                    </Form.Group>
                );
            default:
                return (
                    <Form.Group key={field.id} className="mb-3">
                        <Form.Label>{name}</Form.Label>
                        <Form.Control
                            type="text"
                            value={field.value || ""}
                            onChange={(e) => handleFieldChange(index, e.target.value)}
                        />
                    </Form.Group>
                );
        }
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
                {fields.map((field, index) => renderField(field, index))}
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
