import { Posts } from "../components/communityPosts";
import { Members } from "../components/communityMembers";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { defaultFetchOpts, url } from "../utilities/config";
import {deletePost, setVisitedCommunity} from "../features/communitySlice";
import { setErrorMessage } from "../features/errorSlice";
import { TemplateModal } from "../components/templateModal";
import MakePostModal from "../components/postModal";
import AdvancedSearchModal from "../components/AdvancedSearch";
export const CommunityPage = () => {
  const community = useSelector((state) => state.community.visitedCommunity);
  const [posts, setPosts] = useState(community.posts);
  console.log(posts);
  const loggedInUser = useSelector((state) => state.user.loggedInUser);
  console.log(community);
  const [isSubscribed, setIsSubscribed] = useState(false);
  const [subsButton, setSubsButton] = useState(true);
  const [isTemplateOpen, setIsTemplateOpen] = useState(false);
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [isPostOpen, setIsPostOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const dispatch = useDispatch();
  const params = useParams();
  const navigate = useNavigate();
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

      const response = await fetchWithOpts(`${url}/community/${communityId}/deletePost/${postId}`, {
        ...defaultFetchOpts,
        method: "DELETE",
      });
      dispatch(deletePost(postId));
    } catch (err) {
      dispatch(setErrorMessage(err.message));
    }
  };
  useEffect(() => {
    console.log("entered useEffect");
    console.log(isSubscribed);
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
        setIsSubscribed(
          community.subscriptions.some(
            (subscription) => subscription.id.userId == loggedInUser.id
          )
        );
        setPosts(visitedCommunity.posts);
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
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [
    dispatch,
    community?.id,
    community?.subscriptions.length,
    community?.posts.length,
    community?.templates.length,
    isSubscribed,
  ]);
  return (
    !isLoading && (
      <div className="container mt-5">
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
                <h5 className="card-title">Community Name: {community.name}</h5>
                <p className="card-text">About: {community.about}</p>
                <div className="d-flex justify-content-between align-items-center">
                  <span className="badge bg-success">
                    {community.public ? "Public" : "Private"}
                  </span>

                  {community.public ? (
                    <button
                      className="btn btn-primary"
                      onClick={handleSubscription}
                    >
                      {isSubscribed ? "Leave Community" : "Join Community"}
                    </button>
                  ) : (
                    <button className="btn btn-secondary disabled">
                      Private Community
                    </button>
                  )}
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
              </div>
            </div>
          </div>
        </div>
        <button onClick={() => setIsFilterOpen(true)} className="btn-secondary">
          Filter Posts
        </button>
        <button
          onClick={() => setPosts(community.posts)}
          className="btn-secondary mx-3"
        >
          Reset Filters
        </button>
        <AdvancedSearchModal
          posts={posts}
          setPosts={setPosts}
          isOpen={isFilterOpen}
          setIsOpen={setIsFilterOpen}
          templates={community.templates}
        ></AdvancedSearchModal>
        <Posts posts={posts} onDelete={handleDeletePost}/> <Members members={community.subscriptions} />
        <TemplateModal
          isOpen={isTemplateOpen}
          setIsOpen={setIsTemplateOpen}
        ></TemplateModal>
        <MakePostModal
          templates={community.templates}
          isOpen={isPostOpen}
          setIsOpen={setIsPostOpen}
        />
      </div>
    )
  );
};
