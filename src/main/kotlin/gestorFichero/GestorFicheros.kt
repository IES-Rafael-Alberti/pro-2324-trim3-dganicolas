package gestorFichero

import IGestorFicheros
import androidx.compose.runtime.toMutableStateList
import java.io.File

class GestorFicheros: IGestorFicheros {
    override fun escribir(fichero: File, lista: List<String>): Boolean {
        if (fichero.exists()){
            fichero.writeText("")
            var contador = 0
            lista.forEach {
                if (lista.size-1 == contador) {
                    fichero.appendText(it)
                } else {
                    contador++
                    fichero.appendText("$it\n")
                }
            }
        }else{
            fichero.createNewFile()
            var contador = 0
            lista.forEach {
                if (lista.size-1 == contador) {
                    fichero.appendText(it)
                } else {
                    contador++
                    fichero.appendText("$it\n")
                }
            }
        }

        return true
    }

    override fun leer(fichero: File): MutableList<String> {
        if(fichero.canRead()){
            return fichero.readLines().toMutableStateList()
        }else{
            return mutableListOf()
        }

    }

    override fun leerFicheroConfig(fichero: File):String{
        val lista = fichero.readLines().toList()
        lista.forEach{
            if (it.contains("tipo=")){
                try{
                    if(it.split("=")[1] == "SQL"||
                        it.split("=")[1] == "JSON"||
                        it.split("=")[1] == "XML"||
                        it.split("=")[1] == "TXT"){
                        return it.split("=")[1]
                    }else{
                        return "null"
                    }
                }catch (e:IndexOutOfBoundsException){
                    return "null"
                }
            }
        }
        return ""
    }

}