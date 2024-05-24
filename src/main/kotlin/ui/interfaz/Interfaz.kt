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
import ui.viewModel.IViewModel


//esta clase mostrara la parte grafica de mi proyecto
class InterfazGrafica(private val viewmodel: IViewModel) :
    IinterfazGrafica {

    @Composable
    override fun mostrarPantalla() {
        //me sirve para mantener encendia la pantalla
        val estado = viewmodel.estado.value
        //es el texto qeu tendra el boton de exporta
        val textoExportar = viewmodel.textoExportar.value
        //es el texto que tendra mi campo de escribir
        val texto = viewmodel.texto.value
        //es la lista que tendra la lazy column
        val lista = viewmodel.lista.value

        if (estado) {
            Window(onCloseRequest = { viewmodel.cambiarEstado(false) }) {
                MaterialTheme {
                    //creo una column
                    Column(
                        //hago que ocupe todo
                        modifier = Modifier.fillMaxSize(),
                        //hago queeste centrado horizontalmente
                        horizontalAlignment = Alignment.CenterHorizontally,
                        //hago que este centrado verticalmente
                        verticalArrangement = Arrangement.Center
                    ) {
                        //creo una lazy column
                        LazyColumn {
                            //creo un item que tnega la info de la variable lista
                            item {
                                //creo la cabecera
                                Text("    grupoId   |   grupoDesc   |   mejorPosCTFId    ")
                                Text("---------------------------------------------------")
                                //recorro la lista
                                lista?.forEach {
                                    //aqui pongo lso datos de cada equipo
                                    Text("${it.grupoId}               |     ${it.grupoDesc}      |     ${it.mejorPosCTFId}")
                                    //es un separador de cada equipo
                                    Text("---------------------------------------------------")
                                }
                            }

                        }
                        // creo un row
                        Row{
                            //creo un campo de texto
                            OutlinedTextField(
                                //tendra de valor texto
                                value = texto,
                                //cada vez que cambia el valor llamo a la funcion del view model y le envio el cambio
                                onValueChange = {valor:String -> viewmodel.cambiartexto(valor) },
                                //le pongo de titulo ID grupo
                                label = { Text("ID GRUPO") }
                            )
                            //creo un boton
                            Button(
                                // cuando le de click
                                onClick = {
                                    // si texto esta vacio
                                    if (texto == "") {
                                        //llamo a la funcion de coger los grupos del viewmodel
                                        viewmodel.cogertodo()
                                    }else{
                                        //llamo a la funcion de coger solo un grupo del viewmodel
                                        viewmodel.cogerungrupo()
                                    }
                                    // llamo la funcion cambiar texto
                                    viewmodel.cambiartexto("")
                                }
                            ) {
                                //le pongo de texto Buscar grupo
                                Text("Buscar Grupo")
                            }
                        }
                        //creo un boton
                        Button(
                            // cuando le doy click
                            onClick = {
                                // llamo al funcion exportar del viewmodel
                                viewmodel.exportar()
                            }
                        ) {
                            //esto es lo que mostrara qeu es de la variable texto a exportar
                            Text(textoExportar)
                        }
                    }

                }
            }
        }
    }
    }

