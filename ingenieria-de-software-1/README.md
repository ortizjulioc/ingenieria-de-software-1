
# 🖌️ Aplicación de Dibujo

Este proyecto es una aplicación de dibujo en Java que permite crear y manipular figuras en un lienzo.  
El objetivo es aprender y practicar tanto **programación gráfica** como el **flujo de trabajo con Git**.

---

## 🚀 Comandos básicos de Git

A continuación, encontrarás los comandos más usados para trabajar con este repositorio:

### 🔹 Configuración inicial
```bash
git config --global user.name "Tu Nombre"
git config --global user.email "tuemail@example.com"
```
🔹 Guardar cambios (commit)
```bash
git add .
git commit -m "Descripción de los cambios"
```
🔹 Subir cambios a GitHub (push)
```bash
Copiar código
git push origin nombre-de-la-rama
```
🔹 Cambiar de rama
```bash
git checkout nombre-de-la-rama
```
🔹 Crear una nueva rama
```bash
git checkout -b nueva-rama
```
🔹 Fusionar ramas (merge)
```bash
git checkout main
git merge nombre-de-la-rama
```
📌 Flujo de trabajo recomendado
Crear una nueva rama para cada funcionalidad o corrección:

```bash
git checkout -b feature/nombre-funcionalidad
Realizar cambios y hacer commits descriptivos.
```
Subir la rama a GitHub:
```bash
git push origin feature/nombre-funcionalidad
Crear un Pull Request en GitHub para fusionar los cambios a main.
```
💡 Ejemplo práctico
Si quieres agregar una nueva herramienta de dibujo:

```bash
git checkout -b feature/herramienta-borrador
# (haces cambios en el código)
git add .
git commit -m "Agregada herramienta de borrador"
git push origin feature/herramienta-borrador
```
