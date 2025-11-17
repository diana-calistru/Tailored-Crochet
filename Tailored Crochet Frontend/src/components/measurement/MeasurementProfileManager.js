import React, { useEffect, useState } from "react";
import "./MeasurementProfileManager.css";
import ProfileCard from "./ProfileCard";
import ProfileDetails from "./ProfileDetails";
import axios from "axios";
import MeasurementProfileForm from "./MeaurementProfileForm";

const API_URL = 'http://localhost:8080/api/profiles';

const MeasurementProfileManager = () => {
    const [profiles, setProfiles] = useState([]);
    const [selectedProfile, setSelectedProfile] = useState(null);
    const [showForm, setShowForm] = useState(false);

    useEffect(() => {
        fetchProfiles();
    }, []);

    const fetchProfiles = async () => {
        try {
            const userData = localStorage.getItem('user');
            const user = JSON.parse(userData);
            const token = user.accessToken;
            const email = user.email;
            const res = await axios.get(`${API_URL}/get`, {
                headers: { Authorization: `Bearer ${token}` },
                params: { email: email },
            });
            console.log("API Response:", res.data);
            setProfiles(res.data);
        } catch (err) {
            console.error("Error loading profiles:", err);
        }
    };

    return (
        <div className="main-container">
            <div className="left-panel">
                <div className="card-list">
                    {profiles.map((profile) => (
                        <ProfileCard key={profile.name}
                                     profile={profile}
                                     onClick={() => {
                                         setSelectedProfile(profile);
                                         setShowForm(false);
                                     }} />
                    ))}
                </div>
            </div>

            <div className="right-panel">
                <button className="create-btn" onClick={() => {
                    setShowForm(true);
                    setSelectedProfile(null);
                }}>
                    + Create New Profile
                </button>

                <div className="card-info">
                    {showForm ? (
                        <MeasurementProfileForm
                            onClose={() => {
                                setShowForm(false);
                                fetchProfiles();
                            }}
                        />
                    ) : selectedProfile ? (
                        <ProfileDetails
                            profile={selectedProfile}
                            onClose={() => setSelectedProfile(null)}
                            onUpdate={fetchProfiles}
                        />
                    ) : (
                        <div className="placeholder">
                            <p>Select a profile to view details</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default MeasurementProfileManager;