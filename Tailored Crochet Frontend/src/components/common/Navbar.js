import { NavLink, useNavigate } from 'react-router-dom';
import { Container, Navbar as BSNavbar, Button } from 'react-bootstrap';
import AuthService from '../../services/AuthService';
import { useEffect, useState } from 'react';
import './Navbar.css';
import { BiSolidBuildingHouse } from "react-icons/bi";

export default function Navbar() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);

    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem('user'));
        setUser(storedUser);

        const handleStorageChange = () => {
            setUser(JSON.parse(localStorage.getItem('user')));
        };

        window.addEventListener('storage', handleStorageChange);
        return () => window.removeEventListener('storage', handleStorageChange);
    }, []);

    const handleLogout = () => {
        AuthService.logout();
        setUser(null);
        navigate('/welcome');
    };

    return (
        <BSNavbar className="custom-navbar">
            <Container fluid className="navbar-layout">
                {/* Left: Home Icon */}
                <div className="nav-left">
                    <BSNavbar.Brand as={NavLink} to="/">
                        <BiSolidBuildingHouse className="placeholder-icon" />
                    </BSNavbar.Brand>
                </div>

                {/* Center: Links */}
                <div className="nav-center">
                    <div className="nav-links">
                        <NavLink
                            to="/measurement"
                            className={({ isActive }) =>
                                `link-container ${isActive ? 'active-link' : ''}`
                            }
                        >
                            <span className="link">Measurement Profiles</span>
                        </NavLink>
                        <NavLink
                            to="/create"
                            className={({ isActive }) =>
                                `link-container ${isActive ? 'active-link' : ''}`
                            }
                        >
                            <span className="link">Pattern Creation</span>
                        </NavLink>
                        <NavLink
                            to="/saved"
                            className={({ isActive }) =>
                                `link-container ${isActive ? 'active-link' : ''}`
                            }
                        >
                            <span className="link">Saved Patterns</span>
                        </NavLink>
                    </div>
                </div>

                {/* Right: Logout */}
                <div className="nav-right">
                    <Button onClick={handleLogout} className="logout-btn">
                        Logout
                    </Button>
                </div>
            </Container>
        </BSNavbar>
    );
}
