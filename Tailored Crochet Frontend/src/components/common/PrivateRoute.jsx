import { Navigate } from 'react-router-dom';
import {useEffect, useState} from "react";

export default function PrivateRoute({ children }) {
    const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));

    useEffect(() => {
        const checkUser = () => {
            const currentUser = JSON.parse(localStorage.getItem('user'));
            if (currentUser !== user) {
                setUser(currentUser);
            }
        };

        window.addEventListener('storage', checkUser);
        return () => window.removeEventListener('storage', checkUser);
    }, [user]);

    return user ? children : <Navigate to="/welcome" />;
}