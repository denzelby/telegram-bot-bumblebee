package com.github.bumblebee.security.roles.dao;

import com.github.bumblebee.security.roles.domain.UserRoleRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolesRepository extends CrudRepository<UserRoleRecord, Long> {

    List<UserRoleRecord> findByUserId(Long userId);

    int deleteByUserId(Long userId);
}
