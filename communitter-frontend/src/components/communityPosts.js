import PostCard from "./PostCard";

export const Posts = ({ posts, onDelete, onEdit,handleEditPost, communityId }) => {
  return (
    <div className="mt-4">
      <h2>Posts</h2>
      {posts.length > 0 ? (
        <div className="row row-cols-1 g-4">
          {posts
            .slice()
            .sort((a, b) => new Date(b.date) - new Date(a.date))
            .map((post) => (
              <div key={post.id} className="col">
                <PostCard post={post} onDelete={onDelete} onEdit={onEdit} handleEditPost={handleEditPost} communityId={communityId} />
              </div>
            ))}
        </div>
      ) : (
        <p>No posts found.</p>
      )}
    </div>
  );
};
