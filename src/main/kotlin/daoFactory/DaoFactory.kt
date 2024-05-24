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
        return when (opcion) {
            //implementar un dao factory
            "SQL" -> SqlDaoGroup(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI))
            "XML" -> XmlGruposDao()
            "JSON" -> JsonGruposDao()
            "TXT" -> TxtGruposDao()
            else -> SqlDaoGroup(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI))
        }
    }

    override fun asignarDaoCtf(opcion: String): IDaoCtf {
        return when (opcion) {
            //implementar un dao factory
            "SQL" -> SqlDaoCtf(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI))
            "XML" -> XmlCtfDao()
            "JSON" -> JsonCtfDao()
            "TXT" -> TxtCtfDao()
            else -> SqlDaoCtf(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI))
        }
    }

    override fun asignarDaoOperaciones(opcion: String, daogrupo: IGruposService, daoctf: ICtfsService, interfazGrafica: IinterfazGrafica): IDAOOperaciones {
        return when (opcion) {
            //implementar un dao factory
            "SQL" -> OperacionesSql(daoctf,daogrupo,interfazGrafica)
            "XML" -> OperacionesXml(daoctf,daogrupo,interfazGrafica)
            "JSON" -> OperacionesJSON(daoctf,daogrupo,interfazGrafica)
            "TXT" -> OperacionesTxt(daoctf,daogrupo,interfazGrafica)
            else -> OperacionesSql(daoctf,daogrupo,interfazGrafica)

        }
    }
}