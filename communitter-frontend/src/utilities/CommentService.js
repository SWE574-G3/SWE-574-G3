import { tokenName, url } from "./config";

export class CommentService {
    static async createComment(comment, postId) {
        const token = localStorage.getItem(tokenName);
        try {
            console.log(postId);
            const commentResponse = await fetch(`${url}/comments/posts/${postId}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(comment),
            });

            if (!commentResponse.ok) {
                throw new Error("Failed to create comment");
            }
            return await commentResponse.json(); 
        } catch (error) {
            console.log(error.message);
        }
    }
}