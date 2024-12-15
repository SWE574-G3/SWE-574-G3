import React, { useEffect, useState, useRef } from "react";
import ProfileService from "../utilities/ProfileService";
import { url, tokenName } from "../utilities/config";
import axios from "axios";
import Cropper from "react-cropper";
import "cropperjs/dist/cropper.css";
import { useSelector } from "react-redux";
import "../css/UserProfile.css"; // Import the CSS for animations

export const UserProfile = ({ shownUser }) => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadStatus, setUploadStatus] = useState("");
  const [profilePicture, setProfilePicture] = useState("");
  const [showCropper, setShowCropper] = useState(false);
  const [fieldToUpdate, setFieldToUpdate] = useState(null);
  const [newFieldValue, setNewFieldValue] = useState("");
  const [editMode, setEditMode] = useState(false);
  const [localShownUser, setLocalShownUser] = useState(shownUser);
  const [notification, setNotification] = useState({ message: "", type: "" }); // Notification state
  const cropperRef = useRef(null);

  const token = localStorage.getItem(tokenName);
  const loggedInUser = useSelector((state) => state.user.loggedInUser);
  const OwnProfile = localShownUser.id === loggedInUser.id;
  const defaultProfilePicture =
      "https://beforeigosolutions.com/wp-content/uploads/2021/12/dummy-profile-pic-300x300-1.png";

  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const validatePassword = (password) => {
    const passwordRegex =
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return passwordRegex.test(password);
  };

  const showNotification = (message, type) => {
    setNotification({ message, type });
    setTimeout(() => {
      setNotification({ message: "", type: "" }); // Clear notification after 3 seconds
    }, 3000);
  };

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        setSelectedFile(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleFormSubmit = async (event) => {
    event.preventDefault();
    const cropper = cropperRef.current?.cropper;

    if (cropper) {
      const croppedImage = cropper.getCroppedCanvas().toDataURL("image/png");

      const base64ToBlob = (base64) => {
        const [header, data] = base64.split(",");
        const mime = header.match(/:(.*?);/)[1];
        const binary = atob(data);
        const array = [];
        for (let i = 0; i < binary.length; i++) {
          array.push(binary.charCodeAt(i));
        }
        return new Blob([new Uint8Array(array)], { type: mime });
      };

      const croppedBlob = base64ToBlob(croppedImage);

      try {
        await ProfileService.uploadProfilePicture(croppedBlob, localShownUser.id);
        setUploadStatus("Profile image updated successfully!");
        fetchProfilePicture();
        setShowCropper(false);
        showNotification("Profile image updated successfully!", "success");
      } catch (error) {
        console.error("Error uploading profile image:", error);
        setUploadStatus("Failed to upload profile image.");
        showNotification("Failed to upload profile image.", "error");
      }
    }
  };

  const fetchProfilePicture = async () => {
    try {
      const response = await axios.get(
          `${url}/images/user/${localShownUser.id}/profile-picture`,
          { headers: { Authorization: `Bearer ${token}` } }
      );
      setProfilePicture(response.data || defaultProfilePicture);
    } catch (error) {
      setProfilePicture(defaultProfilePicture);
      console.error(error);
    }
  };

  const handleUpdateField = async () => {
    if (fieldToUpdate === "email" && !validateEmail(newFieldValue)) {
      showNotification("Invalid email format. Please enter a valid email.", "error");
      return;
    }

    if (fieldToUpdate === "password" && !validatePassword(newFieldValue)) {
      showNotification(
          "Password must be at least 8 characters long, include one uppercase letter, one lowercase letter, one number, and one special character.",
          "error"
      );
      return;
    }

    try {
      const updateUrl = `${url}/user/update/${fieldToUpdate}/${localShownUser.id}`;
      const params = new URLSearchParams();
      params.append(fieldToUpdate, newFieldValue);

      await axios.put(updateUrl, null, {
        params,
        headers: { Authorization: `Bearer ${token}` },
      });

      setLocalShownUser((prev) => ({
        ...prev,
        [fieldToUpdate]: newFieldValue,
      }));

      showNotification(`${fieldToUpdate} updated successfully!`, "success");
      setFieldToUpdate(null);
      setNewFieldValue("");
    } catch (error) {
      console.error(`Error updating ${fieldToUpdate}:`, error);
      showNotification(`Failed to update ${fieldToUpdate}.`, "error");
    }
  };

  useEffect(() => {
    fetchProfilePicture();
  }, [localShownUser.id]);

  return (
      <div className="container mt-4">
        {/* Notification Pop-Up */}
        {notification.message && (
            <div
                className={`notification ${
                    notification.type === "error" ? "notification-error" : "notification-success"
                }`}
            >
              {notification.message}
            </div>
        )}
        {/* Edit Profile Button */}
        <div className="row">
          <div className="col-md-12 text-end">
            {OwnProfile && (
                <button
                    className={`btn ${editMode ? "btn-danger" : "btn-warning"}`}
                    onClick={() => setEditMode(!editMode)}
                >
                  {editMode ? "Close Edit Mode" : "Edit Profile"}
                </button>
            )}
          </div>
        </div>

        {/* Profile Image & Info */}
        <div className="row mt-3">
          <div className="col-md-2">
            <div className="user-image-update-image-button">
            <img
                src={profilePicture}
                alt="Profile"
                className="img-thumbnail rounded-circle d-block img-fluid mx-auto"
            />
            {OwnProfile && editMode && (

                  <button
                      className="btn btn-primary"
                      onClick={() => setShowCropper(!showCropper)}
                  >
                    Update Profile Image
                  </button>

            )}
            </div>
          </div>
          <div className="col-md-8">
            <div className="card">
              <div className="card-header">
                <h2>{localShownUser.username}</h2>
              </div>
              <ul className="list-group list-group-flush">
                <li className="list-group-item d-flex justify-content-between align-items-center">
                  <span>About: {localShownUser.about}</span>
                  {OwnProfile && editMode && (
                      <button
                          className="btn btn-secondary"
                          onClick={() => setFieldToUpdate("about")}
                      >
                        Update About
                      </button>
                  )}
                </li>
                <li className="list-group-item d-flex justify-content-between align-items-center">
                  <span>Header: {localShownUser.header}</span>
                  {OwnProfile && editMode && (
                      <button
                          className="btn btn-secondary"
                          onClick={() => setFieldToUpdate("header")}
                      >
                        Update Header
                      </button>
                  )}
                </li>
                <li className="list-group-item d-flex justify-content-between align-items-center">
                  <span>Email: {localShownUser.email}</span>
                  {OwnProfile && editMode && (
                      <button
                          className="btn btn-secondary"
                          onClick={() => setFieldToUpdate("email")}
                      >
                        Update Email
                      </button>
                  )}
                </li>
                {OwnProfile && editMode && (
                    <li className="list-group-item d-flex justify-content-between align-items-center">
                      <button
                          className="btn btn-warning"
                          onClick={() => setFieldToUpdate("password")}
                      >
                        Update Password
                      </button>
                    </li>
                )}
              </ul>
            </div>
          </div>
        </div>

        {/* Profile Image Cropper */}
        {showCropper && (
            <form id="uploadForm" onSubmit={handleFormSubmit} className="mt-3">
              <input
                  type="file"
                  name="imageFile"
                  onChange={handleFileChange}
                  className="form-control my-3"
              />
              {selectedFile && (
                  <Cropper
                      src={selectedFile}
                      style={{ height: 300, width: "100%" }}
                      aspectRatio={1}
                      guides={true}
                      ref={cropperRef}
                  />
              )}
              <div className="d-flex mt-3">
                <button type="submit" className="btn btn-primary me-2">
                  Crop & Upload
                </button>
              </div>
            </form>
        )}

        {/* Field Update Form */}
        {editMode&&fieldToUpdate && (
            <div className="mt-3">
              <h4>Update {fieldToUpdate}</h4>
              <input
                  type="text"
                  className="form-control my-2"
                  placeholder={`Enter new ${fieldToUpdate}`}
                  value={newFieldValue}
                  onChange={(e) => setNewFieldValue(e.target.value)}
              />
              <button className="btn btn-success" onClick={handleUpdateField}>
                Save
              </button>
            </div>
        )}

        {/* Status */}
        <div className="mt-2">
          <p>{uploadStatus}</p>
        </div>
      </div>
  );
};
