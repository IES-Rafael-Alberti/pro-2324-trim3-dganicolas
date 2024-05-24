package daoFactory

import dao.iDao.IDAOOperaciones
import dao.iDao.IDaoCtf
import dao.iDao.IDaoGroup
import ui.interfaz.IinterfazGrafica
import service.ICtfsService
import service.IGruposService

interface IDaoFactory {
    fun asignarDaoCtf(opcion: String): IDaoCtf
    fun asignarDaoGroup(opcion: String): IDaoGroup
    fun asignarDaoOperaciones(opcion: String, daogrupo: IGruposService, daoctf: ICtfsService, interfazGrafica: IinterfazGrafica): IDAOOperaciones
}