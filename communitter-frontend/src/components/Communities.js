import React, { useEffect, useState } from "react";
import Table from "react-bootstrap/Table";
import { Link } from "react-router-dom";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { url } from "../utilities/config";
import { setErrorMessage } from "../features/errorSlice";

const Communities = () => {
  const [communities, setCommunities] = useState([]);

  useEffect(() => {
    fetchWithOpts(`${url}/community/all`, {
      method: "GET",
      headers: {},
    })
      .then((data) => setCommunities(data))
      .catch((e) => setErrorMessage(e.message));
  }, [communities.length]);
  return communities.length ? (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>Community Name</th>
          <th>About</th>
          <th>Public/Private</th>
        </tr>
      </thead>
      <tbody>
        {communities.map((community) => (
          <tr key={community.id}>
            <td>
              <Link to={`/community/${community.id}`}>{community.name}</Link>
            </td>
            <td>{community.about}</td>
            <td>{community.public ? "Public" : "Private"}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  ) : (
    <p>No communities found</p>
  );
};

export default Communities;
