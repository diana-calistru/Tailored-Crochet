import { useNavigate } from 'react-router-dom';
import './WelcomePage.css';

export default function WelcomePage() {
    const navigate = useNavigate();

    return (
        <div className="welcome-container">
            <div className="welcome-content">
                <h1 className="welcome-title">Welcome to Tailored Crochet!</h1>
                <p className="welcome-text">Get started with your personalized crochet patterns</p>

                <div className="welcome-buttons">
                    <button
                        className="welcome-button"
                        onClick={() => navigate('/login')}
                    >
                        Login
                    </button>
                    <button
                        className="welcome-button"
                        onClick={() => navigate('/register')}
                    >
                        Register
                    </button>
                </div>
            </div>
        </div>
    );
}