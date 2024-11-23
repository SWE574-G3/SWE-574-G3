import { CommentCard } from './CommentCard';

export const PostComments = ({comments, onDeleteComment}) => {
    if (!Array.isArray(comments) || comments.length === 0) {
        return <p>No Comments Found!</p>;
    }

    return (
        <div className="mt-5">
            <h2>Comments</h2>
            {comments.length > 0 ? (
                <div className="row row-cols-1 g-4">
                    {comments
                        .slice()
                        .sort((a, b) => new Date(b.date) - new Date(a.date)) 
                        .map((comment) => (
                            <div key={comment.id} className="col">
                                <CommentCard comment={comment} onDeleteComment={onDeleteComment} />
                            </div>
                        ))}
                </div>
            ) : (
                <p>No Comments Found!</p>
            )}
        </div>
    );
};