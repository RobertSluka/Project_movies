import React, { useState, useEffect, useRef } from 'react';
import { useAuth } from '../../context/AuthProvider';
import { Container, Form, Button, Card, ListGroup, Alert } from 'react-bootstrap';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import api from '../../api/axiosConfig';

const Chat = () => {
    const { auth } = useAuth();
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const [connected, setConnected] = useState(false);
    const [error, setError] = useState(null);
    const messagesEndRef = useRef(null);

    useEffect(() => {
        let client = null;

        const connect = () => {
            console.log('Attempting to connect to WebSocket...');
            
            // Get the access token
            const token = localStorage.getItem('accessToken');
            if (!token) {
                setError('No authentication token found');
                return;
            }

            // Create a new STOMP client with SockJS
            const socket = new SockJS('http://localhost:8080/ws');
            client = new Client({
                webSocketFactory: () => socket,
                connectHeaders: {
                    Authorization: `Bearer ${token}`
                },
                debug: function (str) {
                    console.log(str);
                },
                reconnectDelay: 5000,
                heartbeatIncoming: 4000,
                heartbeatOutgoing: 4000,
            });

            // Set up connection handlers
            client.onConnect = () => {
                console.log('Connected to WebSocket');
                setConnected(true);
                setError(null);
                setStompClient(client);

                // Subscribe to the public topic
                client.subscribe('/topic/public', (message) => {
                    const receivedMessage = JSON.parse(message.body);
                    console.log('Received message:', receivedMessage);
                    setMessages(prev => [...prev, receivedMessage]);
                });

                // Send join message
                client.publish({
                    destination: '/app/chat.addUser',
                    body: JSON.stringify({ 
                        sender: auth.user, 
                        content: '', 
                        type: 'JOIN' 
                    })
                });
            };

            client.onStompError = (frame) => {
                console.error('STOMP error:', frame);
                setError('Failed to connect to chat server');
                setConnected(false);
                setStompClient(null);
            };

            client.onWebSocketError = (event) => {
                console.error('WebSocket error:', event);
                setError('WebSocket connection error');
                setConnected(false);
                setStompClient(null);
            };

            client.onDisconnect = () => {
                console.log('Disconnected from WebSocket');
                setConnected(false);
                setStompClient(null);
            };

            // Activate the client
            client.activate();
        };

        if (auth?.user && !stompClient) {
            connect();
        }

        return () => {
            if (client) {
                client.deactivate();
            }
        };
    }, [auth?.user]);

    const sendMessage = (e) => {
        e.preventDefault();
        if (stompClient && newMessage.trim()) {
            const chatMessage = {
                sender: auth.user,
                content: newMessage,
                type: 'CHAT'
            };

            stompClient.publish({
                destination: '/app/chat.sendMessage',
                body: JSON.stringify(chatMessage)
            });

            setNewMessage('');
        }
    };

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    return (
        <Container className="mt-4">
            <Card>
                <Card.Header>
                    Chat Room
                    {!connected && (
                        <small className="text-warning ms-2">(Connecting...)</small>
                    )}
                </Card.Header>
                <Card.Body>
                    {error && <Alert variant="danger">{error}</Alert>}
                    <ListGroup variant="flush" style={{ maxHeight: '400px', overflowY: 'auto' }}>
                        {messages.map((message, index) => (
                            <ListGroup.Item key={index}>
                                {message.type === 'JOIN' ? (
                                    <em>{message.sender} joined the chat!</em>
                                ) : message.type === 'LEAVER' ? (
                                    <em>{message.sender} left the chat!</em>
                                ) : (
                                    <strong>{message.sender}: </strong>
                                )}
                                {message.content}
                                {message.timestamp && <small className="text-muted ms-2">{message.timestamp}</small>}
                            </ListGroup.Item>
                        ))}
                        <div ref={messagesEndRef} />
                    </ListGroup>
                    <Form onSubmit={sendMessage} className="mt-3">
                        <Form.Group className="d-flex">
                            <Form.Control
                                type="text"
                                value={newMessage}
                                onChange={(e) => setNewMessage(e.target.value)}
                                placeholder="Type a message..."
                                disabled={!connected}
                                className="me-2"
                            />
                            <Button 
                                type="submit" 
                                variant="primary"
                                disabled={!connected || !newMessage.trim()}
                            >
                                Send
                            </Button>
                        </Form.Group>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default Chat; 