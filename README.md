# Agencia Max - Gestión de oportunidades

Aplicación móvil desarrollada en **Kotlin y Jetpack Compose** para la gestión de oportunidades corporativas y activaciones de la ficticia Agencia Max.

El proyecto corresponde a la Evaluación Parcial 2 de la asignatura **DSY1105 - Desarrollo de Aplicaciones Móviles** y busca aplicar los contenidos del curso mediante una aplicación funcional, con una interfaz intuitiva, validaciones y acceso a recursos del dispositivo.

## Integrantes

- **Integrante 1:** Maxine Moreno
- **Integrante 2:** Dalí Morales
- **Equipo:** 9

## Sobre el proyecto

Agencia Max necesita organizar sus oportunidades de negocio, como eventos corporativos, ferias y jornadas de beneficios. La aplicación permite registrar estas oportunidades, consultar su información y actualizar su estado de gestión.

También incorpora el escaneo de códigos QR mediante la cámara y una función para consultar la ubicación del dispositivo. Se trata de una demostración académica que utiliza datos ficticios y no se conecta con sistemas reales.

## Funcionalidades

| Módulo                 | Descripción                                                                                                                                                                                               |
| ---------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Inicio de sesión**   | Permite acceder con credenciales de demostración y valida los campos obligatorios y los datos ingresados.                                                                                                 |
| **Navegación**         | Permite desplazarse entre el menú principal, las oportunidades, sus detalles, el formulario, el escáner QR y la ubicación.                                                                                |
| **Oportunidades**      | Muestra una lista con el nombre, tipo, prioridad y estado de cada oportunidad. Incluye datos de ejemplo y permite registrar nuevas oportunidades.                                                         |
| **Detalle y edición**  | Permite consultar la información completa de una oportunidad y modificar sus datos.                                                                                                                       |
| **Formulario**         | Permite crear y editar oportunidades mediante ocho campos, siete de ellos obligatorios. Incluye opciones predefinidas para los catálogos y asigna el estado «Evento identificado» a los nuevos registros. |
| **Validaciones**       | Muestra mensajes de error junto a los campos correspondientes e impide guardar información que no cumple los requisitos.                                                                                  |
| **Persistencia local** | Mantiene los registros en el dispositivo para que continúen disponibles al cerrar y volver a abrir la aplicación.                                                                                         |
| **Escaneo QR**         | Utiliza la cámara para leer códigos QR con el prefijo `MAX:`. Los códigos válidos permiten abrir la oportunidad asociada y los inválidos se rechazan sin modificar los registros.                       |
| **Ubicación**          | Permite consultar la ubicación del dispositivo mediante una acción del usuario, sin seguimiento continuo ni almacenamiento del resultado.                                                                 |
| **Animaciones**        | Incorpora transiciones entre pantallas, animaciones en la lista y efectos visuales para los cambios de estado y los mensajes de error.                                                                    |

## Credenciales de demostración

Para ingresar a la aplicación:

- **Correo:** `dali.morales@max.com`
- **Contraseña:** `dali123`

## Tecnologías utilizadas

- **Lenguaje:** Kotlin 2.2
- **Interfaz:** Jetpack Compose y Material Design 3
- **Arquitectura:** MVVM, ViewModel, StateFlow y Repository
- **Persistencia:** Room 2.8.4 con KSP
- **Navegación:** Navigation Compose
- **Cámara y códigos QR:** CameraX 1.4.2 y ML Kit Barcode Scanning 17.3.0
- **Ubicación:** LocationManager de Android
- **Entorno:** Android Studio Quail 4
- **SDK:** compileSdk y targetSdk 37; minSdk 24

## Estructura del proyecto

La organización del proyecto separa las responsabilidades para facilitar su mantenimiento:

```text
app/src/main/java/cl/duocuc/evaluacion2/
├── data/
│   ├── local/
│   ├── model/
│   └── repository/
├── navigation/
├── location/
├── qr/
└── ui/
    ├── components/
    ├── screens/
    ├── theme/
    └── viewmodels/
```

- `data`: contiene los modelos, el acceso a los datos y su gestión.
- `navigation`: organiza las rutas y la navegación entre pantallas.
- `location`: gestiona la consulta de ubicación.
- `qr`: contiene la funcionalidad de escaneo y validación de códigos QR.
- `ui`: reúne las pantallas, los componentes visuales, el tema y los modelos de vista.

### Requisitos para ejecutar la aplicación

- Android Studio Quail 4 o una versión compatible.
- JDK 17 o superior.
- SDK de Android con la plataforma 37 y las herramientas de compilación requeridas por el proyecto.
- Un emulador o dispositivo Android compatible.

## Probar el escáner QR

La aplicación reconoce códigos cuyo contenido comienza con `MAX:`. Las oportunidades de ejemplo utilizan los siguientes códigos:

- `MAX:OP-001`
- `MAX:OP-002`
- `MAX:OP-003`

Para probar el escáner:

1. Generar un código QR con uno de los valores anteriores.
2. Abrir la opción **Escanear código QR** desde el menú.
3. Conceder el permiso para utilizar la cámara.
4. Escanear el código y comprobar que se abra la oportunidad correspondiente.

Si el contenido no es válido, la aplicación debe mostrar un mensaje de error sin modificar los registros.

Para probar la ubicación en el emulador, se puede configurar una ubicación desde **Extended Controls → Location**.
