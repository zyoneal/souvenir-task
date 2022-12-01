package com.viktor.task.service;

import com.viktor.task.model.Souvenir;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public interface FileService {

  Collection<Souvenir> readSouvenirsFromFile(InputStream file, char delimiter) throws IOException;

  void writeSouvenirsToFile(String SAMPLE_CSV_FILE, List<Souvenir> souvenirs, char delimiter) throws IOException;

}
