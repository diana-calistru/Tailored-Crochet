import React from "react";
import "./ProfileDetails.css";

const API_URL = 'http://localhost:8080/api/profiles';

const ProfileDetails = ({ profile, onClose, onUpdate }) => {
    const handleDelete = async () => {
        const confirmed = window.confirm("Are you sure you want to delete this profile?");
        if (!confirmed) return;

        const userData = localStorage.getItem('user');
        const user = JSON.parse(userData);
        const token = user.accessToken;
        const email = user.email;
        const profileName = profile.name;

        try {
            const response = await fetch(`${API_URL}/delete?email=${email}&name=${encodeURIComponent(profileName)}`, {
                method: 'DELETE',
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            if (!response.ok) {
                throw new Error("Failed to delete profile");
            }

            onUpdate();
            onClose();
        } catch (error) {
            console.error("Error deleting profile:", error);
            alert("Error deleting profile");
        }
    };

    return (
        <div className="profile-details">
            <button className="close-btn" onClick={onClose}>✖ Close</button>
            <h2>{profile.name}</h2>
            <p>{profile.notes}</p>
            <div className="measurements-section">
                <h3>Measurements</h3>
                <ul>
                    {Object.entries(profile.measurements || {}).map(([key, value]) => (
                        <li key={key}>
                            <span className="measurement-label">
                                {/* Format keys: "chest_circ" => "Chest Circ" */}
                                {key.replace(/_/g, " ").replace(/\b\w/g, c => c.toUpperCase())}:
                            </span>
                            <span className="measurement-value">
                                {value.toFixed(2)} cm {/* Format to 2 decimal places */}
                            </span>
                        </li>
                    ))}
                </ul>
            </div>
            <button className="delete-btn" onClick={handleDelete}>🗑 Delete</button>
        </div>
    );
};

export default ProfileDetails;
