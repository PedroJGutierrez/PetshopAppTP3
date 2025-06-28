# 🐾 Petshop App TP3

## 📱 Descripción

**Petshop App TP3** es una aplicación móvil desarrollada en Kotlin que permite a los usuarios de PetLovers acceder a una experiencia de compra completa y personalizada para productos de mascotas. La aplicación está diseñada para atender tanto a usuarios finales como a revendedores, ofreciendo funcionalidades específicas para cada tipo de perfil.

## ✨ Características Principales

### 👤 Para Usuarios Finales

- **Registro y gestión de perfil** personalizado
- **Navegación por categorías**: alimentos, juguetes, higiene, salud, accesorios y más
- **Sistema de favoritos** para productos preferidos
- **Carrito de compras** con funcionalidad completa de checkout
- **Seguimiento de pedidos** en tiempo real
- **Historial de compras** detallado

### 🏪 Para Revendedores

- **Acceso a precios mayoristas** exclusivos
- **Gestión de múltiples pedidos** simultáneos
- **Beneficios y descuentos** especiales
- **Panel comercial** con métricas de ventas
- **Historial especializado** para fines comerciales

### 🔍 Funcionalidades Adicionales

- **Búsqueda avanzada** por categoría o palabra clave
- **Modo claro/oscuro** configurable
- **Notificaciones** de estado de pedidos
- **Interfaz intuitiva** y responsive
- **Integración con múltiples métodos de pago**

## 🏗️ Arquitectura

La aplicación está construida siguiendo el patrón **MVVM (Model-View-ViewModel)** con las siguientes características:

- **Kotlin** como lenguaje principal
- **Android Jetpack Components**
- **Room Database** para persistencia local
- **Firebase Firestore** para base de datos en la nube
- **Retrofit** para comunicación con API REST
- **Navigation Component** para navegación entre pantallas
- **ViewBinding** para manipulación segura de vistas

## 📁 Estructura del Proyecto

```
app/
├── manifests/
├── kotlin+java/
│   └── com.proyecto.petshopapp/
│       ├── data/
│       │   ├── local/          # Base de datos local (Room)
│       │   └── models/         # Modelos de datos
│       ├── navigation/         # Navegación entre pantallas
│       ├── onboarding/         # Pantallas de introducción
│       ├── payment/            # Gestión de pagos
│       │   └── components/
│       ├── profile/            # Gestión de perfiles
│       ├── login/              # Autenticación
│       ├── remote/             # Servicios API
│       └── ui/                 # Interfaces de usuario
│           └── home/
└── res/                        # Recursos (layouts, drawables, etc.)
```

## 🚀 Instalación y Configuración

### Prerrequisitos
- Android Studio Arctic Fox o superior
- Kotlin 1.8+
- Android SDK 24 (API Level 24) mínimo
- Gradle 7.0+

### Pasos de instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/PedroJGutierrez/PetshopAppTP3
   cd petshop-app-tp3
   ```

2. **Abrir en Android Studio**
   - Importar el proyecto
   - Sincronizar Gradle

3. **Configurar dependencias**
   ```bash
   ./gradlew build
   ```

4. **Ejecutar la aplicación**
   - Conectar dispositivo Android o usar emulador
   - Ejecutar desde Android Studio o usar:
   ```bash
   ./gradlew installDebug
   ```

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **Framework**: Android SDK
- **Base de Datos**: Room Database
- **Base de Datos en la Nube**: Firebase Firestore
- **Backend as a Service**: Firebase
- **Networking**: Retrofit + OkHttp
- **Arquitectura**: MVVM + LiveData
- **Navegación**: Navigation Component
- **UI**: Material Design Components
- **Inyección de Dependencias: Hilt (basado en Dagger)

## 📋 Funcionalidades por Pantalla

### 🔐 Autenticación
- `LoginScreen.kt` - Inicio de sesión
- `CreateAccountScreen.kt` - Registro de usuarios
- `ForgotPasswordScreen.kt` - Recuperación de contraseña

### 🏠 Pantalla Principal
- `HomeScreen.kt` - Dashboard principal
- `ProductDetailScreen.kt` - Detalles de productos
- `SearchScreen.kt` - Búsqueda de productos

### 🛒 Compras
- `CartScreen.kt` - Carrito de compras
- `AddPaymentScreen.kt` - Métodos de pago
- `PaymentSuccessScreen.kt` - Confirmación de pago

### 👤 Perfil
- `ProfileScreen.kt` - Gestión de perfil
- `SettingsScreen.kt` - Configuraciones
- `OrdersScreen.kt` - Historial de pedidos

## 🔧 Configuración de Desarrollo

### Variables de Entorno
Crear archivo `local.properties`:
```properties
API_BASE_URL="https://api.petlovers.com/"
API_KEY="your_api_key_here"
```

### Construcción
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Ejecutar tests
./gradlew test
```

## 🧪 Testing

La aplicación incluye tests unitarios y de integración:

```bash
# Tests unitarios
./gradlew testDebugUnitTest

# Tests de instrumentación
./gradlew connectedAndroidTest
```

## 🤝 Contribución

1. Fork del proyecto
2. Crear rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request


## 👥 Equipo de Desarrollo

- **Pedro Gutierrez** - Desarrollador Principal
- **Juan Frick** - Desarrollador
- **Nicolas Lell** - Desarrollador




---

<div align="center">
  <p>Hecho con ❤️ para PetLovers</p>
  <p>🐕 🐱 🐦 🐠 🐹</p>
</div>
