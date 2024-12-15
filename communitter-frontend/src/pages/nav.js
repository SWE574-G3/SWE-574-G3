import { useNavigate } from "react-router-dom";
import { useLogout } from "../utilities/auth";
import { useSelector } from "react-redux";
import { useState } from "react";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { defaultFetchOpts, url } from "../utilities/config";
import "../css/nav.css";
import { Link } from "react-router-dom";

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
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);

  const handleSearchTermChange = (event) => {
    setSearchTerm(event.target.value);
    if (event.target.value === "") {
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
    <nav className="nav" style={{ height: "56px" }}>
      <div className="container d-flex justify-content-between align-items-center w-100">
        {/* Hamburger button */}
        <button
          className="navbar-toggler"
          type="button"
          onClick={() => setIsDrawerOpen(!isDrawerOpen)}
        >
          ☰
        </button>

        <Link to="/" className="text-dark text-decoration-none fs-4">
          Communitter
        </Link>

        <li className="nav-item position-relative">
          <form
            className="d-flex align-items-center"
            onSubmit={handleSearchButtonClick}
          >
            <div className="input-group">
              <input
                className="form-control"
                type="search"
                placeholder="Search"
                aria-label="Search"
                value={searchTerm}
                onChange={handleSearchTermChange}
              />
              <button className="btn btn-outline-secondary" type="submit">
                <i className="bi bi-search"></i>{" "}
                {/* Optional: Use Bootstrap icons */}
              </button>
            </div>
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

        {/* Shade for the rest of the page */}
        <div
          className={`drawer-backdrop ${isDrawerOpen ? "show" : ""}`}
          onClick={() => setIsDrawerOpen(false)} // Close drawer when backdrop is clicked
        ></div>

        {/* Drawer */}
        <div className={`drawer ${isDrawerOpen ? "open" : ""}`}>
          {/* Close Button */}
          <button
            className="drawer-close-btn"
            onClick={() => setIsDrawerOpen(false)}
          >
            ✕
          </button>
          <ul className="nav nav-pills">
            <li
                className="nav-item"
                onClick={() => {
                  navigate("/home");
                  setIsDrawerOpen(false);
                }}
            >
              <button className="nav-link">Home</button>
            </li>
            <li
                className="nav-item"
                onClick={() => {
                  navigate("/communities");
                }}
            >
              <button className="nav-link">Communities</button>
            </li>
            <li
                className="nav-item"
                onClick={() => {
                  navigate("/community/create");
                  setIsDrawerOpen(false);
                }}
            >
              <button className="nav-link">Create Community</button>
            </li>
            <li
                className="nav-item"
                onClick={() => {
                  navigate(`/user/${loggedInUser.id}`);
                  setIsDrawerOpen(false);
                }}
            >
              <button className="nav-link">Profile</button>
            </li>
            <li
                className="nav-item"
                onClick={() => {
                  navigate(`/user/interest`);
                  setIsDrawerOpen(false);
                }}
            >
              <button className="nav-link">Interests</button>
            </li>
            <li onClick={logout} className="nav-item">
              <button className="nav-link">Logout</button>
            </li>
          </ul>
        </div>

        <ul className="nav nav-pills desktop-nav">
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
                navigate("/communities");
              }}
          >
            <button className="nav-link">Communities</button>
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
          <li
              className="nav-item"
              onClick={() => {
                navigate(`/user/interest`);
              }}
          >
            <button className="nav-link">Interests</button>
          </li>
          <li onClick={logout} className="nav-item">
            <button className="nav-link">Logout</button>
          </li>
        </ul>
      </div>
    </nav>
  );
}
