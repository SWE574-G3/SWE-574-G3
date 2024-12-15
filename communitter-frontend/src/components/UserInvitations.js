import React, { useCallback, useEffect, useState } from "react";
import { Button, Table } from "react-bootstrap";
import { BsCheckCircle, BsXCircle } from "react-icons/bs";
import { useDispatch } from "react-redux";
import { setErrorMessage } from "../features/errorSlice";
import { url } from "../utilities/config";
import { fetchWithOpts } from "../utilities/fetchWithOptions";

const UserInvitations = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [invitations, setInvitations] = useState({});
  const dispatch = useDispatch();

  const fetchInvitations = useCallback(() => {
    fetchWithOpts(`${url}/invitation`, {
      method: "GET",
      headers: {},
    })
      .then((data) => setInvitations(data))
      .catch((e) => setErrorMessage(e.message));
  }, []);

  useEffect(() => {
    fetchInvitations();
  }, [fetchInvitations]);

  const handleAcceptInvitation = async (invitationId) => {
    setIsLoading(true);
    try {
      await fetchWithOpts(
        `${url}/invitation/${invitationId}/accept-invitation`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      setIsLoading(false);
      window.location.reload();
    } catch (err) {
      dispatch(setErrorMessage(err.message));
      setIsLoading(false);
    }
  };

  const handleRejectInvitation = async (invitationId) => {
    if (window.confirm("Are you sure you want to reject this invitation?")) {
      setIsLoading(true);
      try {
        await fetchWithOpts(
          `${url}/invitation/${invitationId}/reject-invitation`,
          {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        setIsLoading(false);
        fetchInvitations();
      } catch (err) {
        dispatch(setErrorMessage(err.message));
        setIsLoading(false);
      }
    }
  };

  return (
    <>
      <div className="mt-5">
        <div className="d-flex justify-content-between align-items-center">
          <h2>My Invitations</h2>
        </div>
        {invitations.length > 0 ? (
          <Table bordered responsive>
            <thead>
              <tr>
                <th>Community</th>
                <th>Role</th>
                <th>Invited By</th>
                <th>Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {invitations.map((invitation, index) => (
                <tr key={index}>
                  <td>{invitation.community.name}</td>
                  <td>{invitation.role.name}</td>
                  <td>
                    <a
                      href={`/user/${invitation.sentBy.id}`}
                      className="text-decoration-none"
                    >
                      {invitation.sentBy.username}
                    </a>
                  </td>
                  <td>
                    {new Date(invitation.sentAt).toLocaleDateString("tr-TR", {
                      year: "numeric",
                      month: "2-digit",
                      day: "2-digit",
                      hour: "2-digit",
                      minute: "2-digit",
                      hour12: false,
                    })}
                  </td>
                  <td>{invitation.invitationStatus}</td>
                  <td>
                    <div className="d-flex gap-2">
                      <Button
                        variant="success"
                        onClick={() => handleAcceptInvitation(invitation.id)}
                        size="sm"
                        disabled={isLoading}
                      >
                        <BsCheckCircle />
                      </Button>
                      <Button
                        variant="danger"
                        onClick={() => handleRejectInvitation(invitation.id)}
                        size="sm"
                        disabled={isLoading}
                      >
                        <BsXCircle />
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <p>No invitation found.</p>
        )}
      </div>
    </>
  );
};

export default UserInvitations;
