import { useEffect, useState } from "react";
import { setErrorMessage } from "../features/errorSlice";
import { useDispatch } from "react-redux";
import { defaultFetchOpts } from "../utilities/config";
import { fetchWiki } from "../utilities/fetchWiki";
import { buildGetParams } from "../utilities/buildGetParams";
import { buildWikiEntities } from "../utilities/buildWikiEntities";
import { fetchWithOpts } from "../utilities/fetchWithOptions";

export function WikidataInterface({url}){
  const [isLoading, setIsLoading] = useState(false);
  const dispatch = useDispatch();
  const [searchTerm, setSearchTerm] = useState("");
  const [showResults, setShowResults] = useState(false);
  const [searchResults, setSearchResults] = useState([]);
  const [interests, setInterests] = useState([]);
  const [existingInterests,setExistingInterests]=useState([]);

  
  const searchParams = {
    origin: "*",
    action: "wbsearchentities",
    format: "json",
    language: "en",
    search: "",
  };
  const getParams={
            origin:"*",
            action: 'wbgetentities',
            format: 'json',
            languages: 'en'
  }
  const handleChange = (e) => {
    setSearchTerm(() => e.target.value);
  };
  const handleRemove = (item) => {
    setInterests(interests.filter((interest) => interest !== item));
  };
  const handleDelete=async (interest)=>{
    console.log(interest);
    
    try{
      const deleteResult= await fetchWithOpts(`${url}/${interest.code}`,{
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        }
      })
      setExistingInterests(existingInterests.filter(item=>item.code!==interest.code));
      console.log(deleteResult);
      
    }
    catch(err){
      dispatch(setErrorMessage(err));
    }
  }
  const handleSearch = async (e) => {
    e.preventDefault();
    console.log(searchTerm);
    setIsLoading(true)
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
    setIsLoading(false)
  };
  const handleSubmit=async (e)=>{
    e.preventDefault();
    setIsLoading(true);
    if(interests.length){
      console.log(interests);
      buildGetParams(interests,getParams);
      console.log(getParams);
      try{
        const data=await fetchWiki(getParams,defaultFetchOpts);
        console.log(data);
        let sanitizedEntities=buildWikiEntities(data.entities);
        const response= await fetchWithOpts(`${url}`,{
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(sanitizedEntities),
        })

        
        setExistingInterests([...existingInterests,...sanitizedEntities.map(interest=>interest.wikiEntity)]);
        setInterests([]);    
        
      }catch(err){
        console.log(err);
        
        dispatch(setErrorMessage("Failed to Submit"))
      }
      
    }
    else{
      dispatch(setErrorMessage("There is nothing to submit"))
    }
    setIsLoading(false);
    
  }
  const handleAdd = (result) => {
    console.log(result);
    
    if (!interests.concat(existingInterests).find((interest) => interest.id === result.id)) {
      setInterests([...interests, result]);
    } else {
      dispatch(setErrorMessage("Label already added"));
    }
  };
  const clearResults = () => {
    setSearchResults([]);
    setShowResults(false);
  };
  useEffect(()=>{
    const getInterests= async()=>{
      try{
        const interests=await fetchWithOpts(`${url}`,defaultFetchOpts);
        console.log(interests);
        
        setExistingInterests(interests.map(interest=>interest.wikiEntity));
      }catch(err){
        console.log(err);
        
        dispatch(setErrorMessage(err.message));
      }
    }
    getInterests();
  },[])
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
           {existingInterests.map((item, index) => (
            <p
              key={index}
              className="me-3 p-1 bg-success border border-secondary rounded position-relative"
            >
              <span
                onClick={() => handleDelete(item)}
                className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-secondary"
                role="button"
              >
                x
              </span>
              {item.label}
            </p>
          ))}
        </div>
        <form className="d-flex">
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
            onClick={handleSearch}
            className={`btn btn-primary d-block ms-3 my-auto`}
          >
            Search
          </button>
          <button
            disabled={isLoading}
            onClick={handleSubmit}
            className={`btn btn-primary d-block ms-3 my-auto`}
          >
            Submit
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