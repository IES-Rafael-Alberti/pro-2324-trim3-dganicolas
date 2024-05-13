import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dbConnection.DataSourceFactory
import gestorFichero.GestorFicheros
import jdk.internal.net.http.common.Pair.pair
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

fun main(args: Array<String>){

    val ficheroConfiguracion = File("config.init")
    val gestorFicheros = GestorFicheros()
    val opcion = gestorFicheros.leerFicheroConfig(ficheroConfiguracion)
    val (fuenteDeDato, userDao) = when (opcion){
        "SQL" -> pair(DataSourceFactory.getDS(DataSourceFactory.DataSourceType.HIKARI),)
    }
    //id	comando	Descripción
    //1	-g <grupoId> <grupoDesc>
    // Añade un nuevo grupo con <grupoid> y <grupodesc> en la tabla GRUPOS.
    if (args[0] == "-g"){
        for (i in  1 .. args.size){

        }
    }
    //2	-p <ctfId> <grupoId> <puntuacion>
    // Añade una participación del grupo <grupoid> en el CTF <ctfid> con la puntuación <puntuacion>. Si la participación del grupo <grupoid> en el CTF <ctfid> ya existe, actualiza la puntualización. En cualquiera de los casos, recalcula el campo mejorposCTFid de los grupos en la tabla GRUPOS.
    if (args[0] == "-p"){

    }
    //3	-t <grupoId>
    // Elimina el grupo <grupoid> en la tabla GRUPOS, por tanto también elimina todas sus participaciones en los CTF.
    if (args[0] == "-t"){

    }
    //4	-e <ctfId> <grupoId>
    // Elimina la participación del grupo <grupoid> en el CTF <ctfid>. Si no existe la participación, no realiza nada. Finalmente, recalcula el campo mejorposCTFid de los grupos en la tabla GRUPOS.
    if (args[0] == "-e"){

    }
    //5	-l <grupoId>	Si <grupoId>
    // esta presente muestra la información del grupo <grupoId> y sus participaciones. Si el grupo no está presente muestra la información de todos los grupos.
    if (args[0] == "-l"){

    }
    //6	-c <ctfId>	Si <ctfId>
    // esta presente muestra la participación de los grupos y la puntuación obtenida, ordenado de mayor a menor puntuación.
    if (args[0] == "-c"){

    }
    //7	-f <filepath>
    if (args[0] == "-f"){

    }
    // Si <filepath> existe, será un fichero con un conjunto de comandos para procesamiento por lotes.

    //8	-i
    // Lanza la interface gráfica.
    if (args[0] == "-i"){

    }
    else{
        // mensaje de error
    }
}
