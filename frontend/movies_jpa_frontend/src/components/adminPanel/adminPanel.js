import React, { useState, useEffect } from "react";
import { Container, Table, Button, Alert } from 'react-bootstrap';
import api from '../../api/axiosConfig';
import { useAuth } from '../../context/AuthProvider';

const AdminPanel = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState(null);
  const { auth } = useAuth();

  useEffect(() => {
    const fetchUsersAndRoles = async () => {
      try {
        const [usersRes, rolesRes, userRolesRes] = await Promise.all([
          api.get('/users/all'),
          api.get('/roles/all'), // Optional if you need all role types
          api.get('/user/roles/all')
        ]);

        const usersData = usersRes.data;
        const userRolesData = userRolesRes.data;

        // Group roles by userId
        const rolesByUserId = {};
        userRolesData.forEach(entry => {
          const userId = entry.user.userId;
          if (!rolesByUserId[userId]) {
            rolesByUserId[userId] = [];
          }
          rolesByUserId[userId].push(entry.role); // Assuming role object has 'name'
        });

        // Merge roles into each user object
        const usersWithRoles = usersData.map(user => ({
          ...user,
          roles: rolesByUserId[user.userId] || []
        }));

        setUsers(usersWithRoles);
        setError(null);
      } catch (err) {
        console.error('Error fetching users/roles:', err);
        setError('Failed to load data. Please check your permissions.');
      }
    };

    if (auth?.accessToken) {
      fetchUsersAndRoles();
    }
  }, [auth?.accessToken]);

  const handleDeleteUser = async (username) => {
    try {
      await api.delete(`/admin/remove-roles/${username}`);
      await api.delete(`/admin/delete/${username}`);

      setUsers(prev => prev.filter(user => user.userName !== username));
      setError(null);
    } catch (err) {
      console.error('Error deleting user:', err);
      setError('Failed to delete user. Please try again.');
    }
  };

  return (
    <Container className="mt-4">
      <h2>Admin Management Panel</h2>
      {error && <Alert variant="danger">{error}</Alert>}
      <Table className="table table-dark table-striped table-sm table-hover">
        <thead>
          <tr>
            <th>Username</th>
            <th>Email</th>
            <th>Roles</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map(user => (
            <tr key={user.userId}>
              <td>{user.userName}</td>
              <td>{user.email}</td>
              <td>{user.roles.map(role => role.name.replace('ROLE_', '')).join(', ')}</td>
              <td>
                <Button
                  variant="danger"
                  size="sm"
                  onClick={() => handleDeleteUser(user.userName)}
                >
                  Delete
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </Container>
  );
};

export default AdminPanel;
