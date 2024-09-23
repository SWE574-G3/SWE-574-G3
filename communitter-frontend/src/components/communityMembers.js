import { MemberRow } from "./MemberRow";

export const Members = ({ members }) => {
  return (
    <div className="mt-5">
      <h2>Members</h2>
      {members.length > 0 ? (
        <table className="table table-striped">
          <thead>
            <tr>
              <th scope="col">Username</th>
              <th scope="col">Role</th>
            </tr>
          </thead>
          <tbody>
            {members.map((subscription) => (
              <MemberRow
                key={subscription.id.userId + subscription.id.communityId}
                subscription={subscription}
                userRole={subscription.role.name}
              ></MemberRow>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No members found.</p>
      )}
    </div>
  );
};
