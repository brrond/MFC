package ua.nix.onishchenko.mfc.rest.service;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.nix.onishchenko.mfc.rest.entity.User;
import ua.nix.onishchenko.mfc.rest.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@CommonsLog
@Service
public class UserService implements UserDetailsService,
        ua.nix.onishchenko.mfc.rest.service.Service<User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user, boolean encode) {
        if (encode) user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User save(User user) {
        return save(user, true);
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(UUID.fromString(id));
        if (user.isEmpty()) {
            log.warn("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }
        return new org.springframework.security.core.userdetails.User(user.get().getId().toString(), user.get().getPassword(), new ArrayList<>());
    }

}
