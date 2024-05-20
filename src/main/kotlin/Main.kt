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

//fun main(){
//    //esto me sirve para ordenar los diccioanrios
//    //puedo hacer un for que vaya cambiando y que cada vesz que cambie la clave del diccionario
//    // se cambie de nuevo una variable valor,
//    // y que si ese valor esmenor que el valor anterior cambie en que mejor ctf :D variable mejor ctf
//    val numero: MutableList<MutableMap<Int, MutableMap<Int, Int>>> = mutableListOf(
//        mutableMapOf(1 to mutableMapOf(2 to 1)),
//        mutableMapOf(3 to mutableMapOf(3 to 3)),
//        mutableMapOf(4 to mutableMapOf(1 to 2)),
//        mutableMapOf(2 to mutableMapOf(1 to 2))
//    )
//
//    val listaOrdenados = numero.sortedWith(compareBy({ it.keys.first() }, { it.values.first().keys.first() }, { it.values.first().values.first() }))
//
//    println(listaOrdenados)
//    var idctf=  1
//    var competicion = 0
//    var posicion = 0
//    var posicionactual = 0
//    var contador = 0
//    for (i in listaOrdenados){
//        if (idctf != i.keys.first()){
//            if(posicionactual < posicion){
//                competicion = idctf
//                posicion = posicionactual
//                contador = 0
//                posicionactual = 0
//                idctf= i.keys.first()
//            }
//            contador ++
//            val valores=listaOrdenados[idctf]
//            val grupo = valores.values.first()
//            if (grupo.values.first() == 2){
//                posicionactual = contador
//                println("el equipo ${grupo.values.first()} ha conseguido el puesto $contador")
//            }
//        }
//
//
//
//    }
//}

/**
 * notas:
 * problema 1: dia 20/05/2024 a las 6:34: estado no solucionado
 * voy a tener problema con los for a la hora del parametro -f
 * solucion hechar los for en el main y que cada funcion reciba lo que tenga que recibir
 *
 * problema numero 2 20/05/2024 a la 6:43: estado no solucionado
 * como mi funcionana retorna algo TODAS, hacer que los mensajes de error o success se muestren en el main, asi la clase DAO se encarga de lo que tiene que hacer y no la sobvrecargo tontamente
 * */

fun main(args: Array<String>) = application {

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

    fun crearGrupo() {//-g, esta bien realizada
        //esta funcion no se debe de tocar mas
        //funciona bien
        for (i in 1..args.size) {
            //compruebo al grupo
            val grupo: Grupos? = comprobador.comprobarGrupos(args[i])
            if (grupo != null) {
                // si el grupo es distinto de nulo inserto el grupo
                val grupoInsertado = groupService.crearGrupo(grupo)
                //si se ha insertado correctamente imprimo por pantalla
                consola.grupoInsertado(grupoInsertado)
            } else {
                consola.showMessage(
                    "ERROR: El parámetro <grupoid> " +
                            "debe ser un valor numérico de tipo entero."
                )
            }
        }
    }

    fun anadirParticipacion() {//-p
        //funcion comprobada no tocar o modificar
        for (i in 1..args.size) {
            //esto me devuelve un ctf o un nulo como error
            val ctf: Ctfs? = comprobador.comprobarCtfs(args[i])
            if (ctf != null) {
                // si el grupo no es nulo y existe actualiza su puntuacion
                if (ctfService.comprobarExistencia(ctf)) {

                    //esta parte esta correcta
                    val puntuacionantigua = ctf.puntuacion
                    val ctfsInsertado = ctfService.actualizarPuntuacion(ctf)
                    consola.ctfsactualizado(groupService, ctfsInsertado, puntuacionantigua)

                    //si no lo que hace es crear una nueva participacion
                } else {

                    //esta parte tambien esta correcta
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

    fun eliminargrupo() {//-t
        //funcion no comprobada, no tocar a menos que a la hora de compilar de fallos
        //compruebo el grupo por si acaso el usuario me lo ha puesto erroneo
        var grupo: Grupos? = try {
            comprobador.comprobarGrupos(args[1])
        } catch (e: IndexOutOfBoundsException) {
            null
        }

        if (grupo != null) {
            //compruebo si el grupo esta en la base de datos
            val grupoEscogido = groupService.getById(grupo.grupoId)
            if (grupoEscogido != null) {
                //escogo toda la lista de ctf, antes de eliminar
                val lista = ctfService.getAll()
                //elimino las participaciones del grupo en al tabla ctf, ME FUNCIONA ESTAS FUNCION NO TOCAR
                ctfService.eliminarParticipacion(grupoEscogido.grupoId)
                //elimino en este caso al grupo, FUNCIONA NO TOCAR
                groupService.eliminarGrupo(grupoEscogido.grupoId)
                //muestro por consola el numero de participaciones eliminada y el grupo
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

    fun eliminarSoloPArticipacion() {//-e
        //FUNCIONA NO TOCAR
        try {
            //compruebo si el grupo y el usuario estan bien
            val grupo = groupService.getById(args[1].toInt())
            val ctfid = ctfService.escogerUnSoloCtf(args[2].toInt())
            //si las dos cosas estan bien entonces lo que hago es lo siguiente
            if (grupo != null && ctfid != null) {
                //aqui intento eliminar esa participacion
                if (ctfService.eliminarParticipacion(grupo.grupoId, ctfid.ctfdId)) {
                    //si se mete en esta parte del codigo es que ha salid ocorrectamente la transccion
                    consola.showMessage(
                        "Procesado: Eliminada participación del grupo \"${grupo.grupoDesc}\"" +
                                " en el CTF ${ctfid}."
                    )
                    //aqui miro si ese grupo ha tenido algun otro mejor puntuacion
                    val grupoActualizado = groupService.actualizarmejorCtfs(grupo, ctfService.getAll())
                    //si al final no tiene ninguna participacion entoncesm uestro un mensaje o otro
                    if (grupoActualizado != null) {
                        if (grupoActualizado.mejorPosCTFId == null) {
                            consola.showMessage("el grupo ${grupoActualizado.grupoDesc} no tiene ninguna participacion despues de eliminar su unica participacion.")
                        } else {
                            consola.showMessage("el grupo ${grupoActualizado.grupoDesc} ahora su mejor ctf es ~${grupoActualizado.mejorPosCTFId}")
                        }
                    }
                } else {
                    consola.showMessage("ERROR: participacion no eliminada")
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

when (args[0]) {

    //id	comando	Descripción
    //1	-g <grupoId> <grupoDesc>
    // Añade un nuevo grupo con <grupoid> y <grupodesc> en la tabla GRUPOS.
    "-g" -> {
        //esta funcion no se debe de tocar mas
        crearGrupo()
        //funciona bien
    }
    //terminado pero no probado

    //2	-p <ctfId> <grupoId> <puntuacion>
    // Añade una participación del grupo <grupoid> en el CTF <ctfid>
    // con la puntuación <puntuacion>. Si la participación del grupo <grupoid>
    // en el CTF <ctfid> ya existe, actualiza la puntualización.
    // En cualquiera de los casos, recalcula el campo mejorposCTFid
    // de los grupos en la tabla GRUPOS.
    "-p" -> {
        //funcion comprobada no tocar o modificar
        anadirParticipacion()
    }
    //terminado pero no probado

    //3	-t <grupoId>
    // Elimina el grupo <grupoid> en la tabla GRUPOS,
    // por tanto también elimina todas sus participaciones en los CTF.
    "-t" -> {
        eliminargrupo()
    }
    //4	-e <ctfId> <grupoId>
    // Elimina la participación del grupo <grupoid> en el CTF <ctfid>.
    // Si no existe la participación, no realiza nada.
    // Finalmente, recalcula el campo mejorposCTFid de los grupos en la tabla GRUPOS.
    "-e" -> {
        eliminarSoloPArticipacion()
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
