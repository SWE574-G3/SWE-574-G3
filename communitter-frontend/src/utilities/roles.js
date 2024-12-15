export const roles = [
  { value: 4, label: "creator" },
  { value: 3, label: "owner" },
  { value: 2, label: "moderator" },
  { value: 1, label: "user" },
];

export const getUserRoleValue = (loggedInUser, communityId) => {
  const userSubscription = loggedInUser?.subscriptions?.find(
    (subscription) => subscription.id.communityId === communityId
  );

  return userSubscription ? userSubscription.role.id : 0;
};
