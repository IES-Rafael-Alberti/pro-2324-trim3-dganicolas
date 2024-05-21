import dataclassEntity.Ctfs
import dataclassEntity.Grupos
import service.IGruposService


interface Iconsola {
    fun showMessage(message: String, lineBreak: Boolean=true)
}