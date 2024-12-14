import { Outlet, useLocation, useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import { Navbar } from "./nav";
import { AuthService } from "../utilities/auth";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setLoggedInUser } from "../features/userSlice";
import ErrorMessage from "../components/ErrorMessage";
import { setErrorMessage } from "../features/errorSlice";
export function SharedLayout() {
  const error = useSelector((state) => state.error.errorMessage);
  const location = useLocation();
  const navigate = useNavigate();
  const [isAllowed, setIsAllowed] = useState(false);
  const dispatch = useDispatch();
  const checkLogin = async () => {
    return await AuthService.isloggedIn();
  };
  useEffect(() => {
    async function protectRoute() {
      const isloggedIn = await checkLogin();
      if (!isloggedIn.ok) {
        setIsAllowed(false);
        location.pathname !== "/" ? navigate("/") : setIsAllowed(true);
      } else {
        dispatch(setLoggedInUser(isloggedIn.userInfo));
        console.log("User info in shared layout");
        console.log(isloggedIn.userInfo);
        location.pathname === "/"
          ? navigate(`/user/${isloggedIn.userInfo.id}`)
          : setIsAllowed(true);
      }
      return () => {
        setIsAllowed(false);
      };
    }

    protectRoute();
  }, [navigate, location, dispatch]);
  useEffect(() => {
    const errorTimeout = setTimeout(() => {
      dispatch(setErrorMessage(""));
    }, 1500);
    return () => clearTimeout(errorTimeout);
  }, [error, dispatch]);
  return (
    <div className="d-flex flex-column vh-100">
      {error && <ErrorMessage message={error} />}
      <header className="header fixed-top bg-light shadow-sm">
        {location.pathname !== "/" && isAllowed && <Navbar></Navbar>}
      </header>
      <main className="container flex-grow-1">
        {isAllowed && <Outlet></Outlet>}
      </main>
    </div>
  );
}
