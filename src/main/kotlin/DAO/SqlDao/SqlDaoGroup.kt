package DAO.SqlDao

import DAO.IDaoGroup
import Iconsola
import dataclass.Ctfs
import dataclass.Grupos
import java.sql.SQLException
import javax.sql.DataSource

class SqlDaoGroup(
    val conexionBD: DataSource,
    val consola: Iconsola
) : IDaoGroup {
    override fun insert(grupo: Grupos): Grupos? {
        //intentar que no se inserte grupos con mismo nombre
        val sql = "INSERT INTO GRUPOS (GRUPODESC) VALUES (?)"
        return try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, grupo.grupoDesc)
                    val rs = stmt.executeUpdate()
                    if (rs == 1) {
                        grupo
                    } else {
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            null
        }
    }


    override fun getAll(): List<Grupos>? {
        TODO("Not yet implemented")
    }


    override fun selectById(id: Int): Grupos? {
        val sql = "SELECT * FROM grupos WHERE GRUPOID = ?"
        return try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, id.toString())
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
            consola.showMessage("3: error* insert query failed! (${e.message})")
            null
        }
    }

    override fun update(book: Grupos): Grupos? {
        TODO("Not yet implemented")
    }


    override fun deleteById(id: Int): Boolean {
        val sql = "DELETE FROM GRUPOS WHERE GRUPOID = ?"
        return false
    }
    fun saberMejorCtf(ctfs: List<Ctfs>,codGrupo:Int):Int {
        var ctfid = 0
        var posicion = 0
        var posicionActual = 0
        var ctfdidmejor = 0
        var hasalido= true

        ctfs.sortedWith(compareBy({ it.ctfdId },{it.puntuacion},{it.grupoId})).forEach {
            if(it.ctfdId != ctfid){
                if(posicion < posicionActual){
                    posicionActual = 0
                    ctfid = it.ctfdId
                }else{
                    posicion = posicionActual
                    posicionActual = 0
                    ctfid = it.ctfdId
                    ctfdidmejor = it.ctfdId
                }

            }
            if(it.grupoId == codGrupo){
                hasalido = false
            }
            if (hasalido){
                posicionActual ++
            }

        }
        return ctfdidmejor
    }
    override fun actualizarPosiciones(grupo: Grupos, ctfs: List<Ctfs>?) {
        if (ctfs != null) {
            val mejorctf = saberMejorCtf(ctfs,grupo.grupoId)
            val sql = "update grupos set mejor stfid where codigo grupo"
        }else{
            consola.showMessage("no se encontro todos los ctfs")
        }
    }

}

