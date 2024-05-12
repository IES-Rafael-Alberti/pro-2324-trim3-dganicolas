# Prueba específica unidad 7 y 9

> Se evaluará el RA7 y RA9


## 1. Problema
El equipo de ciberseguridad del IESRA quiere llevar un histórico de la puntuación que consigue cada equipo en los CTFs que se realizan a lo largo del curso.

> Un CTF (Capture The Flag) en Español, «Captura la bandera». Son competiciones que permiten a los participantes poner a prueba las habilidades sobre hacking por medio de retos de diferentes modalidades que tendremos que resolver para conseguir la famosa «flag». Aquel que más flags consiga, más puntos obtendrá y por lo tanto, ganará la competición.

Para ello han diseñado una base de datos muy simple en la que tiene dos entidades:
- entidad `GRUPOS`: Que almacenará cada uno de los grupos participantes junto con el id del CTF en el que han obtenido la mayor puntuación.
    - `grupoid`: Identificador del grupo.
    - `grupodesc`: Descripción del grupo.
    - `mejorposCTFid`: Id del CTF en el que ha logrado la _mejor posición_.
- entidad `CTFS`: Que almacenará la información sobre la participación de cada `GRUPO` en un `CTFS`.
    - `CTFid`: Identificador del CTF
    - `grupoid`: Identificador del grupo que participa en el CTF
    - `puntuacion`: Puntuación lograda.

El programa tendrá que soportar las siguientes operaciones, que se pasarán a través de línea de comandos:

| id | **comando**                       	  | **Descripción**                                                                    	                                                                                                                                                                                                                         |
|----|--------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1  | `-g <grupoId> <grupoDesc>` 	         | Añade un nuevo grupo con `<grupoid>` y `<grupodesc>` en la tabla `GRUPOS`.                                                                                                                                                                                                                                   |
| 2  | `-p <ctfId> <grupoId> <puntuacion>` 	 | Añade una participación del grupo `<grupoid>` en el CTF `<ctfid>` con la puntuación `<puntuacion>`. Si la participación del grupo `<grupoid>` en el CTF `<ctfid>` ya existe, actualiza la puntualización. En cualquiera de los casos, recalcula el campo `mejorposCTFid` de los grupos en la tabla `GRUPOS`. |
| 3  | `-t <grupoId>` 	                     | Elimina el grupo `<grupoid>` en la tabla `GRUPOS`, por tanto también elimina todas sus participaciones en los CTF.                                                                                                                                                                                           |
| 4  | `-e <ctfId> <grupoId>`              	 | Elimina la participación del grupo `<grupoid>` en el CTF `<ctfid>`. Si no existe la participación, no realiza nada. Finalmente, recalcula el campo `mejorposCTFid` de los grupos en la tabla `GRUPOS`.                                                                                                       |
| 5  | `-l <grupoId>`              	        | Si `<grupoId>` esta presente muestra la información del grupo `<grupoId>` y sus participaciones. Si el grupo no está presente muestra la información de todos los grupos.                                                                                                                                    |
| 6  | `-c <ctfId>`              	          | Si `<ctfId>` esta presente muestra la participación de los grupos y la puntuación obtenida, ordenado de mayor a menor puntuación.                                                                                                                                                                            |
| 7  | `-f <filepath>`              	       | Si `<filepath>` existe, será un fichero con un conjunto de comandos para procesamiento por lotes.                                                                                                                                                                                                            |
| 8  | `-i`              	       | Lanza la interface gráfica.                                                                                                                                                                                 |

### 1.1. Entrada
La ejecución de cualquiera de las operaciones 1, 2, ... descritas en la tabla. 
Ejemplo de formato del fichero indicado en la opción 7:
```
# Comentario de la línea. Las opciones son: -g, -p, -t, -e, -l, -c, y pueden o no estar en fichero. 
# Crea grupos, podrá tener tantas líneas como quiera
-g
1;1DAM-G1
1;1DAM-G2
1;1DAM-G3

# Añade/Actualiza participaciones, podrá tener tantas líneas como quiera
-p
1;1;100
1;2;90
1;3;80

# Elimina el grupo y sus participaciones, podrá tener tantas líneas como quiera.
-t
1

# Elimina participación, podrá tener tantas líneas como quiera.
-e
1;1

# Listado de participaciones, podrá tener tantas líneas como quiera.
-l
1

# Listado de grupos y puntuaciones, podrá tener tantas líneas como quiera.
-c
1

```


### 1.2. Salida
- Mensaje adecuado si el comando suministrado no es correcto en cualquiera de las operaciones 1, 2, ....

  ```
  $ un9pe -p 1 1 
  $ ERROR: El número de parámetros no es adecuado.
  ```

  ```
  $ un9pe -g 1DAM-G1 1
  $ ERROR: El parámetro <grupoid> debe ser un valor numérico de tipo entero.
  ```
  
- Mensaje de confirmación de realización correcta de la operaciones 1, 2 o 3. 

  ```
  $ un9pe -g 1 1DAM-G1
  $ Procesado: Añadido el grupo "1DAM-G1".
  ```

  ```
  $ un9pe -p 1 1 100
  $ Procesado: Añadida participación del grupo "1DAM-G1" en el CTF 1 con una puntuación de 100 puntos.
  ```

  ```
  $ un9pe -p 1 1 105
  $ Procesado: Actualizada la participación del grupo "1DAM-G1" en el CTF 1. La puntuación cambió de 100 a 105 puntos.
  ```

  ```
  $ un9pe -e 1 1
  $ Procesado: Eliminada participación del grupo "1DAM-G1" en el CTF 1.
  ```

  ```
  $ un9pe -t 1
  $ Procesado: Eliminada el grupo "1DAM-G1" y su participación en los CTFs: 1, 2 y 4.
  ```


- Listado de información 
  ```
  $ un9pe -l 1
  $ Procesado: Listado participación del grupo "1DAM-G1"
    GRUPO: 1   1DAM-G1  MEJORCTF: 4, Posición: 2, Puntuación: 70
    CTF   | Puntuación | Posición
    -----------------------------
        4 |       70   |        2
        2 |       90   |        5
  ```

  ```
  $ un9pe -c 1
  $ Procesado: Listado participación en el CTF "1"
    GRUPO GANADOR: 1DAM-G1  Mejor puntuación: 90 Total participants: 3
    GRUPO   | Puntuación
    --------------------
    1DAM-G2 |       90
    1DAM-G1 |       80
    1DAM-G3 |       70
  ```


### Esquema de base de base de datos.

Tabla `GRUPOS`
```
CREATE TABLE GRUPOS (
    grupoid INT NOT NULL AUTO_INCREMENT,
    grupodesc VARCHAR(100) NOT NULL,
    mejorposCTFid INT,
    PRIMARY KEY (grupoid)
);
```

Tabla `CTFS`
```
CREATE TABLE CTFS (
    CTFid INT NOT NULL,
    grupoid INT NOT NULL,
    puntuacion INT NOT NULL,
    PRIMARY KEY (CTFid, grupoid)
);
```

Restricciones de clave foránea para la tabla `GRUPOS`
```
ALTER TABLE GRUPOS
ADD FOREIGN KEY (mejorposCTFid, grupoid)
REFERENCES CTFS(CTFid,grupoid);
```

Valores para la tabla `GRUPOS`
```
insert into grupos(grupoid, grupodesc) values(1, '1DAM-G1');
insert into grupos(grupoid, grupodesc) values(2, '1DAM-G2');
insert into grupos(grupoid, grupodesc) values(3, '1DAM-G3');
insert into grupos(grupoid, grupodesc) values(4, '1DAW-G1');
insert into grupos(grupoid, grupodesc) values(5, '1DAW-G2');
insert into grupos(grupoid, grupodesc) values(6, '1DAW-G3');
```

## 2. ¿Qué se pide?

a. Realizar el programa haciendo uso de la base de datos H2. No olvidar configurar adecuadamente el archivo `build.gradle`.

b. Poder ejecutar las operaciones, haciendo uso de los objetos DAO necesarios que accedan a la BBDD h2. Recuerda que los objetos DAO deben ser inyectados en la clase servicio. Y los servicios deben inyectarse en la clase principal que se encargará de procesar las operaciones.

c. Utilizar una Abstract Factory para poder cargar los datos de DAOs que tengan distintas fuentes de datos. En principio solo se utilizará una fuente de datos, en nuestro caso SQL, pero se quiere tener la posibilidad de poder cambiar de fuente de datos en un futuro a JSON y XML. Existirá una clase abstracta DAOFactory, que contendrá los métodos necesarios para obtener los DAOs necesarios. Y una clase concreta SQLDAOFactory que implementará los métodos de la clase abstracta DAOFactory. Para configurar la fuente de datos se leerá de un fichero de configuración, que contendrá el tipo de fuente de datos a utilizar. Un ejemplo del fichero puede ser el siguiente:

    ```
    # Fichero de configuración
    # Tipo de fuente de datos a utilizar
    tipo=SQL
    ```


d. Realizar las operaciones necesarias para que el programa funcione correctamente.

1. Ejecutar la operación 1 para añadir un nuevo grupo. Actualización de la tabla GRUPOS.

2. Ejecutar la operación 2 para añadir una participación de un grupo en un CTF. Actualización del CTF en el que quedó mejor situado de todos los grupos.

3. Ejecutar la operación 3 eliminar un grupo.

4. Ejecutar la operación 4 para eliminar una participación de un grupo en un CTF. Actualización del CTF en el que quedó mejor situado de todos los grupos.

5. Ejecutar la operación 5 para listar la información de todos los grupos o un grupo en concreto.

6. Ejecutar la operación 6 para listar la información de participación de un CTF.

7. Además de ejecutar las operaciones desde la línea de comandos, ejecuta un fichero para realizar la carga de los datos, grupos y resultados, utilizando la operación 7. 

    * El formato del fichero será el indicado en apartado de 1.1. Entrada.
    * Durante la ejecución de la operación 7, tiene que mostrar los mismos mensajes que si se ejecutara el comando desde la línea de comandos, en caso de error al procesar una carta mostrará el mensaje de aviso, tal y como se indicaba en los ejemplos, y continuará con la carga.

7. El programa podrá ser ejecutado desde la línea de comandos con cualquiera de las 8 operaciones anteriores o sin ningún parámetro. Mostrará por consola la información necesaria según los requisitos solicitados y posteriormente abrirá una ventana gráfica, que tendrá el siguiente funcionamiento:
    
    * La ventana contendrá como mínimo un componente Column o LazyColumn, dos botones y un campo de edición de texto (TextEditor/OutlinedTextEditor).
    * Por defecto mostrará la información de todos los grupos (grupoid, grupodesc, mejorposCTFid).
    * Si se pulsa el botón "Mostrar" deberá mostrar la información del grupo introducido en el campo de edición (si existe en la base de datos).
    * Si el campo está vacío deberá mostrar la información de todos los grupos.
    * Siempre, después de mostrar la información el campo debe ser limpiado.
    * El segundo botón, "Exportar", realizará la exportación de la clasificación final por CTFs a un fichero.

    Un ejemplo de fichero de clasificación exportado:

   ```
   CTF: 1
   1. 1DAM-G2 (123 puntos)
   2. 1DAW-G1 (76 puntos)
   3. 1DAW-G3 (63 puntos)
   ...

   CTF: 3
   1. 1DAM-G1 (99 puntos)
   2. 1DAW-G1 (88 puntos)
   ...
   ```

## 3. Ejecución y test
Para probar el programa se realizará distintas llamadas al programa, comparando la salida con la salida esperada.
- Deberá comprobar los distintos casos de error. 
- Deberá mostrar la información sobre las operaciones realizadas como se muestra en el apartado Salida.

## 4. Evaluación

En tarea.

## 5. Condiciones de entrega

Repositorio y ejecutable.

## 4. Bibliografía

Mas documentación en la tarea.
