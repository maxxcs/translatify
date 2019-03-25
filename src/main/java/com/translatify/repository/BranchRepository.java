package com.translatify.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.translatify.models.Branch;
import com.translatify.models.Document;
import com.translatify.models.User;

public interface BranchRepository extends CrudRepository<Branch, String> {
	
	Branch findById(long id);
	
	@Query("SELECT b FROM Branch b WHERE b.flag = 'RAW' AND b.document = :doc")
	Branch findRaw(@Param("doc") Document doc);
	
	@Query("SELECT b FROM Branch b WHERE b.document = :doc")
	Iterable<Branch> findAllByDoc(@Param("doc") Document doc);
	
	@Query("SELECT b FROM Branch b WHERE b.user = :user")
	Iterable<Branch> findAllByUser(@Param("user") User user);
	
	@Query("SELECT b FROM Branch b WHERE b.name LIKE CONCAT('%',:search,'%') OR b.document.title LIKE CONCAT('%',:search,'%')")
	Iterable<Branch> findAllBySearch(@Param("search") String search);
}
