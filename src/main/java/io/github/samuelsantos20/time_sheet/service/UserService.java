package io.github.samuelsantos20.time_sheet.service;

import io.github.samuelsantos20.time_sheet.data.UserData;
import io.github.samuelsantos20.time_sheet.model.Manager;
import io.github.samuelsantos20.time_sheet.model.User;
import io.github.samuelsantos20.time_sheet.validation.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    //private final PasswordEncoder passwordEncoder;

    public User saveUser(User user) {

        userValidation.validation(user);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String encode = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encode);

        return userData.save(user);

    }

    public void Update(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        String encode = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encode);

        userData.save(user);
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

    @Transactional(readOnly = true)
    public Optional<User> findByManagers_User(User user) {

        return userData.findByManagers_User(user);

    }

    @Transactional(readOnly = true)
    public Optional<User> findByRegistration(String registration) {

        return userData.findByRegistration(registration);

    }

}
