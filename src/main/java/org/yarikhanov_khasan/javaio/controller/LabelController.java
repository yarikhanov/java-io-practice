package org.yarikhanov_khasan.javaio.controller;

import org.yarikhanov_khasan.javaio.model.Label;
import org.yarikhanov_khasan.javaio.repositoryImpl.GsonLabelRepoImpl;
import org.yarikhanov_khasan.javaio.repositoryInterface.LabelRepo;

import java.util.List;

public class LabelController {
    private final LabelRepo labelRepo = new GsonLabelRepoImpl();

    public Label getById(Long id) {
        return labelRepo.getById(id);
    }

    public List<Label> getAll() {
        return labelRepo.getAll();
    }

    public Label save(Label label) {
        return labelRepo.save(label);
    }

    public Label update(Label label) {
        return labelRepo.save(label);
    }

    public void delete(Long id) {
        labelRepo.deleteById(id);
    }
}
