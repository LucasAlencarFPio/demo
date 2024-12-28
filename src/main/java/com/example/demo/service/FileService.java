package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Service
public class FileService {

    private static final String UPLOAD_DIRECTORY = "uploads"; // Diretório onde os arquivos serão salvos

    public String readFile(MultipartFile file) throws Exception {
        // Salvar o arquivo no disco
        File savedFile = saveFileToDisk(file);

        // Ler o conteúdo do arquivo salvo
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(savedFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        } catch (Exception e) {
            throw new Exception("Erro ao ler o arquivo", e);
        }

        // Retornar o conteúdo do arquivo como análise (ou processá-lo de outra forma)
        return "Arquivo salvo com sucesso em: " + savedFile.getAbsolutePath() + 
               "\nConteúdo do arquivo:\n" + fileContent;
    }

    private File saveFileToDisk(MultipartFile file) throws Exception {
        // Criar o diretório de upload, se não existir
        File uploadDir = new File(UPLOAD_DIRECTORY);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                throw new Exception("Não foi possível criar o diretório para upload.");
            }
        }

        // Salvar o arquivo no diretório
        File savedFile = new File(uploadDir, file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(savedFile)) {
            fos.write(file.getBytes());
        } catch (Exception e) {
            throw new Exception("Erro ao salvar o arquivo no disco", e);
        }

        return savedFile;
    }
}