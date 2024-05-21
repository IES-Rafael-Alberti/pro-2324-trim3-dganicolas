package consola

import Iconsola
import dataclassEntity.Ctfs
import dataclassEntity.Grupos
import service.IGruposService


class Consola : Iconsola {

    override fun showMessage(message:String, lineBreak:Boolean){
        if(lineBreak) println(message)
        else print(message)
    }
}