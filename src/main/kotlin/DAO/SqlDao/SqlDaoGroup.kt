package DAO.SqlDao

import DAO.IDaoGroup
import Errores.MiPropioError
import Iconsola
import dataclassEntity.Ctfs
import dataclassEntity.Grupos
import java.sql.SQLException
import java.sql.Types
import javax.sql.DataSource

class SqlDaoGroup(
    val conexionBD: DataSource
) : IDaoGroup {

    override fun crearGrupo(grupo: Grupos): Grupos? {
        //añadido: intentar que no se inserte grupos con mismo nombre
        //sentencia sql probada y funciona como se espera
        val sql = "INSERT INTO GRUPOS (GRUPODESC) VALUES (?)"
        return try {
            //abro conexion a la base datos
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    conn.commit()
                    //le insert oel nombre del grupo
                    try{
                        stmt.setString(1, grupo.grupoDesc)
                        val rs = stmt.executeUpdate()
                        //si ha habido un cambio, entonces operacion realizada correctamente
                        if (rs == 1) {
                            conn.commit()
                            grupo
                        } else {
                            null
                        }
                    }catch (e:SQLException){
                        conn.rollback()
                        null
                    }
                }
            }

        } catch (e: SQLException) {
            null
        }
    }

    override fun eliminarCtf(grupo: Grupos): Grupos? {
        val sql = "UPDATE GRUPOS set MEJORPOSCTFID = null WHERE grupoId = ?"
        return try {
            conexionBD.connection.use { conn ->
                conn.commit()
                try{
                    conn.prepareStatement(sql).use { stmt ->
                        stmt.setString(1,grupo.grupoId.toString())
                        val rs = stmt.executeUpdate()
                        if (rs == 1)  {
                            conn.commit()
                            return grupo
                        } else {
                            null
                        }
                    }
                }catch (e:SQLException){
                    conn.rollback()
                    null
                }

            }
        } catch (e: SQLException) {
            null
        }
    }


    override fun getAll(): List<Grupos>? {
        val lista = emptyList<Grupos>().toMutableList()
        val sql = "SELECT * FROM grupos"
        return try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        lista.add(Grupos(
                            grupoId = rs.getInt("GRUPOID"),
                            grupoDesc = rs.getString("GRUPODESC"),
                            mejorPosCTFId = rs.getInt("MEJORPOSCTFID")
                        ))
                    } else {
                        null
                    }
                    return lista
                }
            }
        } catch (e: SQLException) {
            null
        }
    }


    override fun selectById(id: Int): Grupos? {
        val sql = "SELECT * FROM grupos WHERE GRUPOID = ?"
        return try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, id)
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        Grupos(
                            grupoId = rs.getInt("GRUPOID"),
                            grupoDesc = rs.getString("GRUPODESC"),
                            mejorPosCTFId = rs.getInt("MEJORPOSCTFID")
                        )
                    } else {
                        null
                    }
                }
            }
        } catch (e: SQLException) {
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
                    if (rs.next()) {
                        true
                    } else {
                        false
                    }
                }
            }
        } catch (e: SQLException) {
            throw MiPropioError("ERROR: no se pudo verificar si el grupo existe, no se introduce el grupo")
        }
    }

    override fun eliminarGrupo(id: Int): Boolean {
        //sentencia sql no probada
        val sql = "DELETE FROM GRUPOS WHERE GRUPOID = ?"
        return try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    //le insert el nombre del grupo
                    stmt.setInt(1, id)
                    val rs = stmt.executeUpdate()
                    //si ha habido un cambio, entonces operacion realizada correctamente
                    if(rs == 1){
                        true
                    }else{
                        false
                    }
                }
            }
        }catch (e:SQLException){
            conexionBD.connection.rollback()
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
    private fun saberMejorCtf(ctfs: List<Ctfs>, codGrupo: Int): Int {
        //este es el ctf en cual estamos analizando en el momento
        var ctfid = 0
        //esta es la posicion que mejor ha tenido
        var posicion = 0
        //esta es la posicion en la cual estoy analizando del ctf actual
        var posicionActual = 0
        //este es el mejor ctf que ha tenido ese grupo
        var ctfdidmejor = 0
        //en el caso de que haya salido el grupo, dejo de sumar la posicion actual
        var hasalido = true
//me lo ordena al revez quiero L REVEZ
        ctfs.sortedWith(compareBy({ it.ctfdId }, { it.puntuacion }, { it.grupoId })).forEach {
            //if (it.ctfdId != ctfid) {
            //    if (posicion < posicionActual) {
            //        hasalido = true
            //        posicionActual = 0
            //        ctfid = it.ctfdId
            //    } else {
            //        ctfdidmejor = it.ctfdId
            //        hasalido = true
            //        posicion = posicionActual
            //        posicionActual = 0
//
            //    }
//
            //}
            //if (it.grupoId == codGrupo) {
            //    hasalido = false
            //}
            //if (hasalido) {
            //    posicionActual++
            //}



        }
        return ctfdidmejor-1
    }

    override fun actualizarPosiciones(grupo: Grupos, ctfs: List<Ctfs>?):Grupos? {
        if (ctfs != null) {
            // si me retorna 0 es que no hay ninguna participacion
            val mejorctf = saberMejorCtf(ctfs, grupo.grupoId)
            //sentencia sql no probada
            if(mejorctf == 0){
                //no hay ninguna participacion del grupo
                return try {
                    //abro conexion con la base de datos
                    val sql2 = "update grupos set MEJORPOSCTFID = null WHERE GRUPOID = ?"
                    conexionBD.connection.use { conn ->
                        conn.prepareStatement(sql2).use { stmt ->
                            //el valor es de tipo integer pero es nulo
                            stmt.setInt(1, grupo.grupoId)
                            val rs = stmt.executeQuery()
                            //si ha habido un cambio, entonces operacion realizada correctamente
                            if (rs.next()){
                                Grupos(
                                    grupoId =rs.getInt("grupoId"),
                                    grupoDesc = rs.getString("grupodesc"),
                                    mejorPosCTFId = rs.getInt("mejorPosCTFId")

                                )
                            }else{
                                null
                            }
                        }
                    }
                }catch (e:SQLException){
                    return null
                }
            }else{
                val sql = "update grupos set MEJORPOSCTFID = ? WHERE GRUPOID = ?"
                return try {
                    conexionBD.connection.use { conn ->
                        conn.prepareStatement(sql).use { stmt ->
                            stmt.setObject(1, mejorctf)
                            stmt.setInt(2, grupo.grupoId)
                            val rs = stmt.executeQuery()
                            //si ha habido un cambio, entonces operacion realizada correctamente
                            if (rs.next()){
                                Grupos(
                                    grupoId =rs.getInt("grupoId"),
                                    grupoDesc = rs.getString("grupodesc"),
                                    mejorPosCTFId = rs.getInt("mejorPosCTFId")

                                )
                            }else{
                                null
                            }
                        }
                    }
                }catch (e:SQLException){
                    return null
                }
            }

        } else {
            return null
        }
    }
    private fun filtrarSeleccion(id:Int? = null): List<Grupos>?{
        val todosGrupos= getAll()
        if(id != null && todosGrupos != null){
            val listaFiltro = emptyList<Grupos>().toMutableList()
            todosGrupos.forEach {
                if(it.grupoId == id){
                    listaFiltro.add(it)
                }
            }
            return listaFiltro
        }else{
            return todosGrupos
        }
    }
    override fun mostrarInformacionGrupo(id: Int?): List<Grupos>? {
        val lista = filtrarSeleccion(id)
        return lista
    }

}

