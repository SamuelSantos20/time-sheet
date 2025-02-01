package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.data.UserData;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.validation.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class UserService {

    private final UserData userData;

    private final UserValidation userValidation;


    public User saveUser(User user) {

        userValidation.validation(user);

        return userData.save(user);

    }

    @Transactional(readOnly = true)
    public List<User> userList() {

        return userData.findAll();

    }

    @Transactional(readOnly = true)
    public Optional<User> findByUserId(UUID id) {

        return userData.findById(id);

    }

    public void DeleteUser(User user) {

        userData.delete(user);

    }


}
