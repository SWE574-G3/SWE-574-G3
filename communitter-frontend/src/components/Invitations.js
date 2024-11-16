import React, { useCallback, useEffect, useState } from "react";
import { Button, Table } from "react-bootstrap";
import { BsTrash } from "react-icons/bs";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { setErrorMessage } from "../features/errorSlice";
import { url } from "../utilities/config";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { getUserRoleValue } from "../utilities/roles";
import NewInvitationModal from "./NewInvitationModal";

const Invitations = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [invitations, setInvitations] = useState({});
  const dispatch = useDispatch();
  const { id: communityId } = useParams();
  const loggedInUser = useSelector((state) => state.user.loggedInUser);
  const community = useSelector((state) => state.community.visitedCommunity);

  const userRoleValue = getUserRoleValue(loggedInUser, community.id);

  const [showNewInvitationModal, setShowNewInvitationModal] = useState(false);

  const handleOpenNewInvitationModal = () => setShowNewInvitationModal(true);
  const handleCloseNewInvitationModal = () => setShowNewInvitationModal(false);

  const fetchInvitations = useCallback(() => {
    fetchWithOpts(`${url}/invitation?communityId=${communityId}`, {
      method: "GET",
      headers: {},
    })
      .then((data) => setInvitations(data))
      .catch((e) => setErrorMessage(e.message));
  }, [communityId]);

  useEffect(() => {
    fetchInvitations();
  }, [communityId, fetchInvitations, loggedInUser]);
  useEffect(() => {
    fetchInvitations();
  }, [communityId, fetchInvitations, loggedInUser]);

  const handleAddInvitation = async (newInvitation) => {
    setIsLoading(true);
    try {
      await fetchWithOpts(`${url}/invitation`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newInvitation),
      });
      setShowNewInvitationModal(false);
      setIsLoading(false);
      fetchInvitations();
      console.log(newInvitation);
    } catch (err) {
      dispatch(setErrorMessage(err.message));
      setIsLoading(false);
    }
  };

  const handleCancelInvitation = async (invitationId) => {
    if (window.confirm("Are you sure you want to cancel this invitation?")) {
      setIsLoading(true);
      try {
        await fetchWithOpts(
          `${url}/invitation/${invitationId}/cancel-invitation`,
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
          <h2>Invitations</h2>
          <Button
            variant="primary"
            onClick={handleOpenNewInvitationModal}
            className="ms-3"
          >
            New Invitation
          </Button>
        </div>
        {invitations.length > 0 ? (
          <Table bordered responsive>
            <thead>
              <tr>
                <th>Username</th>
                <th>Role</th>
                <th>Invited By</th>
                <th>Date</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {invitations.map((invitation, index) => (
                <tr key={index}>
                  <td>{invitation.user.username}</td>
                  <td>{invitation.role.name}</td>
                  <td>{invitation.sentBy.username}</td>
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
                    {userRoleValue > invitation.role.id &&
                    invitation.invitationStatus === "PENDING" ? (
                      <Button
                        variant="danger"
                        onClick={() => handleCancelInvitation(invitation.id)}
                        size="sm"
                      >
                        <BsTrash />
                      </Button>
                    ) : null}
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <p>No invitation found.</p>
        )}
      </div>
      <NewInvitationModal
        show={showNewInvitationModal}
        onHide={handleCloseNewInvitationModal}
        onSubmit={handleAddInvitation}
        isLoading={isLoading}
      />
    </>
  );
};

export default Invitations;
