import ImageCardField from "./ImageCardField"; // Import ImagePostField component
import GeolocationCardField from "./GeolocationCardField"; // Import GeolocationPostField component
import CardText from "react-bootstrap/CardText";

const PostCardField = ({ postField }) => {
  const { dataField, value } = postField;
  const { type } = dataField.dataFieldType;

  switch (type) {
    case "string":
    case "number":
    case "date":
      return (
        <CardText>
          {dataField.name}: {value}
        </CardText>
      );
    case "geolocation":
      return <GeolocationCardField dataField={dataField} value={value} />;
    case "image":
      return <ImageCardField dataFieldName={dataField.name} value={value} />;
    default:
      console.error(`Unknown data field type: ${type}`);
      return null;
  }
};

export default PostCardField;
