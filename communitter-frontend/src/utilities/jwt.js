import { jwtDecode } from "jwt-decode";

export function extractId(token) {
  return jwtDecode(token).userId;
}
