import Card from "react-bootstrap/Card";
import CardBody from "react-bootstrap/CardBody";
import CardTitle from "react-bootstrap/CardTitle";
import PostField from "./PostCardField";
import Button from "react-bootstrap/Button";
import React, { useState, useEffect } from "react";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { url } from "../utilities/config";
import {setErrorMessage} from "../features/errorSlice";
import { useNavigate } from "react-router-dom";
import EditPostModal from "./EditPostModal";
import {useDispatch, useSelector} from "react-redux";
import { useLocation } from "react-router-dom";

const PostCard = ({ post, onDelete, onEdit,handleEditPost }) => {
  const { author, postFields, date: timestamp, id } = post; // Destructure post object
    const community = useSelector((state) => state.community.visitedCommunity);
    const [showEditModal,setShowEditModal]=useState(false);
    const [voteCount, setVoteCount] = useState(0);
    const loggedInUser = useSelector((state) => state.user.loggedInUser);
    const navigate = useNavigate();
    const location = useLocation();
    const currentPath = location.pathname;
    const dispatch = useDispatch();

    console.log(currentPath);
    console.log(`community info = ${JSON.stringify(community)}`)
    console.log(`post info = ${JSON.stringify(post)}`)
    // Function to fetch the latest vote count
    const fetchVoteCount = async () => {
        fetchWithOpts(`${url}/posts/${post.id}/voteCount`, {
            method: "GET",
            headers: {},
        })
            .then((data) => setVoteCount(data))
            .catch((e) => setErrorMessage(e.message));
    };
    const handleEditClick = () => {
        setShowEditModal(true)
    };


    // Initial fetch of the vote count when the component mounts
    useEffect(() => {
        fetchVoteCount();
    }, [post.id]);

    // Directing to postview
    const directToPostView = () => {
        let communityId;
        if(currentPath==="/home") {
            communityId = post.community.id
        } else{
            communityId=community.id
        }
        navigate(`/community/${communityId}/posts/${post.id}`);
      };

    // Handle upvotexs
    const handleUpvote = async () => {
        try {
            const response = await fetchWithOpts(`${url}/posts/${post.id}/vote?isUpvote=true`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
            });
            if (response) {
                await fetchVoteCount();
            }
        } catch (error) {
            dispatch(setErrorMessage("Failed to upvote: " + error.message));
        }
    };

    // Handle downvote
    const handleDownvote = async () => {
        try {
            const response = await fetchWithOpts(`${url}/posts/${post.id}/vote?isUpvote=false`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
            });
            if (response) {
                await fetchVoteCount();
            }
        } catch (error) {
            dispatch(setErrorMessage("Failed to downvote: " + error.message));
        }
    };
    
    const handleDeleteClick = () => {
        if (window.confirm("Are you sure you want to delete this post?")) {
            onDelete(id);
        }
    };

  return (
    <Card className="mb-3">
    <CardTitle
    style={{
        cursor: "pointer",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
    }}
    >
    <span onClick={directToPostView}>
        {author.username} -{" "}
        {new Date(timestamp).toLocaleDateString("tr-TR", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
        })}{" "}
        - Template: {post.template?.name}
    </span>
    {currentPath=="/home" && (<span
        onClick={() => navigate(`/community/${post.community?.id}`)}
        style={{
        fontStyle: "italic",
        fontWeight: "normal",
        fontSize: "0.85rem",
        color: "blue",
        cursor: "pointer",
        textDecoration: "underline",
        }}
    >
        via {post.community?.name}
    </span>)}

    </CardTitle>
      <CardBody>
        {postFields.map((postField) => (
          <PostField key={postField.id} postField={postField} />
        ))}
          <div className="d-flex">
            {post.author.id == loggedInUser.id && (
              <Button variant="danger" onClick={handleDeleteClick} className="me-2">
                    Delete
                </Button>
                )}
              {post.author.id === loggedInUser.id && (
              <Button variant="primary" onClick={handleEditClick}>
                  Edit
              </Button>
              )}
          </div>
          <div className="vote-buttons mt-2  d-flex align-items-center" style={{ position: "absolute", bottom: "10px", right: "10px" }}>
              <i onClick={handleUpvote} className="bi bi-arrow-up me-2" style={{ cursor: "pointer", color: "green" }}></i>
              <span>{voteCount}</span>
              <i onClick={handleDownvote} className="bi bi-arrow-down ms-2" style={{ cursor: "pointer", color: "red" }}></i>
          </div>
      </CardBody>

      {showEditModal && (
            <EditPostModal
                post={post}
                show={showEditModal}
                onClose={() => setShowEditModal(false)}
                onSave={handleEditPost}
            />
        )}
    </Card>
  );
};

export default PostCard;
