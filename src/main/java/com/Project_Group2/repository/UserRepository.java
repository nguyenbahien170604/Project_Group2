package com.Project_Group2.repository;

import com.Project_Group2.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);


    User findByUsername(String blogUserName);

    Page<User> findUserByRole_Id(int role_id, Pageable pageable);

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserByPhoneNumber(String phone);

    User findUserById(int id);

    @Query("SELECT u FROM User u WHERE u.role.id = 3 AND " +
            "(LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(CASE WHEN u.isDeleted = false THEN 'active' ELSE 'deleted' END) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR CAST(u.createdAt AS string) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<User> searchManagers(@Param("keyword") String keyword, Pageable pageable);

}
