package learning.securitydemo.service;

import learning.securitydemo.models.Role;
import learning.securitydemo.repositories.RoleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional // I need everything in this class to be transactional
@Slf4j // for log
public class RoleServiceImpl implements RoleService{

    private final RoleRepo roleRepo;

    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database",role.getName());
        return roleRepo.save(role);
    }
}
