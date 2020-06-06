package br.com.laercioskt.backend.service;

import br.com.laercioskt.backend.data.User;
import br.com.laercioskt.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById((long) id);
    }

}
