package com.translatify.repository;

import org.springframework.data.repository.CrudRepository;

import com.translatify.models.User;

public interface UserRepository extends CrudRepository<User, String> {
	User findById(long id);
	User findByEmail(String email);
	User findByUsername(String username);
}
