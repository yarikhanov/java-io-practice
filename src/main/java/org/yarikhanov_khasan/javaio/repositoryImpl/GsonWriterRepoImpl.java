package org.yarikhanov_khasan.javaio.repositoryImpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.yarikhanov_khasan.javaio.model.Status;
import org.yarikhanov_khasan.javaio.model.Writer;
import org.yarikhanov_khasan.javaio.repositoryInterface.WriterRepo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonWriterRepoImpl implements WriterRepo {

    private final String FILE_PATH = "src/main/resources/writers.json";
    private final Gson GSON = new Gson();

    @Override
    public Writer getById(Long id) {
        try (JsonReader jsonReader = new JsonReader(new FileReader(FILE_PATH))) {
            Type typeToken = new TypeToken<ArrayList<Writer>>() {
            }.getType();
            List<Writer> writers = GSON.fromJson(jsonReader, typeToken);

            if (writers == null || writers.isEmpty()) {
                return null;
            }

            return writers.stream().filter(writer -> id.equals(writer.getId()) && writer.getStatus() == Status.ACTIVE)
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
    }

    @Override
    public List<Writer> getAll() {
        try (JsonReader jsonReader = new JsonReader(new FileReader(FILE_PATH))) {
            Type typeToken = new TypeToken<ArrayList<Writer>>() {
            }.getType();

            return GSON.fromJson(jsonReader, typeToken);
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
    }

    @Override
    public Writer save(Writer writer) {
        List<Writer> writersList = getAll();

        if (writersList == null || writersList.isEmpty()) {
            writersList = new ArrayList<>();
        }

        Long newId = writersList.stream()
                .map(Writer::getId)
                .max(Long::compareTo)
                .map(id -> id + 1)
                .orElse(1L);

        writer.setId(newId);
        writer.setStatus(Status.ACTIVE);
        writersList.add(writer);

        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            GSON.toJson(writersList, fileWriter);
            return writer;
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
    }

    @Override
    public Writer update(Writer writerToUpdate) {
        List<Writer> writersList = getAll();

        if (writersList == null || writersList.isEmpty()) {
            System.err.println("Объект с ID " + writerToUpdate.getId() + " не найден");
            return null;
        }

        for (int i = 0; i < writersList.size(); i++) {
            Writer writer = writersList.get(i);
            if (writerToUpdate.getId().equals(writer.getId())) {
                if (writer.getStatus() == Status.ACTIVE) {
                    writersList.set(i, writerToUpdate);
                    break;
                } else {
                    System.err.println("Нельзя обновить удаленный объект c ID " + writerToUpdate.getId());
                    return null;
                }
            }
        }

        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            GSON.toJson(writersList, fileWriter);
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
        return writerToUpdate;
    }

    @Override
    public void deleteById(Long id) {
        List<Writer> writersList = getAll();

        if (writersList == null || writersList.isEmpty()) {
            System.err.println("Объект с ID " + id + " не найден");
            return;
        }

        for (Writer writer : writersList) {
            if (writer.getId().equals(id)) {
                if (writer.getStatus() == Status.ACTIVE) {
                    writer.setStatus(Status.DELETED);
                    break;
                } else {
                    System.err.println("Объект с ID " + id + " был удален");
                    return;
                }
            }
        }

        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            GSON.toJson(writersList, fileWriter);
            System.out.println("Объект с ID " + id + " успешно удален");
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
        }
    }
}
