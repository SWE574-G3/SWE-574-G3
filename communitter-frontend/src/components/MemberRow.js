import { useEffect, useState } from "react";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { url } from "../utilities/config";
import { useDispatch } from "react-redux";
import { setErrorMessage } from "../features/errorSlice";
import { useNavigate } from "react-router-dom";

export const MemberRow = ({ subscription, userRole }) => {
  const [member, setMember] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  useEffect(() => {
    async function getCommunity() {
      try {
        const user = await fetchWithOpts(
          `${url}/user/${subscription.id.userId}`,
          {
            method: "GET",
            headers: {},
          }
        );
        setMember(user);
        setIsLoading(false);
      } catch (err) {
        dispatch(
          setErrorMessage(
            `An error occured for user with id ${subscription.id.userId}`
          )
        );
        setIsLoading(false);
      }
    }
    getCommunity();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [dispatch, member?.id]);
  return (
    !isLoading && (
      <tr
        role="button"
        onClick={() => {
          navigate(`/user/${member.id}`);
        }}
      >
        <td>{member.username}</td>
        <td>{userRole}</td>
      </tr>
    )
  );
};
