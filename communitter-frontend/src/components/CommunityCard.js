import { useEffect, useState } from "react";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { url } from "../utilities/config";
import { useDispatch } from "react-redux";
import { setErrorMessage } from "../features/errorSlice";
import { useNavigate } from "react-router-dom";

export const CommunityCard = ({ subscription, userRole }) => {
  const [community, setCommunity] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  useEffect(() => {
    async function getCommunity() {
      try {
        const community = await fetchWithOpts(
          `${url}/community/${subscription.id.communityId}`,
          {
            method: "GET",
            headers: {},
          }
        );
        setCommunity(community);
        setIsLoading(false);
      } catch (err) {
        dispatch(
          setErrorMessage(
            `An error occured for community with id ${subscription.id.communityId}`
          )
        );
        setIsLoading(false);
      }
    }
    getCommunity();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [dispatch, community?.id]);

  return (
      <div
          className="card mb-3"
          role="button"
          onClick={() => {
            navigate(`/community/${community.id}`);
          }}
      >
        <div className="card-body">
          {!isLoading && (
              <>
                <h5 className="card-title">
                  {community.name}
                </h5>
                <p className="card-text">{community.about}</p>
                <div className="d-flex justify-content-between align-items-center flex-wrap">
                  <div className="d-flex align-items-center gap-2">

                    <span className="badge bg-primary">{userRole}</span>

                    {!community.public && (
                        <span className="badge bg-danger">Private</span>
                    )}
                  </div>
                </div>
              </>
          )}
        </div>
      </div>
  );
};
