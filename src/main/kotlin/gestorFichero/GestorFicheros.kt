package gestorFichero

import androidx.compose.runtime.toMutableStateList
import java.io.File

class GestorFicheros : IGestorFicheros {
    override fun escribir(fichero: File, lista: List<String>) {
        //si el fichero existe
        if (fichero.exists()) {
            //entonces sobrescribo el fichero a vacio
            fichero.writeText("")
            escribirfichero(fichero,lista)
        }
        //si el fichero no existe entonces
        else {
            //creo un nuevo fichero
            fichero.createNewFile()
            //si largo de lista menos uno es igual contador entonces
            escribirfichero(fichero,lista)
        }
    }

    private fun escribirfichero(fichero: File, lista: List<String>) {
        //pongo un contador
        var contador = 0
        lista.forEach {
            //si largo de lista menos uno es igual contador entonces
            if (lista.size - 1 == contador) {
                //lo pongo el elemento de la lista sin salto de linea en el fichero
                fichero.appendText(it)

            }
            //si no entonces
            else {
                //sumo 1 al contador
                contador++
                //pongo el elemento en el fichero con salto del linea
                fichero.appendText("$it\n")
            }
        }
    }

    override fun leer(fichero: File): MutableList<String> {
        //si el fichero se puede leer
        return if (fichero.canRead()) {
            //retorno una lista del contenido del fichero
            fichero.readLines().toMutableStateList()
        } else {
            //retorno una lista vacia
            emptyList<String>().toMutableList()
        }

    }

    override fun leerFicheroConfig(fichero: File): String {
        //declaro una lista
        val lista = fichero.readLines().toList()
        //recorro la lista
        lista.forEach {
            // si el iterador  contiene la cadena tipo=
            if (it.contains("tipo=")) {
                //entonces que retorne
                return try {
                    //convierto la cadena en una lista [tipo,opcion]
                    //si la opcion es SQL,XML,JSON,TXT retorno la opcion
                    if (it.split("=")[1] == "SQL" ||
                        it.split("=")[1] == "JSON" ||
                        it.split("=")[1] == "XML" ||
                        it.split("=")[1] == "TXT"
                    ) {
                        it.split("=")[1]
                    } else {
                        //si no retorno un null
                        "null"
                    }
                } catch (e: IndexOutOfBoundsException) {
                    //en el caos que la opcion este vacia retorno null
                    "null"
                }
            }
        }
        //retorno null en el caso de que haya una mala configuracion del codigo
        return "null"
    }

}