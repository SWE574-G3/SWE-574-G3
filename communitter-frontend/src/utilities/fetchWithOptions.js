import { tokenName } from "./config";

export async function fetchWithOpts(url, options) {
  const token = localStorage.getItem(tokenName);
  options.headers.Authorization = "Bearer " + token;
  
  
  const response = await fetch(url, options);
  if (!response.ok) {
    const message = await response.text();
    throw new Error(message);
  }
  const responseType = response.headers.get("Content-Type");
  let data;
  if (responseType.includes("text")) data = await response.text();
  if (responseType.includes("json")) data = await response.json();
  return data;
}
