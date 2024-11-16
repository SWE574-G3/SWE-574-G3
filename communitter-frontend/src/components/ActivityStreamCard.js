// components/ActivityFeedCard.js
import { Link } from "react-router-dom";

const ActivityFeedCard = ({ activities, selectedAction, communityCreatorId }) => {
    const filterActivities = () => {
        if (!selectedAction) return activities;
        return activities.filter(activity => activity.action === selectedAction);
    };

    return (
        <div className="activity-feed">
            <h3>Activity Feed</h3>
            <div className="activity-list">
                {filterActivities().map((activity, index) => (
                    <div
                        className={`activity-item ${activity.userId === communityCreatorId ? 'highlight' : ''}`}
                        key={index}
                    >
                        <div className="activity-header">
                            <span className="activity-time">
                                {new Date(activity.timestamp).toLocaleString()}
                            </span>
                        </div>
                        <span className="activity-description">
                            <Link to={`/user/${activity.userId}`} className="activity-link">
                                {activity.userName}
                            </Link>
                            {activity.action === "CREATE" && activity.postId ? (
                                <>
                                    {" "}created a{" "}
                                    <Link to={`/posts/${activity.postId}`} className="activity-link">
                                        Post
                                    </Link>
                                </>
                            ) : activity.action === "UPVOTE" ? (
                                <>{" "}upvoted the{" "}
                                    <Link to={`/posts/${activity.postId}`} className="activity-link">
                                        Post
                                    </Link>
                                </>
                            ) : activity.action === "DOWNVOTE" ? (
                                <> {" "}downvoted the{" "}
                                    <Link to={`/posts/${activity.postId}`} className="activity-link">
                                        Post
                                    </Link>
                                </>
                            ) : activity.action === "JOIN" ? (
                                <> {" "}joined{" "}
                                    <Link to={`/community/${activity.communityId}`} className="activity-link">
                                        {activity.communityName}
                                    </Link>
                                </>
                            ) : null}
                        </span>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ActivityFeedCard;
