import React from "react";
import "./ProfileCard.css";

const ProfileCard = ({ profile, onClick }) => (
    <div className="profile-card" onClick={onClick}>
        <h3>{profile.name}</h3>
    </div>
);

export default ProfileCard;
