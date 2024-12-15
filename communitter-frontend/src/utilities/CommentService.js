import { tokenName, url } from "./config";

export class CommentService {
    static async createComment(comment, postId) {
        const token = localStorage.getItem(tokenName);
        //try {
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
               const message = await commentResponse.text();
               throw new Error(message);
            }
            return await commentResponse.json(); 
        /*} catch (error) {
            console.log(error.message);
           throw new Error(error)
        }*/
    }

    static async editComment(comment,commentId){
        const token = localStorage.getItem(tokenName);
        //try {
            const response = await fetch(`${url}/comments/edit/${commentId}`, {
                method: "POST",
                headers: {
                    "Content-Type" : "application/json",
                    "Authorization" : `Bearer ${token}`
                },
                body: JSON.stringify(comment)
            });

            if(!response.ok){
                const message = await response.text();
                throw new Error(message);
            }
            return await response.json();
        /*} catch (error) {
            console.log(error.message);
            this.dispatch(setErrorMessage(error.message));
        }*/
    }

    static async deleteComment(commentId){
        const token = localStorage.getItem(tokenName);
        //try {
            const response = await fetch(`${url}/comments/delete/${commentId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type" : "application/json",
                    "Authorization" : `Bearer ${token}`
                }
            });
            if (!response.ok){
                const message = await response.text();
                throw new Error(message);
            }
            return await response.text();
        /*} catch (error) {
            console.log(error.message);
            this.dispatch(setErrorMessage(error.message));
        }*/
    }
}