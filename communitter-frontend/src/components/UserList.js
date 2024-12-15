import React from "react";
import "../css/UserList.css";

const UserList = ({ users }) => {
  return (
    <div className="user-list">
      {users.map((user) => (
        <div
          key={user.user.id}
          className="d-flex justify-content-between align-items-center py-2"
        >
          {/* User Information */}
          <div>
            <a href={`/user/${user.user.id}`} className="text-decoration-none">
              <h5>{user.user.username}</h5>
            </a>
            <p className="text-muted">{user.user.email}</p>
          </div>
          {/* Follow Date */}
          <div className="text-muted">
            {new Date(user.followedAt).toLocaleDateString()}
          </div>
        </div>
      ))}
    </div>
  );
};

export default UserList;
