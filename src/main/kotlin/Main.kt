import DAO.SqlDao.SqlDaoCtf
import DAO.SqlDao.SqlDaoGroup
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import comprobadorArgumentos.ComprobadorArgs
import consola.Consola
import dataclass.Ctfs
import dataclass.Grupos
import dbConnection.DataSourceFactory
import gestorFichero.GestorFicheros
import service.CtfService
import service.GroupService
import service.ICtfsService
import service.IGruposService
import java.io.File

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

fun main(){
    //esto me sirve para ordenar los diccioanrios
    //puedo hacer un for que vaya cambiando y que cada vesz que cambie la clave del diccionario
    // se cambie de nuevo una variable valor,
    // y que si ese valor esmenor que el valor anterior cambie en que mejor ctf :D variable mejor ctf
    val numero: MutableList<MutableMap<Int, MutableMap<Int, Int>>> = mutableListOf(
        mutableMapOf(1 to mutableMapOf(2 to 1)),
        mutableMapOf(1 to mutableMapOf(3 to 3)),
        mutableMapOf(1 to mutableMapOf(1 to 2))
    )

    val listaOrdenados = numero.sortedWith(compareBy({ it.keys.first() }, { it.values.first().keys.first() }, { it.values.first().values.first() }))

    println(listaOrdenados)
    var idctf: Int
    for (i in listaOrdenados){

    }
}


fun maxzxzzxzxin(args: Array<String>) = application {

    val ficheroConfiguracion = File("src/main/resources/config.init")
    val comprobador = ComprobadorArgs()
    val consola = Consola()
    val gestorFicheros = GestorFicheros()
    val opcion = gestorFicheros.leerFicheroConfig(ficheroConfiguracion)

    val (fuenteDeDatoGroup, fuenteDeDatoCtfs) = when (opcion) {
        "SQL" -> Pair(
            SqlDaoGroup(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI), consola),
            SqlDaoCtf(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI), consola)

        )


        "XML" -> TODO()
        "JSON" -> TODO()
        "TXT" -> TODO()
        else -> {
            consola.showMessage(
                "ERROR**, El fichero de configuracion esta mal configurado, " +
                        "Abrimos conexion SQL"
            )
            Pair(
                SqlDaoGroup(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI), consola),
                SqlDaoCtf(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI), consola)

            )
        }
    }
    val ctfService: ICtfsService = CtfService(fuenteDeDatoCtfs)
    val groupService: IGruposService = GroupService(fuenteDeDatoGroup)

    when (args[0]) {

        //id	comando	Descripción
        //1	-g <grupoId> <grupoDesc>
        // Añade un nuevo grupo con <grupoid> y <grupodesc> en la tabla GRUPOS.
        "-g" -> {
            for (i in 1..args.size) {
                val grupo: Grupos? = comprobador.comprobarGrupos(args[i])
                if (grupo != null) {
                    val grupoInsertado = groupService.crearGrupo(grupo)
                    consola.grupoInsertado(grupoInsertado)
                } else {
                    consola.showMessage(
                        "ERROR: El parámetro <grupoid> " +
                                "debe ser un valor numérico de tipo entero."
                    )
                }
            }
        }
        //terminado pero no probado

        //2	-p <ctfId> <grupoId> <puntuacion>
        // Añade una participación del grupo <grupoid> en el CTF <ctfid>
        // con la puntuación <puntuacion>. Si la participación del grupo <grupoid>
        // en el CTF <ctfid> ya existe, actualiza la puntualización.
        // En cualquiera de los casos, recalcula el campo mejorposCTFid
        // de los grupos en la tabla GRUPOS.
        "-p" -> {
            for (i in 1..args.size) {
                val ctf: Ctfs? = comprobador.comprobarCtfs(args[i])
                if (ctf != null) {
                    if (ctfService.comprobarExistencia(ctf)) {
                        val puntuacionantigua = ctf.puntuacion
                        val ctfsInsertado = ctfService.actualizarPuntuacion(ctf)
                        consola.ctfsactualizado(groupService, ctfsInsertado, puntuacionantigua)
                    } else {
                        val ctfsInsertado = ctfService.anadirParticipacion(ctf)
                        consola.ctfsInsertado(groupService, ctfsInsertado)
                    }

                } else {
                    consola.showMessage(
                        "ERROR: El número de parámetros no es adecuado."
                    )
                }
            }
        }
        //terminado pero no probado

        //3	-t <grupoId>
        // Elimina el grupo <grupoid> en la tabla GRUPOS,
        // por tanto también elimina todas sus participaciones en los CTF.
        "-t" -> {
            var grupo: Grupos? = try {
                comprobador.comprobarGrupos(args[1])
            } catch (e: IndexOutOfBoundsException) {
                null
            }

            if (grupo != null) {
                val grupoEscogido = groupService.getById(grupo.grupoId)
                if (grupoEscogido != null) {
                    val lista = ctfService.getAll()
                    ctfService.eliminarParticipacion(grupoEscogido.grupoId)
                    groupService.delete(grupoEscogido.grupoId)
                    consola.participacionEliminadas(grupoEscogido.grupoDesc, lista)
                } else {
                    consola.showMessage(
                        "ERROR: El grupo no esta registrado."
                    )
                }


            } else {
                consola.showMessage(
                    "ERROR: El número de parámetros no es adecuado."
                )
            }
        }
        //4	-e <ctfId> <grupoId>
        // Elimina la participación del grupo <grupoid> en el CTF <ctfid>.
        // Si no existe la participación, no realiza nada.
        // Finalmente, recalcula el campo mejorposCTFid de los grupos en la tabla GRUPOS.
        "-e" -> {
            try {
                val grupo = groupService.getById(args[1].toInt())
                val ctfid = ctfService.getById(args[2].toInt())
                if (grupo != null && ctfid != null) {
                    if (ctfService.eliminarParticipacion(grupo.grupoId, ctfid.ctfdId)) {
                        consola.showMessage(
                            "Procesado: Eliminada participación del grupo \"${grupo.grupoDesc}\"" +
                                    " en el CTF ${ctfid.ctfdId}."
                        )
                        val numero: MutableList<MutableMap<Int,
                                MutableMap<Int, Int>>> =
                            emptyList<MutableMap<Int,
                                    MutableMap<Int, Int>>>().toMutableList()
                        ctfService.getAll()?.forEach {
                                numero.add(mutableMapOf(it.ctfdId to mutableMapOf(it.puntuacion to it.grupoId)))
                        } ?: consola.showMessage(
                            "ERROR 003: no se pudo aceder a la tabla ctfs, " +
                                    "por tanto no puedo recoger todas las puntuaciones"
                        )

                        val listaOrdenados = numero.sortedWith(compareBy({ it.keys.first() }, { it.values.first().keys.first() }, { it.values.first().values.first() }))


                        groupService.getAll()?.forEach {
                            //quiero agarrar un solo grupo no varios
                        } ?: consola.showMessage(
                            "ERROR 003: no se pudo aceder a la tabla group, " +
                                    "por tanto no puedo actualizar la puntuacion de mejorctfdid"
                        )
                    } else {
                        consola.showMessage("participacion no encontrada")
                    }
                } else {
                    consola.showMessage("participacion no encontrada")
                }
            } catch (e: IndexOutOfBoundsException) {
                consola.showMessage("ERROR 001: parametros introducido erroneamente.")
            } catch (e: NumberFormatException) {
                consola.showMessage("ERROR 002: el segundo parametro debe de ser un numero entero.")
            }
        }
    }

//5	-l <grupoId>	Si <grupoId>
// esta presente muestra la información del grupo <grupoId> y sus participaciones. Si el grupo no está presente muestra la información de todos los grupos.
    if (args[0] == "-l") {

    }
//6	-c <ctfId>	Si <ctfId>
// esta presente muestra la participación de los grupos y la puntuación obtenida, ordenado de mayor a menor puntuación.
    if (args[0] == "-c") {

    }
//7	-f <filepath>
    if (args[0] == "-f") {

    }
// Si <filepath> existe, será un fichero con un conjunto de comandos para procesamiento por lotes.

//8	-i
// Lanza la interface gráfica.
    if (args[0] == "-i") {

    } else {
        // mensaje de error
    }
}
