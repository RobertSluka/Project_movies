import React, { useState ,useRef,useEffect} from 'react';
import api from '../../api/axiosConfig';
import { Container, Form, Button, Card, Alert } from 'react-bootstrap';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';


const Login = () => {

  const { setAuth } = useAuth();

  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/";

  const userRef = useRef();
  const errRef = useRef();


  const [user, setUser] = useState('');
  const [pwd, setPwd] = useState('');
  const [errMsg, setErrMsg] = useState('');


  useEffect(() => {
    userRef.current.focus();
  }, [])

  useEffect(() => {
    setErrMsg('');  
  }, [user, pwd])

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
        const response = await api.post("/login",
            JSON.stringify({ userName: user, password: pwd }),
            {
                headers: { 
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                withCredentials: true
            }
        );
        
        const { accessToken, refreshToken, roles } = response.data;
        
        if (!accessToken) {
            throw new Error("No access token received");
        }
        
        // Store tokens
        localStorage.setItem('accessToken', accessToken);
        if (refreshToken) {
            localStorage.setItem('refreshToken', refreshToken);
        }
        
        // Set auth context
        setAuth({ 
            user, 
            roles: roles ? roles.split(',') : [], 
            accessToken,
            refreshToken,
            isAuthenticated: true
        });
        
        setUser('');
        setPwd('');
        navigate(from, { replace: true });
    } catch (err) {
        console.error("Login error:", err);
        if (!err?.response) {
            setErrMsg('No Server Response');
        } else if (err.response?.status === 400) {
            setErrMsg('Missing Username or Password');
        } else if (err.response?.status === 401) {
            setErrMsg('Unauthorized');
        } else {
            setErrMsg('Login Failed');
        }
        errRef.current.focus();
    }
}


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
          <h2 className="text-center mb-4" style={{ color: '#ffffff' }}>Login</h2>
                  {errMsg && (
          <Alert ref={errRef} variant="danger" className="text-center" tabIndex={-1}>
            {errMsg}
          </Alert>
          )}

          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label style={{ color: '#bbbbbb' }}>Username</Form.Label>
              <Form.Control
                type="text"
                ref = {userRef}
                value={user}
                onChange={(e) => setUser(e.target.value)}
                placeholder="Enter your username"
                required
                style={{ backgroundColor: '#2a2a2a', color: '#ffffff', borderColor: '#444' }}
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label style={{ color: '#bbbbbb' }}>Password</Form.Label>
              <Form.Control
                type="password"
                value={pwd}
                onChange={(e) => setPwd(e.target.value)}
                placeholder="Enter your password"
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
              Login
            </Button>
          </Form>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default Login;
