package com.translatify.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.translatify.models.Branch;
import com.translatify.models.Comment;
import com.translatify.models.User;

public interface CommentRepository extends CrudRepository<Comment, String> {
	
	@Query("SELECT c FROM Comment c WHERE c.user = :user")
	Iterable<Comment> findAllByUser(@Param("user") User user);
	
	@Query("SELECT c FROM Comment c WHERE c.branch = :branch")
	Iterable<Comment> findAllByBranch(@Param("branch") Branch branch);
}
