package DAO.SqlDao

import DAO.IDaoCtf
import Iconsola
import dataclass.Ctfs
import java.sql.SQLException
import javax.sql.DataSource

class SqlDaoCtf(
    val conexionBD: DataSource,
    val consola: Iconsola
):IDaoCtf {
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

    override fun getAll(numero:Int?): List<Ctfs>? {
        val sql = "SELECT * FROM "
        conexionBD.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                val ctfs = mutableListOf<Ctfs>()
                while (rs.next()) {
                    if(numero == null)
                        ctfs.add(
                            Ctfs(
                                ctfdId = rs.getInt("CTFID"),
                                grupoId = rs.getInt("GRUPOID"),
                                puntuacion = rs.getInt("PUNTUACION")
                            )
                        )
                    else{
                        val ctf = Ctfs(
                            ctfdId = rs.getInt("CTFID"),
                            grupoId = rs.getInt("GRUPOID"),
                            puntuacion = rs.getInt("PUNTUACION")
                        )
                        if (numero == ctf.grupoId){
                            ctfs.add(ctf)
                        }
                    }
                }
                ctfs
            }
        }
    }



}