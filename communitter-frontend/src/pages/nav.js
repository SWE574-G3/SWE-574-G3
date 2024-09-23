import { useNavigate } from "react-router-dom";
import { useLogout } from "../utilities/auth";
import { useSelector } from "react-redux";
import { useState } from "react";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { defaultFetchOpts, url } from "../utilities/config";

export function Navbar() {
  const logout = useLogout();
  const loggedInUser = useSelector((state) => state.user.loggedInUser);
  const navigate = useNavigate();

  const [searchTerm, setSearchTerm] = useState("");
  const [searchResults, setSearchResults] = useState({
    communities: [],
    users: [],
  });
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const handleSearchTermChange = (event) => {
    setSearchTerm(event.target.value);
    if (event.target.value == "") {
      setIsDropdownOpen(false);
    }
  };

  const handleSearchButtonClick = async (e) => {
    e.preventDefault();
    if (!searchTerm) return;
    const results = await fetchWithOpts(
      `${url}/search?input=${searchTerm.toLowerCase()}`,
      defaultFetchOpts
    );
    console.log(results);
    setSearchResults(results);
    setIsDropdownOpen(true);
  };

  const handleDropdownClose = () => {
    setIsDropdownOpen(false);
    setSearchTerm("");
  };

  const handleResultClick = (item) => {
    if (item.type === "user") {
      navigate(`/user/${item.id}`);
    } else if (item.type === "community") {
      navigate(`/community/${item.id}`);
    }
    handleDropdownClose();
  };

  return (
    <nav className="nav">
      <ul className="nav nav-pills">
        <li
          className="nav-item"
          onClick={() => {
            navigate("/home");
          }}
        >
          <button className="nav-link">Home</button>
        </li>
        <li
          className="nav-item"
          onClick={() => {
            navigate("/community/create");
          }}
        >
          <button className="nav-link">Create Community</button>
        </li>
        <li
          className="nav-item"
          onClick={() => {
            navigate(`/user/${loggedInUser.id}`);
          }}
        >
          <button className="nav-link">Profile</button>
        </li>
        <li className="nav-item position-relative">
          <form
            className="d-flex align-items-center"
            onSubmit={handleSearchButtonClick}
          >
            <input
              className="form-control me-2"
              type="search"
              placeholder="Search"
              aria-label="Search"
              value={searchTerm}
              onChange={handleSearchTermChange}
            />
            <button className="btn btn-outline-secondary">Search</button>
          </form>

          {isDropdownOpen && (
            <div className="position-absolute top-100 start-0 bg-white border shadow-lg">
              {" "}
              <ul className="list-group mb-0">
                {" "}
                <li className="list-group-item">Users:</li>
                {searchResults.users.length > 0 ? (
                  searchResults.users.map((user) => (
                    <li key={user.id} className="list-group-item">
                      <button
                        className="btn btn-link"
                        onClick={() =>
                          handleResultClick({ type: "user", id: user.id })
                        }
                      >
                        {user.username}
                      </button>
                    </li>
                  ))
                ) : (
                  <li className="list-group-item text-muted">No users found</li>
                )}
                <li className="list-group-item border-top">Communities:</li>
                {searchResults.communities.length > 0 ? (
                  searchResults.communities.map((community) => (
                    <li key={community.id} className="list-group-item">
                      <button
                        className="btn btn-link"
                        onClick={() =>
                          handleResultClick({
                            type: "community",
                            id: community.id,
                          })
                        }
                      >
                        {community.name}
                      </button>
                    </li>
                  ))
                ) : (
                  <li className="dropdown-item text-muted">
                    No communities found
                  </li>
                )}
              </ul>
            </div>
          )}
        </li>
        <li onClick={logout} className="nav-item">
          <button className="nav-link">Logout</button>
        </li>
      </ul>
    </nav>
  );
}
