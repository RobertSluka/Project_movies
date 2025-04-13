package dev.sluka.movies.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import dev.sluka.movies.Entity.UserRole;
import dev.sluka.movies.Repository.UserRoleRepository;
import jakarta.transaction.Transactional;

@Service
public class RoleService {

    private final UserRoleRepository userRoleRepository;

    public RoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Transactional
    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }
}
