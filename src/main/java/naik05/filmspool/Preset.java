package naik05.filmspool;

public class Preset {
    private String make, model, iso, comment;
    private String lensMake, lensModel, focalLength, fNumber;
    private String dateTime; // ExifTool braucht Datum und Uhrzeit zusammengebaut


    public String getMake() { return make; }
    public String getModel() { return model; }
    public String getIso() { return iso; }
    public String getComment() { return comment; }
    public String getLensMake() { return lensMake; }
    public String getLensModel() { return lensModel; }
    public String getFocalLength() { return focalLength; }
    public String getfNumber() { return fNumber; }
    public String getDateTime() { return dateTime; }


    public void setMake(String make) { this.make = make; }
    public void setModel(String model) { this.model = model; }
    public void setIso(String iso) { this.iso = iso; }
    public void setComment(String comment) { this.comment = comment; }
    public void setLensMake(String lensMake) { this.lensMake = lensMake; }
    public void setLensModel(String lensModel) { this.lensModel = lensModel; }
    public void setFocalLength(String focalLength) { this.focalLength = focalLength; }
    public void setfNumber(String fNumber) { this.fNumber = fNumber; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
}