package DAO

import Iconsola
import dataclass.Ctfs
import dataclass.Grupos
import java.sql.SQLException
import javax.sql.DataSource

class SqlDao(
    val conexionBD: DataSource,
    val consola: Iconsola
) : Dao {
    override fun insert(grupo: Grupos): Grupos? {
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


        override fun anadirParticipacion(ctf: Ctfs): Ctfs? {
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

        override fun getAll(): List<Grupos>? {
            TODO("Not yet implemented")
        }

        override fun getAllCtf(): List<Ctfs>? {
            TODO("Not yet implemented")
        }

        override fun selectById(id: Int): Grupos? {
            TODO("Not yet implemented")
        }

        override fun selectByIdCtf(id: Int): Ctfs? {
            TODO("Not yet implemented")
        }

        override fun update(book: Grupos): Grupos? {
            TODO("Not yet implemented")
        }

        override fun update(book: Ctfs): Ctfs? {
            TODO("Not yet implemented")
        }

        override fun deleteById(id: Int): Boolean {
            TODO("Not yet implemented")
        }

        override fun deleteByIdCtf(id: Int): Boolean {
            TODO("Not yet implemented")
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
}

