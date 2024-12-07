import React, { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { url } from "../utilities/config";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import PostCard from "../components/PostCard";

export function HomePage() {
  const loggedInUser = useSelector((state) => state.user.loggedInUser);
  const [joinedCommunities, setJoinedCommunities] = useState([]);

  // Function to get random posts from the community's already available posts
  function getRandomPosts(posts) {
    // Shuffle the posts and pick a maximum of 5
    return posts
      .sort(() => 0.5 - Math.random()) // Shuffle the posts
      .slice(0, 5); // Limit to 5 posts
  }

  // Fetch communities by their IDs
  async function fetchCommunitiesByIds(subscriptions) {
    try {
      const communities = await Promise.all(
        subscriptions.map(async (subscription) => {
          const communityId = subscription.id.communityId;

          if (!communityId) {
            console.error("No communityId found for subscription:", subscription);
            return { id: null, name: "Unknown Community", posts: [] };
          }

          try {
            const response = await fetchWithOpts(
              `${url}/community/${communityId}`,
              {
                method: "GET",
                headers: {},
              }
            );

            const community = response;

            // Use the existing posts in the community
            const randomPosts = getRandomPosts(community.posts);

            return {
              ...community,
              posts: randomPosts, // Add the random posts to the community data
            };
          } catch (error) {
            console.error(`Error fetching community ID ${communityId}:`, error);
            return { id: communityId, name: "Unknown Community", posts: [] };
          }
        })
      );

      setJoinedCommunities(communities);
    } catch (error) {
      console.error("Failed to fetch communities:", error);
    }
  }

  useEffect(() => {
    if (loggedInUser && loggedInUser.subscriptions) {
      console.log("Logged in user data:", loggedInUser);

      fetchCommunitiesByIds(loggedInUser.subscriptions);
    }
  }, [loggedInUser]);

  return (
    <div>
      <h2>Welcome</h2>
      {joinedCommunities.length > 0 ? (
        <ul>
          {joinedCommunities.map((community, index) => (
            <li key={community.id || index}>
              {community.posts.length > 0 ? (
                <div>
                  {community.posts.map((post, postIndex) => (
                    <PostCard key={post.id || postIndex} post={post} />
                  ))}
                </div>
              ) : (
                <p>No posts available.</p>
              )}
            </li>
          ))}
        </ul>
      ) : (
        <p>You haven't joined any communities yet.</p>
      )}
    </div>
  );
}
