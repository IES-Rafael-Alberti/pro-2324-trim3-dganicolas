package UI.Interfaz

import IGestorFicheros
import UI.IinterfazGrafica.IinterfazGrafica
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import dataclassEntity.Ctfs
import dataclassEntity.Grupos
import service.ICtfsService
import service.IGruposService
import java.io.File


class InterfazGrafica(val serviceGroup: IGruposService, val service: ICtfsService, val fichero: IGestorFicheros) :
    IinterfazGrafica {

    @Composable
    override fun mostrarPantalla() {
        mostrar()
    }

    @Composable
    private fun mostrar() {
        var estado by remember { mutableStateOf(true) }
        var texto by remember { mutableStateOf("") }
        var lista by remember { mutableStateOf(serviceGroup.getAll()) }
        if (estado) {
            Window(onCloseRequest = { estado = false }) {
                MaterialTheme {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                        ) {
                        LazyColumn {
                            item {
                                Text("    grupoId   |   grupoDesc   |   mejorPosCTFId    ")
                                Text("---------------------------------------------------")
                                lista?.forEach {

                                    Text("${it.grupoId}               |     ${it.grupoDesc}      |     ${it.mejorPosCTFId}")
                                    Text("---------------------------------------------------")
                                }
                            }

                            }
                        Row(

                        ) {
                            OutlinedTextField(
                                value = texto,
                                onValueChange = { texto = it },
                                label = { Text("ID GRUPO") }
                            )
                            Button(
                                onClick = {
                                    if (texto == "") {
                                        lista = serviceGroup.getAll()
                                    }else{

                                        if (texto.toIntOrNull() != null){
                                            val grupo = serviceGroup.getById(texto.toInt())
                                            if(grupo != null){
                                                lista = listOf(grupo)
                                            }else{
                                                lista = listOf(Grupos(404,"ERROR: Grupo no existe", null))
                                            }

                                        }else{
                                            lista = listOf(Grupos(404,"Grupo no existe", null))
                                        }
                                    }
                                    texto = ""
                                }
                            ) {
                                Text("Buscar Grupo")
                            }
                        }
                        Button(
                            onClick = {
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
                        ) {
                            Text("Exportar Lista")
                        }
                    }

                }
            }
        }
    }
}

/**
Por defecto mostrará la información de todos los grupos (grupoid, grupodesc, mejorposCTFid).
Si se pulsa el botón "Mostrar" deberá mostrar la información del grupo introducido en el campo de edición (si existe en la base de datos).
Si el campo está vacío deberá mostrar la información de todos los grupos.
Siempre, después de mostrar la información el campo debe ser limpiado.
El segundo botón, "Exportar", realizará la exportación de la clasificación final por CTFs a un fichero.
 * */

