import axios from "axios";
import {tokenName, url} from "./config";

export class ProfileService {


    static async uploadProfilePicture(image, userId) {

        const token = localStorage.getItem(tokenName);

        if (!token) {
            throw new Error("Authentication token not found");
        }

        const formData = new FormData();
        formData.append("image", image);

        try {
            const response = await axios.post(
                `${url}/images/user/${userId}/profile-picture`,
                formData,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "multipart/form-data",
                    },
                }
            );
            return response.data; // Return server response for further processing
        } catch (error) {
            console.error("Error uploading profile picture:", error.response?.data || error.message);
            throw error;
        }
    }


}

export default ProfileService;
