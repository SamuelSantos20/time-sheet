package io.github.samuelsantos20.time_sheet.data;

import io.github.samuelsantos20.time_sheet.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserData extends JpaRepository<User, UUID> {


    @Query("select u from User u where u.registration = ?1")
    Optional<User> findByRegistration(String registration);

    @Query("select u from User u where u.registration = ?1 and u.password = ?2")
    Optional<User> findByRegistrationAndPassword(String registration, String password);

    @EntityGraph(attributePaths = {"managers"})
    @Query("select u from User u inner join u.managers managers where managers.user = ?1")
    Optional<User> findByManagers_User(User user);







}