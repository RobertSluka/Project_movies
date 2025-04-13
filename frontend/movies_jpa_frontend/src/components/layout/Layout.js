import React from 'react';
import { Container } from 'react-bootstrap';
import Navbar from '../navbar/Navbar';
import Chat from '../chat/Chat';
import { useAuth } from '../../context/AuthProvider';

const Layout = ({ children }) => {
    const { auth } = useAuth();

    return (
        <>
            <Navbar />
            <Container className="mt-4">
                {children}
                {auth?.isAuthenticated && <Chat />}
            </Container>
        </>
    );
};

export default Layout; 