package consola

import Iconsola
import dataclass.Ctfs
import dataclass.Grupos
import service.ICtfsService
import service.IGruposService


class Consola : Iconsola {

    override fun showMessage(message:String, lineBreak:Boolean){
        if(lineBreak) println(message)
        else print(message)
    }

//    override fun show(userList: List<Book>?,message: String){
//        if(userList != null){
//            if(userList.isNotEmpty()){
//                showMessage(message)
//                userList.forEachIndexed { index, userEntity ->
//                    showMessage("\t${index+1}. $userEntity")
//                }
//            }else{
//                showMessage("No Books Found")
//            }
//        }
//
//    }

    override fun elegirOpcion(): Int {
        showMessage("¿que modo iniciar?\n1.Bases De Datos\n2.Fichero Json")
        var opcion:Int?
        do{
            opcion = readln().toIntOrNull()
            if (opcion != 1 && opcion != 2){
                showMessage("ERROR** elige una opcion valida")
            }
        }while (opcion != 1 && opcion != 2)
        return opcion
    }

     override fun ctfsInsertado(fuenteDeDato: IGruposService, ctfs: Ctfs?){
         if(ctfs == null){
             showMessage(
                 "ERROR: El parámetro <grupoid> debe " +
                         "ser un valor numérico " +
                         "de tipo entero."
             )
         }else{
             val grupo = fuenteDeDato.getById(ctfs.grupoId)
             if (grupo != null) {
                 showMessage(" Procesado: Añadida participación del grupo " +
                         "\"${grupo.grupoDesc}\" en el CTF ${ctfs.ctfdId} con una puntuación de ${ctfs.puntuacion} puntos.")
             }
         }
     }

    override fun grupoInsertado(grupo: Grupos?){
        if(grupo == null){
            showMessage(
                "ERROR: El parámetro <grupoid> debe " +
                        "ser un valor numérico " +
                        "de tipo entero."
            )
        }else{
            showMessage(
                "Procesado: Añadido el grupo \"${grupo.grupoDesc}\""
            )
        }
    }

    fun crearserie(lista: List<Ctfs>?):String{
        var serie = ""
        if (lista!= null)
            for (i in 0..lista.size){
                val ctf = lista[i]
                if (i == lista.size-1){
                    serie += " y ${ctf.ctfdId}"
                }else{
                    serie+= "${ctf.ctfdId}, "
                }
            }
        return serie
    }

    override fun participacionEliminadas(nombreGrupo: String, lista: List<Ctfs>?) {
        var serie = crearserie(lista)
        showMessage("Procesado: Eliminada el grupo \"$nombreGrupo\" y su participación en los CTFs: $serie.")
    }

    fun ctfsactualizado(ctfService: IGruposService, ctfsInsertado: Ctfs?, puntuacionantigua: Int) {
        if (ctfsInsertado == null){
            showMessage("ERROR: El número de parámetros no es adecuado.")
        }else{
            val grupo = ctfService.getById(ctfsInsertado.grupoId)
            if (grupo != null) {
                showMessage("Procesado: Actualizada la participación del grupo" +
                        " \"${grupo.grupoDesc}\" en el CTF 1. " +
                        "La puntuación cambió de ${ctfsInsertado.puntuacion} a $puntuacionantigua puntos.")
            }
        }
    }
}