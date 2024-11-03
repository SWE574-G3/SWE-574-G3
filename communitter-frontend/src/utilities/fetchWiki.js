import { wikidataUrl } from "./config";

export async function fetchWiki(searchParams, options) {
  const paramsString = new URLSearchParams(searchParams).toString();
  const url = wikidataUrl + paramsString;
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
