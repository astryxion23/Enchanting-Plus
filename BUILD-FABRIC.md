# Building Enchanting Plus (Fabric 1.20.1)

This project is set up for **Fabric 1.20.1**. The build **reaches compileJava** and fails there with Forge→Fabric port errors (missing packages, Forge APIs). Port the Java code to Fabric equivalents to fix.

**Important:** The Foojay plugin is commented out in `settings.gradle` so that the buildscript classpath resolves Gson 2.10.1 (required for Loom on Java 17+). You can re-enable it after porting if you need toolchain auto-detection.

## Requirements

- **Java 17** or newer
- **Gradle 8.6** (included via wrapper)

## Running the build

**Recommended:** use the wrapper that forces Java 17:

```cmd
gradlew-fabric.bat build
```

If your JDK 17 is elsewhere, edit `gradlew-fabric.bat` and set `JAVA_HOME` to your JDK 17 path.

**Alternatively**, set **Java 17** as `JAVA_HOME` before running `gradlew.bat`:

**Windows (cmd):**
```cmd
set JAVA_HOME=C:\Path\To\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%
gradlew.bat build
```

**Windows (PowerShell):**
```powershell
$env:JAVA_HOME = "C:\Path\To\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
.\gradlew.bat build
```

**If you see a Gson/ReflectionAccessFilter or "Cannot get MinecraftProvider" error:** (1) Use Java 17 via `gradlew-fabric.bat` or `JAVA_HOME`. (2) Clear the Loom cache: delete the project `.gradle` folder and, if needed, `%USERPROFILE%\.gradle\caches\fabric-loom`, then run the build again.

## Tasks

- `gradlew runClient` – run Minecraft client with the mod
- `gradlew runServer` – run dedicated server
- `gradlew build` – build the mod JAR (in `build/libs/`)
