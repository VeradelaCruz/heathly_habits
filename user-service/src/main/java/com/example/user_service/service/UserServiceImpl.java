package com.example.user_service.service;


import com.example.user_service.exception.DuplicateUserException;
import com.example.user_service.exception.UserNotFoundByNameException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.models.User;
import com.example.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{


    private  final UserRepository userRepository;

    @Override
    public Mono<User> createUser(User user) {
        return userRepository.findUserByUserNameAndEmail(user.getUserName(), user.getEmail())
                .flatMap(existingUser -> Mono.<User>error(new DuplicateUserException("This user already exists.")))
                .switchIfEmpty(Mono.defer(() -> {
                    Mono<User> saveMono = userRepository.save(user);
                    return saveMono;
                }));
    }


    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> getUserById(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(id)));
    }

    @Override
    public Mono<Void> deleteUser(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(id)))
                .flatMap(existingUser -> userRepository.deleteById(id));
    }

    @Override
    public Mono<User> getUserByName(String userName) {
        return userRepository.findUserByUserName(userName)
                .switchIfEmpty(Mono.error(new UserNotFoundByNameException(userName)));
    }

}
