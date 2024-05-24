import androidx.compose.ui.window.application
import consola.Consola
import daoFactory.DaoFactory
import gestorFichero.GestorFicheros
import service.CtfService
import service.GroupService
import service.ICtfsService
import service.IGruposService

import ui.interfaz.InterfazGrafica
import ui.viewModel.ViewModel
import java.io.File


//el fichero lo exporto a resources ctfs.txt

//te lo he documentado asi para que sea anti ia
fun main(args: Array<String>) = application {
    //instancion la consola
    val consola = Consola()

    //instancio el gestor de ficheros
    val gestorFicheros = GestorFicheros()

    //compruebo que la opcion sea la correcta en el fichero config
    val opcion = gestorFicheros.leerFicheroConfig(File("config.init"))

    //si es diferente de null la configuracion entro en el fichero
    if (opcion != "null") {

        //instancion el dao factory que dara valor a cada dao
        val daoFactory = DaoFactory()

        //le asigno un dao grupos a la variable
        val fuenteDeDatoGroup = daoFactory.asignarDaoGroup(opcion)

        //le asigno un dao ctf a la variable
        val fuenteDeDatoCtfs = daoFactory.asignarDaoCtf(opcion)

        //instancio la clase service de ctf a la variable
        val ctfService: ICtfsService = CtfService(fuenteDeDatoCtfs)

        //le pongo una clase service de grupo a la variable
        val groupService: IGruposService = GroupService(fuenteDeDatoGroup)

        //instancio el viewmodel
        val viewModel = ViewModel(groupService, ctfService, gestorFicheros)

        //instancio la parte grafica de mi proyecto
        val interfazGrafica = InterfazGrafica(viewModel)

        //le pongo una clase operaciones dao a la variable
        val operaciones = daoFactory.asignarDaoOperaciones(opcion, groupService, ctfService, interfazGrafica)

        //compruebo los argumentos
        val argumentos = operaciones.comprobarArgumentos(args)

        //leo que me han dado como argumentos
        operaciones.queOpcionEs(argumentos)

        // miro si me han pasado un -f o un normal
        if (operaciones.leyendo) {

            // si argumento es null es que es error
            if (argumentos[1] != "null") {

                //compruebo el fichero a leer
                val fichero = gestorFicheros.leer(File(argumentos[1]))

                // si la lista no esta vacia entonces
                if (fichero.isNotEmpty()) {

                    //llamo a la funcion que hacer
                    operaciones.queHago(argumentos[0])

                    // itero la lista
                    for (instruccion in fichero) {

                        //itero todas las intrucciones
                        val resultado = operaciones.realizando(instruccion)
                        consola.showMessage(resultado)
                    }
                } else {
                    consola.showMessage("el fichero no existe o esta vacio")
                }
            } else {

                //mensaje de error
                consola.showMessage("Error en la opcion ${argumentos[0]}")
            }
        } else {
            if ((argumentos[0] == "-i" || argumentos[0] == "-l") || argumentos[1] != "null") {
                for (i in 1..<argumentos.size) {
                    operaciones.actualizarTodo()
                    val resultado = operaciones.realizando(argumentos[i])
                    consola.showMessage(resultado)
                }
            } else {
                consola.showMessage("Error en la opcion ${argumentos[0]}")
            }
        }
    } else {
        consola.showMessage("ERROR: configuracion en el fichero init.config")
    }

}
