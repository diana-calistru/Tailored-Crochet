import { useEffect, useState } from "react";
import axios from "axios";
import "./PatternGallery.css";

export default function PatternGallery() {
    const [patterns, setPatterns] = useState([]);
    const [selectedPatternId, setSelectedPatternId] = useState(null);
    const [selectedPatternName, setSelectedPatternName] = useState(null);

    useEffect(() => {
        const fetchPatterns = async () => {
            try {
                const userData = localStorage.getItem("user");
                const user = JSON.parse(userData);

                const res = await axios.get("http://localhost:8080/api/patterns/get", {
                    headers: { Authorization: `Bearer ${user.accessToken}` },
                    params: { email: user.email },
                });

                setPatterns(res.data);
            } catch (err) {
                console.error("Error fetching patterns:", err);
            }
        };

        fetchPatterns();
    }, []);

    const handleDownloadPDF = async () => {
        try {
            const userData = localStorage.getItem("user");
            const user = JSON.parse(userData);

            if (!selectedPatternId) {
                alert("No pattern selected.");
                return;
            }

            const res = await axios.post(
                `http://localhost:8080/api/patterns/generate-pdf`,
                null,  // No body
                {
                    headers: {
                        Authorization: `Bearer ${user.accessToken}`
                    },
                    params: { patternId: selectedPatternId , patternName: selectedPatternName},
                    responseType: "blob"
                }
            );

            const blob = new Blob([res.data], { type: "application/pdf" });
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement("a");
            link.href = url;
            link.download = "pattern.pdf";
            link.click();
            window.URL.revokeObjectURL(url);

        } catch (err) {
            console.error("Failed to generate or download PDF:", err);
            alert("Could not generate or download the PDF.");
        }
    };


    const handleDeletePattern = async (patternId) => {
        const confirm = window.confirm("Are you sure you want to delete this pattern?");
        if (!confirm) return;

        try {
            const userData = localStorage.getItem("user");
            const user = JSON.parse(userData);

            await axios.delete(`http://localhost:8080/api/patterns/${patternId}`, {
                headers: {
                    Authorization: `Bearer ${user.accessToken}`,
                },
            });

            // Remove from UI
            setPatterns((prev) => prev.filter((p) => p.id !== patternId));

            // If the deleted pattern was selected, reset selection
            if (patternId === selectedPatternId) {
                setSelectedPatternId(null);
            }

            alert("Pattern deleted successfully.");
        } catch (err) {
            console.error("Failed to delete pattern:", err);
            alert("Failed to delete the pattern.");
        }
    };


    return (
        <div className="pattern-gallery-container">
            <div className="pattern-gallery-page">
                <h2>Your Saved Patterns</h2>

                {patterns.length === 0 ? (
                    <p>No patterns found.</p>
                ) : (
                    <ul className="pattern-list">
                        {patterns.map((pattern) => (
                            <li
                                key={pattern.id}
                                className={`pattern-card ${selectedPatternId === pattern.id ? "selected" : ""}`}
                                onClick={() => {
                                    setSelectedPatternId(pattern.id);
                                    setSelectedPatternName(pattern.patternName);
                                }}
                            >
                                <h3>{pattern.patternName}</h3>
                                <p><strong>Measurement Profile:</strong> {pattern.measurementProfileName}</p>
                                <ul>
                                    {pattern.parts.map((part, i) => (
                                        <li key={i}>
                                            <strong>{part.partType}:</strong> {part.partStyle}
                                        </li>
                                    ))}
                                </ul>
                                <button
                                    className="delete-btn"
                                    onClick={(e) => {
                                        e.stopPropagation(); // Prevent selecting the card
                                        handleDeletePattern(pattern.id);
                                    }}
                                >
                                    Delete
                                </button>
                            </li>

                        ))}
                    </ul>
                )}
                <button
                    className="download-btn"
                    onClick={handleDownloadPDF}
                    disabled={!selectedPatternId}
                >
                    Download PDF
                </button>

            </div>
        </div>
    );
}
