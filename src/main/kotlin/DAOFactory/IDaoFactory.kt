package DAOFactory

import DAO.IDAO.IDAOOperaciones
import DAO.IDAO.IDaoCtf
import DAO.IDAO.IDaoGroup
import UI.IinterfazGrafica.IinterfazGrafica
import service.ICtfsService
import service.IGruposService

interface IDaoFactory {
    fun asignarDaoCtf(opcion: String): IDaoCtf
    fun asignarDaoGroup(opcion: String): IDaoGroup
    fun asignarDaoOperaciones(opcion: String, daogrupo: IGruposService, daoctf: ICtfsService, interfazGrafica: IinterfazGrafica): IDAOOperaciones
}