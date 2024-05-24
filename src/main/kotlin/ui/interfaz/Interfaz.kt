package ui.interfaz




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
import ui.viewModel.ViewModel



class InterfazGrafica(val viewmodel: ViewModel) :
    IinterfazGrafica {

    @Composable
    override fun mostrarPantalla() {
        val estado = viewmodel.estado.value
        val textoExportar = viewmodel.textoExportar.value
        val texto = viewmodel.texto.value
        val lista = viewmodel.lista.value

        if (estado) {
            Window(onCloseRequest = { viewmodel.cambiarEstado(false) }) {
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
                        Row{
                            OutlinedTextField(
                                value = texto,
                                onValueChange = {valor:String -> viewmodel.cambiartexto(valor) },
                                label = { Text("ID GRUPO") }
                            )
                            Button(
                                onClick = {
                                    if (texto == "") {
                                        viewmodel.cogertodo()
                                    }else{
                                        viewmodel.cogerungrupo()
                                    }
                                    viewmodel.cambiartexto("")
                                }
                            ) {
                                Text("Buscar Grupo")
                            }
                        }
                        Button(
                            onClick = {
                                viewmodel.exportar()
                            }
                        ) {
                            Text(textoExportar)
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

