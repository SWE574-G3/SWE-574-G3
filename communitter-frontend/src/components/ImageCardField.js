import CardText from "react-bootstrap/CardText";
import CardImg from "react-bootstrap/CardImg";
import {useEffect, useState} from "react";
import { Modal, Button, Form } from "react-bootstrap";

const ImageCardField = ({ value, dataFieldName,  isEditable, onChange  }) => {


    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];
        if (selectedFile) {

            const reader = new FileReader();
            reader.onloadend = () => {
                if (onChange) {
                    onChange(reader.result);
                }
            };
            reader.readAsDataURL(selectedFile);
        }
    };

    return (
        <CardText>
            {isEditable ? (
                <Form.Group className="mt-2">
                    <Form.Label>Upload Image</Form.Label>
                    <Form.Control
                        type="file"
                        accept="image/*"
                        onChange={handleFileChange}
                    />
                </Form.Group>
            ) : (
                <>
                    {dataFieldName}:{" "}
                    <CardImg
                        src={value}
                        crossOrigin="user-credentials"
                        alt="Post Image"
                        variant="top"
                        style={{ maxWidth: "150px", maxHeight: "150px" }}
                    />
                </>
            )}
        </CardText>
    );
};

export default ImageCardField;
