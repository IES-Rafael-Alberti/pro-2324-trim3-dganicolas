package dao.sqlDao

import ui.interfaz.IinterfazGrafica
import androidx.compose.runtime.*
import dao.iDao.IDAOOperaciones
import dataclassEntity.Ctfs
import dataclassEntity.Grupos
import service.ICtfsService
import service.IGruposService

class OperacionesSql(
    override val ctfService: ICtfsService,
    override val groupService: IGruposService,
    override val interfazGrafica: IinterfazGrafica
) : IDAOOperaciones {
    //esta variable hace que si estamso leyendo del archivo o no
    override var leyendo = false

    //es la opcion la guardo para cuando estemos leyendo
    private var opcion = ""

    @Composable
    override fun queOpcionEs(args: List<String>) {

        // si el parametro es -f entonces
        if (args[0] == "-f") {

            //pongo la variable leyendo a true
            leyendo = true

            //opcion es igual a -f
            opcion = "-f"
        }
        //si no
        else {

            // si argumento es igual a -g,-p,-t,-e,-l,-c,-f,-i entonces
            if (args[0] == "-g" || args[0] == "-p" || args[0] == "-t" || args[0] == "-e" || args[0] == "-l" || args[0] == "-c" || args[0] == "-f" || args[0] == "-i") {

                //opcion es igual a la opcion que le ha pasado
                opcion = args[0]

                //si opcion es igual a -i
                if (opcion == "-i") {

                    //entonces llamo a la funcion interfaz grafica
                    intefarzGrafica()
                }
            }

            //si las opciones no son ninguna de esas
            else {

                //opcion es -1 referenciando error
                opcion = "-1"
            }

        }
    }

    @Composable
    override fun queHago(args: String): String {

        // si argumento es igual a -g,-p,-t,-e,-l,-c,-f,-i entonces
        if (args == "-g" || args == "-p" || args == "-t" || args == "-e" || args == "-l" || args == "-c" || args == "-f" || args == "-i") {

            //opcion es igual a la opcion que ha llegado
            opcion = args

            //si opcion es igual -i entonces
            if (opcion == "-i") {

                //llamo a la interfaz grafica
                intefarzGrafica()
            }

            //retorno un string mostrandole al usuario que esta pasando
            return "procesado: activando instrucciones de $opcion"
        }

        //si no
        else {

            //retorno un strin de este when
            return when (opcion) {

                //si opcion es igual a -g entonces
                "-g" -> {

                    //compruebo que me han pasado el parametro correctamente
                    val grupo = comprobarGrupos(args)

                    // si grupo es igual a null entonces
                    if (grupo == null) {
                        // retorno mensaje de error
                        "\"ERROR: el parametro -g, debe de ser asi \"nombre\";\"mejorctfid\""
                    } else

                        // si no llamo a la funcion crear grupo
                        crearGrupo(grupo)
                }

                //si la opcion es igual a -p entonces
                "-p" -> {


                    anadirParticipacion(args)
                }

                //si la opcion es igual a -t entonces
                "-t" -> {

                    eliminargrupo(args)
                }

                //si la opcion es igua la -e
                "-e" -> {

                    eliminarSoloPArticipacion(args)
                }

                //si la opcion es igual a -l
                "-l" -> {

                    mostrarInformacionGrupoParticipacion(args)
                }

                //si opcion es igual a -c
                "-c" -> {

                    mostrarInformacionDeUnCtf(args)
                }

                // si la opcion es -f entonces
                "-f" -> {

                    // te dice que esta ocurriendo
                    return "leyendo archivo"
                }

                // si la opcion es -i entonces
                "-i" -> {

                    // te dice un mensaje de que esta ocurriendo
                    return ("abriendo pantalla")
                }

                //esto es necesario pero nunca llegara :D
                else -> {

                    return ("comando desconocido")
                }
            }
        }

    }

    @Composable
    override fun realizando(operacion: String): String {

        //aqui miro si la lista tiene un # o esta vacia y retorno un vacio o
        return if (operacion.contains("#") || operacion.isBlank()) {
            ""
        } else {

            // hago la operacion
            queHago(operacion)
        }


    }

    private fun comprobarGrupos(grupo: String): Grupos? {

        //retorno un null como error o un grupo
        return try {

            //si el grupo que me han pasado contiene ; entonces
            if (grupo.contains(";")) {

                // creo una lista separando por ;
                val lista = grupo.split(";")

                //creo el grupo con el segundo calro de la lista
                Grupos(1, lista[1], null)
            } else {

                //si no creo el grupo con el string que me han pasado
                Grupos(1, grupo, null)
            }

            //en caso qeu em de erro por no tener 2 elemento la lista doy error
        } catch (e: IndexOutOfBoundsException) {
            return null
        }
    }

    private fun crearGrupo(grupo: Grupos): String {//-g, esta bien realizada

        // esta variable es de tipo booleano, y compruebo si el grupo existe
        val existeGrupo = groupService.verificargrupo(grupo)

        //en el caso de que no exista entonces
        if (!existeGrupo) {

            // creo una variable que sera un grupo y llamo a la funcion crear grupo del service grupo
            val grupoNuevo = groupService.crearGrupo(grupo)

            // si grupo es distinto de nulo
            return if (grupoNuevo != null) {

                //retorno un mensaje de exito
                "Procesado: Añadido el grupo \"${grupoNuevo.grupoDesc}\"."
            }

            //si no
            else {
                //retorno un mensaje de error
                ("ERROR al crear  el grupo $grupo")
            }
        }

        //si no
        else {

            //retorno un mensaje de que ese grupo ya existe
            return ("ERROR este grupo ya existe en la base de datos $grupo")
        }
    }

    private fun comprobarCtfs(ctf: String): Ctfs? {

        // hago una lista separando por ;
        val lista = ctf.split(";")
        return try {

            //si la lista es igual a 3 entonces
            if (lista.size == 3) {
                //creo el ctf
                Ctfs(lista[0].toInt(), lista[1].toInt(), lista[2].toInt())
            }

            //si es menor pues retorno nulo
            else
                null
        } catch (e: NumberFormatException) {

            //retorno nulo al caos de que me haya puesto una cosa que no sea numero
            return null
        }
    }

    private fun crearparticipacion(ctf: Ctfs, grupo: Grupos): String{

        //intento coger la puntuacion antigua
        val puntuacionantigua =
            ctfService.getAll(ctf.ctfdId)?.find { ctf.grupoId == it.grupoId }?.puntuacion ?: 0

        //actualizo el ctf con la nueva puntuacion
        val ctfActualizado = ctfService.actualizarPuntuacion(ctf)
            ?:
            //si me viene nulo es decir que no se pudo actializar la infomarcion entonces, mensaje de error
            return ("no se pudo agregar la participacion de ${ctf.ctfdId}, ${ctf.grupoId} y ${ctf.puntuacion} a la BBDD")

        //actualizo todas las marcas
        actualizarTodo()

        // si hubo exito retorno un mensaje informando al usuario
        return ("Procesado: Actualizada la participación del grupo" +
                " \"${grupo.grupoDesc}\" en el CTF ${ctf.ctfdId}. " +
                "La puntuación cambió de  $puntuacionantigua a ${ctfActualizado.puntuacion} puntos.\n" + asignarMejorCtf(
            grupo
        ))
    }

    private fun actualizacionDeCtf(ctf: Ctfs, grupo: Grupos):String {

        //hago una variable que llama a crear una nueva particiapcion
        val ctfNuevo = ctfService.anadirParticipacion(ctf)
            ?:
            //retorno estem ensaje como error
            return ("no se pudo agregar la participacion de ${ctf.ctfdId}, ${ctf.grupoId} y ${ctf.puntuacion} a la BBDD")

        //actualizo todas las marcas
        actualizarTodo()

        //retorno un mensaje de exito
        return (" Procesado: Añadida participación del grupo " +
                "\"${grupo.grupoDesc}\" en el CTF ${ctfNuevo.ctfdId} con una puntuación de ${ctfNuevo.puntuacion} puntos.\n" + asignarMejorCtf(
            grupo
        ))
    }
    private fun anadirParticipacion(prCtf: String): String {//-p

        //compruebo si me han puesto correctamente el ctf
        val ctf: Ctfs? = comprobarCtfs(prCtf)

        // si el ctf no es correcto entonces
        if (ctf != null) {

            //esta variable escoge el grupo mediante is ud grupo
            val grupo = groupService.getById(ctf.grupoId)

            //si el grupo es diferente de nulo significa que existe
            if (grupo != null) {
                //si el ctf no es
                return if (ctfService.comprobarExistencia(ctf)) {
                    crearparticipacion(ctf,grupo)
                }

                //si no lo que hace es crear una nueva participacion
                else {
                    actualizacionDeCtf(ctf,grupo)
                }

            } else {

                //mensaje de error
                return ("El equipo no existe")
            }
        } else {

            //mensaje de error
            return "se introdujo mal los parametros para la instruccion -p"
        }

    }

    private fun crearserie(lista: List<Ctfs>?): String {

        //declaro la serie
        var serie = ""

        //si la lista es distinta de nulo es decir hay alguna particiapcion entonces
        if (lista != null)

        //itero la lista
            for (i in 0..lista.size - 1) {

                //igualo
                val ctf = lista[i]

                //si la iteraccion es igual a lo largo de la lista entonces
                if (i == lista.size - 1) {

                    // pongo el ultimo ctf id y la y
                    serie += " y ${ctf.ctfdId}"
                } else {

                    // si no pongo espacio y ctf id
                    serie += "${ctf.ctfdId}, "
                }
            }

        return serie
    }

    private fun eliminargrupo(args: String): String {//-t
        //intento eliminar un grupo

        try {

            //escogo el grupo a eliminar si existe
            val grupoEliminar = groupService.getById(args.toInt())

            //si es diferente de nulo es que el grupo existe ne la BBDD
            if (grupoEliminar != null) {

                // elimino el mejor ctf id al grupo
                val grupoModificado = groupService.eliminarCtf(grupoEliminar)

                // si el grupo no es nulo es que hemos podido actualizarle el campo
                if (grupoModificado != null) {

                    //escogo toda la lista de ctf, antes de eliminar
                    val lista = ctfService.getAll(args.toInt())

                    //elimino las participaciones del grupo en al tabla ctf
                    ctfService.eliminarParticipacion(grupoModificado.grupoId)

                    //elimino en este caso al grupo, FUNCIONA NO TOCAR
                    groupService.eliminarGrupo(grupoModificado.grupoId)

                    //muestro por consola el numero de participaciones eliminada y el grupo
                    val registros = crearserie(lista)

                    //retorno por consola el mensaje
                    return ("Procesado: Eliminada el grupo \"${grupoEliminar.grupoDesc}\" y su participación en los CTFs: ${registros}.")
                } else {

                    //mensaje de error
                    return ("Error a la hora de eliminar al grupo ${grupoEliminar.grupoDesc}")
                }

            } else {

                //mensaje de error
                return ("ERROR: El grupo no esta registrado.")
            }
        } catch (e: NumberFormatException) {

            //mensaje de error
            return "Error: numero de parametros incorrectos"
        }

    }

    private fun asignarMejorCtf(grupo: Grupos): String {
        //esta funcion asigna la mejor ctf a un equipo

        //escogo los ctf enteros
        val lista = ctfService.getAll()

        //paso esa lista a la funcion actualizar mejor ctf
        val grupoActualizado = groupService.actualizarmejorCtfs(grupo, lista)

        //declaro la variable serie que metere un pequeño mensaje
        var serie = ""

        //retorno si el grupo es distinto de grupoactualizado pues
        return if (grupoActualizado != null) {

            serie +=

                    //si el grupo tiene el valor como null entonces
                if (grupoActualizado.mejorPosCTFId == null) {

                    //serie es igual a un mensaje diciendo que el gurpo no tiene ningun mejor ctf
                    "\nel grupo ${grupoActualizado.grupoDesc} no tiene ninguna participacion despues de eliminar su unica participacion."
                } else {

                    // si no serie es igua la que el grupo tiene un mejro ctf
                    "\nel grupo ${grupoActualizado.grupoDesc} ahora su mejor ctf es ${grupoActualizado.mejorPosCTFId}"
                }

            //retorno la serie
            serie
        } else {

            //retorno un mensaje de error
            return ("Error: no se pudo actualizar el grupo ${grupo.grupoDesc}")
        }
    }

    override fun actualizarTodo() {
        for (i in groupService.getAll()!!) {
            asignarMejorCtf(i)
        }
    }

    private fun eliminarSoloPArticipacion(arg: String): String {//-e

        //hago una lista con el argumento qeu me ha pasado
        val args = arg.split(";")
        try {

            //compruebo si el grupo y el ctf estan bien
            val grupo = groupService.getById(args[1].toInt())
            val ctfid = ctfService.escogerUnSoloCtf(args[0].toInt())

            //si las dos cosas estan bien entonces lo que hago es lo siguiente
            if (grupo != null && ctfid != null) {

                //aqui intento eliminar esa participacion
                groupService.eliminarCtf(grupo)

                //aqui elimino el ctf
                return if (ctfService.eliminarParticipacion(grupo.grupoId, ctfid.ctfdId)) {

                    //actualizo todas las mejores ctfid
                    actualizarTodo()
                    //si se mete en esta parte del codigo es que ha salido correctamente y retorno
                    "Procesado: Eliminada participación del grupo \"${grupo.grupoDesc}\"" +
                            " en el CTF ${ctfid}." + asignarMejorCtf(grupo)
                } else {

                    //mensaje de error
                    ("ERROR: participacion no eliminada")
                }
            } else {

                //mensajes de errores
                return if (ctfid == null) {
                    ("ERROR: participacion no existe")
                } else {
                    ("ERROR: grupo no existe")
                }
            }
        } catch (e: IndexOutOfBoundsException) {

            //mensaje de error
            return ("ERROR 001: parametros introducido erroneamente.")
        } catch (e: NumberFormatException) {

            //mensaje de error
            return ("ERROR 002: el segundo parametro debe de ser un numero entero.")
        }
    }

    private fun saberPosicion(lista: List<Ctfs>, ctfdId: Int, grupoId: Int): Int {

        //declaro un booleano
        var salio = false

        //y un contador
        var contador = 0

        // hago lo siguiente
        lista

            //filtro por el ctfid que quiero
            .filter { it.ctfdId == ctfdId }

            //ordeno por puntuacion en descenso
            .sortedWith(compareByDescending { it.puntuacion })

            //recorro la lista
            .forEach {

                // si ha salido entonces deja de contar
                if (!salio) {
                    contador++
                }

                // si es ese grupo deja de contar
                if (it.grupoId == grupoId) {
                    salio = true
                }
            }
        //retorno el contador
        return contador
    }

    private fun sacarLaInfoDeUnGrupo(args: String): String {

        //creo una lista con mostra informacion
        val lista =
            ctfService.mostrarInformacionGrupo(args.toInt())//esto puedo hacer la comprobacion antes de pasarlo y no tengo que comprobar tanto

        //si la lista es distinta e nula significa que hay participaciones
        if (lista != null) {

            // escogo a un grupo
            val grupo = groupService.getById(args.toInt())

            // si el grupo es distinto de nulo significa que existe
            if (grupo != null) {

                // retorno la serie como exito
                var serie = "Procesado: Listado participación del grupo \"${grupo.grupoDesc}\"" +
                        "\nGRUPO: ${grupo.grupoId}   ${grupo.grupoDesc}  MEJORCTF: ${grupo.mejorPosCTFId}, Posición: ${

                            // aqui hago que saber la posicion del grupo 
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

                    //recorro la lista sacando todas las participaciones
                    if (it.grupoId == args.toInt()) {

                        //anado a la serie esto
                        serie += "\n    ${it.ctfdId} |       ${it.puntuacion}   |        ${

                            // aqui hago que saber la posicion del grupo 
                            saberPosicion(
                                lista,
                                it.ctfdId,
                                it.grupoId
                            )
                        }\n"
                    }

                }

                //retorno la lista
                return serie
            } else {

                //error
                return ("ERROR: el grupo no existe")
            }
        } else {

            //mensaje de error
            return ("este grupo con id: $args, no tiene ninguna participacion o no existe ")
        }
    }

    private fun sacarLaInfoTodosCtfs(): String {

        //para sabe el ctf actual
        var ctfactual = 0

        //cogo todos los ctf
        val lista =
            ctfService.getAll()

        //si lista es distinto de null entonces
        if (lista != null) {

            //cogo todos los grupos
            val grupo = groupService.getAll()

            // si grupo es distinto de nulo entonces
            if (grupo != null) {

                //declaro la variable serie
                var serie = ""

                //a la lista
                lista

                    //la ordeno por ctfid
                    .sortedWith(compareBy { it.ctfdId })

                    //me recorro la lista
                    .forEach {

                        // si ctf actual no es el ctfid a analizar entonces
                        if (ctfactual != it.ctfdId) {

                            // igualo el ctfactual a ctfid
                            ctfactual = it.ctfdId

                            //añado estas lineas al a serie
                            serie += "\nequipo | CTF   | Puntuación | Posición\n"
                            serie += "-------------------------------\n"
                            serie += "CTF: $ctfactual"
                        }

                        //añado estas lineas a la serie
                        serie += "\n*  ${it.grupoId} |  ${it.ctfdId} |       ${it.puntuacion}   |        ${

                            //saco mejor posicion 
                            saberPosicion(
                                lista,
                                it.ctfdId,
                                it.grupoId
                            )
                        }\n"
                    }

                //retorno serie
                return serie
            } else {

                //mensaje de eror
                return ("ERROR: al recoger todos los datos")
            }
        } else {

            //mensaje de error
            return ("ERROR: al recoger todos los datos")
        }
    }

    private fun mostrarInformacionGrupoParticipacion(args: String): String {

        return try {

            // si el argumento es distinto de null entonces
            if (args != "null") {
                sacarLaInfoDeUnGrupo(args)
            } else {
                sacarLaInfoTodosCtfs()
            }

        } catch (e: NumberFormatException) {

            //mensaje de error
            ("ERROR, el parametro -l debe de ir acompañado cn un numero entero")
        }

    }

    private fun mostrarInformacionDeUnCtf(args: String): String {

        try {

            //cogo todos los grupos
            val grupos = groupService.getAll()

            //cogo una parte de los ctf
            val ctf = ctfService.escogerListaCtfOParteDeCtf(args.toInt())

            // me creo un diccionario vacio
            val diccionario = emptyMap<String, Int>().toMutableMap()

            // si las dos cosas a comparar estan vaicas entonces
            if (grupos != null && ctf != null) {

                // me recorro la lista ctf
                ctf.forEach { itCtf ->

                    // añado al diccionario con clave el nombre del grupo y valor la puntacion
                    diccionario[grupos.find { grupo -> grupo.grupoId == itCtf.grupoId }?.grupoDesc ?: ""] =
                        itCtf.puntuacion
                }

                // me creo una variable con el diccioanrio anterior ordenado
                val diccionarioOrdenado = diccionario.toSortedMap(compareByDescending { it })

                // me creo la variable booleano primera vez
                var primeraVez = true

                // me creo la serie
                var serie = ""

                //me recorro el diccionario ordenado
                for (i in diccionarioOrdenado) {

                    // hago esto una sola vez
                    if (primeraVez) {
                        primeraVez = false

                        //anado el mensaje a serie
                        serie +=
                            "GRUPO GANADOR: ${i.key}  Mejor puntuacion: ${i.value} Total participants: ${diccionarioOrdenado.size}\n" +
                                    "GRUPO   | Puntuación\n" +
                                    "--------------------"
                    }

                    // anado las lineas a la variable serie
                    serie += "\n${i.key} | ${i.value}"
                }

                //si dicionario esta vacio entonces
                if (diccionarioOrdenado.isEmpty()) {
                    //mensaje de info error
                    serie = "No se encontraron registros del ctfid $args"
                }

                //retorno serie
                return serie
            } else {

                // si grupo es nulo entonces
                return if (grupos == null) {

                    // mensaje de error de grupos
                    ("ERROR no se pudo acceder a la tabla grupos ")
                } else {

                    // si no mensaje de error de ctfs
                    ("ERROR no se pudo acceder a la tabla ctfs ")
                }
            }
        } catch (e: NumberFormatException) {

            // mensaje de error
            return "Error fatal: has introducido mal los parametros"
        }
    }


    @Composable
    private fun intefarzGrafica() {

        //llamo a la funcion grafica de mi proyecto
        interfazGrafica.mostrarPantalla()
    }

    override fun comprobarArgumentos(args: Array<String>): List<String> {
        //compruebo si los argumentos me han llegado correctamente

        //delcaro una lista vacia mutable
        val lista = mutableListOf<String>()

        //si args no esta vacio entonces
        if (args.isNotEmpty()) {

            //hago un for iterando todo
            for (i in args) {
                //añado esa iteracion a la lista
                lista.add(i)
            }

            //si lista es igual a 1 entonces añado un null string
            if (lista.size == 1) {
                lista.add("null")
            }

        }

        //si no lo que hago es
        else {

            //mensaje de error
            lista.add("no se añadio ningun parametro")

            //añado un null
            lista.add("null")
        }

        //retorno la lista
        return lista
    }
}
