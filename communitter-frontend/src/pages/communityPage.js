import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import ActivityStreamCard from "../components/ActivityStreamCard";
import AdvancedSearchModal from "../components/AdvancedSearch";
import { ModalWrapper } from "../components/ModalWrapper";
import MakePostModal from "../components/postModal";
import { TemplateModal } from "../components/templateModal";
import { WikidataInterface } from "../components/wikidataInterface";
import "../css/communityPage.css";
import { deletePost, setVisitedCommunity } from "../features/communitySlice";
import { setErrorMessage } from "../features/errorSlice";
import { defaultFetchOpts, url } from "../utilities/config";
import { fetchWithOpts } from "../utilities/fetchWithOptions";

import { useNavigate, useParams } from "react-router-dom";
import { Members } from "../components/communityMembers";
import { Posts } from "../components/communityPosts";
import Invitations from "../components/Invitations";
import { getUserRoleValue } from "../utilities/roles";
export const CommunityPage = () => {
  const community = useSelector((state) => state.community.visitedCommunity);
  const [posts, setPosts] = useState(community.posts);
  const [editPost, setEditPost] = useState(null);
  const [isPostEdited, setIsPostEdited] = useState(false);
  const loggedInUser = useSelector((state) => state.user.loggedInUser);
  console.log(community);
  const [isSubscribed, setIsSubscribed] = useState(false);
  const [subsButton, setSubsButton] = useState(true);
  const [isTemplateOpen, setIsTemplateOpen] = useState(false);
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [isPostOpen, setIsPostOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [role, setRole] = useState("visitor");
  const [showLabelModal, setShowLabelModal] = useState(false);
  const [communityCreatorId, setCommunityCreatorId] = useState(0);
  const [selectedAction, setSelectedAction] = useState("");
  const [activities, setActivities] = useState([]);
  const [isActivityModelOpen, setIsActivityModelOpen] = useState(false);

  const dispatch = useDispatch();
  const params = useParams();
  const navigate = useNavigate();

  const userRoleValue = getUserRoleValue(loggedInUser, community.id);

  const handleModalOpen = () => {
    setIsActivityModelOpen(true);
    console.log(`is activity model oepn = ${isActivityModelOpen}`);
  };

  const handleModalClose = () => {
    setIsActivityModelOpen(false);
  };

  const handleSubscription = async () => {
    setSubsButton(false);
    try {
      if (!isSubscribed) {
        const subscription = await fetchWithOpts(
          `${url}/community/subscribe/${params.id}`,
          { ...defaultFetchOpts, method: "POST" }
        );
        console.log(subscription);
        setIsSubscribed(true);
      } else {
        const unsubscription = await fetchWithOpts(
          `${url}/community/unsubscribe/${params.id}`,
          { ...defaultFetchOpts, method: "DELETE" }
        );
        console.log("left community");
        setIsSubscribed(false);
      }
    } catch (err) {
      dispatch(setErrorMessage(err.message));
    }
    setSubsButton(true);
  };
  const handleDeletePost = async (postId) => {
    try {
      const communityId = community.id;

      const response = await fetchWithOpts(
        `${url}/community/${communityId}/delete-post/${postId}`,
        {
          ...defaultFetchOpts,
          method: "DELETE",
        }
      );
      dispatch(deletePost(postId));
    } catch (err) {
      dispatch(setErrorMessage(err.message));
    }
  };

  const handleEditPost = (updatedPost) => {
    const communityId = params.id;

    fetchWithOpts(
      `${url}/community/${communityId}/edit-post/${updatedPost.id}`,
      {
        ...defaultFetchOpts,
        method: "PUT",
        headers: {
          "Content-Type": "application/json", // Explicitly set the Content-Type
        },
        body: JSON.stringify({
          postFields: updatedPost.postFields,
        }),
      }
    )
      .then((response) => {
        dispatch(
          setVisitedCommunity({
            ...community,
            posts: community.posts.map((post) =>
              post.id === updatedPost.id
                ? { ...post, postFields: updatedPost.postFields } // Creating a new object for the updated post
                : post
            ),
          })
        );
        setIsPostEdited(true);
      })
      .catch((err) => {
        console.error("Request error:", err.message);
        dispatch(setErrorMessage(err.message));
      });
  };
  const getCommunityCreatorId = (community) => {
    for (let i = 0; i < community.subscriptions.length; i++) {
      if (community.subscriptions[i].role.name === "creator") {
        setCommunityCreatorId(community.subscriptions[i].id.userId);
      }
    }
  };

  const fetchActivities = async () => {
    try {
      const response = await fetchWithOpts(
        `${url}/community/${params.id}/activity-stream`,
        { ...defaultFetchOpts, method: "GET" }
      );
      setActivities(response);
    } catch (err) {
      console.error("Failed to load activities");
    } finally {
      setIsLoading(false);
    }
  };

  const handleActionFilterChange = (event) => {
    setSelectedAction(event.target.value);
  };

  useEffect(() => {
    fetchActivities().then((r) => console.log("activities fetched"));

    const intervalId = setInterval(fetchActivities, 1000);

    return () => clearInterval(intervalId);
  }, [params.id]);
  useEffect(() => {
    async function getCommunity() {
      try {
        const visitedCommunity = await fetchWithOpts(
          `${url}/community/${params.id}`,
          {
            method: "GET",
            headers: {},
          }
        );
        dispatch(setVisitedCommunity(visitedCommunity));
        const currentSub = community.subscriptions.find(
          (subscription) => subscription.id.userId === loggedInUser.id
        );
        setIsSubscribed(currentSub ? true : false);
        setRole(currentSub ? currentSub.role.name : role);
        setPosts(visitedCommunity.posts);
        getCommunityCreatorId(visitedCommunity);
        setIsLoading(false);
      } catch (err) {
        dispatch(
          setErrorMessage(`An error occured for community with id ${params.id}`)
        );
        navigate("/home");
        setIsLoading(false);
      }
    }

    getCommunity();
    setIsPostEdited(false);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [
    dispatch,
    community?.id,
    community?.subscriptions.length,
    community?.posts.length,
    community?.templates.length,
    isSubscribed,
    isPostEdited,
  ]);

  return (
    !isLoading && (
      <div className="community-page">
        <div>
          <div className="container mt-3">
            <div className="card mb-3">
              <div className="row g-0">
                <div className="col-md-4">
                  <img
                    src="https://beforeigosolutions.com/wp-content/uploads/2021/12/dummy-profile-pic-300x300-1.png"
                    alt={community.name}
                    className="img-fluid rounded-start"
                  />
                </div>
                <div className="col-md-8">
                  <div className="card-body">
                    <div className="community-info-section">
                      <div>
                        <h5 className="card-title">{community.name}</h5>
                        <p className="card-text">{community.about}</p>
                        <span className="badge bg-success">
                          {community.public ? "Public" : "Private"}
                        </span>
                      </div>

                      <div className="join-button">
                        <div className="d-flex justify-content-between align-items-center">
                          {community.public ? (
                            <button
                              className={`btn ${
                                isSubscribed ? "btn-danger" : "btn-primary"
                              }`}
                              onClick={handleSubscription}
                            >
                              {isSubscribed
                                ? "Leave Community"
                                : "Join Community"}
                            </button>
                          ) : (
                            <button className="btn btn-secondary disabled">
                              Private Community
                            </button>
                          )}
                        </div>
                      </div>
                    </div>
                    <button
                      onClick={() => {
                        setIsTemplateOpen(true);
                      }}
                      className="btn btn-primary mt-3"
                    >
                      Create Template
                    </button>
                    <button
                      onClick={() => {
                        setIsPostOpen(true);
                      }}
                      className="btn btn-primary mt-3 mx-3"
                    >
                      Make a Post
                    </button>
                    {(role === "owner" || role === "creator") && (
                      <button
                        onClick={() => {
                          setShowLabelModal(true);
                        }}
                        className="btn btn-primary mt-3 mx-3"
                      >
                        Labels
                      </button>
                    )}
                  </div>
                </div>
              </div>
            </div>
            <div className="filter-post-reset-filter-buttons">
              <button
                onClick={() => setIsFilterOpen(true)}
                className="btn-secondary"
              >
                Filter Posts
              </button>
              <button
                onClick={() => setPosts(community.posts)}
                className="btn-secondary mx-3"
              >
                Reset Filters
              </button>

              <button
                className="activity-drawer-toggle d-md-none"
                onClick={handleModalOpen}
              >
                Open Activity Stream
              </button>
            </div>

            <AdvancedSearchModal
              posts={posts}
              setPosts={setPosts}
              isOpen={isFilterOpen}
              setIsOpen={setIsFilterOpen}
              templates={community.templates}
            ></AdvancedSearchModal>
            <Posts
              posts={posts}
              communityId={community.id}
              onDelete={handleDeletePost}
              onEdit={(post) => setEditPost(post)}
              handleEditPost={handleEditPost}

            />
            <Members members={community.subscriptions} />
            {!community.public && userRoleValue !== 0 ? <Invitations /> : null}
            <TemplateModal
              isOpen={isTemplateOpen}
              setIsOpen={setIsTemplateOpen}
            ></TemplateModal>
            <MakePostModal
              templates={community.templates}
              isOpen={isPostOpen}
              setIsOpen={setIsPostOpen}
            />
            <ModalWrapper show={showLabelModal} handleClose={setShowLabelModal}>
              <WikidataInterface
                url={`${url}/recommendation/community/${community.id}/labels`}
              />
            </ModalWrapper>
          </div>
        </div>

        {isActivityModelOpen && (
          <div className="activity-modal-overlay">
            <div className="activity-modal">
              <button className="close-modal-button" onClick={handleModalClose}>
                X
              </button>
              <div>
                <h3>Activity Stream</h3>
                <label htmlFor="actionFilter">Filter by action:</label>
                <select
                  id="actionFilter"
                  onChange={handleActionFilterChange}
                  value={selectedAction}
                >
                  <option value="">All Actions</option>
                  <option value="CREATE">Create</option>
                  <option value="UPVOTE">Upvote</option>
                  <option value="DOWNVOTE">Downvote</option>
                  <option value="JOIN">Join</option>
                  <option value="COMMENT">Comment</option>
                </select>
                <ActivityStreamCard
                  communityId={community.id}
                  activities={activities}
                  selectedAction={selectedAction}
                  communityCreatorId={communityCreatorId}
                />
              </div>
            </div>
          </div>
        )}

        {/* Normal Activity Feed for larger screens */}
        <div className="activity-feed-parent d-none d-md-block">
          <div className="container mt-5">
            <label htmlFor="actionFilter">Filter by action:</label>
            <select
              id="actionFilter"
              onChange={handleActionFilterChange}
              value={selectedAction}
            >
              <option value="">All Actions</option>
              <option value="CREATE">Create</option>
              <option value="UPVOTE">Upvote</option>
              <option value="DOWNVOTE">Downvote</option>
              <option value="JOIN">Join</option>
              <option value="COMMENT">Comment</option>
            </select>
            <ActivityStreamCard
              communityId={community.id}
              activities={activities}
              selectedAction={selectedAction}
              communityCreatorId={communityCreatorId}
            />
          </div>
        </div>
      </div>
    )
  );
};
