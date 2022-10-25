package ua.nix.onishchenko.mfc.rest.service;

import java.util.Optional;
import java.util.UUID;

public interface Service<T> {

    Optional<T> findById(UUID id);

   T save(T entity);

   void delete(T entity);

   void deleteById(UUID id);

}
