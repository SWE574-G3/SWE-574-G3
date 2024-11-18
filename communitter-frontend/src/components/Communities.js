import React, { useEffect, useState } from "react";
import Table from "react-bootstrap/Table";
import { Link } from "react-router-dom";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { url } from "../utilities/config";
import { setErrorMessage } from "../features/errorSlice";

const Communities = () => {
  const [communities, setCommunities] = useState([]);
  const [showRecommended,setShowRecommended]=useState(false);

  useEffect(() => {
    let path=showRecommended?"/recommendation/communities":"/community/all"
    fetchWithOpts(`${url}${path}`, {
      method: "GET",
      headers: {},
    })
      .then((data) => setCommunities(data))
      .catch((e) => setErrorMessage(e.message));
  }, [showRecommended]);
  return (
    <div className="container mt-4">
    {/* Switch for toggling between Recommended and All Communities */}
    <div className="d-flex align-items-center mb-4">
      <label className="me-2">Recommended Communities</label>
      <div className="form-check form-switch">
        <input
          className="form-check-input"
          type="checkbox"
          id="recommendedSwitch"
          checked={showRecommended}
          onChange={(e) => setShowRecommended(e.target.checked)}
        />
      </div>
    </div>
      {

      communities.length ? (
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
    )
      }
    
  </div>
    )

    
};

export default Communities;
