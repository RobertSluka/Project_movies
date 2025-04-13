package dev.sluka.movies.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.sluka.movies.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
    List<Role> findAll();
}
