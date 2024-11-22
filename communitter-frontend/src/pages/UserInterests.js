
import { url } from "../utilities/config";
import { WikidataInterface } from "../components/wikidataInterface";

export function UserInterestsPage() {
return (
  <WikidataInterface url={`${url}/recommendation/user/interests`}></WikidataInterface>
)
}
