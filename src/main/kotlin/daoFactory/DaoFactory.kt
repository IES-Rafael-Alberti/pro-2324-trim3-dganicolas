package daoFactory


import dao.iDao.IDAOOperaciones
import dao.iDao.IDaoCtf
import dao.jsonDao.OperacionesJSON
import dao.txtDao.OperacionesTxt
import dao.sqlDao.OperacionesSql
import dao.iDao.IDaoGroup
import dao.jsonDao.JsonCtfDao
import dao.jsonDao.JsonGruposDao
import dao.sqlDao.SqlDaoCtf
import dao.sqlDao.SqlDaoGroup
import dao.txtDao.TxtCtfDao
import dao.txtDao.TxtGruposDao
import dao.xmlDao.OperacionesXml
import dao.xmlDao.XmlCtfDao
import dao.xmlDao.XmlGruposDao
import ui.interfaz.IinterfazGrafica
import dbConnection.DataSourceFactory
import service.ICtfsService
import service.IGruposService

class DaoFactory:IDaoFactory {
    override fun asignarDaoGroup(opcion:String): IDaoGroup {
        //si la opcion que me llega es una de esta instancio un dao de grupos su dicha clase
        return when (opcion) {
            "SQL" -> SqlDaoGroup(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI))
            "XML" -> XmlGruposDao()
            "JSON" -> JsonGruposDao()
            "TXT" -> TxtGruposDao()
            //lo tengo montado de tal manera que nunca llegara a este else, pero lo tengo que poner
            else -> SqlDaoGroup(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI))
        }
    }

    override fun asignarDaoCtf(opcion: String): IDaoCtf {
        return when (opcion) {
            //si la opcion que me llega es una de esta instancio un dao de ctf su dicha clase
            "SQL" -> SqlDaoCtf(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI))
            "XML" -> XmlCtfDao()
            "JSON" -> JsonCtfDao()
            "TXT" -> TxtCtfDao()
            //lo tengo montado de tal manera que nunca llegara a este else, pero lo tengo que poner
            else -> SqlDaoCtf(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI))
        }
    }

    override fun asignarDaoOperaciones(opcion: String, daogrupo: IGruposService, daoctf: ICtfsService, interfazGrafica: IinterfazGrafica): IDAOOperaciones {
        return when (opcion) {
            //si la opcion que me llega es una de esta instancio un dao de operaciones su dicha clase
            "SQL" -> OperacionesSql(daoctf,daogrupo,interfazGrafica)
            "XML" -> OperacionesXml(daoctf,daogrupo,interfazGrafica)
            "JSON" -> OperacionesJSON(daoctf,daogrupo,interfazGrafica)
            "TXT" -> OperacionesTxt(daoctf,daogrupo,interfazGrafica)
            //lo tengo montado de tal manera que nunca llegara a este else, pero lo tengo que poner
            else -> OperacionesSql(daoctf,daogrupo,interfazGrafica)

        }
    }
}