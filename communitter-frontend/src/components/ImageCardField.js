import CardText from "react-bootstrap/CardText";
import CardImg from "react-bootstrap/CardImg";

const ImageCardField = ({ value, dataFieldName }) => {
  return (
    <CardText>
      {dataFieldName}:{" "}
      <CardImg
        src={value}
        crossOrigin="user-credentials"
        alt="Post Image"
        variant="top"
        style={{ maxWidth: "150px", maxHeight: "150px" }}
      />
    </CardText>
  );
};

export default ImageCardField;
