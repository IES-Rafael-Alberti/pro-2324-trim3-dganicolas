package dao.sqlDao

import dao.iDao.IDaoGroup
import dataclassEntity.Ctfs
import dataclassEntity.Grupos
import java.sql.SQLException
import java.sql.Types
import javax.sql.DataSource

class SqlDaoGroup(
    private val conexionBD: DataSource
) : IDaoGroup {
    override fun crearGrupo(grupo: Grupos): Grupos? {
        //creo la variable sentencia sql
        val sql = "INSERT INTO GRUPOS (GRUPODESC) VALUES (?)"
        //retorno el grupo o un null en caso de error
        return try {
            //abro conexion a la base datos
            conexionBD.connection.use { conn ->
                //preparo la declaracion
                conn.prepareStatement(sql).use { stmt ->
                    //le inserto el nombre del grupo
                    stmt.setString(1, grupo.grupoDesc)
                    // reviso si ha ocurrido exitosamente la operacion
                    val rs = stmt.executeUpdate()
                    //si ha habido un cambio, entonces operacion realizada correctamente
                    if (rs == 1) {
                        grupo
                    }
                    //si no ha ocurrido un fallo y retorno null
                    else {
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            null
        }
    }

    override fun eliminarCtf(grupo: Grupos): Grupos? {

        //variable sql
        val sql = "UPDATE GRUPOS set MEJORPOSCTFID = ? WHERE grupoId = ?"

        //intento
        return try {

            //abro conexion a la base de datos
            conexionBD.connection.use { conn ->
                //preparo la declaracion sql con sus valor indexados
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setNull(1, Types.INTEGER)
                    stmt.setInt(2, grupo.grupoId)

                    //variable sql para ver si hay cambios
                    val rs = stmt.executeUpdate()

                    // en el caos de que haya cambios entonces
                    if (rs == 1) {
                        //retorno el grupo con el campo a nulo
                        return selectById(grupo.grupoId)
                    } else {
                        //si no hago un nulo como error
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            //nulo como error
            null
        }
    }


    override fun getAll(): List<Grupos>? {

        // me creo una lista vacia
        val lista = emptyList<Grupos>().toMutableList()

        //sentencia sql
        val sql = "SELECT * FROM grupos"
        return try {

            // abro conexion con la base de datos
            conexionBD.connection.use { conn ->

                //preparo la declaracion
                conn.prepareStatement(sql).use { stmt ->

                    //creo una variable que tien adentro datos
                    val rs = stmt.executeQuery()
                    while (rs.next()) {

                        // saco esos datos y lo meto en una lista
                        lista.add(
                            Grupos(
                                grupoId = rs.getInt("GRUPOID"),
                                grupoDesc = rs.getString("GRUPODESC"),
                                mejorPosCTFId = rs.getInt("MEJORPOSCTFID")
                            )
                        )
                    }

                    // si la lista esta vacio null como error
                    if (lista.isEmpty()) {
                        return null
                    }
                    // retorno la lista
                    return lista
                }
            }
        } catch (e: SQLException) {
            //null como error
            null
        }
    }


    override fun selectById(id: Int): Grupos? {
        //variable sql
        val sql = "SELECT * FROM grupos WHERE GRUPOID = ?"
        return try {
            //abro conexion
            conexionBD.connection.use { conn ->
                //preparo la declaracion con sus valores indexados
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, id)

                    //variable para ver si ahy cambios
                    val rs = stmt.executeQuery()

                    // si hay cambios entonces
                    if (rs.next()) {

                        //hago un grupo
                        Grupos(
                            grupoId = rs.getInt("GRUPOID"),
                            grupoDesc = rs.getString("GRUPODESC"),
                            mejorPosCTFId = rs.getInt("MEJORPOSCTFID")
                        )
                    }
                    else {
                        //retrno null en caso de error
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            //retorno null en caso de error
            null
        }
    }

    override fun verificarGrupo(grupo: Grupos): Boolean {
        val sql = "SELECT * FROM grupos WHERE grupodesc = ?"
        return try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, grupo.grupoDesc)
                    val rs = stmt.executeQuery()
                    rs.next()
                }
            }
        } catch (e: SQLException) {
            false
        }
    }

    override fun eliminarGrupo(id: Int): Boolean {
        //variable sql
        val sql = "DELETE FROM GRUPOS WHERE GRUPOID = ?"

        //retorno el
        return try {
            //Abro conexion con la base de datos
            conexionBD.connection.use { conn ->

                //creo un commit para ver si hay un fallo
                conn.commit()
                //preparo la declaracion sql con sus calores indexados
                conn.prepareStatement(sql).use { stmt ->
                    //le insert el nombre del grupo
                    stmt.setInt(1, id)

                    //creo una variable para ver si ha error
                    val rs = stmt.executeUpdate()
                    //si ha habido un cambio, entonces operacion realizada correctamente
                    if(rs == 1){
                        //guardo cambios
                        conn.commit()
                        true
                    }else{
                        //deshago cambios
                        conn.rollback()
                        false
                    }
                }
            }
        } catch (e: SQLException) {
            return false
        }
    }

    /**
     * esta funcion comprueba que ctfa tenido ese grupo
     * @param ctfs son todos los ctf hasta la fecha, de momento son registros pequeños, entonces esta solucion es buena temporalmente
     *  @param codGrupo el codigo del grupo a revisar
     *  @return retorna el mejor ctfid o 0 si no hay ninguna participacion del grupo
     *  @author Nicolás De Gomar NO CHAT GPT :/
     * */
    private fun saberMejorCtf(ctfs: List<Ctfs>, codGrupo: Int): Int? {

        //este es el ctf en cual estamos analizando en el momento
        var ctfid = 0

        //esta es la posicion que tnedra en ese ctf a analizar
        var posicion = 0

        //esta es la posicion en la cual estoy analizando del ctf actual
        var posicionActual = 1

        //este es el mejor ctf que ha tenido ese grupo
        val ctfdidmejor: MutableMap<Int, Int> = emptyMap<Int, Int>().toMutableMap()

        //en el caso de que haya salido el grupo, dejo de sumar la posicion actual
        var primeraVez = true

        //cojo la lista de ctfs
        ctfs
            //ordeno por descenso por puntuacion, despues por ctfid y despues por grupo id entonces
            .sortedWith(
                compareByDescending<Ctfs>
                { it.puntuacion }.thenBy { it.ctfdId }.thenBy { it.grupoId })
            //me recorro toda la lista
            .forEach {

                // si el ctfid que estamso analizando es diferente a nuestra variable ctfid entonces
                if (it.ctfdId != ctfid) {

                    //igualo nuestra variable ctfid
                    ctfid = it.ctfdId

                    // y reinicio la posicion actual
                    posicionActual = 1
                }

                // si nuestro codigo de grupo a salido entonces
                if (it.grupoId == codGrupo) {

                    // pongo en un diccionairo un nuevo registro con valor el ctfid a analizar del momento y de valor la posicion que ha tenido
                    ctfdidmejor[it.ctfdId] = posicionActual
                }
                //sumo +1 a la posicion actual
                posicionActual += 1
            }

        // declaro el mejro ctf a nulo
        var mejorctf: Int? = null

        // itero el diccionario
        for (i in ctfdidmejor) {

            // en la primera iterracion lo que hago es
            if (primeraVez) {

                // igualar a false la variable
                primeraVez = false

                //poner la posiccion que ha tenido a en un ctf
                posicion = i.value

                // poner como mejor ctf el ctf a analizar
                mejorctf = i.key
            }
            //si no entonces
            else {
                //si la posicion que hemos igualado antes es mayor que la posicion de un ctf entonces
                if (posicion > i.value) {

                    //igualo el ctf analizar como el mejor
                    mejorctf = i.key
                }
            }

        }
        //retorno el mejor ctf
        return mejorctf
    }

    //con esta funcion actualizo al grupo a null en mejor ctf
    private fun noHayParticiapcion(grupoId: Int): Grupos? {

        //no hay ninguna participacion del grupo, retorno grupo como exito o nulo como fracaso
        return try {

            //pongo una variable con una sentencia sql
            val sql2 = "update grupos set MEJORPOSCTFID = null WHERE GRUPOID = ?"

            //abro conexxion con la base de datos
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql2).use { stmt ->

                    //hago un commit por si acaso da fallos
                    conn.commit()

                    //pongo el codigo del grupo
                    stmt.setObject(1, grupoId)

                    //compruebo si hay cambios
                    val rs = stmt.executeUpdate()

                    //si ha habido un cambio, entonces operacion realizada correctamente
                    if (rs == 1) {

                        //hago un commit para guardar datos y entonces
                        conn.commit()

                        //retorno ese mismo grupo por id
                        selectById(grupoId)
                    }

                    // si no entonces
                    else {

                        //hago un rollback por si acaso la base e datos se ha corrompido
                        conn.rollback()

                        //retorno null
                        null
                    }
                }
            }
        } catch (e: SQLException) {

            //si hay un error de sql entonces retorno nulo
            null
        }
    }

    private fun hayParticipacion(grupoId: Int, mejorctf: Int): Grupos? {

        //variable con sentencia sql
        val sql = "update grupos set MEJORPOSCTFID = ? WHERE GRUPOID = ?"

        //retorno el grupo como exito o nulo como fracaso
        return try {

            //abro conexion con la base de datos
            conexionBD.connection.use { conn ->

                //hago un commit por si acaso tengo que haccer un rollback
                conn.commit()

                //preparo la declaracion sql
                conn.prepareStatement(sql).use { stmt ->

                    //le inserto los datos de manera indexada
                    stmt.setInt(1, mejorctf)
                    stmt.setInt(2, grupoId)

                    //hago una variable rs para ver si hay un minimo cambio
                    val rs = stmt.executeUpdate()

                    //si ha habido un cambio, entonces operacion realizada correctamente
                    if (rs == 1) {

                        //hago un commit para guardar datos
                        conn.commit()

                        //retorno ese grupo
                        selectById(grupoId)
                    } else {

                        //retorno como nulo como signo de error
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            //retorno como nulo como signo de error
            null
        }
    }

    override fun actualizarPosiciones(grupo: Grupos, ctfs: List<Ctfs>?): Grupos? {
        // si la lista ctf es distinto de nulo entonces
        return if (ctfs != null) {
            // le paso la lista a la funcion sabermejorctf
            val mejorctf = saberMejorCtf(ctfs, grupo.grupoId)

            // si ctf es igual a null, significa que no ha tenido ninguna participacion entonces
            if (mejorctf == null) {
                noHayParticiapcion(grupo.grupoId)
            }
            //si no entonces pues pongo la mejor ctf en el campo mejorctfid del grupo
            else {
                hayParticipacion(grupo.grupoId, mejorctf)
            }

        } else {
            //retorno como nulo como signo de error
            null
        }
    }

    private fun filtrarSeleccion(id: Int? = null): List<Grupos>? {
        val todosGrupos = getAll()
        return if (id != null && todosGrupos != null) {
            val listaFiltro = emptyList<Grupos>().toMutableList()
            todosGrupos.forEach {
                if (it.grupoId == id) {
                    listaFiltro.add(it)
                }
            }
            listaFiltro
        } else {
            todosGrupos
        }
    }

    override fun mostrarInformacionGrupo(id: Int?): List<Grupos>? {
        return filtrarSeleccion(id)
    }

}

