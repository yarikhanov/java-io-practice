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

    private final String FILE_PATH = "src/main/resources/labels.json";
    private final Gson GSON = new Gson();

    @Override
    public Label getById(Long id) {
        try (JsonReader jsonReader = new JsonReader(new FileReader(FILE_PATH))) {
            Type typeToken = new TypeToken<ArrayList<Label>>() {
            }.getType();
            List<Label> labels = GSON.fromJson(jsonReader, typeToken);

            if (labels == null || labels.isEmpty()) {
                return null;
            }

            return labels.stream().filter(label -> id.equals(label.getId()) && label.getStatus() == Status.ACTIVE)
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
    }

    @Override
    public List<Label> getAll() {
        try (JsonReader jsonReader = new JsonReader(new FileReader(FILE_PATH))) {
            Type typeToken = new TypeToken<ArrayList<Label>>() {
            }.getType();

            return GSON.fromJson(jsonReader, typeToken);
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
            return null;
        }
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
        List<Label> labelList = getAll();

        if (labelList == null || labelList.isEmpty()) {
            System.err.println("Объект с ID " + labelToUpdate.getId() + " не найден");
            return null;
        }

        for (int i = 0; i < labelList.size(); i++) {
            Label label = labelList.get(i);
            if (labelToUpdate.getId().equals(label.getId())) {
                if (label.getStatus() == Status.ACTIVE) {
                    labelList.set(i, labelToUpdate);
                    break;
                } else {
                    System.err.println("Нельзя обновить удаленный объект c ID " + labelToUpdate.getId());
                    return null;
                }
            }
        }

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
        List<Label> labelList = getAll();

        if (labelList == null || labelList.isEmpty()) {
            System.err.println("Объект с ID " + id + " не найден");
            return;
        }

        for (Label label : labelList) {
            if (label.getId().equals(id)) {
                if (label.getStatus() == Status.ACTIVE) {
                    label.setStatus(Status.DELETED);
                    break;
                } else {
                    System.err.println("Объект с ID " + id + " был удален");
                    return;
                }
            }
        }

        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            GSON.toJson(labelList, fileWriter);
            System.out.println("Объект с ID " + id + " успешно удален");
        } catch (IOException e) {
            System.err.println(e.getMessage() + e.getCause());
        }
    }
}
