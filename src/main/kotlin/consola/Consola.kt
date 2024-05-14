package consola

import DAO.Dao
import Iconsola
import dataclass.Ctfs
import dataclass.Grupos


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

     override fun ctfsInsertado(fuenteDeDato: Dao,ctf: Ctfs?){
         if(ctf == null){
             showMessage(
                 "ERROR: El parámetro <grupoid> debe " +
                         "ser un valor numérico " +
                         "de tipo entero."
             )
         }else{
             val grupo = fuenteDeDato.selectById(ctf.grupoId)
             if (grupo != null) {
                 showMessage(" Procesado: Añadida participación del grupo " +
                         "\"${grupo.grupoDesc}\" en el CTF ${ctf.ctfdId} con una puntuación de ${ctf.puntuacion} puntos.")
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

    fun ctfsactualizado(fuenteDeDato: Dao, ctfsInsertado: Ctfs?, puntuacionantigua: Int) {
        if (ctfsInsertado == null){
            showMessage("ERROR: El número de parámetros no es adecuado.")
        }else{
            val grupo = fuenteDeDato.selectById(ctfsInsertado.grupoId)
            if (grupo != null) {
                showMessage("Procesado: Actualizada la participación del grupo" +
                        " \"${grupo.grupoDesc}\" en el CTF 1. " +
                        "La puntuación cambió de ${ctfsInsertado.puntuacion} a $puntuacionantigua puntos.")
            }
        }
    }
}