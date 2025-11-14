# practica-pdl
### Librerías usadas
* [TSLIB](https://dlsiis.fi.upm.es/procesadores/Herramientas.html)
### Compilación
Se usa la herramienta Gradle:
```
./gradlew build
```
### Ejecución
```
./gradlew run --args="<PATH_AL_ARCHIVO_FUENTE>"
```
La salida será:
1. Un archivo de tokens: `tokens.txt`
2. Un archivo con las tablas de símbolos: `simbolos.txt`
3. Un archivo con el parse: `parse.txt`
### Estructura del sistema de archivos
Se incluyen los directorios
* `src/main/java`: El código del proyecto principal
* `src/test/java`: El código de los tests unitarios
* `src/test/resources`: Los archivos fuente de prueba usados para los tests unitarios
* `src/test/resources/soluciones`: Los archivos de tokens esperados al probar algunas de las fuentes para el Analizador Léxico
* `src/test/resources/anexos`: Los archivos que sirven como anexos para la memoria
* `tslib/`: Archivos de la librería TSLIB
