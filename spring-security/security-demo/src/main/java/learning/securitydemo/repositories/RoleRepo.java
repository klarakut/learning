package learning.securitydemo.repositories;

import learning.securitydemo.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {

    Role findByName(String roleName);
}
