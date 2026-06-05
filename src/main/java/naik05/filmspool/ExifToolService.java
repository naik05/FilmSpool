package naik05.filmspool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ExifToolService {

    private final String exifToolPath;

    public ExifToolService() {

        Path path = Paths.get(System.getProperty("user.dir"), "exiftool.exe");
        this.exifToolPath = path.toAbsolutePath().toString();
        System.out.println("Searching for ExifTool in: " + this.exifToolPath);
    }

    public void applyPresetToFolder(String folderPath, Preset preset) {
        List<String> command = new ArrayList<>();

        command.add(exifToolPath);
        command.add("-overwrite_original");

        addIfNotEmpty(command, "Make", preset.getMake());
        addIfNotEmpty(command, "Model", preset.getModel());
        addIfNotEmpty(command, "ISO", preset.getIso());
        addIfNotEmpty(command, "UserComment", preset.getComment());

        addIfNotEmpty(command, "LensMake", preset.getLensMake());
        addIfNotEmpty(command, "LensModel", preset.getLensModel());
        addIfNotEmpty(command, "FocalLength", preset.getFocalLength());
        addIfNotEmpty(command, "FNumber", preset.getfNumber());
        addIfNotEmpty(command, "DateTimeOriginal", preset.getDateTime());

        command.add("-ext");
        command.add("jpg");
        command.add(folderPath);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("ExifTool: " + line);
            }

            process.waitFor();
        } catch (Exception e) {
            System.err.println("Error executing ExifTool!");
            e.printStackTrace();
        }
    }

    private void addIfNotEmpty(List<String> command, String tag, String value) {
        if (value != null && !value.trim().isEmpty()) {
            command.add("-" + tag + "=" + value.trim());
        }
    }
}