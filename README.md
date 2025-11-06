<div align="center">
  <img src="images/lebentech_logo.png" alt="Lebentech Logo" width="200"/>
</div>

# Lebentech Bluetooth Sender SDK

SDK para Android que permite la comunicación y envío de datos a través de Bluetooth.

## Índice

- [Instalación](#instalación)
- [Configuración](#configuración)
  - [Permisos requeridos](#permisos-requeridos)
  - [Configuración del Manifest](#configuración-del-manifest)
- [Uso básico](#uso-básico)
  - [Inicialización del SDK](#inicialización-del-sdk)
  - [Escaneo de dispositivos](#escaneo-de-dispositivos)
  - [Estado del Bluetooth (BluetoothUiState)](#estado-del-bluetooth-bluetoothuistate)
  - [Navegación entre pantallas](#navegación-entre-pantallas)
  - [Envío de mensajes Bluetooth](#envío-de-mensajes-bluetooth)

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
    implementation("com.github.Lebentech:bluetooth-sender-sdk:v0.0.2")
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
        }
    }
}
```

### Configuración del Manifest

Asegúrate de que tu aplicación tenga configurado correctamente el `targetSdkVersion` y `compileSdkVersion` para soportar las características del SDK.

## Uso básico

### Inicialización del SDK

Para crear una instancia del SDK:

```kotlin
private val bluetoothService: BluetoothService by lazy {
    BluetoothService(this)
}
```

### Escaneo de dispositivos

Ejemplo básico para comenzar a escuchar dispositivos cercanos:

```kotlin
val state by bluetoothService.state.collectAsState()

this.checkAndRequestBluetoothPermissions {
    bluetoothService.startScan()
}

Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    LazyColumn(
        modifier = Modifier.padding(innerPadding)
    ) {
        item {
            Text(
                text = "Dispositivos vinculados."
            )
        }

        items(state.pairedDevices) {
            Text(
                it.name ?: it.address,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            Text(
                text = "Dispositivos encontrados."
            )
        }

        items(state.scannedDevices) {
            Text(
                it.name ?: it.address,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
```

### Estado del Bluetooth (BluetoothUiState)

El SDK proporciona un `MutableStateFlow` con el siguiente estado:

```kotlin
data class BluetoothUiState(
    val scannedDevices: List<ApplicationDevice> = emptyList(),
    val pairedDevices: List<ApplicationDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val messages: BluetoothMessage = BluetoothMessage("", "", "", "", "", "", "", "")
)
```

**Propiedades del estado:**

- `scannedDevices`: Dispositivos detectados cercanos durante el escaneo
- `pairedDevices`: Dispositivos previamente vinculados al dispositivo
- `isConnected`: Indica si se estableció correctamente una conexión
- `isConnecting`: Indica si está en proceso de realizar una conexión
- `errorMessage`: Mensaje de error en caso de problemas de conexión
- `messages`: Último mensaje enviado a través de Bluetooth

### Navegación entre pantallas

Ejemplo de navegación sencilla basada en el estado de conexión:

```kotlin
val state by bluetoothService.state.collectAsState()

this.checkAndRequestBluetoothPermissions {
    bluetoothService.startScan()
}

MyApplicationTheme {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (state.isConnected) {
            ConnectionScreen(
                modifier = Modifier.padding(innerPadding),
                bluetoothService = bluetoothService
            )
        } else {
            HomeScreen(
                modifier = Modifier.padding(innerPadding),
                state = state
            )
        }
    }
}
```

**Flujo de navegación:**

1. **HomeScreen**: Muestra dispositivos disponibles y permite seleccionar uno para conectar
2. **ConnectionScreen**: Se muestra automáticamente cuando `state.isConnected` es `true`, permite enviar mensajes al dispositivo conectado

La navegación se maneja automáticamente basándose en el estado de conexión del SDK.

### Envío de mensajes Bluetooth

Ejemplo de cómo enviar mensajes una vez conectado a un dispositivo:

```kotlin
var currentId by remember { mutableStateOf("") }
var isFirstCall by remember { mutableStateOf(true) }

Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Button(
        onClick = {
            currentId = UUID.randomUUID().toString()
            isFirstCall = false
            bluetoothService.sendMessage("1", currentId)
        },
        enabled = isFirstCall
    ) {
        Text("Iniciar camara")
    }

    Button(
        onClick = {
            isFirstCall = true
            bluetoothService.sendMessage("2", currentId)
        },
        enabled = !isFirstCall
    ) {
        Text("Finalizar camara")
    }

    Button(
        onClick = {
            bluetoothService.disconnectFromDevice()
        }
    ) {
        Text("Salir")
    }
}
```

**Funciones principales:**

- `sendMessage(message, id)`: Envía un mensaje al dispositivo conectado
- `disconnectFromDevice()`: Desconecta del dispositivo actual

*Documentación en desarrollo...*
