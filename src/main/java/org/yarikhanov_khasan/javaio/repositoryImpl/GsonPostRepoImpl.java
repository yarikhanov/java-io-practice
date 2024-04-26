package org.yarikhanov_khasan.javaio.repositoryImpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.yarikhanov_khasan.javaio.model.Post;
import org.yarikhanov_khasan.javaio.model.Status;
import org.yarikhanov_khasan.javaio.repositoryInterface.PostRepo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonPostRepoImpl implements PostRepo {

    private final String FILE_PATH = "src/main/resources/posts.json";
    private final Gson GSON = new Gson();

    @Override
    public Post getById(Long id) {
        try (JsonReader jsonReader = new JsonReader(new FileReader(FILE_PATH))) {
            Type typeToken = new TypeToken<ArrayList<Post>>() {
            }.getType();
            List<Post> posts = GSON.fromJson(jsonReader, typeToken);

            if (posts == null || posts.isEmpty()) {
                return null;
            }

            return posts.stream().filter(post -> id.equals(post.getId()) && post.getStatus() == Status.ACTIVE)
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
    }

    @Override
    public List<Post> getAll() {
        try (JsonReader jsonReader = new JsonReader(new FileReader(FILE_PATH))) {
            Type typeToken = new TypeToken<ArrayList<Post>>() {
            }.getType();

            return GSON.fromJson(jsonReader, typeToken);
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
    }

    @Override
    public Post save(Post post) {
        List<Post> postList = getAll();

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
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
    }

    @Override
    public Post update(Post postToUpdate) {
        List<Post> postList = getAll();

        if (postList == null || postList.isEmpty()) {
            System.err.println("Объект с ID " + postToUpdate.getId() + " не найден");
            return null;
        }

        for (int i = 0; i < postList.size(); i++) {
            Post post = postList.get(i);
            if (postToUpdate.getId().equals(post.getId())) {
                if (post.getStatus() == Status.ACTIVE) {
                    postList.set(i, postToUpdate);
                    break;
                } else {
                    System.err.println("Нельзя обновить удаленный объект c ID " + postToUpdate.getId());
                    return null;
                }
            }
        }

        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            GSON.toJson(postList, fileWriter);
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
        return postToUpdate;
    }

    @Override
    public void deleteById(Long id) {
        List<Post> postList = getAll();

        if (postList == null || postList.isEmpty()) {
            System.err.println("Объект с ID " + id + " не найден");
            return;
        }

        for (Post post : postList) {
            if (post.getId().equals(id)) {
                if (post.getStatus() == Status.ACTIVE) {
                    post.setStatus(Status.DELETED);
                    break;
                } else {
                    System.err.println("Объект с ID " + id + " был удален");
                    return;
                }
            }
        }

        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            GSON.toJson(postList, fileWriter);
            System.out.println("Объект с ID " + id + " успешно удален");
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
        }
    }
}
