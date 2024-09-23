import { CommunityCard } from "./CommunityCard";

export const Subscriptions = ({ subscriptions }) => {
  return (
    <div className="mt-5">
      <h2>Memberships</h2>
      {subscriptions.length > 0 ? (
        <div className="row row-cols-1 row-cols-md-2 g-4">
          {subscriptions.map((subscription) => (
            <div
              key={subscription.id.userId + subscription.id.communityId}
              className="col"
            >
              <CommunityCard
                subscription={subscription}
                userRole={subscription.role.name}
              />
            </div>
          ))}
        </div>
      ) : (
        <p>No subscriptions found.</p>
      )}
    </div>
  );
};
