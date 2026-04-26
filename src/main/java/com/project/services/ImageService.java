package com.project.services;

import java.io.File;
import java.io.IOException;

public interface ImageService {
    String subirImagen(File archivo) throws IOException, InterruptedException;

}
