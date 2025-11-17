import './App.css';
import { BrowserRouter, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import Welcome from "./components/welcome page/WelcomePage";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import Navbar from "./components/common/Navbar";
import PrivateRoute from "./components/common/PrivateRoute";
import MeasurementProfileManager from "./components/measurement/MeasurementProfileManager";
import MainPage from "./components/main page/MainPage";
import PatternGallery from "./components/saved patterns/PatternGallery";
import PatternCreationManager from "./components/pattern creation/PatternCreationManager";

function AppContent() {
    const location = useLocation();
    const hideNavbarPaths = ['/welcome', '/login', '/register'];

    return (
        <>
            {!hideNavbarPaths.includes(location.pathname) && <Navbar />}
            <div className={`container ${!hideNavbarPaths.includes(location.pathname) ? 'mt-4' : ''}`}>
                <Routes>
                    <Route path="/welcome" element={<Welcome />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/main" element={
                        <PrivateRoute>
                            <MainPage />
                        </PrivateRoute>
                    } />
                    <Route path="/measurement" element={
                        <PrivateRoute>
                            <MeasurementProfileManager />
                        </PrivateRoute>
                    } />
                    <Route path="/create" element={
                        <PrivateRoute>
                            <PatternCreationManager />
                        </PrivateRoute>
                    } />
                    <Route path="/saved" element={
                        <PrivateRoute>
                            <PatternGallery />
                        </PrivateRoute>
                    } />
                    <Route path="/" element={<Navigate to="/main" />} />
                </Routes>
            </div>
        </>
    );
}

function App() {
    return (
        <BrowserRouter>
            <AppContent />
        </BrowserRouter>
    );
}

export default App;