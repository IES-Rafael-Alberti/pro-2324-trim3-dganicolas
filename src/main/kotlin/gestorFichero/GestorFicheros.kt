package gestorFichero

import androidx.compose.runtime.toMutableStateList
import java.io.File

class GestorFicheros: IGestorFicheros {
    override fun escribir(fichero: File, lista: List<String>) {
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
    }

    override fun leer(fichero: File): MutableList<String> {
        return if(fichero.canRead()){
            fichero.readLines().toMutableStateList()
        }else{
            emptyList<String>().toMutableList()
        }

    }

    override fun leerFicheroConfig(fichero: File):String{
        val lista = fichero.readLines().toList()
        lista.forEach{
            if (it.contains("tipo=")){
                return try{
                    if(it.split("=")[1] == "SQL"||
                        it.split("=")[1] == "JSON"||
                        it.split("=")[1] == "XML"||
                        it.split("=")[1] == "TXT"){
                        it.split("=")[1]
                    }else{
                        "null"
                    }
                }catch (e:IndexOutOfBoundsException){
                    "null"
                }
            }
        }
        return ""
    }

}