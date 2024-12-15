import ImageCardField from "./ImageCardField"; // Import ImagePostField component
import GeolocationCardField from "./GeolocationCardField"; // Import GeolocationPostField component
import CardText from "react-bootstrap/CardText";
import {  Form } from "react-bootstrap";

const PostCardField = ({ postField, isEditable, onFieldChange }) => {
  const { dataField, value } = postField;
  const { type } = dataField.dataFieldType;

    const handleChange = (e) => {
        if (onFieldChange) {
            onFieldChange(e.target.value);
        }
    };

  switch (type) {
    case "string":
    case "number":
    case "enumeration":
    case "date":

        return isEditable ? (
            <Form.Group className="mb-3">
                <Form.Label>{dataField.name}</Form.Label>
                <Form.Control type="text" value={value} onChange={handleChange} />
            </Form.Group>
        ) : (
            <CardText>
                {dataField.name}: {value}
            </CardText>
        );
    case "geolocation":
      return (
          <GeolocationCardField
              dataField={dataField}
              value={value}
              isEditable={isEditable}
              onChange={onFieldChange}
          />
      );
    case "image":
        return (
            <ImageCardField
                dataFieldName={dataField.name}
                value={value}
                isEditable={isEditable}
                onChange={onFieldChange}
            />
        );
    default:
      console.error(`Unknown data field type: ${type}`);
      return null;
  }
};

export default PostCardField;
