package dao.sqlDao

import dao.iDao.IDaoCtf
import dataclassEntity.Ctfs
import java.sql.SQLException
import javax.sql.DataSource

class SqlDaoCtf(
    private val conexionBD: DataSource,
) : IDaoCtf {
    override fun anadirParticipacion(ctf: Ctfs): Ctfs? {

        //vsariable con sqntencia sql
        val sql = "INSERT INTO CTFS ( CTFID, GRUPOID, PUNTUACION ) VALUES (?, ?,?)"

        //retorno el ctf o un nulo en caso de error
        return try {
            //abro conexion con la base de datos
            conexionBD.connection.use { conn ->
                //preparo la declaracion
                conn.prepareStatement(sql).use { stmt ->

                    //le paso sus parametros
                    stmt.setInt(1, ctf.ctfdId)
                    stmt.setInt(2, ctf.grupoId)
                    stmt.setInt(3, ctf.puntuacion)

                    //creo una variavle para si hay el minimo cambio
                    val rs = stmt.executeUpdate()
                    //retorno el ctf en caso de que sea exitoso, si no un null indicando que es mala operacion
                    if (rs == 1) {
                        ctf
                    } else {
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            //retrono null como error
            null
        }
    }

    override fun getAll(numero: Int?): List<Ctfs>? {
        //si el numero viene nulo me retorna todos los registros de ctf,
        // si no los registros que contenga ese numero

        //variable sql
        val sql = "SELECT * FROM ctfs"

        //intento
        try {
            //abro la conexion con la base de datos
            conexionBD.connection.use { conn ->

                //creo la declaracion sql
                conn.prepareStatement(sql).use { stmt ->

                    //creo una variable sql con todos lso registros
                    val rs = stmt.executeQuery()

                    //me hago una lista que metere todos los registros de la BD
                    val ctfs = mutableListOf<Ctfs>()

                    //bucle iterando la lista de rs
                    while (rs.next()) {
                        //si el numero es nulo, meto todos los registros
                        if (numero == null) {
                            ctfs.add(
                                Ctfs(
                                    ctfdId = rs.getInt("CTFID"),
                                    grupoId = rs.getInt("GRUPOID"),
                                    puntuacion = rs.getInt("PUNTUACION")
                                )
                            )

                        }
                        //si no lo que hago es que solo meto en la lista los numeros que sean igual al numero que parametro
                        else {
                            //declaro el grupo
                            val ctf = Ctfs(
                                ctfdId = rs.getInt("CTFID"),
                                grupoId = rs.getInt("GRUPOID"),
                                puntuacion = rs.getInt("PUNTUACION")
                            )
                            // si la id del grupo es igual al numero entonces se añade
                            if (numero == ctf.ctfdId) {
                                ctfs.add(ctf)
                            }
                        }
                    }
                    //retorno si hay minimo un registro
                    return ctfs.ifEmpty {
                        null
                    }

                }
            }
        } catch (e: SQLException) {
            //error como nulo
            return null
        }
    }

    override fun selectById(id: Int): Ctfs? {
        //variable sql
        val sql = "SELECT * FROM ctfs WHERE CTFid = ?"
        return try {
            //abro conexion
            conexionBD.connection.use { conn ->
                //preparo la declaracion con sus valores
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, id.toString())

                    //hago una variable sql y si tiene algo pues es correcto
                    val rs = stmt.executeQuery()
                    if (rs.next()) {

                        //retorno el grupo
                        Ctfs(
                            ctfdId = rs.getInt("CTFid"),
                            grupoId = rs.getInt("grupoid"),
                            puntuacion = rs.getInt("puntuacion")
                        )
                    } else {

                        //si no nulo como error
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            //nulo como error
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
                        if (id == null)
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
                            if (id == ctf.grupoId) {
                                ctfs.add(ctf)
                            }
                        }
                    }
                    //retorno si hay minimo un registro
                    return ctfs.ifEmpty {
                        null
                    }

                }
            }
        } catch (e: SQLException) {
            //nulo como error
            return null
        }
    }

    override fun comprobarExistencia(ctf: Ctfs): Boolean {
        //variable con sentencia sql
        val sql = "SELECT * FROM CTFS where grupoid = ? and CTFID = ? "
        return try {
            //abro conexion
            conexionBD.connection.use { conn ->
                //preparo la declaracion
                conn.prepareStatement(sql).use { stmt ->
                    //inserto los valor de manera indexada
                    stmt.setInt(1, ctf.grupoId)
                    stmt.setInt(2, ctf.ctfdId)
                    //creo la variable rs para ver si ha recogido algo
                    val rs = stmt.executeQuery()
                    //si no ha recogido nada es un false y si hay algo pues es un true
                    rs.next()
                }
            }
        } catch (e: SQLException) {
            //en caso de error pues retorno un false
            false
        }
    }

    override fun eliminarParticipacion(id: Int): Boolean {

        //vairable sql
        val sql = "DELETE FROM CTFS WHERE GRUPOID = ?"

        //retorno si
        return try {
            //abro conexion al a base de datos y borro todos los registros
            conexionBD.connection.use { conn ->
                //preparo la delcaracion sql con los valores indexados
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, id)

                    //si las lineas afectadas es mayor o igual a una la operacion es un acierto, por que ha eliminado lineas de la BBDD
                    stmt.executeUpdate() <= 1
                }
            }
        } catch (e: SQLException) {
            //como error o que no ha elimiando niguna
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
                    stmt.executeUpdate() == 1
                }
            }
        } catch (e: SQLException) {
            false
        }
    }
}