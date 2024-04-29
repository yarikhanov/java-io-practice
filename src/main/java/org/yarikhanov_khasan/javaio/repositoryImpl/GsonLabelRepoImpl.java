package org.yarikhanov_khasan.javaio.repositoryImpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.yarikhanov_khasan.javaio.model.Label;
import org.yarikhanov_khasan.javaio.model.Status;
import org.yarikhanov_khasan.javaio.repositoryInterface.LabelRepo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonLabelRepoImpl implements LabelRepo {

    private static final String FILE_PATH = "src/main/resources/labels.json";
    private static final Gson GSON = new Gson();

    @Override
    public Label getById(Long id) {
        List<Label> labels = getAllLabels();

            if (labels == null || labels.isEmpty()) {
                return null;
            }

            return labels.stream().filter(label -> id.equals(label.getId()) && label.getStatus() == Status.ACTIVE)
                    .findFirst()
                    .orElse(null);
    }

    @Override
    public List<Label> getAll() {
        return getAllLabels();
    }

    @Override
    public Label save(Label label) {
        List<Label> labelList = getAll();

        if (labelList == null || labelList.isEmpty()) {
            labelList = new ArrayList<>();
        }

        Long newId = labelList.stream()
                .map(Label::getId)
                .max(Long::compareTo)
                .map(id -> id + 1)
                .orElse(1L);

        label.setId(newId);
        label.setStatus(Status.ACTIVE);
        labelList.add(label);

        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            GSON.toJson(labelList, fileWriter);
            return label;
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
    }

    @Override
    public Label update(Label labelToUpdate) {
        List<Label> labelList = getAllLabels();

        if (labelList == null || labelList.isEmpty()) {
            System.err.println("Объект с ID " + labelToUpdate.getId() + " не найден");
            return null;
        }
        labelList.forEach(label -> {
            if (label.getId().equals(labelToUpdate.getId())) {
                if (label.getStatus() == Status.ACTIVE) {
                    label = labelToUpdate;
                } else {
                    System.err.println("Нельзя обновить удаленный объект c ID " + labelToUpdate.getId());
                }
            }
        });
        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            GSON.toJson(labelList, fileWriter);
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
        return labelToUpdate;
    }

    @Override
    public void deleteById(Long id) {
        List<Label> labelList = getAllLabels();

        if (labelList == null || labelList.isEmpty()) {
            System.err.println("Объект с ID " + id + " не найден");
            return;
        }

        labelList.forEach(label -> {
            if (label.getId().equals(id)) {
                if (label.getStatus() == Status.ACTIVE) {
                    System.err.println("Объект с ID " + id + " был удален");

                }
            }
        });
        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            GSON.toJson(labelList, fileWriter);
            System.out.println("Объект с ID " + id + " успешно удален");
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
        }
    }

    private List<Label> getAllLabels() {
        try (JsonReader jsonReader = new JsonReader(new FileReader(FILE_PATH))) {
            Type typeToken = new TypeToken<ArrayList<Label>>() {
            }.getType();

            return GSON.fromJson(jsonReader, typeToken);
        } catch (IOException e) {
            System.out.println(e.getMessage() + e.getCause());
            return null;
        }
    }
}
