package com.example.finalproject.finalproject.app.post.repository;

import com.example.finalproject.finalproject.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

}
