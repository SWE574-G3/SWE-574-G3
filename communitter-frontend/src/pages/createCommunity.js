import { useState } from "react";
import { setErrorMessage } from "../features/errorSlice";
import { useDispatch } from "react-redux";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import { url } from "../utilities/config";
import { useNavigate } from "react-router-dom";
export function CommunityCreationPage() {
  const [communityState, setCommunityState] = useState({
    name: "",
    about: "",
    public: true,
  });
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const dispatch = useDispatch();
  const handleChange = (e) => {
    if (e.target.name === "public") {
      setCommunityState({
        ...communityState,
        [e.target.name]: e.target.checked,
      });
      console.log(communityState);
    } else {
      setCommunityState({ ...communityState, [e.target.name]: e.target.value });
    }
  };
  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      const data = await fetchWithOpts(`${url}/community/create`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(communityState),
      });
      console.log(data);
      setIsLoading(false);
      navigate(`/community/${data.id}`);
    } catch (err) {
      dispatch(setErrorMessage(err.message));
      setIsLoading(false);
    }
  };
  return (
    <section className="position-relative h-100 d-grid align-items-center justify-content-center">
      <section className="d-grid justify-content-center align-content-center">
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label htmlFor="name" className="form-label">
              Community Name
            </label>
            <input
              type="name"
              id="name"
              name="name"
              className="form-control"
              value={communityState.name}
              onChange={handleChange}
              required={true}
            />
          </div>

          <div className="mb-3">
            <label htmlFor="about" className="form-label">
              About
            </label>
            <input
              type="about"
              id="about"
              name="about"
              className="form-control"
              value={communityState.about}
              onChange={handleChange}
              required={true}
            />
          </div>
          <div className="mb-3 form-check">
            <label htmlFor="public" className="form-label">
              Public
            </label>
            <input
              type="checkbox"
              id="public"
              name="public"
              checked={communityState.public}
              className="form-check-input"
              onChange={handleChange}
            />
          </div>
          <button
            disabled={isLoading}
            className={`btn btn-primary d-block mx-auto`}
          >
            Create
          </button>
        </form>
      </section>
    </section>
  );
}
