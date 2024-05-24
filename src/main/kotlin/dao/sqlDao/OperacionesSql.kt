package DAO.SqlDao

import DAO.IDAO.IDAOOperaciones
import ui.Interfaz.IinterfazGrafica
import androidx.compose.runtime.*
import dataclassEntity.Ctfs
import dataclassEntity.Grupos
import service.ICtfsService
import service.IGruposService

class OperacionesSql(
    override val ctfService: ICtfsService,
    override val groupService: IGruposService,
    override val interfazGrafica: IinterfazGrafica
) : IDAOOperaciones {
    override var leyendo = false
    private var opcion = ""

    @Composable
    override fun queOpcionEs(args: List<String>) {
        if (args[0] == "-f") {
            leyendo = true
            opcion = "-f"
        } else {
            if (args[0] == "-g" || args[0] == "-p" || args[0] == "-t" || args[0] == "-e" || args[0] == "-l" || args[0] == "-c" || args[0] == "-f" || args[0] == "-i") {
                opcion = args[0]
                if (opcion == "-i") {
                    intefarzGrafica()
                }
            } else {
                opcion = "-1"
            }

        }
    }

    @Composable
    override fun queHago(args: String): String {
        if (args == "-g" || args == "-p" || args == "-t" || args == "-e" || args == "-l" || args == "-c" || args == "-f" || args == "-i") {
            opcion = args
            if (opcion == "-i") {
                intefarzGrafica()
            }
            return "procesado: activando instrucciones de $opcion"
        } else {
            return when (opcion) {
                "-g" -> {
                    val grupo = comprobarGrupos(args)
                    if (grupo == null) {
                        "\"ERROR: el parametro -g, debe de ser asi \"nombre\";\"mejorctfid\""
                    } else
                        crearGrupo(grupo)//nice
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
                    anadirParticipacion(args)
                }
                //terminado pero no probado

                //3	-t <grupoId>
                // Elimina el grupo <grupoid> en la tabla GRUPOS,
                // por tanto también elimina todas sus participaciones en los CTF.
                "-t" -> {
                    eliminargrupo(args)
                }
                //4	-e <ctfId> <grupoId>
                // Elimina la participación del grupo <grupoid> en el CTF <ctfid>.
                // Si no existe la participación, no realiza nada.
                // Finalmente, recalcula el campo mejorposCTFid de los grupos en la tabla GRUPOS.
                "-e" -> {
                    eliminarSoloPArticipacion(args)
                }
                //5	-l <grupoId>	Si <grupoId>
                // esta presente muestra la información del grupo <grupoId> y sus participaciones.
                // Si el grupo no está presente muestra la información de todos los grupos.
                "-l" -> {
                    //el maestro me metera el -c 1
                    mostrarInformacionGrupoParticipacion(args)
                }
                //6	-c <ctfId>	Si <ctfId>
                // esta presente muestra la participación de los grupos y la puntuación obtenida, ordenado de mayor a menor puntuación.
                "-c" -> {
                    //siempre me pasara el ctfid
                    mostrarInformacionDeUnCtf(args)
                }
                //7	-f <filepath>
                // Si <filepath> existe, será un fichero con un conjunto de comandos para procesamiento por lotes.
                "-f" -> {
                    return "leyendo archivo"
                }
                //8	-i
                // Lanza la interface gráfica.
                //"-i" -> {

                //}

                else -> {
                    return ("comando desconocido")
                }
            }
        }

    }

    @Composable
    override fun realizando(operacion: String): String {
        if (operacion.contains("#") || operacion.isBlank()) {
            return ""
        } else {
            return queHago(operacion)
        }


    }

    private fun comprobarGrupos(grupo: String): Grupos? {
        return try {
            if (grupo.contains(";")) {
                val lista = grupo.split(";")
                Grupos(1, lista[1], null)
            } else {
                Grupos(1, grupo, null)
            }
        } catch (e: NumberFormatException) {
            return null
        } catch (e: IndexOutOfBoundsException) {
            return null
        }
    }

    private fun crearGrupo(grupo: Grupos): String {//-g, esta bien realizada
        //esta funcion no se debe de tocar mas
        //funciona bien
        //compruebo al grupo
        // si el grupo es distinto de nulo inserto el grupo
        val existeGrupo = groupService.verificargrupo(grupo)
        if (!existeGrupo) {
            val grupoNuevo = groupService.crearGrupo(grupo)
            if (grupoNuevo != null) {
                return "Procesado: Añadido el grupo \"${grupoNuevo.grupoDesc}\"."
            } else {
                return ("ERROR al crear  el grupo $grupo")
            }
        } else {
            return ("ERROR este grupo ya existe en la base de datos $grupo")
        }
    }

    private fun comprobarCtfs(ctf: String): Ctfs? {
        val lista = ctf.split(";")
        return try {
            if (lista.size == 3) {
                Ctfs(lista[0].toInt(), lista[1].toInt(), lista[2].toInt())
            } else
                null
        } catch (e: NumberFormatException) {
            return null
        }
    }

    private fun anadirParticipacion(PrCtf: String): String {//-p
        //funcion comprobada no tocar o modificar
        //esto me devuelve un ctf o un nulo como error
        val ctf: Ctfs? = comprobarCtfs(PrCtf)
        // si el grupo no es nulo y existe actualiza su puntuacion
        if (ctf != null) {
            val grupo = groupService.getById(ctf.grupoId)
            if (grupo != null) {
                if (ctfService.comprobarExistencia(ctf)) {


                    //esta parte esta correcta
                    val puntuacionantigua =
                        ctfService.getAll(ctf.ctfdId)?.find { ctf.grupoId == it.grupoId }?.puntuacion ?: 0
                    val ctfActualizado = ctfService.actualizarPuntuacion(ctf)

                    if (ctfActualizado == null) {
                        return ("no se pudo agregar la participacion de ${ctf.ctfdId}, ${ctf.grupoId} y ${ctf.puntuacion} a la BBDD")
                    }
                    return ("Procesado: Actualizada la participación del grupo" +
                            " \"${grupo.grupoDesc}\" en el CTF ${ctf.ctfdId}. " +
                            "La puntuación cambió de  $puntuacionantigua a ${ctfActualizado.puntuacion} puntos.\n" + asignarMejorCtf(
                        grupo
                    ))

                    //si no lo que hace es crear una nueva participacion
                } else {

                    //esta parte tambien esta correcta
                    val ctfNuevo = ctfService.anadirParticipacion(ctf)
                    if (ctfNuevo == null) {
                        return ("no se pudo agregar la participacion de ${ctf.ctfdId}, ${ctf.grupoId} y ${ctf.puntuacion} a la BBDD")
                    }
                    return (" Procesado: Añadida participación del grupo " +
                            "\"${grupo.grupoDesc}\" en el CTF ${ctfNuevo.ctfdId} con una puntuación de ${ctfNuevo.puntuacion} puntos.\n" + asignarMejorCtf(
                        grupo
                    ))
                }

            } else {
                return ("El equipo no existe")
            }
        } else {
            return "se introdujo mal los parametros para la instruccion -p"
        }

    }

    private fun crearserie(lista: List<Ctfs>?): String {
        var serie = ""
        if (lista != null)
            for (i in 0..lista.size - 1) {
                val ctf = lista[i]
                if (i == lista.size - 1) {
                    serie += " y ${ctf.ctfdId}"
                } else {
                    serie += "${ctf.ctfdId}, "
                }
            }
        return serie
    }

    private fun eliminargrupo(args: String): String {//-t
        //funcion no comprobada, no tocar a menos que a la hora de compilar de fallos
        //compruebo el grupo por si acaso el usuario me lo ha puesto erroneo
        //compruebo si el grupo esta en la base de datos
        try {

            val grupoEliminar = groupService.getById(args.toInt())
            if (grupoEliminar != null) {
                val grupoModificado = groupService.eliminarCtf(grupoEliminar)
                if (grupoModificado != null) {
                    //escogo toda la lista de ctf, antes de eliminar
                    val lista = ctfService.getAll(args.toInt())
                    //elimino las participaciones del grupo en al tabla ctf, ME FUNCIONA ESTAS FUNCION NO TOCAR
                    ctfService.eliminarParticipacion(grupoModificado.grupoId)
                    //elimino en este caso al grupo, FUNCIONA NO TOCAR
                    groupService.eliminarGrupo(grupoModificado.grupoId)
                    //muestro por consola el numero de participaciones eliminada y el grupo
                    val registros = crearserie(lista)

                    return ("Procesado: Eliminada el grupo \"${grupoEliminar.grupoDesc}\" y su participación en los CTFs: ${registros}.")
                } else {
                    return ("Error a la hora de eliminar al grupo ${grupoEliminar.grupoDesc}")
                }

            } else {
                return ("ERROR: El grupo no esta registrado.")
            }
        } catch (e: NumberFormatException) {
            return "Error: numero de parametros incorrectos"
        }

    }

    private fun asignarMejorCtf(grupo: Grupos): String {
        val lista = ctfService.getAll()
        val grupoActualizado = groupService.actualizarmejorCtfs(grupo, lista)
        var serie = ""
        return if (grupoActualizado != null) {
            if (grupoActualizado.mejorPosCTFId == null) {
                serie += "\nel grupo ${grupoActualizado.grupoDesc} no tiene ninguna participacion despues de eliminar su unica participacion."
            } else {
                serie += "\nel grupo ${grupoActualizado.grupoDesc} ahora su mejor ctf es ${grupoActualizado.mejorPosCTFId}"
            }
            serie
        } else {
            return ("Error: no se pudo actualizar el grupo ${grupo.grupoDesc}")
        }
    }

    override fun actualizarTodo() {
        for (i in groupService.getAll()!!) {
            asignarMejorCtf(i)
        }
    }

    private fun eliminarSoloPArticipacion(arg: String): String {//-e
        //FUNCIONA NO TOCAR
        val args = arg.split(";")
        try {
            //compruebo si el grupo y el usuario estan bien
            val grupo = groupService.getById(args[1].toInt())
            val ctfid = ctfService.escogerUnSoloCtf(args[0].toInt())
            //si las dos cosas estan bien entonces lo que hago es lo siguiente
            if (grupo != null && ctfid != null) {
                //aqui intento eliminar esa participacion
                groupService.eliminarCtf(grupo)
                if (ctfService.eliminarParticipacion(grupo.grupoId, ctfid.ctfdId)) {
                    //si se mete en esta parte del codigo es que ha salid ocorrectamente la transccion
                    val serie = "Procesado: Eliminada participación del grupo \"${grupo.grupoDesc}\"" +
                            " en el CTF ${ctfid}." + asignarMejorCtf(grupo)
                    return serie
                } else {
                    return ("ERROR: participacion no eliminada")
                }
            } else {
                if (ctfid == null) {
                    return ("ERROR: participacion no existe")
                } else {
                    return ("ERROR: grupo no existe")
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            return ("ERROR 001: parametros introducido erroneamente.")
        } catch (e: NumberFormatException) {
            return ("ERROR 002: el segundo parametro debe de ser un numero entero.")
        }
    }

    private fun saberPosicion(lista: List<Ctfs>, ctfdId: Int,grupoId:Int): Int {
        var salio = false
        var contador = 0
        lista.filter { it.ctfdId == ctfdId }.sortedWith(compareByDescending({it.puntuacion})).forEach {
            if (!salio) {
                contador++
            }
            if(it.grupoId == grupoId){
                salio = true
            }
        }
        return contador
    }

    private fun mostrarInformacionGrupoParticipacion(args: String): String {
        //5	-l <grupoId>	Si <grupoId>
        // esta presente muestra la información del grupo <grupoId> y sus participaciones.
        // Si el grupo no está presente muestra la información de todos los grupos.

        try {
            if (args != "null") {
                val lista =
                    ctfService.mostrarInformacionGrupo(args.toInt())//esto puedo hacer la comprobacion antes de pasarlo y no tengo que comprobar tanto
                if (lista != null) {
                    val grupo = groupService.getById(args.toInt())
                    if (grupo != null) {
                        var serie = "Procesado: Listado participación del grupo \"${grupo.grupoDesc}\"" +
                                "\nGRUPO: ${grupo.grupoId}   ${grupo.grupoDesc}  MEJORCTF: ${grupo.mejorPosCTFId}, Posición: ${
                                    grupo.mejorPosCTFId?.let {
                                        saberPosicion(
                                            lista,
                                            it,
                                            grupo.grupoId
                                        )
                                    }
                                }, Puntuación: ${lista.find { it.ctfdId == grupo.mejorPosCTFId }?.puntuacion ?: "error"}" +
                                "\n CTF   | Puntuación | Posición\n" +
                                "  -----------------------------"
                        lista.forEach {
                            if (it.grupoId == args.toInt()) {
                                serie += "\n    ${it.ctfdId} |       ${it.puntuacion}   |        ${
                                    saberPosicion(
                                        lista,
                                        it.ctfdId,
                                        it.grupoId
                                    )
                                }\n"
                            }

                        }
                        return serie
                    } else {
                        return ("ERROR: el grupo no existe")
                    }
                } else {
                    return ("este grupo con id: $args, no tiene ninguna participacion o no existe ")
                }
            } else {
                var ctfactual= 0
                val lista =
                    ctfService.getAll()//esto puedo hacer la comprobacion antes de pasarlo y no tengo que comprobar tanto
                if (lista != null) {
                    val grupo = groupService.getAll()
                    if (grupo != null) {
                        var serie = ""
                        lista.sortedWith(compareBy({it.ctfdId})).forEach {
                            if(ctfactual != it.ctfdId){
                                ctfactual = it.ctfdId
                                serie += "\nequipo | CTF   | Puntuación | Posición\n"
                                serie += "-------------------------------\n"
                                serie += "CTF: $ctfactual"
                            }
                            serie += "\n*  ${it.grupoId} |  ${it.ctfdId} |       ${it.puntuacion}   |        ${
                                saberPosicion(
                                    lista,
                                    it.ctfdId,
                                    it.grupoId
                                )
                            }\n"
                        }
                        return serie
                    } else {
                        return ("ERROR: al recoger todos los datos")
                    }
                } else {
                    return ("ERROR: al recoger todos los datos")
                }
            }
        } catch (e: NumberFormatException) {
            return ("ERROR, el parametro -l debe de ir acompañado cn un numero entero")
        }

    }

    private fun mostrarInformacionDeUnCtf(args: String): String {
        //comprobar
        try {
            val grupos = groupService.getAll()
            val ctf = ctfService.escogerListaCtfOParteDeCtf(args.toInt())
            val diccionario = emptyMap<String, Int>().toMutableMap()
            if (grupos != null && ctf != null) {
                ctf.forEach { itCtf ->
                    diccionario[grupos.find { grupo -> grupo.grupoId == itCtf.grupoId }?.grupoDesc ?: ""] =
                        itCtf.puntuacion
                }
                val diccionarioOrdenado = diccionario.toSortedMap(compareByDescending { it })
                var primeraVez = true
                var serie = ""
                for (i in diccionarioOrdenado) {
                    if (primeraVez) {
                        primeraVez = false
                        serie +=
                            "GRUPO GANADOR: ${i.key}  Mejor puntuacion: ${i.value} Total participants: ${diccionarioOrdenado.size}\n" +
                                    "GRUPO   | Puntuación\n" +
                                    "--------------------"
                    }
                    serie += "\n${i.key} | ${i.value}"
                }
                if (diccionarioOrdenado.isEmpty()) {
                    serie = "No se encontraron registros del ctfid $args"
                }
                return serie
            } else {
                if (grupos == null) {
                    return ("ERROR no se pudo acceder a la tabla grupos ")
                } else {
                    return ("ERROR no se pudo acceder a la tabla ctfs ")
                }
            }
        } catch (e: NumberFormatException) {
            return "Error fatal: has introducido mal los parametros"
        }


        //lo que hago aqui es retorna una lista donde tengo que sacar los datos de tipo asi
        //solucionar
        //Procesado: Listado participación en el CTF "1"
        //  GRUPO GANADOR: 1DAM-G1  Mejor puntuación: 90 Total participants: 3
//          GRUPO   | Puntuación
//          --------------------
        //  1DAM-G2 |       90
        //  1DAM-G1 |       80
        //  1DAM-G3 |       70
    }


    @Composable
    private fun intefarzGrafica() {
        interfazGrafica.mostrarPantalla()
    }

    override fun comprobarArgumentos(args: Array<String>): List<String> {
        val lista = mutableListOf<String>()
        if (args.isNotEmpty()) {
            for (i in args) {
                lista.add(i)
            }
            if (lista.size == 1) {
                lista.add("null")
            }
        } else {
            lista.add("-o")
            lista.add("null")
        }
        return lista
    }
}
