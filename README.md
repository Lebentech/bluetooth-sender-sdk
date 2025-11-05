# LebentechSenderSDK

SDK para Android que permite la comunicación y envío de datos a través de Bluetooth.

## Índice

- [Instalación](#instalación)
- [Configuración](#configuración)
  - [Permisos requeridos](#permisos-requeridos)
  - [Configuración del Manifest](#configuración-del-manifest)
- [Uso básico](#uso-básico)

## Instalación

### 1. Configurar repositorio

Agrega JitPack a tu archivo `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Agregar dependencia

Agrega la dependencia a tu archivo `build.gradle.kts` (Module: app):

```kotlin
dependencies {
    implementation("com.github.Lebentech:bluetooth-sender-sdk:v0.0.1")
}
```

## Configuración

### Permisos requeridos

El SDK requiere los siguientes permisos para funcionar correctamente:

#### AndroidManifest.xml

Agrega estos permisos a tu archivo `AndroidManifest.xml`:

```xml
<!-- Permisos de ubicación -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- Permisos de Bluetooth -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" android:usesPermissionFlags="neverForLocation" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />

<!-- Características de hardware -->
<uses-feature android:name="android.hardware.bluetooth" android:required="true"/>
```

#### Solicitud de permisos en tiempo de ejecución

El siguiente es un codigo de ejemplo para solicitar los permisos necesarios para que el SDK funcione correctamente:

```kotlin
// Obtener permisos requeridos según la versión de Android
fun MainActivity.getRequiredBluetoothPermissions(): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Android 12+ (API 31)
        arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
        )
    } else {
        // Android 6.0 (API 23) hasta Android 11 (API 30)
        arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}

// Verificar y solicitar permisos
fun MainActivity.checkAndRequestBluetoothPermissions(onBluetoothPermissionsGranted: () -> Unit) {
    val permissionsToRequest = getRequiredBluetoothPermissions()
        .filter { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
        .toTypedArray()

    if (permissionsToRequest.isNotEmpty()) {
        this.requestPermissions(permissionsToRequest, 1001)
    } else {
        onBluetoothPermissionsGranted()
    }
}
```

#### Uso en tu Activity

```kotlin
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Solicitar permisos antes de usar el SDK
        this.checkAndRequestBluetoothPermissions {
            Log.d("PERMISSION", "All permissions granted")
            // Inicializar el SDK aquí
        }
    }
}
```

### Configuración del Manifest

Asegúrate de que tu aplicación tenga configurado correctamente el `targetSdkVersion` y `compileSdkVersion` para soportar las características del SDK.

## Uso básico

*Documentación en desarrollo...*