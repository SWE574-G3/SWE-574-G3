import { useState } from "react";
import { setErrorMessage } from "../features/errorSlice";
import { useDispatch } from "react-redux";
import { defaultFetchOpts } from "../utilities/config";
import { url } from "../utilities/config";
import { useNavigate } from "react-router-dom";
import { fetchWiki } from "../utilities/fetchWiki";
export function UserInterestsPage() {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const dispatch = useDispatch();
  const [searchTerm, setSearchTerm] = useState("");
  const [showResults, setShowResults] = useState(false);
  const [searchResults, setSearchResults] = useState([]);
  const [interests, setInterests] = useState([]);

  const searchParams = {
    origin: "*",
    action: "wbsearchentities",
    format: "json",
    language: "en",
    search: "",
  };
  const handleChange = (e) => {
    setSearchTerm(() => e.target.value);
  };
  const handleRemove = (item) => {
    setInterests(interests.filter((interest) => interest !== item));
  };
  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log(searchTerm);
    if (searchTerm.trim()) {
      try {
        searchParams.search = searchTerm;
        const data = await fetchWiki(searchParams, defaultFetchOpts);
        setSearchResults(data.search);
        setShowResults(true);
      } catch (err) {
        dispatch(setErrorMessage(err.message));
        setIsLoading(false);
      }
    } else {
      setSearchResults([]);
      setShowResults(false);
      dispatch(setErrorMessage("Please enter a valid term"));
    }
  };
  const handleAdd = (result) => {
    if (!interests.find((interest) => interest.label === result.label)) {
      setInterests([...interests, result]);
    } else {
      dispatch(setErrorMessage("Label already added"));
    }
  };
  const clearResults = () => {
    setSearchResults([]);
    setShowResults(false);
  };
  return (
    <section className="position-relative h-100 d-grid align-items-center justify-content-center">
      <section className="justify-content-center align-content-center position-relative">
        <div className="position-absolute vw-100 bottom-100 start-50 translate-middle-x mb-3 d-flex flex-wrap justify-content-center">
          {interests.map((item, index) => (
            <p
              key={index}
              className="me-3 p-1 border border-secondary rounded position-relative"
            >
              <span
                onClick={() => handleRemove(item)}
                className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-secondary"
                role="button"
              >
                x
              </span>
              {item.label}
            </p>
          ))}
        </div>
        <form onSubmit={handleSubmit} className="d-flex">
          <div className="mb-3">
            <label htmlFor="name" className="form-label">
              Search the labels in Wikidata
            </label>
            <input
              type="name"
              id="name"
              name="name"
              className="form-control"
              value={searchTerm}
              onChange={handleChange}
              required={true}
              placeholder="Enter your search term..."
            />
          </div>

          <button
            disabled={isLoading}
            className={`btn btn-primary d-block ms-3 my-auto`}
          >
            Search
          </button>
        </form>
        {/* Search results dropdown */}
        {showResults && (
          <div
            className="position-absolute bg-white border rounded w-100"
            style={{ top: "100%", maxHeight: "200px", overflowY: "auto" }}
          >
            <button
              className="btn btn-sm btn-secondary w-100"
              onClick={clearResults}
            >
              Close
            </button>
            <ul className="list-group">
              {searchResults.map((result) => (
                <li
                  key={result.id}
                  role="button"
                  className="list-group-item"
                  onClick={() => handleAdd(result)}
                >
                  {`${result.label} (${result.description})`}
                </li>
              ))}
            </ul>
          </div>
        )}
      </section>
    </section>
  );
}
