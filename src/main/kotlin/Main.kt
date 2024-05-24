
import DAOFactory.DaoFactory
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import consola.Consola
import gestorFichero.GestorFicheros
import service.CtfService
import service.GroupService
import service.ICtfsService
import service.IGruposService
import ui.Interfaz.IinterfazGrafica
import ui.interfaz.InterfazGrafica
import ui.viewModel.ViewModel
import java.io.File

/**
 * notas:
 * problema 1: dia 20/05/2024 a las 6:34: estado no solucionado
 * voy a tener problema con los for a la hora del parametro -f
 * solucion hechar los for en el main y que cada funcion reciba lo que tenga que recibir
 *
 * problema numero 2 20/05/2024 a la 6:43: estado no solucionado
 * como mi funcionana retorna algo TODAS, hacer que los mensajes de error o success se muestren en el main, asi la clase DAO se encarga de lo que tiene que hacer y no la sobvrecargo tontamente
 *
 * problema 3: implementar un daoFactory: estado no solucionado
 *
 * problema4: operation service, quitar todas las operaciones del main: estado no solucionado
 *
 * problema 5: sacar la base de datos a la raiz: estado no solucionado
 *
 * problema 6: tengo problemas con -c y -l : estado no solucionado
 *
 * problema 7: si me ponen un -p mal, el programa casca cuando solo le introdusco -p 2 o -p 2;3, pero uand oel pongo -p reacciona bien: estado no solucionado
 *
 * problema 8: cuando pongo -g sin nada el programa no pone ningun mensaje de error pero no casca: estado no solucionado 21/05 0:45: estado no solucionado
 *
 *problema 9 21/05 0:51: el parametro -t si le pongo una letra, me tiene que mostrar un error tipo, el parametro debe de ser un numeor positivo
 *
 *
 * */
fun main(args: Array<String>) = application {
    val consola = Consola()
    val ficheroConfiguracion = File("config.init")
    val gestorFicheros = GestorFicheros()
    val opcion = gestorFicheros.leerFicheroConfig(ficheroConfiguracion)
    if(opcion != "null"){
        val daoFactory = DaoFactory()
        val fuenteDeDatoGroup = daoFactory.asignarDaoGroup(opcion)
        val fuenteDeDatoCtfs = daoFactory.asignarDaoCtf(opcion)
        val ctfService: ICtfsService = CtfService(fuenteDeDatoCtfs)
        val groupService: IGruposService = GroupService(fuenteDeDatoGroup)
        val viewModel = ViewModel(groupService,ctfService,gestorFicheros)
        val interfazGrafica: IinterfazGrafica = InterfazGrafica(viewModel)
        val operaciones = daoFactory.asignarDaoOperaciones(opcion,groupService,ctfService,interfazGrafica)
        val argumentos = operaciones.comprobarArgumentos(args)
        operaciones.queOpcionEs(argumentos)
        if (operaciones.leyendo) {
            if(argumentos[1]!="null"){
                val fichero = gestorFicheros.leer(File(argumentos[1]))
                if(fichero.isNotEmpty()){
                    operaciones.queHago(argumentos[0])
                    for (instruccion in fichero) {
                        operaciones.actualizarTodo()
                        val resultado = operaciones.realizando(instruccion)
                        consola.showMessage(resultado)
                    }
                }else{
                    consola.showMessage("el fichero no existe o esta vacio")
                }
            }else{
                consola.showMessage("Error en la opcion ${argumentos[0]}")
            }
        } else {
            if( (argumentos[0] == "-i" || argumentos[0] == "-l") || argumentos[1]!="null" ){
                for (i in 1..<argumentos.size) {
                    operaciones.actualizarTodo()
                    val resultado = operaciones.realizando(argumentos[i])
                    consola.showMessage(resultado)
                }
            }else{
                consola.showMessage("Error en la opcion ${argumentos[0]}")
            }
        }
    }else{
        consola.showMessage("ERROR: configuracion en el fichero init.config")
    }

}
