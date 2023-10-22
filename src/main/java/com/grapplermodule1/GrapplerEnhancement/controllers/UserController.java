package com.grapplermodule1.GrapplerEnhancement.controllers;
import com.grapplermodule1.GrapplerEnhancement.customexception.CustomResponse;
import com.grapplermodule1.GrapplerEnhancement.customexception.UserNotFoundException;
import com.grapplermodule1.GrapplerEnhancement.dtos.UsersDTO;
import com.grapplermodule1.GrapplerEnhancement.entities.PostValidation;
import com.grapplermodule1.GrapplerEnhancement.entities.PutValidation;
import com.grapplermodule1.GrapplerEnhancement.entities.Users;
import com.grapplermodule1.GrapplerEnhancement.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.grapplermodule1.GrapplerEnhancement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;


import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    /**
     * For Getting List Of Users
     *
     * @return ResponseEntity<List < UsersDTO>>
     */
    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Get All Users API Called, UUID {}", debugUuid);
            List<UsersDTO> usersList = userService.fetchAllUsers();

            return new ResponseEntity<>(usersList, HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            log.error("UUID {} UserNotFoundException In Get All Users API Exception {}", debugUuid, e);
            return new ResponseEntity<>(new CustomResponse<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Get All Users API Exception {}", debugUuid, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For Create New User
     *
     * @return ResponseEntity
     */
    @PostMapping("/")
    public ResponseEntity<?> createUser(@Validated(PostValidation.class) @RequestBody Users user) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("Get Create User API Called, UUID {}", debugUuid);
            Users newUser = userService.addUser(user);
            if (newUser != null) {
                return new ResponseEntity<>(new CustomResponse<>(true, "User Created With Id : " + newUser.getId(), newUser), HttpStatus.OK);
            }
            else {
                log.error("UUID {} User Not Created", debugUuid);
                return new ResponseEntity<>(new CustomResponse<>(false, "User Not Created. Please Try Again", null), HttpStatus.BAD_GATEWAY);
            }
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Create User API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For getting User By I'd
     *
     * @return ResponseEntity<Users>
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@Valid @PathVariable("userId") Long userId) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("UUID {} Inside Get User By Id, User Id {} ", debugUuid, userId);
            UsersDTO user = userService.fetchUserById(userId);

            log.info("Get User By Id Returning User in ResponseEntity, User Id {} ", user.getId());
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            log.error("UUID {}, UserNotFoundException in Get User BY Id API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Get User By Id API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For Update User By Id
     *
     * @return ResponseEntity
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserById(@PathVariable("userId") Long userId,
                                            @Validated(PutValidation.class) @RequestBody Users user) {
        String debugUuid = UUID.randomUUID().toString();
        try {
            log.info("UUID {} Inside Update User By Id, User Id {} ", debugUuid, userId);
            Users updatedUser = userService.updateUserDetails(userId, user);

            log.info("Update User By Id Returning User in ResponseEntity, Updated User Id {} ", updatedUser.getId());
            return new ResponseEntity<>(new CustomResponse<>(true, "User Updation Successful.", updatedUser), HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            log.error("UUID {}, UserNotFoundException in Update User BY Id API, Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(false, e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("UUID {} Exception In Get User By Id API Exception {}", debugUuid, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * For Delete User
     *
     * @return ResponseEntity
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUserById(@PathVariable("userId") Long userId) {
        return null;
    }

}