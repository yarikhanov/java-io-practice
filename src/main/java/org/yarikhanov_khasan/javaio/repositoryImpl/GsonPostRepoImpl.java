package org.yarikhanov_khasan.javaio.repositoryImpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.yarikhanov_khasan.javaio.model.Post;
import org.yarikhanov_khasan.javaio.model.Status;
import org.yarikhanov_khasan.javaio.repositoryInterface.PostRepo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonPostRepoImpl implements PostRepo {

    private static final String FILE_PATH = "src/main/resources/posts.json";
    private static final Gson GSON = new Gson();

    @Override
    public Post getById(Long id) {
            List<Post> posts = getAllPosts();

            if (posts == null || posts.isEmpty()) {
                return null;
            }

            return posts.stream().filter(post -> id.equals(post.getId()) && post.getStatus() == Status.ACTIVE)
                    .findFirst()
                    .orElse(null);
    }

    @Override
    public List<Post> getAll() {
            return getAllPosts();
    }

    @Override
    public Post save(Post post) {
        try {
            List<Post> postList = getAllPosts();

            if (postList == null || postList.isEmpty()) {
                postList = new ArrayList<>();
            }

            Long newId = postList.stream()
                    .map(Post::getId)
                    .max(Long::compareTo)
                    .map(id -> id + 1)
                    .orElse(1L);

            post.setId(newId);
            post.setStatus(Status.ACTIVE);
            postList.add(post);

            try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
                GSON.toJson(postList, fileWriter);
                return post;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
    }

    @Override
    public Post update(Post postToUpdate) {
        try {
            List<Post> postList = getAllPosts();

            if (postList == null || postList.isEmpty()) {
                System.err.println("Объект с ID " + postToUpdate.getId() + " не найден");
                return null;
            }

            postList.forEach(post -> {
                if (post.getId().equals(postToUpdate.getId())) {
                    if (post.getStatus() == Status.ACTIVE) {
                        post = postToUpdate;
                    } else {
                        System.err.println("Нельзя обновить удаленный объект c ID " + postToUpdate.getId());
                    }
                }
            });

            try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
                GSON.toJson(postList, fileWriter);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
        return postToUpdate;
    }

    @Override
    public void deleteById(Long id) {
        try {
            List<Post> postList = getAllPosts();

            if (postList == null || postList.isEmpty()) {
                System.err.println("Объект с ID " + id + " не найден");
                return;
            }

            postList.forEach(post -> {
                if (post.getId().equals(id)) {
                    if (post.getStatus() == Status.ACTIVE) {
                        post.setStatus(Status.DELETED);
                    } else {
                        System.err.println("Объект с ID " + id + " был удален");
                    }
                }
            });

            try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
                GSON.toJson(postList, fileWriter);
                System.out.println("Объект с ID " + id + " успешно удален");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
        }
    }

    private List<Post> getAllPosts() {
        try (JsonReader jsonReader = new JsonReader(new FileReader(FILE_PATH))) {
            Type typeToken = new TypeToken<ArrayList<Post>>() {
            }.getType();

            return GSON.fromJson(jsonReader, typeToken);

        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
    }
}
