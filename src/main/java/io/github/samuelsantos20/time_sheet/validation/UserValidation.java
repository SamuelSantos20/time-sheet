package io.github.samuelsantos20.time_sheet.validation;

import io.github.samuelsantos20.time_sheet.data.UserData;
import io.github.samuelsantos20.time_sheet.exception.DuplicateRecord;
import io.github.samuelsantos20.time_sheet.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidation {

    private final UserData userData;


    public void validation(User user) {

        if (exists(user)){

            throw new DuplicateRecord("Já existe uma conta criada para esse usuario!");

        }


    }


    private boolean exists(User user){

        Optional<User> byUserId = userData.findByUser_id(user.getUser_id());

        if (user.getId() == null){

            return byUserId.isPresent();
        }

        return byUserId.stream().anyMatch(user1 -> !user1.equals(user.getId()));
    }


}
