import React, { useState } from "react";
import axios from "axios";
import "./MeasurementProfileForm.css";
const API_URL = 'http://localhost:8080/api/profiles';


const MeasurementProfileForm = () => {
    const [name, setName] = useState("");
    const [notes, setNotes] = useState("");
    const [height, setHeight] = useState("");
    const [image, setImage] = useState(null);
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const isSubmitDisabled =
        !name ||
        !height ||
        !image
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!name || !height || !image) {
            setError("Please fill all required fields.");
            return;
        }

        const userData = localStorage.getItem('user');
        const user = JSON.parse(userData);

        const formData = new FormData();
        formData.append("email", user.email);
        formData.append("name", name);
        formData.append("notes", notes);
        formData.append("height", height);
        formData.append("image", image);

        try {
            setLoading(true);
            setError("");
            setResult(null);

            const response = await axios.post(`${API_URL}/create`, formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                    // "Authorization": `Bearer ${yourToken}`
                },
            });

            setResult(response.data);
            window.location.reload();
        } catch (err) {

            setError("Failed to create profile or run inference.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="profile-form-container">
            <h2>Create Measurement Profile</h2>
            <form className="profile-form" onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Name *"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
                <textarea
                    placeholder="Notes"
                    value={notes}
                    onChange={(e) => setNotes(e.target.value)}
                />
                <input
                    type="number"
                    placeholder="Height (cm) *"
                    value={height}
                    onChange={(e) => setHeight(e.target.value)}
                />
                <input
                    type="file"
                    accept="image/*"
                    onChange={(e) => setImage(e.target.files[0])}
                />
                <button type="submit" disabled={isSubmitDisabled}>
                    {loading ? "Creating profile..." : "Create Profile"}
                </button>
                {loading && <div className="spinner"></div>}
                {error && <p className="error-message">{error}</p>}
            </form>
        </div>
    );
};

export default MeasurementProfileForm;
