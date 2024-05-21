package DAO.SqlDao

import DAO.IDaoCtf
import Iconsola
import dataclassEntity.Ctfs
import java.sql.SQLException
import javax.sql.DataSource

class SqlDaoCtf(
    val conexionBD: DataSource,
) : IDaoCtf {
    override fun anadirParticipacion(ctf: Ctfs): Ctfs? {
        //sentencia sql no probada
        val sql = "INSERT INTO CTFS ( CTFID, GRUPOID, PUNTUACION ) VALUES (?, ?,?)"
        return try {
            //abro conexi9on con la base de datos
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    //le paso sus parametros
                    stmt.setInt(1, ctf.ctfdId)
                    stmt.setInt(2, ctf.grupoId)
                    stmt.setInt(3, ctf.puntuacion)
                    val rs = stmt.executeUpdate()
                    //retorno el ctf en casop de que sea exitoso, si no un null indicando que es mala operacion
                    if (rs == 1) {
                        ctf
                    } else {
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            null
        }
    }

    override fun getAll(numero: Int?): List<Ctfs>? {
        //si el numero viene nulo me retorna todos los registros de ctf,
        // si no los registros que contenga ese numero
        //sentencia sql no probada
        val sql = "SELECT * FROM ctfs"
        try {
            //abro la conexion con la base de datos
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    val rs = stmt.executeQuery()
                    //me hago una lista que metere todos los registros de la BD
                    val ctfs = mutableListOf<Ctfs>()

                    while (rs.next()) {
                        //si el numero es nulo, meto todos los registros
                        if (numero == null)
                            ctfs.add(
                                Ctfs(
                                    ctfdId = rs.getInt("CTFID"),
                                    grupoId = rs.getInt("GRUPOID"),
                                    puntuacion = rs.getInt("PUNTUACION")
                                )
                            )
                        //si no lo que hago es que solo meyto en la lista los numeros que sean igual al numero que parametro
                        else {
                            //declaro el grupo
                            val ctf = Ctfs(
                                ctfdId = rs.getInt("CTFID"),
                                grupoId = rs.getInt("GRUPOID"),
                                puntuacion = rs.getInt("PUNTUACION")
                            )
                            // si la id del grupo es igual al numero entonces se añade
                            if (numero == ctf.grupoId) {
                                ctfs.add(ctf)
                            }
                        }
                    }
                    //retorno si hay minimo un registro
                    return if (ctfs.isNotEmpty()) {
                        ctfs
                    //retorno un nulo indicando que no hay ningun registro
                    } else {
                        null
                    }

                }
            }
        } catch (e: SQLException) {
            return null
        }
    }

    override fun selectById(id: Int): Ctfs? {
        val sql = "SELECT * FROM ctfs WHERE CTFid = ?"
        return try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, id.toString())
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        Ctfs(
                            ctfdId = rs.getInt("CTFid"),
                            grupoId = rs.getInt("grupoid"),
                            puntuacion = rs.getInt("puntuacion")
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

    override fun actualizarPuntuacion(ctf: Ctfs): Ctfs? {
        //sentencia sql no probada
        val sql = "UPDATE CTFS SET PUNTUACION = ? WHERE CTFID= ? and GRUPOID = ? "
        return try {
            //abro conexion con la base de datos
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    //le paso los parametros por orden
                    stmt.setInt(1, ctf.puntuacion)
                    stmt.setInt(2, ctf.ctfdId)
                    stmt.setInt(3, ctf.grupoId)
                    val rs = stmt.executeUpdate()
                    //si ha habido un cambio exito en la operacion
                    if (rs == 1) {
                        ctf
                    } else {
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            null
        }
    }

    override fun mostrarInformacionGrupo(id: Int?): List<Ctfs>? {
        //llamo la getid y el solo me filtra si es numero o null
        return getAll(id)
    }

    override fun comprobarExistencia(ctf: Ctfs): Boolean {
        val sql = "SELECT * FROM CTFS where grupoid = ? and CTFID = ? "
        return try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, ctf.grupoId)
                    stmt.setInt(2, ctf.ctfdId)
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        true
                    } else {
                        false
                    }
                }
            }
        } catch (e: SQLException) {
            false
        }
    }

    override fun eliminarParticipacion(id: Int): Boolean {
        //sentencia sql no probada
        val sql = "DELETE FROM CTFS WHERE GRUPOID = ?"
        return try {
            //abro conexion al a base de datos y borro todos los registros
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, id)
                    //si las lineas afectadas es mayor o igual a una la operacion es un acierto
                    if(stmt.executeUpdate() <=1){
                        true
                    }else{
                        false
                    }
                }
            }
        } catch (e: SQLException) {
            false
        }
    }

    override fun eliminarParticipacionDeUnGrupoEnUnCtf(id: Int, ctfid: Int): Boolean {
        val sql = "DELETE FROM CTFS WHERE GRUPOID = ? and CTFid = ?"
        return try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, id)
                    stmt.setInt(2, ctfid)
                    if(stmt.executeUpdate() ==1){
                        true
                    }else{
                        false
                    }
                }
            }
        } catch (e: SQLException) {
            false
        }
    }
}