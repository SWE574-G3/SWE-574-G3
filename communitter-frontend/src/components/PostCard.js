import Card from "react-bootstrap/Card";
import CardBody from "react-bootstrap/CardBody";
import CardTitle from "react-bootstrap/CardTitle";
import PostField from "./PostCardField";
import Button from "react-bootstrap/Button";

const PostCard = ({ post, onDelete }) => {
  const { author, postFields, date: timestamp, id } = post; // Destructure post object

    const handleDeleteClick = () => {
        if (window.confirm("Are you sure you want to delete this post?")) {
            onDelete(id);
        }
    };
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
          <div className="d-flex justify-content-between">
              <Button variant="danger" onClick={handleDeleteClick}>
                  Delete
              </Button>
          </div>
      </CardBody>
    </Card>
  );
};

export default PostCard;
