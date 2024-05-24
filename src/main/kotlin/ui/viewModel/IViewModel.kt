package ui.viewModel

import androidx.compose.runtime.mutableStateOf
import dataclassEntity.Ctfs
import dataclassEntity.Grupos
import java.io.File

interface IViewModel {

    val _lista
    val lista

    val _texto
    val texto

    private val _textoExportar = mutableStateOf("Exportar")
    val textoExportar

    private val _estado
    val estado

    fun cambiartexto(texto:String)

    fun cambiarEstado(estado:Boolean)

    fun cogertodo()

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
    }
}