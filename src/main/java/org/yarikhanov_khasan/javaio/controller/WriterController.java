package org.yarikhanov_khasan.javaio.controller;

import org.yarikhanov_khasan.javaio.model.Writer;
import org.yarikhanov_khasan.javaio.repositoryImpl.GsonWriterRepoImpl;

import java.util.List;

public class WriterController {
    private GsonWriterRepoImpl writerRepo = new GsonWriterRepoImpl();

    public Writer getById(Long id) {
        return writerRepo.getById(id);
    }

    public List<Writer> getAll() {
        return writerRepo.getAll();
    }

    public Writer save(Writer writer) {
        return writerRepo.save(writer);
    }

    public Writer update(Writer writer) {
        return writerRepo.save(writer);
    }

    public void delete(Long id) {
        writerRepo.deleteById(id);
    }
}
