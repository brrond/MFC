package ua.nix.onishchenko.mfc.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface Repository<T> extends JpaRepository<T, UUID> {

    @Override
    void deleteById(UUID uuid);

    @Override
    void delete(T entity);

    @Override
    <S extends T> S save(S entity);

    @Override
    Optional<T> findById(UUID uuid);

    @Override
    <S extends T> S saveAndFlush(S entity);

    @Override
    void flush();

}
