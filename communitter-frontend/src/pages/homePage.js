import React, { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { url } from "../utilities/config";
import { fetchWithOpts } from "../utilities/fetchWithOptions";
import PostCard from "../components/PostCard";
import { setErrorMessage } from "../features/errorSlice";

export function HomePage() {
  const loggedInUser = useSelector((state) => state.user.loggedInUser);
  const [joinedCommunities, setJoinedCommunities] = useState([]);
  const [recommendedCommunities, setRecommendedCommunities] = useState([]);
  const [combinedPosts, setCombinedPosts] = useState([]);

  function getRandomPosts(posts) {
    return posts.sort(() => 0.5 - Math.random()).slice(0, 5);
  }

  async function fetchCommunitiesByIds(subscriptions) {
    try {
      const communities = await Promise.all(
        subscriptions.map(async (subscription) => {
          const communityId = subscription.id.communityId;

          if (!communityId) {
            console.error(
              "No communityId found for subscription:",
              subscription
            );
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

            const randomPosts = getRandomPosts(community.posts);

            return {
              ...community,
              posts: randomPosts,
            };
          } catch (error) {
            console.error(`Error fetching community ID ${communityId}:`, error);
            return { id: communityId, name: "Unknown Community", posts: [] };
          }
        })
      );
      console.log(communities);
      setJoinedCommunities(communities);
    } catch (error) {
      console.error("Failed to fetch communities:", error);
    }
  }

  async function fetchRecommendedCommunities() {
    try {
      const path = "/recommendation/communities";
      const recommendedData = await fetchWithOpts(`${url}${path}`, {
        method: "GET",
        headers: {},
      });

      const recommendedIds = recommendedData
        .filter((community) => community.public === true)
        .map((community) => community.id);

      const recommendedDetails = await Promise.all(
        recommendedIds.map(async (id) => {
          try {
            const community = await fetchWithOpts(`${url}/community/${id}`, {
              method: "GET",
              headers: {},
            });
            return community;
          } catch (error) {
            console.error(`Error fetching community with ID ${id}:`, error);
            return null; // Handle failure for specific community
          }
        })
      );

      const validCommunities = recommendedDetails.filter(Boolean);

      setRecommendedCommunities(validCommunities);
    } catch (error) {
      console.error("Failed to fetch recommended communities:", error);
      setErrorMessage(error.message);
    }
  }

  function combinePosts() {
    const joinedCommunityIds = new Set(
      joinedCommunities.map((community) => community.id)
    );
    const uniqueRecommendedCommunities = recommendedCommunities.filter(
      (community) => !joinedCommunityIds.has(community.id)
    );

    const allCommunities = [
      ...joinedCommunities,
      ...uniqueRecommendedCommunities,
    ];

    let selectedPosts = [];

    allCommunities.forEach((community) => {
      if (Array.isArray(community.posts) && community.posts.length > 0) {
        const enrichedPost = community.posts.map((post) => ({
          ...post,
          community: community,
        }));

        const randomCommunityPosts = getRandomPosts(enrichedPost);
        selectedPosts = [...selectedPosts, ...randomCommunityPosts];
      }
    });

    selectedPosts.sort(() => Math.random() - 0.5);

    setCombinedPosts(selectedPosts);
  }

  useEffect(() => {
    if (loggedInUser && loggedInUser.subscriptions) {
      fetchCommunitiesByIds(loggedInUser.subscriptions);
      fetchRecommendedCommunities();
    }
  }, [loggedInUser]);

  useEffect(() => {
    if (joinedCommunities.length > 0 || recommendedCommunities.length > 0) {
      combinePosts();
    }
  }, [joinedCommunities, recommendedCommunities]);

  return (
    <div style={{ marginTop: "56px" }}>
      <h2>Welcome</h2>
      {combinedPosts.length > 0 ? (
        <div>
          {combinedPosts.map((post, postIndex) => (
            <PostCard key={post.id || postIndex} post={post} />
          ))}
        </div>
      ) : (
        <p>No posts available.</p>
      )}
    </div>
  );
}
