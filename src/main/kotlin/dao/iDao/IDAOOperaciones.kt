package dao.iDao

import ui.interfaz.IinterfazGrafica
import androidx.compose.runtime.Composable
import service.ICtfsService
import service.IGruposService

interface IDAOOperaciones {
    val ctfService: ICtfsService
    val groupService: IGruposService
    val interfazGrafica: IinterfazGrafica
    var leyendo: Boolean

    @Composable
    fun queOpcionEs(args: List<String>)

    @Composable
    fun queHago(args: String): String

    @Composable
    fun realizando(operacion: String): String
    fun actualizarTodo()
    fun comprobarArgumentos(args: Array<String>): List<String>
}