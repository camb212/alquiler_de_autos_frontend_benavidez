# carrt_cam

Kotlin Android frontend for the Django REST backend in `alquiler_de_autos_backend_benavidez`.

## Backend routes used

- `GET /api/categories/`
- `GET /api/vehicles/`
- `GET /api/reservations/`

## Project setup

The app uses the backend IP you requested by default:

- `http://74.163.98.15:8000/`

If you run the backend on a different host or port, update `API_BASE_URL` in `app/build.gradle.kts`.

## Build

```powershell
cd carrt_cam
.\gradlew.bat assembleDebug
```

## Install

```powershell
cd carrt_cam
.\gradlew.bat installDebug
```
