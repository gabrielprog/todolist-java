package br.com.gabriel.todolist.Utils;

import org.modelmapper.ModelMapper;

public class ObjectComparator {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static <T> T mergeObjects(T existingObject, T newObject) {
        modelMapper.map(newObject, existingObject);
        return existingObject;
    }
}

