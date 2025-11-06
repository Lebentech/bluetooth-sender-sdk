# MyApplication SDK

SDK para funcionalidades de Bluetooth y comunicación entre dispositivos Android.

## Índice

- [Instalación](#instalación)
- [Configuración](#configuración)
- [Permisos Requeridos](#permisos-requeridos)
- [Uso Básico](#uso-básico)
- [API Reference](#api-reference)
- [Ejemplos](#ejemplos)
- [Troubleshooting](#troubleshooting)

## Permisos Requeridos

### AndroidManifest.xml

Agrega los siguientes permisos en tu `AndroidManifest.xml`:

```xml
<!-- Permisos de ubicación -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- Permisos de Bluetooth -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" android:usesPermissionFlags="neverForLocation" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />

<!-- Hardware requerido -->
<uses-feature android:name="android.hardware.bluetooth" android:required="true"/>
```

### Solicitud de Permisos en Runtime

```kotlin
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

### Uso en MainActivity

```kotlin
this.checkAndRequestBluetoothPermissions {
    Log.d("PERMISSION", "All permissions granted")
    // Inicializar SDK aquí
}
```