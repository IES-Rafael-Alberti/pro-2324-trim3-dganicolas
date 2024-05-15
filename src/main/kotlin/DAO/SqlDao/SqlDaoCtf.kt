package DAO.SqlDao

import DAO.IDaoCtf
import Iconsola
import dataclass.Ctfs
import dataclass.Grupos
import java.sql.SQLException
import javax.sql.DataSource

class SqlDaoCtf(
    val conexionBD: DataSource,
    val consola: Iconsola
) : IDaoCtf {
    override fun insert(ctf: Ctfs): Ctfs? {
        val sql = "INSERT INTO CTFS ( CTFID, GRUPOID, PUNTUACION ) VALUES (?, ?,?)"
        return try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, ctf.ctfdId)
                    stmt.setInt(2, ctf.grupoId)
                    stmt.setInt(3, ctf.puntuacion)
                    val rs = stmt.executeUpdate()
                    if (rs == 1) {
                        ctf
                    } else {
                        consola.showMessage("error insert query failed! ($rs records inserted)")
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            consola.showMessage("1 :error* insert query failed! (${e.message})")
            null
        }
    }

    override fun getAll(numero: Int?): List<Ctfs>? {
        val sql = "SELECT * FROM ctfs"
        try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    val rs = stmt.executeQuery()
                    val ctfs = mutableListOf<Ctfs>()
                    while (rs.next()) {
                        if (numero == null)
                            ctfs.add(
                                Ctfs(
                                    ctfdId = rs.getInt("CTFID"),
                                    grupoId = rs.getInt("GRUPOID"),
                                    puntuacion = rs.getInt("PUNTUACION")
                                )
                            )
                        else {
                            val ctf = Ctfs(
                                ctfdId = rs.getInt("CTFID"),
                                grupoId = rs.getInt("GRUPOID"),
                                puntuacion = rs.getInt("PUNTUACION")
                            )
                            if (numero == ctf.grupoId) {
                                ctfs.add(ctf)
                            }
                        }
                    }
                    return if (ctfs.isNotEmpty()) {
                        ctfs
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
        val sql = "SELECT * FROM ctfs WHERE ctfs = ?"
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
            consola.showMessage("3: error* insert query failed! (${e.message})")
            null
        }
    }

    override fun actualizarPuntuacion(ctf: Ctfs): Ctfs? {
        val sql = "UPDATE CTFS SET PUNTUACION = ? WHERE CTFID= ? and GRUPOID = ? "
        return try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, ctf.puntuacion)
                    stmt.setInt(2, ctf.ctfdId)
                    stmt.setInt(3, ctf.grupoId)
                    val rs = stmt.executeUpdate()
                    if (rs == 1) {
                        ctf
                    } else {
                        consola.showMessage("error insert query failed! ($rs records inserted)")
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            consola.showMessage("1 :error* insert query failed! (${e.message})")
            null
        }
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
                        consola.showMessage("error insert query failed! ($rs records inserted)")
                        false
                    }
                }
            }
        } catch (e: SQLException) {
            false
        }
    }

    override fun update(book: Ctfs): Ctfs? {
        TODO("Not yet implemented")
    }

    override fun eliminarParticipacion(id: Int): Boolean {
        val sql = "DELETE FROM CTFS WHERE GRUPOID = ?"
        return try {
            conexionBD.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, id)
                    (stmt.executeUpdate() ==1)
                }
            }
        } catch (e: SQLException) {
            consola.showMessage("error al eliminar la participacion de ctf $id")
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
                    (stmt.executeUpdate() ==1)
                }
            }
        } catch (e: SQLException) {
            consola.showMessage("error al eliminar la participacion del grupo  $id en el ctf de $ctfid")
            false
        }
    }
}