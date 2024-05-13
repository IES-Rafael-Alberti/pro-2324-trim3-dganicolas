package gestorFichero

import IGestorFicheros
import androidx.compose.runtime.toMutableStateList
import java.io.File

class GestorFicheros: IGestorFicheros {
    override fun escribir(fichero: File, lista: List<String>): Boolean {
        fichero.writeText("")
        var contador = 0
        lista.forEach {
            if (lista.size-1 == contador) {
                fichero.appendText("$it")
            } else {
                contador++
                fichero.appendText("$it\n")
            }
        }
        return true
    }

    override fun leer(fichero: File): MutableList<String> {
        return fichero.readLines().toMutableStateList()
    }

    override fun leerFicheroConfig(fichero: File):String{
        val lista = fichero.readLines().toList()
        lista.forEach{
            if (it.contains("tipo=")){
                return it.split("=")[1]
            }
        }
        return ""
    }

}