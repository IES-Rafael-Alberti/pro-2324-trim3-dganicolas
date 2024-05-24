package ui.viewModel

import androidx.compose.runtime.mutableStateOf
import dataclassEntity.Ctfs
import dataclassEntity.Grupos
import gestorFichero.IGestorFicheros
import service.ICtfsService
import service.IGruposService
import java.io.File

class ViewModel(
    //una vartiable que es de tipo IGruposService
    private val serviceGroup: IGruposService,
    //una vartiable que es de tipo ICtfsService
    override val service: ICtfsService,
    //una vartiable que es de tipo IGestorFicheros
    private val fichero: IGestorFicheros
) :
    IViewModel {

    //inicializo la variable privada lista con una lista de todos los grupos
    override val _lista = mutableStateOf(serviceGroup.getAll())

    //inicializo la variable lista que mira a la variable _lista
    override val lista = _lista

    //inicializo la variable privada texto con un estado vacio
    override val _texto = mutableStateOf("")

    //inicializo la variable texto que mira a texto
    override val texto = _texto

    //inicializo la variable privada texto a exporta que esta vacia
    override val _textoExportar = mutableStateOf("Exportar")

    //inicializo la variable texto a exporta que mira al a variable privada
    override val textoExportar = _textoExportar

    //inicializo la variable privada estado con estado a true
    override val _estado = mutableStateOf(true)

    //inicializo la variable estado que mira a su parte privada
    override val estado = _estado

    override fun cambiartexto(texto: String) {
        //cambio el texto el valor _texto
        _texto.value = texto
    }

    override fun cambiarEstado(estado: Boolean) {
        //cambio el valor de la variable estado, ya sea true o false
        _estado.value = estado
    }

    override fun cogertodo() {
        //en el valor de _lista, introdusco todos los grupos
        _lista.value = serviceGroup.getAll()
    }

    override fun cogerungrupo() {
        //cogo a un grupo
        _lista.value =
                //si el valor de texto no es un numero entonces
            if (texto.value.toIntOrNull() != null) {
                //la variable grupo recoge al grupo por id
                val grupo = serviceGroup.getById(texto.value.toInt())
                //si el grupo existe tendrmos en grupo un grupo si no es null
                if (grupo != null) {
                    //si el grupo existe entonces el valor de lista es grupo
                    listOf(grupo)
                }
                //mensaje de que no existe el grupo
                else {
                    //si el grupo no existe entonces el valor de lista es
                    listOf(Grupos(404, "ERROR: Grupo no existe", 404))
                }

            }
            //mensaje de error
            else {
                //si el valor del texot no es un numero no existe entonces el valor de lista es
                listOf(Grupos(404, "ESO NO ES UN NUMERO", 404))
            }
    }

    override fun exportar() {
        //declaro una lista de string mutable
        val listaAexporta = mutableListOf<String>()
        // declaro una variable a null que es numero de ctf
        var ctfid: Int? = null
        //pongo un contador
        var contador = 1

        //cogo la tabla ctf entera y
        service.getAll()
            //si la tabla no es nula, pues la ordeno por ctfid despues por grupoid y despues por puntuacion
            ?.sortedWith(compareBy<Ctfs> { it.ctfdId }.thenBy { it.grupoId }.thenBy { it.puntuacion })
            //hago un for dentro de la lista
            ?.forEach {
                //si el ctfid que es nulo o un numero es distinto de ctf id entonces
                if (ctfid != it.ctfdId) {
                    //igualo ctfid y el ctfid que estoy iterando
                    ctfid = it.ctfdId
                    //igualo el contador a 1
                    contador = 1
                    //añado una linea que es CTF: 1 que es el ctfid que estamos iterando
                    listaAexporta.add("CTF: $ctfid")
                }
                //añado a la lista una linea con el nombre del grupo y su puntuacion
                listaAexporta.add("$contador. ${serviceGroup.getById(it.grupoId)?.grupoDesc} (${it.puntuacion} puntos)")
            }
        //despues de terminar el for each, lo que hago es pasarle esa lista al gestor de archivos
        _textoExportar.value = "exportado"
        fichero.escribir(File("src/main/resources/ctfs.txt"), listaAexporta)
    }
}