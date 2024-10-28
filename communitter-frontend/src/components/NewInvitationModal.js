import React, { useState } from "react";
import { Button, Form, Modal } from "react-bootstrap";
import { useSelector } from "react-redux";

const NewInvitationModal = ({ show, onHide, onSubmit, isLoading }) => {
  const [username, setUsername] = useState("");
  const [role, setRole] = useState(1);
  const loggedInUser = useSelector((state) => state.user.loggedInUser);
  const community = useSelector((state) => state.community.visitedCommunity);

  const userSubscription = loggedInUser.subscriptions.find(
    (subscription) => subscription.id.communityId === community.id
  );

  const userRoleValue = userSubscription ? userSubscription.role.id : 0;

  const handleSubmit = (e) => {
    e.preventDefault();
    if (username.trim()) {
      onSubmit({
        username: username.trim(),
        roleId: role,
        sentAt: new Date(),
        communityId: community.id,
      });
    }
  };

  const roles = [
    { value: 3, label: "owner" },
    { value: 2, label: "moderator" },
    { value: 1, label: "user" },
  ];

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>New Invitation</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Form.Group className="mb-3" controlId="formUsername">
            <Form.Label>Username</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
            {!username.trim() && (
              <Form.Text className="text-danger">
                Username is required.
              </Form.Text>
            )}
          </Form.Group>

          <Form.Group className="mb-3" controlId="formRole">
            <Form.Label>Role</Form.Label>
            <Form.Select value={role} onChange={(e) => setRole(e.target.value)}>
              {roles.map((role) => (
                <option
                  key={role.value}
                  value={role.value}
                  disabled={userRoleValue <= role.value}
                >
                  {role.label}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          Close
        </Button>
        <Button
          variant="primary"
          onClick={handleSubmit}
          disabled={!username.trim() || isLoading}
        >
          Submit
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default NewInvitationModal;
