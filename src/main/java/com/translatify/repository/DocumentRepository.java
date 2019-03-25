package com.translatify.repository;

import org.springframework.data.repository.CrudRepository;

import com.translatify.models.Document;

public interface DocumentRepository extends CrudRepository<Document, String> {

}
