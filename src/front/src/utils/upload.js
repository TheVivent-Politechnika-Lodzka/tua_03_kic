import { create } from "ipfs-http-client";

const client = create("https://ipfs.infura.io:5001/api/v0");


export const uploadPhoto = async (e) => {
    try {
        const added = await client.add(e.target.files[0]);
        const url = `https://ipfs.infura.io/ipfs/${added.path}`;
        return url;
    } catch (error) {
        console.log("Error while uploading the photo");
    }
};