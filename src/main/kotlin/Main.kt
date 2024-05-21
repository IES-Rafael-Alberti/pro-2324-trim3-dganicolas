import DAOFactory.DaoFactory
import Errores.MiPropioError
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
import java.io.File

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

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

fun main() = application {
    val consola = Consola()
    try {

        val args: Array<String> = arrayOf("-p","1;2;100")
        val ficheroConfiguracion = File("src/main/resources/config.init")
        val gestorFicheros = GestorFicheros()
        val opcion = gestorFicheros.leerFicheroConfig(ficheroConfiguracion)
        val daoFactory = DaoFactory()
        val fuenteDeDatoGroup = daoFactory.asignarDaoGroup(opcion)
        val fuenteDeDatoCtfs = daoFactory.asignarDaoCtf(opcion)
        val ctfService: ICtfsService = CtfService(fuenteDeDatoCtfs)
        val groupService: IGruposService = GroupService(fuenteDeDatoGroup)
        val operaciones = DaoOperaciones(ctfService, groupService)
        operaciones.queOpcionEs(args)
        if (operaciones.leyendo) {
            val fichero = gestorFicheros.leer(File(args[1]))
            operaciones.quehago(args[0])
            for (instruccion in fichero) {
                try {
                    val resultado = operaciones.realizando(instruccion)
                    consola.showMessage(resultado)
                } catch (e: MiPropioError) {
                    consola.showMessage(e.message.toString())
                }
            }
        } else {
            for (i in 1..args.size - 1) {
                try {
                    val resultado = operaciones.realizando(args[i])
                    consola.showMessage(resultado)
                } catch (e: MiPropioError) {
                    consola.showMessage(e.message.toString())
                }
            }
        }
    } catch (e:MiPropioError) {
        consola.showMessage(e.message.toString())
    }
}
