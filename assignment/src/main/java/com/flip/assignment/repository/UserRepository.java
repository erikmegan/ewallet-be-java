package com.flip.assignment.repository;

import com.flip.assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    public User findByUsernameAndIsDeleted(String username, boolean isDeleted);


}
