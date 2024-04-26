package org.yarikhanov_khasan.javaio.controller;

import org.yarikhanov_khasan.javaio.repositoryImpl.GsonPostRepoImpl;
import org.yarikhanov_khasan.javaio.model.Post;

import java.util.List;

public class PostController {
    private GsonPostRepoImpl postRepo = new GsonPostRepoImpl();

    public Post getById(Long id) {
        return postRepo.getById(id);
    }

    public List<Post> getAll() {
        return postRepo.getAll();
    }

    public Post save(Post post) {
        return postRepo.save(post);
    }

    public Post update(Post post) {
        return postRepo.save(post);
    }

    public void delete(Long id) {
        postRepo.deleteById(id);
    }
}
