package ui.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import dataclassEntity.Ctfs
import dataclassEntity.Grupos
import gestorFichero.IGestorFicheros
import service.ICtfsService
import service.IGruposService
import java.io.File

class ViewModel(private val serviceGroup: IGruposService, val service: ICtfsService, private val fichero: IGestorFicheros) {

    val _lista = mutableStateOf(serviceGroup.getAll())
    val lista = _lista

    private val _texto = mutableStateOf("")
    val texto = _texto

    private val _textoExportar = mutableStateOf("Exportar")
    val textoExportar = _textoExportar

    private val _estado = mutableStateOf(true)
    val estado = _estado

    fun cambiartexto(texto:String){
        _texto.value = texto
    }

    fun cambiarEstado(estado:Boolean){
        _estado.value = estado
    }

    fun cogertodo(){
        _lista.value = serviceGroup.getAll()
    }

    fun cogerungrupo(){
        _lista.value = if (texto.value.toIntOrNull() != null){
            val grupo = serviceGroup.getById(texto.value.toInt())
            if(grupo != null){
                listOf(grupo)
            }else{
                listOf(Grupos(404,"ERROR: Grupo no existe", 404))
            }

        }else{
            listOf(Grupos(404,"ESO NO ES UN NUMERO", 404))
        }
    }

    fun exportar(){
        _textoExportar.value = "exportando..."
        val listaAexporta = mutableListOf<String>()
        var ctfid:Int? = null
        var contador = 1
        service.getAll()
            ?.sortedWith(compareBy<Ctfs> {it.ctfdId  }.thenBy { it.grupoId }.thenBy { it.puntuacion })
            ?.forEach {
                if(ctfid!= it.ctfdId){
                    ctfid = it.ctfdId
                    contador = 1
                    listaAexporta.add("CTF: $ctfid")
                }
                listaAexporta.add("$contador. ${serviceGroup.getById(it.grupoId)?.grupoDesc} (${it.puntuacion} puntos)")
            }
        fichero.escribir(File("src/main/resources/ctfs.txt"),listaAexporta)
        Thread.sleep(1000)
        _textoExportar.value = "exportar"
    }
}