import { useNavigate } from "react-router-dom";
import { tokenName, url } from "./config";
import { extractId } from "./jwt";
export class AuthService {
  static async register(registerInfo) {
    try {
      const registerResponse = await fetch(`${url}/auth/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(registerInfo),
      });
      console.log(registerResponse);
      if (!registerResponse.ok) {
        let message = await registerResponse.text();
        let returnMessage = message.includes("duplicate")
          ? "User already exists"
          : "Register failed";
        return [false, returnMessage];
      }
      const data = await registerResponse.json();
      console.log(data);
      localStorage.setItem("CommunitterToken", data.token);
      return [true, data];
    } catch (err) {
      console.log(err.message);
    }
  }
  static async login(creds) {
    try {
      const loginResponse = await fetch(`${url}/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(creds),
      });
      console.log(loginResponse);
      if (!loginResponse.ok) {
        return [false, "Authentication Failed"];
      }
      const data = await loginResponse.json();
      console.log(data);
      localStorage.setItem(tokenName, data.token);
      return [true, data];
    } catch (err) {
      console.log(err.message);
    }
  }
  static async isloggedIn() {
    const token = localStorage.getItem(tokenName);
    if (!token) return false;
    const userId = extractId(token);
    try {
      const userResponse = await fetch(`${url}/user/${userId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      });
      console.log(userResponse);
      if (!userResponse.ok) {
        return { ok: false, userInfo: null };
      }
      const userInfo = await userResponse.json();
      //console.log(userInfo);

      return { ok: true, userInfo };
    } catch (err) {
      console.log(err.message);
    }
  }
}

export function useLogout() {
  const navigate = useNavigate();
  return function () {
    if (localStorage.getItem(tokenName)) localStorage.removeItem(tokenName);
    navigate("/");
  };
}
