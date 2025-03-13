import React, { useState } from 'react';
import api from '../../api/axiosConfig';
import { Container, Form, Button, Card, Alert } from 'react-bootstrap';

const Register = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      await api.post('/register', { userName: username.trim(), password: password.trim() });
      setSuccess('Registration successful! You can now log in.');
      setError(null);
      setUsername('');
      setPassword('');
    } catch (error) {
      setSuccess(null);
      setError('Registration failed! Username may be taken.');
    }
  };

  return (
    <Container 
      className="d-flex justify-content-center align-items-center vh-100" 
      style={{ backgroundColor: '#121212' }} // Dark background
    >
      <Card style={{ 
        width: '350px', 
        padding: '20px', 
        backgroundColor: '#1e1e1e', // Dark form background
        color: '#ffffff', // Light text
        borderRadius: '10px',
        boxShadow: '0px 4px 10px rgba(0,0,0,0.3)' // Subtle shadow
      }}>
        <Card.Body>
          <h2 className="text-center mb-4" style={{ color: '#ffffff' }}>Register</h2>

          {error && <Alert variant="danger">{error}</Alert>}
          {success && <Alert variant="success">{success}</Alert>}

          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label style={{ color: '#bbbbbb' }}>Username</Form.Label>
              <Form.Control
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="Choose a username"
                required
                style={{ backgroundColor: '#2a2a2a', color: '#ffffff', borderColor: '#444' }}
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label style={{ color: '#bbbbbb' }}>Password</Form.Label>
              <Form.Control
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Choose a password"
                required
                style={{ backgroundColor: '#2a2a2a', color: '#ffffff', borderColor: '#444' }}
              />
            </Form.Group>

            <Button 
              variant="primary" 
              type="submit" 
              className="w-100"
              style={{ backgroundColor: '#007bff', borderColor: '#0056b3' }}
            >
              Register
            </Button>
          </Form>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default Register;
