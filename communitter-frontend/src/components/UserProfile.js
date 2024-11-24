import React, {useEffect, useState} from 'react';
import ProfileService from '../utilities/ProfileService';
import {url, tokenName} from "../utilities/config"; // Adjust the import path as needed
import axios from "axios";

export const UserProfile = ({ shownUser }) => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [uploadStatus, setUploadStatus] = useState('');
  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };
  const token = localStorage.getItem(tokenName);
  const [profilePicture, setProfilePicture] = useState('');


  const handleFormSubmit = (event) => {
    event.preventDefault();

    if (selectedFile) {
      ProfileService.uploadProfilePicture(selectedFile,shownUser.id)
          .then((response) => {
            console.log(response.data); // Handle the successful response
            setUploadStatus('File uploaded successfully!');
          })
          .catch((error) => {
            console.error(error); // Handle errors
            setUploadStatus('Error uploading file. Please try again.');
          });
    } else {
      setUploadStatus('No file selected.');
    }
  };


  useEffect(() => {
    const fetchProfilePicture = async () => {
      try {
        const response = await axios.get(`${url}/Images/user/${shownUser.id}/profile-picture`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        console.log(response.data);
        console.log("Success");
        setProfilePicture(response.data); // directly use base64 image data
        console.log("Success22222");
      } catch (error) {
        setProfilePicture("https://beforeigosolutions.com/wp-content/uploads/2021/12/dummy-profile-pic-300x300-1.png")
        console.error('Failed to fetch profile picture:', error);
      }
    };

    fetchProfilePicture();
  }, [shownUser.id]);

  return (
      <div className="container mt-5">
        <div className="row">
          <div className="col-md-2">
            <img
                src={profilePicture}
                alt="Profile"
                className="img-thumbnail rounded-circle d-block img-fluid mx-auto"
            />
          </div>
          <div className="col-md-8">
            <form id="uploadForm" onSubmit={handleFormSubmit}>
              <input type="file" name="imageFile" onChange={handleFileChange} />
              <button type="submit">Upload</button>
            </form>
            <div className="mt-2">
              <p>{uploadStatus}</p>
            </div>
            <div className="card mt-3">
              <div className="card-header">
                <h2>{shownUser.username}</h2>
              </div>
              <ul className="list-group list-group-flush">
                <li className="list-group-item">Email: {shownUser.email}</li>
                <li className="list-group-item">Header: {shownUser.header}</li>
                <li className="list-group-item">About: {shownUser.about}</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
  );
};