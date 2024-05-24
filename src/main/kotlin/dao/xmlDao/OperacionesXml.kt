package DAO.XmlDao

import DAO.IDAO.IDAOOperaciones
import ui.Interfaz.IinterfazGrafica
import androidx.compose.runtime.Composable
import service.ICtfsService
import service.IGruposService

class OperacionesXml(
    override val ctfService: ICtfsService,
    override val groupService: IGruposService,
    override val interfazGrafica: IinterfazGrafica,
) : IDAOOperaciones {
    override var leyendo: Boolean= false

    @Composable
    override fun queOpcionEs(args: List<String>) {
        TODO("aqui miro que opcion ha escogido")
    }

    @Composable
    override fun queHago(args: String): String {
        TODO("aqui lo que hago es llamar a las diferentes funciones de mi programa para cada comando, ya sea el servicio de grupos o ctf")
    }

    @Composable
    override fun realizando(operacion: String): String {
        TODO("aqui lo que hago es leer cada linea del bat")
    }

    override fun actualizarTodo() {
        TODO("aqui actualizo todos los mejores ctf de cada equipo")
    }

    override fun comprobarArgumentos(args: Array<String>): List<String> {
        TODO("aqui compruebo cada argumento")
    }
}