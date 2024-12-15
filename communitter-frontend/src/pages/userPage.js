import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { url } from "../utilities/config";
import { setVisitedUser } from "../features/userSlice";
import { setErrorMessage } from "../features/errorSlice";
import { UserProfile } from "../components/UserProfile";
import { Subscriptions } from "../components/Subscriptions";
import UserInvitations from "../components/UserInvitations";

export function UserPage() {
  const loggedInUser = useSelector((state) => state.user.loggedInUser);
  console.log(loggedInUser);
  const visitedUser = useSelector((state) => state.user.visitedUser);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [shownUser, setShownUser] = useState(loggedInUser);
  const params = useParams();
  const [isLoading, setIsLoading] = useState(true);
  const [isOwnProfile, setIsOwnProfile] = useState(true);

  useEffect(() => {
    async function getUser() {
      // eslint-disable-next-line eqeqeq
      if (params.id === String(loggedInUser.id)) {
        setIsOwnProfile(true);
        setShownUser(loggedInUser);
        setIsLoading(false);
        console.log("my profile");
        return;
      }
      try {
        const data = await fetchWithOpts(`${url}/user/${params.id}`, {
          method: "GET",
          headers: {},
        });
        dispatch(setVisitedUser(data));
        setShownUser(data);
        setIsLoading(false);
        setIsOwnProfile(false);
      } catch (err) {
        dispatch(setErrorMessage(err.message));
        navigate(`/user/${loggedInUser.id}`);
        console.log(err.message);
      }
    }
    getUser();

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [
    loggedInUser.id,
    visitedUser.id,
    params,
    loggedInUser.subscriptions.length,
    visitedUser.subscriptions.length,
    isOwnProfile,
  ]);
  return (
    !isLoading && (
      <>
        <UserProfile shownUser={shownUser} />
        {isOwnProfile ? <UserInvitations /> : null}
        <Subscriptions subscriptions={shownUser.subscriptions} />
      </>
    )
  );
}
