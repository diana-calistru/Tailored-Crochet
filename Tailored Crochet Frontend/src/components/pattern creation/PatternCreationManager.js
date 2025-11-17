import { useState, useEffect } from "react";
import "./PatternCreationManager.css";
import axios from "axios";
import placeholderImg from "../../images/tshirt-placeholder.png";

const API_BASE = "http://localhost:8080/api/patterns";

export default function PatternCreationManager() {
    const [garments, setGarments] = useState([]);
    const [selectedGarment, setSelectedGarment] = useState(null);
    const [partsByType, setPartsByType] = useState({});
    const [selectedOptions, setSelectedOptions] = useState({});
    const [measurementProfiles, setMeasurementProfiles] = useState([]);
    const [showProfileModal, setShowProfileModal] = useState(false);
    const [selectedProfile, setSelectedProfile] = useState(null);
    const [patternName, setPatternName] = useState("");
    const [swatch, setSwatch] = useState({
        width: "",
        height: "",
        rows: "",
        stitches: "",
    });

    // Fetch garment types on load
    useEffect(() => {
        const fetchGarments = async () => {
            try {
                const res = await axios.get(`${API_BASE}/get-garment-types`);
                setGarments(res.data);
            } catch (err) {
                console.error("Error fetching garments:", err);
            }
        };

        const fetchProfiles = async () => {
            try {
                const userData = JSON.parse(localStorage.getItem("user"));
                const token = userData.accessToken;
                const email = userData.email;

                const res = await axios.get(`http://localhost:8080/api/profiles/get`, {
                    headers: { Authorization: `Bearer ${token}` },
                    params: { email }
                });

                setMeasurementProfiles(res.data);
            } catch (err) {
                console.error("Error fetching profiles:", err);
            }
        };

        fetchGarments();
        fetchProfiles();
    }, []);

    // Fetch parts whenever garment changes
    useEffect(() => {
        if (!selectedGarment) return;

        const fetchParts = async () => {
            try {
                const res = await axios.get(`${API_BASE}/get-parts`, {
                    params: { garmentName: selectedGarment.garmentName }
                });

                // Group by partType
                const grouped = {};
                res.data.forEach((part) => {
                    if (!grouped[part.partType]) {
                        grouped[part.partType] = [];
                    }
                    grouped[part.partType].push(part);
                });
                setPartsByType(grouped);
                setSelectedOptions({});
            } catch (err) {
                console.error("Error fetching parts:", err);
            }
        };

        fetchParts();
    }, [selectedGarment]);

    const handleOptionSelect = (partType, part) => {
        setSelectedOptions(prev => ({ ...prev, [partType]: part }));
    };

    const handleSavePattern = async () => {
        try {
            const userData = JSON.parse(localStorage.getItem("user"));
            const payload = {
                patternName: patternName,
                garmentType: selectedGarment.garmentName,
                selectedParts: Object.entries(selectedOptions).map(([partType, part]) => ({
                    partType,
                    partStyle: part.partStyle,
                })),
                swatch: {
                    width: parseFloat(swatch.width),
                    height: parseFloat(swatch.height),
                    rows: parseInt(swatch.rows),
                    stitches: parseInt(swatch.stitches),
                },
                measurementProfileName: selectedProfile.name,
                userEmail: userData.email,
            };

            await axios.post(`${API_BASE}/create`, payload, {
                headers: { Authorization: `Bearer ${userData.accessToken}` }
            });

            alert("Pattern saved successfully!");
        } catch (err) {
            console.error("Failed to save pattern:", err);
            alert("Failed to save pattern.");
        }
    };

    const isSaveDisabled =
        !patternName ||
        !selectedProfile ||
        Object.keys(selectedOptions).length === 0 ||
        Object.values(swatch).some(v => !v);

    const resolveImage = (imageUrl) => {
        try {
            return require(`../../images/${imageUrl}`);
        } catch {
            return placeholderImg;
        }
    };

    return (
        <div className="main-container-p">
            <div className="left-panel-p">
                <h2>Select a Garment</h2>
                <div className="gallery">
                    {garments.map(g => (
                        <div
                            key={g.garmentName}
                            className={`gallery-item ${selectedGarment?.garmentName === g.garmentName ? "selected" : ""}`}
                            onClick={() => setSelectedGarment(g)}
                        >
                            <img src={resolveImage(g.imageUrl)} alt={g.garmentName} />
                            <p>{g.garmentName}</p>
                        </div>
                    ))}
                </div>

                {selectedGarment && Object.keys(partsByType).length > 0 && (
                    <>
                        <h2>Customize {selectedGarment.garmentName}</h2>
                        {Object.entries(partsByType).map(([partType, options]) => (
                            <div key={partType}>
                                <h3>{partType}</h3>
                                <div className="gallery">
                                    {options.map(option => (
                                        <div
                                            key={option.id}
                                            className={`gallery-item ${selectedOptions[partType]?.id === option.id ? "selected" : ""}`}
                                            onClick={() => handleOptionSelect(partType, option)}
                                        >
                                            <img src={resolveImage(option.imageUrl)} alt={option.partStyle} />
                                            <p>{option.partStyle}</p>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        ))}
                    </>
                )}
            </div>

            <div className="right-panel-p">
                <h2>Your Selection</h2>

                {selectedGarment ? (
                    <>
                        <p><strong>Garment:</strong> {selectedGarment.garmentName}</p>
                        <img src={resolveImage(selectedGarment.imageUrl)} alt={selectedGarment.garmentName} className="preview-img" />
                        <p><strong>Parts:</strong></p>
                        <ul>
                            {Object.entries(selectedOptions).map(([partType, part]) => (
                                <li key={partType}>
                                    <strong>{partType}:</strong> {part.partStyle}
                                </li>
                            ))}
                        </ul>
                    </>
                ) : (
                    <p>No garment selected yet.</p>
                )}

                <hr />
                <h3>Pattern Name</h3>
                <input
                    type="text"
                    placeholder="Enter pattern name"
                    value={patternName}
                    onChange={e => setPatternName(e.target.value)}
                    className="pattern-name-input"
                />

                <h3>Crochet Swatch Info</h3>
                <input
                    type="number"
                    placeholder="Width (cm)"
                    value={swatch.width}
                    onChange={e => setSwatch({ ...swatch, width: e.target.value })}
                />
                <input
                    type="number"
                    placeholder="Height (cm)"
                    value={swatch.height}
                    onChange={e => setSwatch({ ...swatch, height: e.target.value })}
                />
                <input
                    type="number"
                    placeholder="# Rows"
                    value={swatch.rows}
                    onChange={e => setSwatch({ ...swatch, rows: e.target.value })}
                />
                <input
                    type="number"
                    placeholder="# Stitches per Row"
                    value={swatch.stitches}
                    onChange={e => setSwatch({ ...swatch, stitches: e.target.value })}
                />

                <button className="choose-profile-button" onClick={() => setShowProfileModal(true)}>
                    Choose Measurement Profile
                </button>
                {selectedProfile && (
                    <p><strong>Selected Profile:</strong> {selectedProfile.name}</p>
                )}

                <button className="save-button" onClick={handleSavePattern} disabled={isSaveDisabled}>
                    Save Pattern
                </button>

                {showProfileModal && (
                    <div className="modal-backdrop">
                        <div className="modal">
                            <h3>Select a Measurement Profile</h3>
                            {measurementProfiles.length === 0 ? (
                                <p>No profiles available.</p>
                            ) : (
                                <ul>
                                    {measurementProfiles.map(profile => (
                                        <li key={profile.name}>
                                            <button onClick={() => {
                                                setSelectedProfile(profile);
                                                setShowProfileModal(false);
                                            }}>
                                                {profile.name}
                                            </button>
                                        </li>
                                    ))}
                                </ul>
                            )}
                            <button onClick={() => setShowProfileModal(false)}>Cancel</button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
