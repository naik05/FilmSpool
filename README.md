# FilmSpool

**FilmSpool** is a lightweight, user-friendly desktop utility designed to simplify adding EXIF metadata to your scanned analog film photos.

#### Motivation

This tool was created out of a personal need to make the excellent **ExifTool** more accessible. While ExifTool is the gold standard for metadata management, it can be intimidating to use via the command line for everyday scanning tasks. FilmSpool provides a clean, intuitive GUI to apply your camera and lens settings to entire folders of images in just a few clicks.

#### Credits

This application is a wrapper built around [ExifTool](https://exiftool.org/) by Phil Harvey. All credit for the incredible engine that powers this software goes to him. Without his work, this tool would not be possible.

#### How to use

1. **Prepare your files:** Ensure your scanned photos are in a single folder.
2. Launch FilmSpool: Simply double-click the `FilmSpool.exe`.
3. **Select your folder:** Either drag and drop the folder containing your JPEGs onto the "Drop Zone" or click the "Or Search Folder..." button to browse for it.
4. **Configure your presets:** Fill in your camera and lens details (e.g., Manufacturer, Model, ISO, Focal Length, Aperture). The Date and Time fields are optional but recommended for better organization.
5. **Apply:** Click the **"Apply Metadata"** button. The app will process your files in the background. Once finished, you will see a success message.

Note: FilmSpool is a portable application and does not require installation. Just keep the `FilmSpool.exe`, `exiftool` and `exiftools_files` folder together in the same directory.

Created by naik05/Kian Nölling
06/05/2026
