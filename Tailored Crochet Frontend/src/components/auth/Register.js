import { useState } from "react";
import {Button, Form, Card, Container} from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import AuthService from "../../services/AuthService";
import "./Auth.css";
import {FaEnvelope, FaLock, FaMagic, FaUser} from "react-icons/fa";

export default function Register () {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();

    const validateForm = () => {
        let isValid = true;
        const newErrors = {};

        if (!username.trim()) {
            newErrors.username = 'Username is required';
            isValid = false;
        }

        if (!email) {
            newErrors.email = 'Email is required';
            isValid = false;
        } else if (!/\S+@\S+\.\S+/.test(email)) {
            newErrors.email = 'Email is invalid';
            isValid = false;
        }

        if (!password) {
            newErrors.password = 'Password is required';
            isValid = false;
        } else if (password.length < 6) {
            newErrors.password = 'Password must be at least 6 characters';
            isValid = false;
        }

        setErrors(newErrors);
        return isValid;
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        if (!validateForm()) return;

        try {
            await AuthService.register(username, email, password);
            navigate('/dashboard');
        } catch (err) {
            let errorMessage = 'Registration failed';
            if (err.response) {
                if (err.response.status === 403) {
                    errorMessage = 'Email already exists';
                } else if (err.response.data?.message) {
                    errorMessage = err.response.data.message;
                }
            }
            alert(errorMessage);
        }
    };

    return (
        <Container className="auth-container">
            <Card className="auth-card">
                <Card.Body className="auth-card-body">
                    <div className="auth-header">
                        <div className="auth-icon">
                            <FaMagic className="icon"/>
                        </div>
                        <h2>Get Started</h2>
                        <p className="subtext">Create your account to start crafting patterns</p>
                    </div>
                    <Form onSubmit={handleRegister} noValidate>
                        <div className="form-group">
                            <label className="form-label">Username</label>
                            <div className="input-group">
                                <span className="input-group-text">
                                    <FaUser className="text-primary"/>
                                </span>
                                <Form.Control
                                    type="text"
                                    className="form-control"
                                    value={username}
                                    onChange={(e) => {
                                        setUsername(e.target.value);
                                        setErrors({...errors, username: ''});
                                    }}
                                    isInvalid={!!errors.username}
                                    required
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.username}
                                </Form.Control.Feedback>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="form-label">Email</label>
                            <div className="input-group">
                                <span className="input-group-text">
                                    <FaEnvelope className="text-primary"/>
                                </span>
                                <Form.Control
                                    type="email"
                                    className="form-control"
                                    value={email}
                                    onChange={(e) => {
                                        setEmail(e.target.value);
                                        setErrors({...errors, email: ''});
                                    }}
                                    isInvalid={!!errors.email}
                                    required
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.email}
                                </Form.Control.Feedback>
                            </div>
                        </div>
                        <div className="form-group">
                            <label className="form-label">Password</label>
                            <div className="input-group">
                                <span className="input-group-text">
                                    <FaLock className="text-primary"/>
                                </span>
                                <Form.Control
                                    type="password"
                                    className="form-control"
                                    value={password}
                                    onChange={(e) => {
                                        setPassword(e.target.value);
                                        setErrors({...errors, password: ''});
                                    }}
                                    isInvalid={!!errors.password}
                                    required
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.password}
                                </Form.Control.Feedback>
                            </div>
                        </div>

                        <Button className="submit-button"
                                type='submit'>
                            Create Account
                        </Button>

                    </Form>
                    <div className="auth-footer">
                        <p className="footer-text">
                            Already have an account?{" "}
                            <a href="/login" className="footer-link">
                                Sign in
                            </a>
                        </p>
                    </div>
                </Card.Body>
            </Card>
        </Container>
    );
}