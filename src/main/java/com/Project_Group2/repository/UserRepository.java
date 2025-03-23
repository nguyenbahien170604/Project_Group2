package com.Project_Group2.repository;

import com.Project_Group2.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByRole_Id(Long roleId);
    @Query("select u from User u where u.role.id = :roleId")
    Page<User> findAllCustomer(int roleId, Pageable pageable);

    Page<User> findByEmailLike(String email, Pageable pageable);
    Page<User> findByUsernameLike(String username, Pageable pageable);

    User findByUsername(String blogUserName);
}
