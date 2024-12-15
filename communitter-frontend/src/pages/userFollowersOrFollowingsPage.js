import UserList from "../components/UserList";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { useCallback, useState, useEffect } from "react";
import { setErrorMessage } from "../features/errorSlice";
import { url } from "../utilities/config";
import { useParams } from "react-router-dom";
import "../css/UserFollowersOrFollowings.css";

export function UserFollowersOrFollowingsPage({ type }) {
  const { id: userId } = useParams();
  const [users, setUsers] = useState([]);

  const title = type === "followers" ? "Followers" : "Followings";

  const fetchFollowers = useCallback(() => {
    fetchWithOpts(`${url}/user/${userId}/followers`, {
      method: "GET",
      headers: {},
    })
      .then((data) => setUsers(data))
      .catch((e) => setErrorMessage(e.message));
  }, [userId]);

  const fetchFollowings = useCallback(() => {
    fetchWithOpts(`${url}/user/${userId}/followees`, {
      method: "GET",
      headers: {},
    })
      .then((data) => setUsers(data))
      .catch((e) => setErrorMessage(e.message));
  }, [userId]);

  useEffect(() => {
    if (type === "followers") fetchFollowers();
    else if (type === "followings") fetchFollowings();
  }, [fetchFollowers, fetchFollowings, type]);
  return (
    <div className="followers-following-page">
      {/* Title */}
      <div className="title">
        <h2>{title}</h2>
      </div>

      {/* Scrollable List */}
      <div className="scrollable-list">
        <UserList users={users} />
      </div>
    </div>
  );
}
