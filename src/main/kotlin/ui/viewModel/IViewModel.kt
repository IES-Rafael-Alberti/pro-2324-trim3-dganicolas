package ui.viewModel

import androidx.compose.runtime.MutableState

import dataclassEntity.Grupos
import service.ICtfsService


interface IViewModel {
    val service: ICtfsService
    val _lista: MutableState<List<Grupos>?>
    val lista: MutableState<List<Grupos>?>
    val _texto: MutableState<String>
    val texto: MutableState<String>
    val _textoExportar: MutableState<String>
    val textoExportar: MutableState<String>
    val _estado: MutableState<Boolean>
    val estado: MutableState<Boolean>
    fun cambiartexto(texto: String)
    fun cambiarEstado(estado: Boolean)
    fun cogertodo()
    fun cogerungrupo()
    fun exportar()
}