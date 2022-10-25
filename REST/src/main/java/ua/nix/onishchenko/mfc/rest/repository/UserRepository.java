package ua.nix.onishchenko.mfc.rest.repository;

import ua.nix.onishchenko.mfc.rest.entity.User;

import java.util.Optional;

public interface UserRepository extends Repository<User> {

    Optional<User> findByEmail(String email);

}
