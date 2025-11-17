import { useState } from "react";
import {Button, Form, Card, Container} from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import AuthService from "../../services/AuthService";
import {FaEnvelope, FaLock, FaSignInAlt} from "react-icons/fa";
import "./Auth.css";

export default function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();

    const validateForm = () => {
        let isValid = true;
        const newErrors = {};

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
        }

        setErrors(newErrors);
        return isValid;
    };

    const handleLogin = async (e) => {
        e.preventDefault();
        if (!validateForm()) return;

        try {
            await AuthService.login(email, password);
            navigate('/main');
        } catch (err) {
            let errorMessage = 'Login failed';
            if (err.response) {
                if (err.response.status === 403) {
                    errorMessage = 'Incorrect email or password';
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
                            <FaSignInAlt className="icon" />
                        </div>
                        <h2>Welcome Back!</h2>
                        <p className="subtext">Sign in to continue to your crochet pattern studio</p>
                    </div>
                    <Form onSubmit={handleLogin} noValidate>
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
                                type="submit">
                            Login
                        </Button>
                    </Form>
                    <div className="auth-footer">
                        <p className="footer-text">
                            Don't have an account?{" "}
                            <a href="/register" className="footer-link">
                                Create one
                            </a>
                        </p>
                        {/*<a href="/forgot-password" className="footer-link small">
                            Forgot Password?
                        </a>*/}
                    </div>
                </Card.Body>
            </Card>
        </Container>
    );
}