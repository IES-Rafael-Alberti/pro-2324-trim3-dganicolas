package DAOFactory

import DAO.IDaoCtf
import DAO.IDaoGroup
import DAO.SqlDao.SqlDaoCtf
import DAO.SqlDao.SqlDaoGroup
import Errores.MiPropioError
import dbConnection.DataSourceFactory
import kotlin.jvm.Throws

class DaoFactory:IDaoFactory {
    override fun asignarDaoGroup(opcion:String): IDaoGroup {
        return when (opcion) {
            //implementar un dao factory
            "SQL" ->
                SqlDaoGroup(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI))


            "XML" -> TODO()
            "JSON" -> TODO()
            "TXT" -> TODO()
            else -> {
                    throw MiPropioError("el fichero de configuracion esta mal")

            }
        }
    }

    override fun asignarDaoCtf(opcion: String): IDaoCtf {
        return when (opcion) {
            //implementar un dao factory
            "SQL" ->
                SqlDaoCtf(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI))
            "XML" -> TODO()
            "JSON" -> TODO()
            "TXT" -> TODO()
            else -> {
                throw MiPropioError("el fichero de configuracion esta mal")
            }
        }
    }
}