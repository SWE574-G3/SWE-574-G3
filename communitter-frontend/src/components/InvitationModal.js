import React, { useState } from "react";
import { Button, Modal, Table } from "react-bootstrap";
import { BsTrash } from "react-icons/bs";
import { setErrorMessage } from "../features/errorSlice";
import { url } from "../utilities/config";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import NewInvitationModal from "./NewInvitationModal";
import { useDispatch } from "react-redux";

const InvitationModal = ({ show, onHide }) => {
  const [isLoading, setIsLoading] = useState(false);
  const dispatch = useDispatch();

  const [showNewInvitationModal, setShowNewInvitationModal] = useState(false);

  const handleOpenNewInvitationModal = () => setShowNewInvitationModal(true);
  const handleCloseNewInvitationModal = () => setShowNewInvitationModal(false);

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
      console.log(newInvitation);
    } catch (err) {
      dispatch(setErrorMessage(err.message));
      setIsLoading(false);
    }
  };

  // Example invitation data
  const invitationData = [
    {
      communityName: "Sample Community",
      invitedBy: "John Doe",
      username: "johndoe123",
      role: "Member",
      date: "2024-10-28",
    },
  ];

  const handleCancelInvitation = (userId) => {
    alert(`Invitation for ${userId} has been canceled.`);
  };

  return (
    <>
      <Modal show={show} onHide={onHide} size="lg" centered>
        <Modal.Header closeButton>
          <Modal.Title>Invitation Details</Modal.Title>
          <Button
            variant="primary"
            onClick={handleOpenNewInvitationModal}
            className="ms-3"
          >
            New Invitation
          </Button>
        </Modal.Header>
        <Modal.Body>
          <Table bordered responsive>
            <thead>
              <tr>
                <th>Username</th>
                <th>Role</th>
                <th>Invited By</th>
                <th>Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {invitationData.map((invitation, index) => (
                <tr key={index}>
                  <td>{invitation.username}</td>
                  <td>{invitation.role}</td>
                  <td>{invitation.invitedBy}</td>
                  <td>{invitation.date}</td>
                  <td>
                    <Button
                      variant="danger"
                      onClick={() => handleCancelInvitation(invitation.id)}
                      size="sm"
                    >
                      <BsTrash /> {/* Trash icon */}
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onHide}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
      <NewInvitationModal
        show={showNewInvitationModal}
        onHide={handleCloseNewInvitationModal}
        onSubmit={handleAddInvitation}
        isLoading={isLoading}
      />
    </>
  );
};

export default InvitationModal;
