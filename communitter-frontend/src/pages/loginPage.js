import { useEffect, useState } from "react";
import { AuthService } from "../utilities/auth";
import { useNavigate } from "react-router-dom";
import { extractId } from "../utilities/jwt";
import { tokenName } from "../utilities/config";
export function LoginPage() {
  const [isSignUp, setIsSignUp] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [warning, setWarning] = useState("");
  const [loginState, setLoginState] = useState({
    password: "",
    email: "",
  });
  const [registerState, setRegisterState] = useState({
    username: "",
    about: "",
    header: "",
    avatar: "",
  });
  const navigate = useNavigate();
  const handleChange = (e) => {
    if (loginState[e.target.name] !== undefined) {
      setLoginState({ ...loginState, [e.target.name]: e.target.value });
    } else {
      setRegisterState({ ...registerState, [e.target.name]: e.target.value });
    }
  };
  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    if (isSignUp) {
      console.log(registerState);
      console.log(loginState);
      const registerResult = await AuthService.register({
        ...registerState,
        ...loginState,
      });
      if (!registerResult[0]) {
        setWarning(registerResult[1]);
      } else {
        const id = extractId(localStorage.getItem(tokenName));
        navigate("/user/" + id);
        console.log(registerResult);
      }
    } else {
      const loginResult = await AuthService.login(loginState);
      if (!loginResult[0]) {
        setWarning(loginResult[1]);
      } else {
        console.log(loginResult);
        const id = extractId(localStorage.getItem(tokenName));
        navigate("/user/" + id);
      }
    }
    setIsLoading(false);
  };

  useEffect(() => {
    let warningTimeout = setTimeout(() => {
      setWarning("");
    }, 2000);
    return () => clearTimeout(warningTimeout);
  }, [warning]);

  return (
    <section className="position-relative h-100 d-grid align-items-center justify-content-center">
      <div className="text-center warning-container position-absolute top-0 start-50 translate-middle-x">
        <span className="warning">{warning}</span>
      </div>
      <section className="d-grid justify-content-center align-content-center">
        <form onSubmit={handleSubmit}>
          {isSignUp && (
            <>
              <div className="mb-3">
                <label htmlFor="username" className="form-label">
                  Username
                </label>
                <input
                  type="text"
                  id="username"
                  name="username"
                  className="form-control"
                  value={registerState.username}
                  onChange={handleChange}
                  required={true}
                />
              </div>
              <div className="mb-3">
                <label htmlFor="about" className="form-label">
                  About
                </label>
                <input
                  type="text"
                  id="about"
                  name="about"
                  className="form-control"
                  value={registerState.about}
                  onChange={handleChange}
                  required={false}
                />
              </div>
              <div className="mb-3">
                <label htmlFor="header" className="form-label">
                  Header
                </label>
                <input
                  type="text"
                  id="header"
                  name="header"
                  className="form-control"
                  value={registerState.header}
                  onChange={handleChange}
                  required={false}
                />
              </div>
              <div className="mb-3">
                <label htmlFor="avatar" className="form-label">
                  Avatar
                </label>
                <input
                  type="text"
                  id="avatar"
                  name="avatar"
                  className="form-control"
                  value={registerState.avatar}
                  onChange={handleChange}
                  required={false}
                />
              </div>
            </>
          )}
          <div className="mb-3">
            <label htmlFor="email" className="form-label">
              Email
            </label>
            <input
              type="email"
              id="email"
              name="email"
              className="form-control"
              value={loginState.email}
              onChange={handleChange}
              required={true}
            />
          </div>

          <div className="mb-3">
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <input
              type="password"
              id="password"
              name="password"
              className="form-control"
              value={loginState.password}
              onChange={handleChange}
              required={true}
              minLength={8}
            />
            <div id="passwordHelp" className="form-text">
              At least 8 characters
            </div>
          </div>
          <button
            className={`btn btn-primary d-block mx-auto`}
            disabled={isLoading}
          >
            {isSignUp ? "Sign Up" : "Sign In"}
          </button>
        </form>
        <p className="mx-auto"> Do you have an Account?</p>
        <button
          className={`btn ${isSignUp ? "" : "btn-info"}`}
          onClick={() => {
            setIsSignUp(false);
            setRegisterState({
              password: "",
              email: "",
              username: "",
              about: "",
              header: "",
              avatar: "",
            });
          }}
        >
          YES
        </button>
        <button
          className={`btn ${isSignUp ? "btn-info" : ""}`}
          onClick={() => {
            setIsSignUp(true);
          }}
        >
          NO
        </button>
      </section>
    </section>
  );
}
