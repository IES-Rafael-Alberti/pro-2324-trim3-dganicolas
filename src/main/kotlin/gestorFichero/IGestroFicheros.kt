import java.io.File

interface IGestorFicheros {
    /**
     * Escribe información en un fichero especificado. Si el fichero no existe, no se crea automáticamente.
     *
     * @param fichero El fichero en el que se desea escribir.
     * @param info La información a escribir en el fichero.
     * @return Verdadero si la escritura fue exitosa, falso de lo contrario.
     */
    fun escribir(fichero: File, info: List<String>): Boolean

    /**
     * Lee el contenido de un fichero y retorna una lista de strings, donde cada elemento representa una línea del fichero.
     *
     * @param fichero El fichero a leer.
     * @return Lista de strings con el contenido del fichero, o null si hubo un error al leer.
     */
    fun leer(fichero: File): MutableList<String>

    /**
     * Lee el contenido de un fichero y retorna la opcion escogida en el fichero de configuracion.
     *
     * @param fichero El fichero a leer.
     * @return opcion escogida.
     */
    fun leerFicheroConfig(fichero: File): String
}