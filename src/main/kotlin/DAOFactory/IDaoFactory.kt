package DAOFactory

import DAO.IDaoCtf
import DAO.IDaoGroup

interface IDaoFactory {
    fun asignarDaoCtf(opcion: String): IDaoCtf
    fun asignarDaoGroup(opcion: String): IDaoGroup
}